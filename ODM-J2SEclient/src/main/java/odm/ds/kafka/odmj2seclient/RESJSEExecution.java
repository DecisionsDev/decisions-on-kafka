package odm.ds.kafka.odmj2seclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;


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

import static odm.ds.kafka.odmj2seclient.MessageCode.RULEAPP_FILE_NOT_FOUND;
import static odm.ds.kafka.odmj2seclient.MessageCode.RULEAPP_CLASSLOADER_RESOURCE_NOT_FOUND;
import static odm.ds.kafka.odmj2seclient.MessageCode.EMPTY_RULEAPP;
import static odm.ds.kafka.odmj2seclient.MessageCode.RULESET_ADDED;
import static odm.ds.kafka.odmj2seclient.MessageCode.RULESETS_ADDED;
import static odm.ds.kafka.odmj2seclient.MessageCode.RULEAPP_PROCESSED;
import static odm.ds.kafka.odmj2seclient.MessageCode.RULEAPP_NOT_PROCESSED;
import static java.util.logging.Level.WARNING;
import static ilog.rules.res.session.config.IlrPersistenceType.MEMORY;
import loan.Report;
import odm.ds.kafka.producer.SampleProducer;
public class RESJSEExecution {
	
	
	private final MessageFormatter formatter=new MessageFormatter();
	private final IlrJ2SESessionFactory factory;
	private static final Logger LOGGER=Logger.getLogger(RESJSEExecution.class.getName());
	
	/**
	 * 
	 * Create a J2SESessionFactory with a configuation of the XU
	 * @return an IlrSessionFactory
	 * 
	 */
	
	 private static IlrJ2SESessionFactory createJ2SESessionFactory() {
	
		 IlrSessionFactoryConfig factoryConfing=IlrJ2SESessionFactory.createDefaultConfig();
		 IlrXUConfig xuconfig=factoryConfing.getXUConfig();
		 xuconfig.setLogAutoFlushEnabled(true);
		 xuconfig.getPersistenceConfig().setPersistenceType(MEMORY);
		 xuconfig.getManagedXOMPersistenceConfig().setPersistenceType(MEMORY);
		 return new IlrJ2SESessionFactory(factoryConfing);
	 }
	 
	 /**
	  *  Execute the rulePath using his path, it creates in first a sessionRequest, make sure the latest version of ruleSet is in usage
	  *  @param rulesetPath : The path of the ruleset
	  *  
	  */

	 public void executeRuleset(IlrPath rulesetPath, Loan loan) throws IlrFormatException,
     IlrSessionCreationException,
     IlrSessionException, JsonGenerationException, JsonMappingException, IOException {
		 
		 IlrSessionRequest sessionRequest=factory.createRequest();
		 sessionRequest.setRulesetPath(rulesetPath);
		 sessionRequest.setForceUptodate(true);
		 Map<String, Object> inputParamters=new HashMap<String, Object>();
		 inputParamters.put("borrower",  loan.getBorrower());
		 inputParamters.put("loan", loan.getLoanrequest());
		 sessionRequest.setInputParameters(inputParamters);
		 IlrStatelessSession session=factory.createStatelessSession();
		 IlrSessionResponse sessionResponse=session.execute(sessionRequest);
		 Report report=(Report)(sessionResponse.getOutputParameters().get("report"));
		 System.out.println(report.toString());
		 SampleProducer myproducer=new SampleProducer();
		 
		 
	 }
	 
	 /**
	  *  Use to get the RuleAppArchive url, in the case there is not a ruleApp then it returns null, if there is a  ruleApp
	  *  and the file exist then return the url
	  * @param ruleAppArchiveName
	  * @return
	  * @throws MalformedURLException
	  */
	 private URL getRuleAppArchiveURL(String ruleAppArchiveName) throws MalformedURLException {
		 if(ruleAppArchiveName==null) {
			 return null;
		 }
		 File file=new File(ruleAppArchiveName);
		 if(file.exists()) {
			 return file.toURI().toURL();
		 }
		 warning(RULEAPP_FILE_NOT_FOUND, ruleAppArchiveName);
		 URL resource=this.getClass().getClassLoader().getResource(ruleAppArchiveName);
		 if(resource==null) {
			 warning(RULEAPP_CLASSLOADER_RESOURCE_NOT_FOUND, ruleAppArchiveName);
		 }
		 return resource;
	 }
	 
