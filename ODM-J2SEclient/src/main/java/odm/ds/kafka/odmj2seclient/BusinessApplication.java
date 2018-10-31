package odm.ds.kafka.odmj2seclient;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;
import ilog.rules.res.session.IlrSessionCreationException;
import ilog.rules.res.session.IlrSessionException;
import odm.ds.kafka.consumer.SampleConsumer;

public class BusinessApplication {

	// Create a Consumer on topic Rq
	// Execute the rulesetPath
	// Create a Producer on topic Rp

	public static void setUpBussinessApp(String serverurl, int numberparam, String consumergroup, String topicNameRq, IlrPath rulesetPath, Loan loan, String
			topicNameRp) throws IlrFormatException, IlrSessionCreationException, JsonGenerationException, JsonMappingException, IlrSessionException, IOException {
		SampleConsumer myConsumer=new SampleConsumer();
		myConsumer.consumeMessage(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicNameRq);
		RESJSEExecution execution = new RESJSEExecution();
		execution.executeRuleset(rulesetPath, loan, serverurl, topicNameRp);
		
	}
}