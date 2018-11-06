package odm.ds.kafka.odmj2seclient;

import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_ERROR_INVALID_RULESET_PATH;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_ERROR_MISSING_RULESET_PATH;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;
import loan.Borrower;
import loan.LoanRequest;
import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.producer.SampleProducer;

public class ClientApplication {
	
	

	private static final Options OPTIONS=new Options();
	private static final MessageFormatter formatter=new MessageFormatter();
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
	
	public static void setUpClientApp(String serverurl, int numberparam, String topicNameRq, String message, String key, String consumergroup, String topicNameRp) {
		SampleProducer myProducer=new SampleProducer();
		myProducer.sendmessageString(myProducer.producerInstance(serverurl, numberparam), topicNameRq, message);
		SampleConsumer myConsumer=new SampleConsumer();
		myConsumer.consumeMessage3(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), key, topicNameRp);
	
	}
	
	public static void setUpClientApp2(String serverurl, int numberparam, String topicNameRq, String message, String consumergroup, String topicNameRp) {
		SampleProducer myProducer=new SampleProducer();
		myProducer.sendmessageString(myProducer.producerInstance(serverurl, numberparam), topicNameRq, message);
		SampleConsumer myConsumer=new SampleConsumer();
		myConsumer.consumeMessage(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRp);
	
	}
	  private String getMandatoryRulesetPathArgument(CommandLine commandLine, String[] arguments) {
	    	System.out.println("Inside getMandatory");
	    	int nbOfArguments=arguments.length;
	    	if(nbOfArguments!=0) {
	    		List<String> unprocessedArguments=Arrays.asList(commandLine.getArgs());
	    		if(!unprocessedArguments.isEmpty()) {
	    			String rulesetPathArgumentAsString=arguments[0];
	    			System.out.println("rulesetPathArgumentAsString "+arguments[0]);
	    			System.out.println("lenght "+arguments[1]);
	    			if(unprocessedArguments.contains(rulesetPathArgumentAsString)) {
	    				return rulesetPathArgumentAsString;
	    			}
	    		}
	    		
	    	}
	    	return null;
	    }
	  private IlrPath getRulesetPath(CommandLine commandLine, String[] arguments) throws IllegalArgumentException {
	    	String rulesetPathArgumentAsString=getMandatoryRulesetPathArgument(commandLine, arguments);
	    	if(rulesetPathArgumentAsString==null) {
	    		String errorMessage=getMessage(SAMPLE_ERROR_MISSING_RULESET_PATH, getMessage(SAMPLE_ERROR_MISSING_RULESET_PATH));
	    		throw new IllegalArgumentException(errorMessage);
	    	}
	    	try {
	    		return IlrPath.parsePath(rulesetPathArgumentAsString);
	    				
	    	} catch (IlrFormatException exception) {
	    		System.out.println(rulesetPathArgumentAsString);
	    		String errorMessage=getMessage(SAMPLE_ERROR_INVALID_RULESET_PATH, rulesetPathArgumentAsString);
	    		System.out.println(errorMessage);
	    		throw new IllegalArgumentException(errorMessage);	
	    	}
	    }
	  private String getMessage(String key, Object... arguments) {
	    	
	    	return formatter.getMessage(key, arguments);
	    } 
	 
		public static String getPayload(CommandLine commandLine, String[] arguments) {
			int nbOfArguments=arguments.length;
	    	if(nbOfArguments!=0) {
	    		List<String> unprocessedArguments=Arrays.asList(commandLine.getArgs());
	    		if(!unprocessedArguments.isEmpty()) {
	    			String payloadAsString=arguments[0];
	    			if(unprocessedArguments.contains(payloadAsString)) {
	    				return payloadAsString;
	    			}
	    		}
	    		
	    	}
	    	return null;
		}
		 public static void setUpkafkaParam(CommandLine commandLine, String[] arguments) {

				int nbOfArguments=arguments.length;
		    	if(nbOfArguments!=0) {
		    		List<String> unprocessedArguments=Arrays.asList(commandLine.getArgs());
		    		if(!unprocessedArguments.isEmpty()) {
		    			serverurl=arguments[1];
		    			System.out.println("The server url is "+serverurl);
		    			topicNameRq=arguments[2];
		    			System.out.println("The topic name for Request is "+topicNameRq);
		    			topicNameRp=arguments[3];
		    			System.out.println("The topic name for Replies is"+topicNameRp);
		    			consumergroup=arguments[4];
		    			System.out.println("The Consumer group is "+consumergroup);
		    			
		    		}
		    		
		    	}

			}
		 
		 public static Loan loanJson( String payload) {
			 
			 ObjectMapper objectMapper=new ObjectMapper();
			 Loan loan=null;				 

				try {
					loan=objectMapper.readValue(payload, Loan.class);
					System.out.println("Loan Borrower "+loan.getBorrower());
					System.out.println("Loan Request "+loan.getLoanrequest());
					
				} catch(IOException e) {
					e.printStackTrace();
				}
				return loan;
		
		 }

		 public static String generateKey() {
			Date date=new Date();
			System.out.println(date.getTime());
			String key=""+date.getTime();
			return key;
		 }
		 
		  public static String BuildMessage(String message, String key) throws JsonProcessingException {
			  Loan myLoan=loanJson(message);
			  Message myMess=new Message();
			  myMess.setPayload(myLoan);
			  myMess.setKey(key);
			  ObjectMapper mapper = new ObjectMapper();
			  String finalMess = mapper.writeValueAsString(myMess);
			  return finalMess;
		  }
		  

		  public static void main(String...args) {
		
			  System.out.println("The Client Application is Running");
			  try {

	    		CommandLineParser parser=new DefaultParser();
				CommandLine commandLine = parser.parse(OPTIONS, args);
				setUpkafkaParam(commandLine, args);
				String mykey=generateKey();
				ClientApplication.setUpClientApp(serverurl, 2, topicNameRq,  BuildMessage(getPayload(commandLine, args), mykey), mykey, consumergroup, topicNameRp);
	//			ClientApplication.setUpClientApp(serverurl, 2, topicNameRq,  getPayload(commandLine, args), consumergroup, topicNameRp);
				
					} catch (ParseException | JsonProcessingException e) {
			// 		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
	}
	

}
