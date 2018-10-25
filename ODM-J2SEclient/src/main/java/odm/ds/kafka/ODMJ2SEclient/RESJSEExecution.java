/*
 *
 *   Copyright IBM Corp. 2018
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package odm.ds.kafka.ODMJ2SEclient;

import static ilog.rules.res.session.config.IlrPersistenceType.MEMORY;
import static odm.ds.kafka.ODMJ2SEclient.MessageCode.EMPTY_RULEAPP;
import static odm.ds.kafka.ODMJ2SEclient.MessageCode.RULEAPP_CLASSLOADER_RESOURCE_NOT_FOUND;
import static odm.ds.kafka.ODMJ2SEclient.MessageCode.RULEAPP_FILE_NOT_FOUND;
import static odm.ds.kafka.ODMJ2SEclient.MessageCode.RULEAPP_PROCESSED;
import static odm.ds.kafka.ODMJ2SEclient.MessageCode.RULEAPP_NOT_PROCESSED;
import static odm.ds.kafka.ODMJ2SEclient.MessageCode.RULESETS_ADDED;
import static odm.ds.kafka.ODMJ2SEclient.MessageCode.RULESET_ADDED;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import ilog.rules.res.model.IlrAlreadyExistException;
import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrMutableRepository;
import ilog.rules.res.model.IlrMutableRuleAppInformation;
import ilog.rules.res.model.IlrMutableRulesetArchiveInformation;
import ilog.rules.res.model.IlrPath;
import ilog.rules.res.model.IlrRepositoryFactory;
import ilog.rules.res.model.archive.IlrArchiveException;
import ilog.rules.res.model.archive.IlrArchiveManager;
import ilog.rules.res.session.IlrJ2SESessionFactory;
import ilog.rules.res.session.IlrSessionCreationException;
import ilog.rules.res.session.IlrSessionException;
import ilog.rules.res.session.IlrSessionRequest;
import ilog.rules.res.session.IlrSessionResponse;
import ilog.rules.res.session.IlrStatelessSession;
import ilog.rules.res.session.config.IlrSessionFactoryConfig;
import ilog.rules.res.session.config.IlrXUConfig;
import loan.Borrower;
import loan.LoanRequest;
import loan.Report;


public class RESJSEExecution {

    private final MessageFormatter formatter = new MessageFormatter();

    private final IlrJ2SESessionFactory factory;

    private static final Logger LOGGER = Logger.getLogger(RESJSEExecution.class.getName());

    public RESJSEExecution() {
        this(createJ2SESessionFactory());
    }

    private static IlrJ2SESessionFactory createJ2SESessionFactory() {
        IlrSessionFactoryConfig factoryConfig = IlrJ2SESessionFactory.createDefaultConfig();
        IlrXUConfig xuConfig = factoryConfig.getXUConfig();
        xuConfig.setLogAutoFlushEnabled(true);
        xuConfig.getPersistenceConfig().setPersistenceType(MEMORY);
        xuConfig.getManagedXOMPersistenceConfig().setPersistenceType(MEMORY);
        return new IlrJ2SESessionFactory(factoryConfig);
    }

    private RESJSEExecution(IlrJ2SESessionFactory factory) {
        this.factory = factory;
    }

    public void release() {
        factory.release();
    }

    public void loadRuleApp(String ruleAppArchiveName) throws IlrSessionCreationException,
            IlrSessionException,
            IOException,
            IlrArchiveException,
            IlrAlreadyExistException,
            IlrFormatException {
        if (ruleAppArchiveName == null) {
            return;
        }
        URL ruleAppArchiveURL = getRuleAppArchiveURL(ruleAppArchiveName);
        if (ruleAppArchiveURL != null) {
            try (InputStream inputStream = ruleAppArchiveURL.openStream()) {
                if (inputStream != null) {
                    try (JarInputStream jarInputStream = new JarInputStream(inputStream)) {
                        IlrArchiveManager archiveManager = new IlrArchiveManager();
                        IlrRepositoryFactory repositoryFactory =
                                factory.createManagementSession().getRepositoryFactory();
                        IlrMutableRepository repository = repositoryFactory.createRepository();
                        archiveManager.read(repositoryFactory, jarInputStream).stream()
                                .forEach(new Consumer<IlrMutableRuleAppInformation>() {

                                    @Override
                                    public void accept(IlrMutableRuleAppInformation ruleApp) {
                                        Set<IlrPath> rulesetPaths = new HashSet<>();
                                        ruleApp.getRulesets().stream()
                                                .map(IlrMutableRulesetArchiveInformation::getCanonicalPath)
                                                .forEach(rulesetPaths::add);
                                        if (rulesetPaths.isEmpty()) {
                                            info(EMPTY_RULEAPP, ruleApp.getCanonicalPath());
                                        } else if (rulesetPaths.size() == 1) {
                                            info(RULESET_ADDED, rulesetPaths.stream().findFirst());
                                        } else {
                                            info(RULESETS_ADDED, rulesetPaths);
                                        }
                                        try {
                                            repository.addRuleApp(ruleApp);
                                        } catch (IlrAlreadyExistException exception) {
                                            // N/A
                                        }
                                    }
                                });
                        info(RULEAPP_PROCESSED, ruleAppArchiveName);
                        return;
                    }
                }
            }
        }
        throw new IllegalArgumentException(formatter.getMessage(RULEAPP_NOT_PROCESSED, ruleAppArchiveName));
    }

    private void info(String key, Object... arguments) {
        log(INFO, key, arguments);
    }

    private void log(Level level, String key, Object... arguments) {
        LOGGER.log(level, getMessage(key, arguments));
    }

    private String getMessage(String key, Object... arguments) {
        return formatter.getMessage(key, arguments);
    }

    private void warning(String key, Object... arguments) {
        log(WARNING, key, arguments);
    }

    private URL getRuleAppArchiveURL(String ruleAppArchiveName) throws MalformedURLException {
        if (ruleAppArchiveName == null) {
            return null;
        }
        File file = new File(ruleAppArchiveName);
        if (file.exists()) {
            return file.toURI().toURL();
        }
        warning(RULEAPP_FILE_NOT_FOUND, ruleAppArchiveName);
        URL resource = this.getClass().getClassLoader().getResource(ruleAppArchiveName);
        if (resource == null) {
            warning(RULEAPP_CLASSLOADER_RESOURCE_NOT_FOUND, ruleAppArchiveName);
        }
        return resource;
    }

    public void executeRuleset(IlrPath rulesetPath) throws IlrFormatException,
            IlrSessionCreationException,
            IlrSessionException {
        // Create a session request object
        IlrSessionRequest sessionRequest = factory.createRequest();
        sessionRequest.setRulesetPath(rulesetPath);
        // Ensure latest version of the ruleset is taken into account
        sessionRequest.setForceUptodate(true);
        // Set the input parameters for the execution of the rules
        Map<String, Object> inputParameters = new HashMap<String, Object>();
        java.util.Date birthDate = loan.DateUtil.makeDate(1950, 1, 1);
        // Set borrower ruleset parameter
        loan.Borrower borrower = new Borrower("Smith", "John", birthDate, "123121234");
        borrower.setZipCode("12345");
        borrower.setCreditScore(200);
        borrower.setYearlyIncome(20000);
        // Set loan ruleset parameter
        inputParameters.put("borrower", borrower);
        loan.LoanRequest loan = new LoanRequest(new Date(), 48, 100000, 1.2);
        inputParameters.put("loan", loan);
        sessionRequest.setInputParameters(inputParameters);
        // Create the stateless rule session.
        IlrStatelessSession session = factory.createStatelessSession();
        // Execute rules
        IlrSessionResponse sessionResponse = session.execute(sessionRequest);
        // Display the report
        Report report = (Report) (sessionResponse.getOutputParameters().get("report"));
        System.out.println(report.toString());
    }
}