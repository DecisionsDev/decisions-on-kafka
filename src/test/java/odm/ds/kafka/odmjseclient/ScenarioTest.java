package odm.ds.kafka.odmjseclient;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;
import ilog.rules.res.session.IlrSessionCreationException;
import ilog.rules.res.session.IlrSessionException;
import odm.ds.kafka.odmjse.businessapp.BusinessApplication;
import odm.ds.kafka.odmjse.clientapp.ClientApplication;
import odm.ds.loanvalidation.Reply;

public class ScenarioTest {
	
	@Test
	public void twoClientsOneBusinessApp() throws Exception{
		
		// Create the client App 1
		System.out.println("******************************************************************************");
		System.out.println("*                         Start the Test 1                                   *");
		System.out.println("******************************************************************************");
		// Send the message
		String payload1="{\"borrower\":{\"lastName\" : \"Smith\",\"firstName\" : \"Alice\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":200000}, \"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":10000,\"loanToValue\":1.20}}";
		String serverurl="localhost:9092";
		String topicNameRq="requests";
		String topicNameRp="replies";
		String consumergroup1="testConsumeGroup1";		
		BusinessApplication bizApp=new BusinessApplication();
		String consumergroup="baConsumerGroup";
		IlrPath rulesetPath=IlrPath.parsePath("/test_deployment/loan_validation_with_score_and_grade");
		Thread t3 = new Thread(() -> {
			try {
				System.out.println("*************************Test1-BusinessApp");
				bizApp.setUpBussinessApp(serverurl, 3, consumergroup, topicNameRq, rulesetPath, topicNameRp);
			} catch (IlrFormatException|JsonGenerationException|JsonMappingException|IlrSessionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		t3.start();
		t3.sleep(4000);	
		String key1="12345";
		String message1="********************************Test1-ClientApp1";
		MyThread t1=new MyThread(payload1, serverurl, topicNameRq, topicNameRp, consumergroup1, key1,message1);
		t1.start();
		t1.sleep(2000);
		// Create the client App 2
		ClientApplication myClientApp2=new ClientApplication();
		String payload2="{\"borrower\":{\"lastName\" :\"Doe\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200, \"yearlyIncome\":55000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":110000,\"loanToValue\":1.20}}";
		String consumergroup2="testConsumeGroup2";
		String key2="12346";
		String message2="********************************Test1-ClientApp2";
		MyThread t2=new MyThread(payload2, serverurl, topicNameRq, topicNameRp, consumergroup2, key2,message2);			
		t2.start();
		t2.sleep(2000);
		Reply rep=new Reply();
		assertEquals(key1,Reply.ExtractKeyFromJson(t1.str));
		assertEquals(key2,Reply.ExtractKeyFromJson(t2.str));


	}
	
	@Test
	public void oneClientTwoBusinessApp() throws InterruptedException {
		// Create the client App
		System.out.println("*******************************************************************************");
		System.out.println("*                     Start the test 2                                        *");
		System.out.println("*******************************************************************************");
		ClientApplication myClientApp3=new ClientApplication();
		String payload3="{\"borrower\":{\"lastName\" : \"Smith\",\"firstName\" : \"Alice\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":200000}, \"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":10000,\"loanToValue\":1.20}}";
		String serverurl="localhost:9092";
		String topicNameRq="requests";
		String topicNameRp="replies";
		String consumergroup3="testConsumeGroup3";
		String key3="12347";
		String message3="********************************Test2-ClientApp1";
		for (int i=0;i<4;i++) 
		{
		MyThread t=new MyThread(payload3, serverurl, topicNameRq, topicNameRp, consumergroup3,new Integer(i).toString() ,message3);
		t.start();
		t.sleep(1000);
		}
		// Create the Business App 1
		BusinessApplication bizApp2=new BusinessApplication();
		String consumergroup="baConsumerGroup";
		Thread t5 = new Thread(() -> {
			try {
				IlrPath rulesetPath=IlrPath.parsePath("/test_deployment/loan_validation_with_score_and_grade");
				System.out.println("*************************Test 2- Business App 1");
				bizApp2.setUpBussinessApp(serverurl, 3, consumergroup, topicNameRq, rulesetPath, topicNameRp);
			} catch (IlrFormatException|JsonGenerationException|JsonMappingException|IlrSessionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		});
		t5.start();
		t5.sleep(4000);
		// Create the Business App 2
		BusinessApplication bizApp3=new BusinessApplication();
		Thread t6 = new Thread(() -> {
			try {
				IlrPath rulesetPath=IlrPath.parsePath("/test_deployment/loan_validation_with_score_and_grade");
				System.out.println("*************************Test 2 - Business App 2");
				bizApp3.setUpBussinessApp(serverurl, 3, consumergroup, topicNameRq, rulesetPath, topicNameRp);
			} catch (IlrFormatException|JsonGenerationException|JsonMappingException|IlrSessionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		t6.start();
		t6.sleep(4000);
//		t5.stop();
//		t6.stop();
		
		
		// Assert that the number of messages received by BA 1 is superior to zero
		// Assert taht the number of messages received by BA 2 is superior to zero
		// Assert that the number of messages received by BA 1 + the number of messages received by BA 2 is equal to the sent one. 
	}
	
	//@Test
	public void brokeTest() throws InterruptedException {
		// Create the Client App
		ClientApplication myClientApp4=new ClientApplication();
		System.out.println("**************************************************************************");
		System.out.println("*                         Start the test 3                               *");
		System.out.println("**************************************************************************");
		String payload1="{\"borrower\":{\"lastName\" : \"Smith\",\"firstName\" : \"Alice\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":200000}, \"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":10000,\"loanToValue\":1.20}}";
		String serverurl="localhost:9092";
		String topicNameRq="requests";
		String topicNameRp="replies";
		String consumergroup1="testConsumeGroup1";
		String key4="12348";
		Thread t7 = new Thread(() -> {
			System.out.println("*******************Test 3 - ClientApp 1");
			myClientApp4.setUpClientAppAndConsume(serverurl, 2, topicNameRq, payload1, key4, consumergroup1, topicNameRp);
		});

		t7.start();
		t7.sleep(2000);
		// Create the BA APP 1
		BusinessApplication bizApp4=new BusinessApplication();
		String consumergroup="baConsumerGroup";
		Thread t8 = new Thread(() -> {
			try {
				IlrPath rulesetPath=IlrPath.parsePath("/test_deployment/loan_validation_with_score_and_grade");
				System.out.println("*************************Test 3 - Business App 1");
				bizApp4.setUpBussinessApp(serverurl, 3, consumergroup, topicNameRq, rulesetPath, topicNameRp);
			} catch (IlrFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IlrSessionCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IlrSessionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		t8.start();
		t8.sleep(2000);

		// Create the BA APP 2
		BusinessApplication bizApp5=new BusinessApplication();
		Thread t9 = new Thread(() -> {
			try {
				IlrPath rulesetPath=IlrPath.parsePath("/test_deployment/loan_validation_with_score_and_grade");
				System.out.println("*************************Test 3 -  Business App 2");
				bizApp4.setUpBussinessApp(serverurl, 3, consumergroup, topicNameRq, rulesetPath, topicNameRp);
			} catch (IlrFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IlrSessionCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IlrSessionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		t9.start();
		t9.sleep(2000);

		// Count that we have received the right number of message
		// Stop the BA1
		// Create the Client App
		ClientApplication myClientApp5=new ClientApplication();
		String key5="12348";
		Thread t10 = new Thread(() -> {
			System.out.println("*******************Test 3 - Client App 2");
			myClientApp4.setUpClientAppAndConsume(serverurl, 2, topicNameRq, payload1, key5, consumergroup1, topicNameRp);
		});

		t10.start();

		// Verify that we have the right amount of message.
	}
	
	private class MyThread extends Thread {

		String str;
		String payload1;
		String serverurl;
		String topicNameRq;
		String topicNameRp;
		String consumergroup1;
		String key1;
		String infomess;
		public MyThread(String payload1,String serverurl,String topicNameRq,String topicNameRp,String consumergroup1,String key1, String infomess) {
			this.str=str;
			this.payload1=payload1;
			this.serverurl=serverurl;
			this.topicNameRq=topicNameRq;
			this.topicNameRp=topicNameRp;
			this.consumergroup1=consumergroup1;
			this.key1=key1;
			this.infomess=infomess;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println(infomess);
			ClientApplication myClientApp1=new ClientApplication();
			 try {
				str = myClientApp1.setUpClientAppAndConsume(serverurl, 2, topicNameRq,myClientApp1.BuildMessage(payload1, key1) , key1, consumergroup1, topicNameRp);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println("***********"+str);

			
		}
		
	}

}
