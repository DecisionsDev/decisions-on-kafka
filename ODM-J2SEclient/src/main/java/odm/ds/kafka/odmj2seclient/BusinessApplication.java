package odm.ds.kafka.odmj2seclient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Logger;

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
	 public static void main(String...args) {
		 
		 BusinessApplication mybizApp=new BusinessApplication();
		 System.out.println("Business Application");
		// mybizApp.setUpBussinessApp(serverurl, numberparam, consumergroup, topicNameRq, rulesetPath, topicNameRp);
		 
	 }
}