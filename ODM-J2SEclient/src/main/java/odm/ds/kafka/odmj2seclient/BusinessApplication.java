package odm.ds.kafka.odmj2seclient;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;
import ilog.rules.res.session.IlrSessionCreationException;
import ilog.rules.res.session.IlrSessionException;
import odm.ds.kafka.consumer.SampleConsumer;

public class BusinessApplication {


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
		String payload=myConsumer.consumeMessage(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRq);
		RESJSEExecution execution = new RESJSEExecution();
		execution.executeRuleset(rulesetPath, loanJson(payload), serverurl, topicNameRp);
		
	}
	 public static Loan loanJson( String payload) {
		 
		 ObjectMapper objectMapper=new ObjectMapper();
		 Loan loan=null;
		 String loanJson =
			 "{\"borrower\":{\"lastName\" : \"Smith\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"11243344\",\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":20000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":100000,\"loanToValue\":1.20}}";
				 

			try {
				loan=objectMapper.readValue(payload, Loan.class);
				System.out.println("Loan Borrower "+loan.getBorrower());
				System.out.println("Loan Request "+loan.getLoanrequest());
				
			} catch(IOException e) {
				e.printStackTrace();
			}
			return loan;
	
	 }
}