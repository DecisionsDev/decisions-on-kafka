package odm.ds.kafka.odmjse.businessapp;

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

public class BusinessApplication {

	
	final static Logger myLogger=Logger.getLogger(SampleConsumer.class.getName());
	static ResourceBundle mybundle = ResourceBundle.getBundle("MessagesBundle");
	private static String serverurl;
	private static String topicNameRq;
	private static String topicNameRp;
	private static String consumergroup;
	private static final Options OPTIONS=new Options();
	private static final MessageFormatter formatter=new MessageFormatter();
	private int nbexec;
	
	public int getNbexec() {
		return nbexec;
	}

	public void setNbexec(int nbexec) {
		this.nbexec = nbexec;
	}

	/**
	 * 
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
	public void setUpBussinessApp(String serverurl, int numberparam, String consumergroup, String topicNameRq, IlrPath rulesetPath, String
			topicNameRp) throws IlrFormatException, IlrSessionCreationException, JsonGenerationException, JsonMappingException, IlrSessionException, IOException {
		SampleConsumer myConsumer=new SampleConsumer();
		consumeAndexec(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRq, serverurl, rulesetPath, topicNameRp);
		
	}
	
	/**
	 * 
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
	public void consumeAndexec(KafkaConsumer<String, String> consumer, String topicName, String serverurl,
			IlrPath rulesetPath, String topicNameRp) throws IlrFormatException, IlrSessionCreationException,
			JsonGenerationException, JsonMappingException, IlrSessionException, IOException {
		RESJSEExecution execution = new RESJSEExecution();
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
		myLogger.info(mybundle.getString("topic_name") + " " + topicName);
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));
			myLogger.info(mybundle.getString("waiting"));
			if (!records.isEmpty()) {
				for (ConsumerRecord<String, String> record : records) {

					String value = record.value();
					myLogger.info(value);
					
					execution.executeRuleset(rulesetPath, ExtractLoanFromJson(value),
							ExtractkeyFromJson(value), serverurl, topicNameRp);
					nbexec++;
				}
			}

		}
	}

	/**
	 * 
	 * @param payload
	 * @return
	 * 
	 */
	public static Loan loanJson(String payload) {

		ObjectMapper objectMapper = new ObjectMapper();
		Loan loan = new Loan();

		try {
			loan = objectMapper.readValue(payload, Loan.class);
			myLogger.info(mybundle.getString("Loan_Borrower")+ loan.getBorrower());
			myLogger.info(mybundle.getString("Loan_Request")+ loan.getLoanrequest());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loan;

	}

	/**
	 * 
	 * @param payload
	 * @return
	 * @throws IOException 
	 * 
	 */
	public String ExtractkeyFromJson(String payload) throws IOException {

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
	 * 
	 * @param payload
	 * @return
	 * @throws IOException 
	 * 
	 */
	public Loan ExtractLoanFromJson(String payload) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		Message mess = new Message();
		Loan loan = new Loan();
		Borrower borrower = new Borrower();
		LoanRequest loanrequest = new LoanRequest();
		try {
			mess = objectMapper.readValue(payload, Message.class);
			borrower = mess.getPayload().getBorrower();
			loanrequest = mess.getPayload().getLoanrequest();
			loan.setLoanrequest(loanrequest);
			loan.setBorrower(borrower);

		} catch (IOException e) {
		//	e.printStackTrace();
			throw e;
		}
		return loan;

	}

	/**
	 * 
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
				myLogger.info(mybundle.getString("rulesetPath")+ arguments[0]);
				if (unprocessedArguments.contains(rulesetPathArgumentAsString)) {
					return rulesetPathArgumentAsString;
				}
			}

		}
		return null;
	}

	/**
	 * 
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
	 * 
	 * @param key
	 * @param arguments
	 * @return
	 * 
	 */
	private String getMessage(String key, Object... arguments) {

		return formatter.getMessage(key, arguments);
	}

	/**
	 * 
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
				myLogger.info(mybundle.getString("serverurl")+ serverurl);
				topicNameRq = arguments[2];
				myLogger.info(mybundle.getString("topicNameRq")+ topicNameRq);
				topicNameRp = arguments[3];
    			myLogger.info(mybundle.getString("topicNameRp")+ topicNameRp);
				consumergroup = arguments[4];
    			myLogger.info(mybundle.getString("consumergroup")+ consumergroup);

			}

		}

	}

	public static void main(String... args) {

		BusinessApplication mybizApp = new BusinessApplication();
		myLogger.info(mybundle.getString("notif_Biz_App"));
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(OPTIONS, args);
			IlrPath rulesetPath = mybizApp.getRulesetPath(commandLine, args);
			mybizApp.setUpkafkaParam(commandLine, args);
			mybizApp.setUpBussinessApp(serverurl, 2, consumergroup, topicNameRq, rulesetPath, topicNameRp);

		} catch (IllegalArgumentException | ParseException | IlrFormatException | IlrSessionException
				| IOException exception) {
			System.err.println(exception.getMessage());
		}

	}
}