	 /**
	  *  To get the warning
	  * @param key
	  * @param arguments
	  * 
	  */
	 private void warning(String key, Object... arguments) {
		 log(WARNING, key, arguments);
		 
	 }
	 
	 /**
	  *  To get the Message
	  * @param key
	  * @param arguments
	  * @return
	  * 
	  */
	 private String getMessage(String key, Object... arguments) {
		 
		 return null;
	 }
	 
	 /**
	  *  base on the JDK logger to provide logs
	  * @param level
	  * @param key
	  * @param arguments
	  */
	 private void log(Level level, String key, Object... arguments) {
		 LOGGER.log(level, getMessage(key, arguments));
	 }
	 
	 /**
	  *  Load the ruleAppArchive using the RuleAppArchiveURL, in the case the RuleSetPath is empty then there is not a RuleApp, in the case we have only
	  *  one RuleApp or more then notify also and afterwards add the RuleApp to the repository
	  * @param key
	  * @param arguments
	  * 
	  */
	 private void info(String key, Object... arguments) {
		 
	 }
	 /**
	  *  To load the RuleApp
	  *  
	  * @param ruleAppArchiveName
	  * @throws IlrSessionCreationException
	  * @throws IlrSessionException
	  * @throws IOException
	  * @throws IlrArchiveException
	  * @throws IlrAlreadyExistException
	  * @throws IlrFormatException
	  * 
	  */
	 public void loadRuleApp(String ruleAppArchiveName) throws IlrSessionCreationException,
     IlrSessionException,
     IOException,
     IlrArchiveException,
     IlrAlreadyExistException,
     IlrFormatException {
		 if (ruleAppArchiveName==null) {
			 return;
		 }
		 URL ruleAppArchiveURL=getRuleAppArchiveURL(ruleAppArchiveName);
		 if(ruleAppArchiveURL!=null) {
			 try(InputStream inputStream=ruleAppArchiveURL.openStream()){
				 if(inputStream!=null) {
					 try(JarInputStream jarInputStream=new JarInputStream(inputStream)){
						 IlrArchiveManager archiveManager=new IlrArchiveManager();
						 IlrRepositoryFactory repositoryFactory=factory.createManagementSession().getRepositoryFactory();
						 IlrMutableRepository repository=repositoryFactory.createRepository();
						 archiveManager.read(repositoryFactory, jarInputStream).stream().forEach(new Consumer<IlrMutableRuleAppInformation>(){

							@Override
							public void accept(IlrMutableRuleAppInformation ruleApp) {
								// TODO Auto-generated method stub
								Set<IlrPath> rulesetPaths=new HashSet<>();
								ruleApp.getRulesets().stream().map(IlrMutableRulesetArchiveInformation::getCanonicalPath).forEach(rulesetPaths::add);
								if(rulesetPaths.isEmpty()) {
									info(EMPTY_RULEAPP, ruleApp.getCanonicalPath());
								} else if (rulesetPaths.size()==1) {
								
									info(RULESET_ADDED, rulesetPaths.stream().findFirst());
								} else {
									info(RULESETS_ADDED, rulesetPaths);
								}
								try {
									repository.addRuleApp(ruleApp);
								} catch(IlrAlreadyExistException exception) {
									
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
	 
	 /**
	  * 
	  */
	 public void release() {
		 factory.release();
		 
	 }
	 
	 /**
	  * Constructor for RESJSEExecution
	  * @param factory
	  * 
	  */
	 private RESJSEExecution(IlrJ2SESessionFactory factory) {
		 this.factory=factory;
	 }
	 
	 /**
	  * Constructor without argument
	  */
	 public RESJSEExecution() {
		 this(createJ2SESessionFactory());
	 }
}
