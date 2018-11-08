package odm.ds.kafka.odmjse.clientapp;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.producer.SampleProducer;
import odm.ds.loanvalidation.Loan;
import odm.ds.loanvalidation.Message;

public class ClientMultiMessage extends ClientApplication{
	
	
	private static final Options OPTIONS=new Options();
	final static Logger myLogger=Logger.getLogger(SampleConsumer.class.getName());
	static ResourceBundle mybundle = ResourceBundle.getBundle("MessagesBundle");
	private static String serverurl;
	private static String topicNameRq;
	private static String topicNameRp;
	private static String consumergroup;
	
	/**
	 * Create a Producer on topic Rq, Create a Consumer on topic Rp 
	 * @param serverurl
	 * @param numberparam
	 * @param topicNameRq
	 * @param message
	 * @param consumergroup
	 * @param topicNameRp
	 * 
	 */
	
	public static void setUpClientAppforMulti(String serverurl, int numberparam, String topicNameRq, String message, String key, String consumergroup, String topicNameRp) {
		SampleProducer myProducer=new SampleProducer();
		myProducer.sendmessageString(myProducer.producerInstance(serverurl, numberparam), topicNameRq, message);
		SampleConsumer myConsumer=new SampleConsumer();
		myConsumer.consumeMessage(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRp);
	
	}
	
	/**
	 * 
	 * @param commandLine
	 * @param arguments
	 * @return
	 * 
	 */
	public String getPayload(CommandLine commandLine, String[] arguments) {
		int nbOfArguments = arguments.length;
		if (nbOfArguments != 0) {
			List<String> unprocessedArguments = Arrays.asList(commandLine.getArgs());
			if (!unprocessedArguments.isEmpty()) {
				String payloadAsString = arguments[0];
				if (unprocessedArguments.contains(payloadAsString)) {
					return payloadAsString;
				}
			}

		}
		return null;
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
		
	/**
	 * 
	 * @param payload
	 * @return
	 * 
	 */
	public Loan loanJson(String payload) {

		ObjectMapper objectMapper = new ObjectMapper();
		Loan loan = null;

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
	 * @return
	 * 
	 */
	public String generateKey() {
		Date date = new Date();
		System.out.println(date.getTime());
		Random rand = new Random();
		int value = rand.nextInt(1000);
		String key = "" + date.getTime() + "" + value;
		return key;
	}
	
	/**
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws JsonProcessingException
	 * 
	 */
	public String BuildMessage(String message, String key) throws JsonProcessingException {
		Loan myLoan = loanJson(message);
		Message myMess = new Message();
		myMess.setPayload(myLoan);
		myMess.setKey(key);
		ObjectMapper mapper = new ObjectMapper();
		String finalMess = mapper.writeValueAsString(myMess);
		return finalMess;
	}		  

	public static void main(String... args) {

		myLogger.info(mybundle.getString("notif_client_App"));
		ClientMultiMessage myClientMulti=new ClientMultiMessage();
		try {

			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(OPTIONS, args);
			myClientMulti.setUpkafkaParam(commandLine, args);
			for (int i = 0; i < 10; i++) {
				String mykey = myClientMulti.generateKey();
				ClientMultiMessage.setUpClientAppforMulti(serverurl, 2, topicNameRq,
						myClientMulti.BuildMessage(myClientMulti.getPayload(commandLine, args), mykey), mykey, consumergroup, topicNameRp);
			}
		} catch (ParseException | JsonProcessingException e) {
			e.printStackTrace();
		}

	}	


}
