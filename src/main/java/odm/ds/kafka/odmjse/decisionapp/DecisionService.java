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
package odm.ds.kafka.odmjse.decisionapp;

import static odm.ds.kafka.odmjse.execution.MessageCode.SAMPLE_ERROR_INVALID_RULESET_PATH;
import static odm.ds.kafka.odmjse.execution.MessageCode.SAMPLE_ERROR_MISSING_RULESET_PATH;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;
import ilog.rules.res.session.IlrSessionCreationException;
import ilog.rules.res.session.IlrSessionException;
import loan.Borrower;
import loan.LoanRequest;
import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.odmjse.execution.MessageFormatter;
import odm.ds.kafka.odmjse.execution.RESJSEExecution;
import odm.ds.loanvalidation.Loan;
import odm.ds.loanvalidation.Message;

public class DecisionService {

	
	final static Logger myLogger=Logger.getLogger(SampleConsumer.class.getName());
	static ResourceBundle mybundle = ResourceBundle.getBundle("messages");
	private static String serverurl;
	private static String topicNameRq;
	private static String topicNameRp;
	private static String consumergroup;
	private static final Options OPTIONS=new Options();
	private static final MessageFormatter formatter=new MessageFormatter();
	private int executionCount;
	
	public int getExecutionCount() {
		return executionCount;
	}

	public void setExecutionCount(int executionCount) {
		this.executionCount = executionCount;
	}

	/**
	 * Creates the Decision Service which is going to be in first a Kafka consumer, receiving payload sent by the client application
	 * executes the payload against a ruleset through the method consumeAndexec and returns the result to the client application
	 * @param serverurl
	 * @param numberparam
	 * @param consumergroup
	 * @param topicNameRq
	 * @param rulesetPath
	 * @param topicNameRp
	 * @throws IlrFormatException
	 * @throws IlrSessionCreationException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IlrSessionException
	 * @throws IOException
	 * 
	 */
	public void setUpDecisionService(String serverurl, int numberparam, String consumergroup, String topicNameRq, IlrPath rulesetPath, String
			topicNameRp) throws IlrFormatException, IlrSessionCreationException, JsonGenerationException, JsonMappingException, IlrSessionException, IOException {
		SampleConsumer myConsumer=new SampleConsumer();
		consumeAndExec(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRq, serverurl, rulesetPath, topicNameRp);
		
	}
	
