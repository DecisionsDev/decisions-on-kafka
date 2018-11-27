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
package odm.ds.kafka.odmjse.clientapp;


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
import com.fasterxml.jackson.databind.ObjectMapper;

import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.producer.SampleProducer;
import odm.ds.loanvalidation.Loan;
import odm.ds.loanvalidation.Message;

import java.util.Random;


public class ClientApplication {
	
	

	private static final Options OPTIONS=new Options();
	final static Logger myLogger=Logger.getLogger(SampleConsumer.class.getName());
	static ResourceBundle mybundle = ResourceBundle.getBundle("MessagesBundle");
	private static String serverurl;
	private static String topicNameRq;
	private static String topicNameRp;
	private static String consumergroup;
	private static Integer nbmessage;
	
	/**
	 * Create a client application which is going to be in first a kafka producer sending the json payload as a string to the topic.
	 * In second time after sending the payload, the client application acts a Kafka consumer waiting for message to consume.
	 * @param serverurl
	 * @param numberparam
	 * @param topicNameRq
	 * @param message
	 * @param consumergroup
	 * @param topicNameRp
	 * 
	 */
	
	public String setUpClientAppAndConsume(String serverurl, int numberparam, String topicNameRq, String message, String key, String consumergroup, String topicNameRp) {
	//public static void setUpClientApp(String serverurl, int numberparam, String topicNameRq, String message, String key, String topicNameRp) {
		SampleProducer myProducer=new SampleProducer();
		System.out.println("Create the producer instance");
		myProducer.sendmessageString(myProducer.producerInstance(serverurl, numberparam), topicNameRq, message);
		System.out.println("Sent the message");
		SampleConsumer myConsumer=new SampleConsumer();
		System.out.println("Create Consumer instance");
//		myConsumer.consumerInstanceCons(serverurl, numberparam);
		String value=myConsumer.consumeMessage(myConsumer.consumerInstance(serverurl, numberparam,consumergroup), key, topicNameRp);
		System.out.println("Consume the message");
		return value;
	}
	
	 /**
	  * Parse the command line and then return the JSON payload as a string
	  * @param commandLine
	  * @param arguments
	  * @return a string corresponding to the payload
	  * 
	  */
		public String getPayload(CommandLine commandLine, String[] arguments) {
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
		
		/**
		 * Initializes the properties we will need to run the client application and setup kafka instance.
		 * Initialization of serverul, topicNameRq (topic for requests),  topicNameRp(topic for replies), 
		 * nbmessage : the number of time the message is going to be sent.
		 * @param commandLine
		 * @param arguments
		 * 
		 */
		 public void setUpkafkaParam(CommandLine commandLine, String[] arguments) {

				int nbOfArguments=arguments.length;
		    	if(nbOfArguments!=0) {
		    		List<String> unprocessedArguments=Arrays.asList(commandLine.getArgs());
		    		if(!unprocessedArguments.isEmpty()) {
		    			serverurl=arguments[1];
		    			myLogger.info(mybundle.getString("serverurl")+ serverurl);
		    			topicNameRq=arguments[2];
		    			myLogger.info(mybundle.getString("topicNameRq")+ topicNameRq);
		    			topicNameRp=arguments[3];
		    			myLogger.info(mybundle.getString("topicNameRp")+ topicNameRp);
//		    			consumergroup=arguments[4];
//		    			myLogger.info(mybundle.getString("consumergroup")+ consumergroup);
		    			nbmessage=new Integer(arguments[4]);
		    			
		    		}
		    		
		    	}

			}
		 /**
		  * 
		  * Takes a JSON payload as a string, converts it to an object Loan which has a Borrower and a LoanRequest object 
		  * @param payload
		  * @return a Loan object
		  * 
		  */
		 public Loan loanJson( String payload) {
			 
			 ObjectMapper objectMapper=new ObjectMapper();
			 Loan loan=null;				 

				try {
					loan=objectMapper.readValue(payload, Loan.class);
//					myLogger.info(mybundle.getString("Loan_Borrower")+ loan.getBorrower());
//					myLogger.info(mybundle.getString("Loan_Request")+ loan.getLoanrequest());
					
				} catch(IOException e) {
					e.printStackTrace();
				}
				return loan;
		
		 }

		 /**
		  * Generate a String key, the key is built from a random value and current timestamp. 
		  * @return a string key
		  * 
		  */
		 public String generateKey() {
			Date date=new Date();
			Random rand = new Random(); 
			int value = rand.nextInt(1000); 
			String key=""+date.getTime()+""+value;
			return key;
		 }
		 /**
		  * Takes a string message and a string key then converts the message message in an object Loan using the method
		  * loanJson, later takes the object Loan and the string key, builds with that an object Message.
		  * @param message
		  * @param key
		  * @return
		  * @throws JsonProcessingException
		  * 
		  */
		  public String BuildMessage(String message, String key) throws JsonProcessingException {
			  Loan myLoan=this.loanJson(message);
			  Message myMess=new Message();
			  myMess.setPayload(myLoan);
			  myMess.setKey(key);
			  ObjectMapper mapper = new ObjectMapper();
			  String finalMess = mapper.writeValueAsString(myMess);
			  return finalMess;
		  }
		  
		  /**
		   * The main method, creates a client application which is going to to send n times a message, the message is a payload json
		   * and n is the number of times the application sends the message, n is defined by the user as argument.
		   * @param args
		   * 
		   */
	public static void main(String... args) {

		myLogger.info(mybundle.getString("notif_client_App"));
		try {

			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(OPTIONS, args);
			ClientApplication myClientApp = new ClientApplication();
			myClientApp.setUpkafkaParam(commandLine, args);
			for (int i = 0; i <nbmessage ; i++) {				
				String mykey = myClientApp.generateKey();
				myClientApp.setUpClientAppAndConsume(serverurl, 2, topicNameRq,myClientApp.BuildMessage(myClientApp.getPayload(commandLine, args), mykey), mykey,
						mykey, topicNameRp);
		//		ClientApplication.setUpClientApp(serverurl, 2, topicNameRq,myClientApp.BuildMessage(myClientApp.getPayload(commandLine, args), mykey), mykey, topicNameRp);
			}
		} catch (ParseException | JsonProcessingException e) {
			e.printStackTrace();
		}

	}	

}
