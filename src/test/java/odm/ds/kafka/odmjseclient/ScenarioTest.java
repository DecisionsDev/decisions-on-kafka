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
		t3.start();
		t3.sleep(5000);

		
		
		
		String key1="12345";
		MyThread t1=new MyThread(payload1, serverurl, topicNameRq, topicNameRp, consumergroup1, key1);
		t1.start();
		t1.sleep(5000);
//		myClientApp1.setUpClientApp(serverurl, 2, topicNameRq, payload1, key1, consumergroup1, topicNameRp);
		// Create the client App 2
		ClientApplication myClientApp2=new ClientApplication();
		String payload2="{\"borrower\":{\"lastName\" :\"Doe\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200, \"yearlyIncome\":55000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":110000,\"loanToValue\":1.20}}";
		String consumergroup2="testConsumeGroup2";
		String key2="12346";
		Thread t2 = new Thread(() -> {
			System.out.println("********************************Test1-ClientApp2");
			
			try {
				String str3 = myClientApp2.setUpClientAppAndConsume(serverurl, 3, topicNameRq, myClientApp2.BuildMessage(payload2, key2), key2, consumergroup2, topicNameRp);
				System.out.println("Value"+str3);
				String str4 ="{\"key\":\"12346\",report\":{\"borrower\":{\"firstName\":\"John\",\"lastNa\r\n" + 
						"me\":\"Doe\",\"yearlyIncome\":55000,\"zipCode\":\"75012\",\"creditScore\":200,\"spouse\":null\r\n" + 
						",\"latestBankruptcy\":null,\"latestBankruptcyDate\":null,\"latestBankruptcyReason\":nu\r\n" + 
						"ll,\"latestBankruptcyChapter\":-1,\"birthDate\":191977200000,\"ssn\":{\"areaNumber\":\"80\r\n" + 
						"0\",\"groupCode\":\"12\",\"serialNumber\":\"0234\"},\"SSN\":{\"areaNumber\":\"800\",\"groupCode\"\r\n" + 
						":\"12\",\"serialNumber\":\"0234\"}},\"loan\":{\"numberOfMonthlyPayments\":48,\"startDate\":1\r\n" + 
						"540822814178,\"amount\":110000,\"loanToValue\":1.2,\"duration\":4},\"validData\":true,\"i\r\n" + 
						"nsuranceRequired\":false,\"insuranceRate\":0.0,\"approved\":false,\"messages\":[\"Risky\r\n" + 
						"loan\",\"Too big Debt/Income ratio: 0.56\",\"We are sorry. Your loan has not been ap\r\n" + 
						"proved\"],\"yearlyInterestRate\":0.055,\"monthlyRepayment\":2558.212274996726,\"messag\r\n" + 
						"e\":\"Risky loan\\nToo big Debt/Income ratio: 0.56\\nWe are sorry. Your loan has not\r\n" + 
						" been approved\",\"insurance\":\"none\",\"yearlyRepayment\":30698.547299960715}}";

//				assertEquals(str3,str4);

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		});
		t2.start();
		t2.sleep(2000);
//		myClientApp2.setUpClientApp(serverurl, 3, topicNameRq, payload2, key2, consumergroup2, topicNameRp);
		// Create the Business App
		// Send the message
	//	t3.stop();
		// Affect to string 1 the message from the Business App
		String str2 ="{\"key\":\"12345\",\"report\":{\"borrower\":{\"firstName\":\"Alice\",\"lastName\":\"Smith\",\"yearlyIncome\":200000,\"zipCode\":\"75012\",\"creditScore\":200,\"spouse\":\r\n" + 
				"null,\"latestBankruptcy\":null,\"latestBankruptcyDate\":null,\"latestBankruptcyReason\r\n" + 
				"\":null,\"latestBankruptcyChapter\":-1,\"birthDate\":191977200000,\"ssn\":{\"areaNumber\"\r\n" + 
				":\"800\",\"groupCode\":\"12\",\"serialNumber\":\"0234\"},\"SSN\":{\"areaNumber\":\"800\",\"groupC\r\n" + 
				"ode\":\"12\",\"serialNumber\":\"0234\"}},\"loan\":{\"numberOfMonthlyPayments\":48,\"startDat\r\n" + 
				"e\":1540822814178,\"amount\":10000,\"loanToValue\":1.2,\"duration\":4},\"validData\":true\r\n" + 
				",\"insuranceRequired\":true,\"insuranceRate\":0.02,\"approved\":true,\"messages\":[\"Low\r\n" + 
				"risk loan\",\"Congratulations! Your loan has been approved\"],\"yearlyInterestRate\":\r\n" + 
				"0.055,\"monthlyRepayment\":232.56475227242964,\"message\":\"Low risk loan\\nCongratula\r\n" + 
				"tions! Your loan has been approved\",\"insurance\":\"2%\",\"yearlyRepayment\":2790.7770\r\n" + 
				"27269156}}";
		Reply rep=new Reply();
		
		assertEquals(key1,Reply.ExtractKeyFromJson(t1.str));
	
		// Affect to the string 2 the message from the Business App 2
		// Assert that myClientApp2 receive the right payload

	}
	
	//@Test
	public void oneClientTwoBusinessApp() throws InterruptedException {
		// Create the client App
		System.out.println("*******************************************************************************");
		System.out.println("*                     Start the test 2                                        *");
		System.out.println("*******************************************************************************");
		ClientApplication myClientApp3=new ClientApplication();
		String payload1="{\"borrower\":{\"lastName\" : \"Smith\",\"firstName\" : \"Alice\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":200000}, \"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":10000,\"loanToValue\":1.20}}";
		String serverurl="localhost:9092";
		String topicNameRq="requests";
		String topicNameRp="replies";
		String consumergroup1="testConsumeGroup1";
		String key3="12347";
		Thread t4 = new Thread(() -> {
			System.out.println("******************* Test 2- ClientApp");
			myClientApp3.setUpClientAppAndConsume(serverurl, 2, topicNameRq, payload1, key3, consumergroup1, topicNameRp);
		});

		t4.start();
		t4.sleep(2000);
		// Create the Business App 1
		BusinessApplication bizApp2=new BusinessApplication();
		String consumergroup="baConsumerGroup";
		Thread t5 = new Thread(() -> {
			try {
				IlrPath rulesetPath=IlrPath.parsePath("/test_deployment/loan_validation_with_score_and_grade");
				System.out.println("*************************Test 2- Business App 1");
				bizApp2.setUpBussinessApp(serverurl, 3, consumergroup, topicNameRq, rulesetPath, topicNameRp);
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
		t5.start();
		t5.sleep(2000);
		// Create the Business App 2
		BusinessApplication bizApp3=new BusinessApplication();
		Thread t6 = new Thread(() -> {
			try {
				IlrPath rulesetPath=IlrPath.parsePath("/test_deployment/loan_validation_with_score_and_grade");
				System.out.println("*************************Test 2 - Business App 2");
				bizApp3.setUpBussinessApp(serverurl, 3, consumergroup, topicNameRq, rulesetPath, topicNameRp);
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
		t6.start();
		t6.sleep(5000);
		t5.stop();
		t6.stop();
		
		
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
		public MyThread(String payload1,String serverurl,String topicNameRq,String topicNameRp,String consumergroup1,String key1) {
			this.str=str;
			this.payload1=payload1;
			this.serverurl=serverurl;
			this.topicNameRq=topicNameRq;
			this.topicNameRp=topicNameRp;
			this.consumergroup1=consumergroup1;
			this.key1=key1;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("********************************Test1-ClientApp1");
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