	/**
	 * a kafka consumer listening to messages sent to the specified topic. For each message the kafka consumer received it
	 * called the method executeRuleset against that message.
	 * @param consumer
	 * @param topicName
	 * @param serverurl
	 * @param rulesetPath
	 * @param topicNameRp
	 * @throws IlrFormatException
	 * @throws IlrSessionCreationException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IlrSessionException
	 * @throws IOException
	 * 
	 */
	public void consumeAndExec(KafkaConsumer<String, String> consumer, String topicName, String serverurl,
			IlrPath rulesetPath, String topicNameRp) throws IlrFormatException, IlrSessionCreationException,
			JsonGenerationException, JsonMappingException, IlrSessionException, IOException {
//		RESJSEExecution execution = new RESJSEExecution();
		consumer.subscribe(Arrays.asList(topicName), new ConsumerRebalanceListener() {
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				System.out.printf("%s topic-partitions are revoked from this consumer\n",
						Arrays.toString(partitions.toArray()));
			}

			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				System.out.printf("%s topic-partitions are assigned to this consumer\n",
						Arrays.toString(partitions.toArray()));
			}
		});
		myLogger.info(mybundle.getString("TOPIC_NAME") + " " + topicName);
		while (true) {
//			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));
			ConsumerRecords<String, String> records =consumer.poll(1000);
			myLogger.info(mybundle.getString("WAITING"));
			if (!records.isEmpty()) {
				for (ConsumerRecord<String, String> record : records) {
					RESJSEExecution execution = new RESJSEExecution();
					String value = record.value();
					myLogger.info(value);
					System.out.println(extractLoanFromJson(value));
					System.out.println(extractkeyFromJson(value));
					execution.executeRuleset(rulesetPath, extractLoanFromJson(value),
							extractkeyFromJson(value), serverurl, topicNameRp);
					executionCount++;
				}
			}

		}
	}

	/**
	 *  Takes a JSON payload as a string, converts it to an object Loan which has a Borrower and a LoanRequest object
	 * @param payload
	 * @return
	 * 
	 */
	public static Loan LoanJson(String payload) {

		ObjectMapper objectMapper = new ObjectMapper();
		Loan loan = new Loan();

		try {
			loan = objectMapper.readValue(payload, Loan.class);
			myLogger.info(mybundle.getString("LOAN_BORROWER")+ loan.getBorrower());
			myLogger.info(mybundle.getString("LOAN_REQUEST")+ loan.getLoanrequest());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loan;

	}

	/**
	 * Takes a string payload as input, converts it to an object of type Message, then extracts the member key from the object Message and 
	 * returns the key
	 * @param payload
	 * @return string key
	 * @throws IOException 
	 * 
	 */
	public String extractkeyFromJson(String payload) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		Message mess = new Message();
		try {
			mess = objectMapper.readValue(payload, Message.class);
		} catch (IOException e) {
			throw e;
		}
		return mess.getKey();

	}

	/**
	 * Takes a string payload as input, converts it to an object of type Message, then extracts the member Loan from the 
	 * object Message and return the Loan
	 * 
	 * @param payload
	 * @return Loan
	 * @throws IOException  
	 * 
	 */
	public Loan extractLoanFromJson(String payload) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		Message message = new Message();
		Loan loan = new Loan();
		Borrower borrower = new Borrower();
		LoanRequest loanrequest = new LoanRequest();
		try {
			message = objectMapper.readValue(payload, Message.class);
			borrower = message.getPayload().getBorrower();
			loanrequest = message.getPayload().getLoanrequest();
			loan.setLoanrequest(loanrequest);
			loan.setBorrower(borrower);

		} catch (IOException e) {
		//	e.printStackTrace();
			throw e;
		}
		return loan;

	}

	/**
	 * Parses the command line arguments and return the rulesetPath as string
	 * @param commandLine
	 * @param arguments
	 * @return
	 * 
	 */
	private String getMandatoryRulesetPathArgument(CommandLine commandLine, String[] arguments) {
		int nbOfArguments = arguments.length;
		if (nbOfArguments != 0) {
			List<String> unprocessedArguments = Arrays.asList(commandLine.getArgs());
			if (!unprocessedArguments.isEmpty()) {
				String rulesetPathArgumentAsString = arguments[0];
				myLogger.info(mybundle.getString("RULESET_PATH")+ arguments[0]);
				if (unprocessedArguments.contains(rulesetPathArgumentAsString)) {
					return rulesetPathArgumentAsString;
				}
			}

		}
		return null;
	}

	/**
	 * returns an IlrPath from the rulesetPath provided as command line argument and 
	 * using the method getMandatoryRulesetPathArgument 
	 * @param commandLine
	 * @param arguments
	 * @return
	 * @throws IllegalArgumentException
	 * 
	 */
	private IlrPath getRulesetPath(CommandLine commandLine, String[] arguments) throws IllegalArgumentException {
		String rulesetPathArgumentAsString = getMandatoryRulesetPathArgument(commandLine, arguments);
		if (rulesetPathArgumentAsString == null) {
			String errorMessage = getMessage(SAMPLE_ERROR_MISSING_RULESET_PATH,
					getMessage(SAMPLE_ERROR_MISSING_RULESET_PATH));
			throw new IllegalArgumentException(errorMessage);
		}
		try {
			return IlrPath.parsePath(rulesetPathArgumentAsString);

		} catch (IlrFormatException exception) {
			String errorMessage = getMessage(SAMPLE_ERROR_INVALID_RULESET_PATH, rulesetPathArgumentAsString);
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * return the key for message
	 * @param key
	 * @param arguments
	 * @return
	 * 
	 */
	private String getMessage(String key, Object... arguments) {

		return formatter.getMessage(key, arguments);
	}

	/**
	 * Initializes the properties we will need to run the Decision Service and setup kafka instance.
	 * Initialization of serverul, topicNameRq (topic for requests),  topicNameRp(topic for replies), 
	 * and the consumergroup
	 * @param commandLine
	 * @param arguments
	 * 
	 */
	public void setUpkafkaParam(CommandLine commandLine, String[] arguments) {

		int nbOfArguments = arguments.length;
		if (nbOfArguments != 0) {
			List<String> unprocessedArguments = Arrays.asList(commandLine.getArgs());
			if (!unprocessedArguments.isEmpty()) {
				serverurl = arguments[1];
				myLogger.info(mybundle.getString("SERVER_URL")+ serverurl);
				topicNameRq = arguments[2];
				myLogger.info(mybundle.getString("TOPIC_NAME_RQ")+ topicNameRq);
				topicNameRp = arguments[3];
    			myLogger.info(mybundle.getString("TOPIC_NAME_RP")+ topicNameRp);
				consumergroup = arguments[4];
    			myLogger.info(mybundle.getString("CONSUMER_GROUP")+ consumergroup);

			}

		}

	}

	  /**
	   * The main method, creates a Decision Service which is going to listen to payload from client application, executes
	   * and returns the result. 
	   * @param args
	   * 
	   */
	public static void main(String... args) {

		DecisionService decisionservice = new DecisionService();
		myLogger.info(mybundle.getString("NOTIF_BIZ_APP"));
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(OPTIONS, args);
			IlrPath rulesetPath = decisionservice.getRulesetPath(commandLine, args);
			decisionservice.setUpkafkaParam(commandLine, args);
			decisionservice.setUpDecisionService(serverurl, 2, consumergroup, topicNameRq, rulesetPath, topicNameRp);

		} catch (IllegalArgumentException | ParseException | IlrFormatException | IlrSessionException
				| IOException exception) {
			System.err.println(exception.getMessage());
		}

	}
}