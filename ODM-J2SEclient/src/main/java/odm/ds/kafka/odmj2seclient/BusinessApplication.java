package odm.ds.kafka.odmj2seclient;

import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_ERROR_INVALID_RULESET_PATH;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_ERROR_MISSING_RULESET_PATH;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;
import ilog.rules.res.session.IlrSessionCreationException;
import ilog.rules.res.session.IlrSessionException;
import odm.ds.kafka.consumer.SampleConsumer;

public class BusinessApplication {

	
	final static Logger myLogger=Logger.getLogger(SampleConsumer.class.getName());
	static ResourceBundle mybundle = ResourceBundle.getBundle("MessagesBundle");
	private static String serverurl;
	private static String topicNameRq;
	private static String topicNameRp;
	private static String consumergroup;
	private static final Options OPTIONS=new Options();
	private static final MessageFormatter formatter=new MessageFormatter();

	/**
	 * Create a Consumer on topic Rq
	 * Execute the rulesetPath
	 * Create a Producer on topic Rp
	 * 
	 * @param serverurl
	 * @param numberparam
	 * @param consumergroup
	 * @param topicNameRq
	 * @param rulesetPath
	 * @param loan
	 * @param topicNameRp
	 * @throws IlrFormatException
	 * @throws IlrSessionCreationException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IlrSessionException
	 * @throws IOException
	 * 
	 */
	public static void setUpBussinessApp(String serverurl, int numberparam, String consumergroup, String topicNameRq, IlrPath rulesetPath, String
			topicNameRp) throws IlrFormatException, IlrSessionCreationException, JsonGenerationException, JsonMappingException, IlrSessionException, IOException {
		SampleConsumer myConsumer=new SampleConsumer();
//		String payload=myConsumer.consumeMessage(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRq);
		String[] payloads=myConsumer.consumeMessage2(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRq);
//		for (String payload:payloads) System.out.println("payload is "+payload);
		RESJSEExecution execution = new RESJSEExecution();
		for (String payload:payloads)
		execution.executeRuleset(rulesetPath, loanJson(payload), serverurl, topicNameRp);
//		consumeAndexec(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRq, serverurl, rulesetPath, topicNameRp);
		
	}
	
	public static void consumeAndexec(KafkaConsumer<String, String> consumer, String topicName,String serverurl,IlrPath rulesetPath,String
			topicNameRp) throws IlrFormatException, IlrSessionCreationException, JsonGenerationException, JsonMappingException, IlrSessionException, IOException {
		RESJSEExecution execution = new RESJSEExecution();
		consumer.subscribe(Arrays.asList(topicName),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		myLogger.info(mybundle.getString("topic_name")+" "+topicName);
//		long endTimeMillis = System.currentTimeMillis() + 1000;
		System.out.println("Inside consume message");
		int i=0;
		
		while(true){
		@SuppressWarnings("deprecation")
		ConsumerRecords<String,String> records=consumer.poll(1000);
		if(!records.isEmpty()) {
		for(ConsumerRecord<String,String> record:records) {

//			System.out.printf("Offset=%d, key=%s,value=%s\n",record.offset(),record.key(),record.value());
			//myLogger.info("Offset=%d, key=%s,value=%s\n "+record.offset()+record.key()+record.value());
			myLogger.info(record.value());
//		if (System.currentTimeMillis() > endTimeMillis) {
            // do some clean-up
  //          return;
			execution.executeRuleset(rulesetPath, loanJson(record.value()), serverurl, topicNameRp);
			}
		}
		break;
	
		}
		consumer.close();
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
	    
	 public static void main(String...args) {
		 
		 BusinessApplication mybizApp=new BusinessApplication();
		 System.out.println("Business Application");
		 // Demarrer une biz application qui se comporte en consumer et producer
		 
		
		 try {
	    	 CommandLineParser parser=new DefaultParser();
	    	 CommandLine commandLine = parser.parse(OPTIONS, args);
	    	 IlrPath rulesetPath = mybizApp.getRulesetPath(commandLine, args);
	    	 mybizApp.setUpBussinessApp(serverurl, 2, consumergroup, topicNameRq, rulesetPath, topicNameRp);
			 
		 } catch(IllegalArgumentException | ParseException | IlrFormatException | IlrSessionException | IOException exception) {
			 System.err.println(exception.getMessage());
		 }

    	 
		 
	 }
}