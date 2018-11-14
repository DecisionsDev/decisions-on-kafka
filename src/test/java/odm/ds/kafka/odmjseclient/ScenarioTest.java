package odm.ds.kafka.odmjseclient;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import odm.ds.kafka.odmjse.businessapp.BusinessApplication;
import odm.ds.kafka.odmjse.clientapp.ClientApplication;

public class ScenarioTest {
	
	@Test
	public void twoClientsOneBusinessApp() throws Exception{
	
		// Create the client App 1
		ClientApplication myClientApp1=new ClientApplication();
		// Send the message 
		// Create the client App 2
		ClientApplication myClientApp2=new ClientApplication();
		// Create the Business App
		// Send the message
		BusinessApplication bussApp=new BusinessApplication();
		// Listen to message from Client Application
		// Affet to string 1 the message from the Business App
		// Assert that myClientApp1 receive the right payload
		String str1 = "report\":{\"borrower\":{\"firstName\":\"John\",\"lastNa\r\n" + 
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
				" been approved\",\"insurance\":\"none\",\"yearlyRepayment\":30698.547299960715}\"";
		String str2 = "report\":{\"borrower\":{\"firstName\":\"John\",\"lastNa\r\n" + 
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
				" been approved\",\"insurance\":\"none\",\"yearlyRepayment\":30698.547299960715}\"";
		assertEquals(str1,str2);
		// Affect to the string 2 the message from the Business App 2
		// Assert that myClientApp2 receive the right payload
		String str3 = "report\":{\"borrower\":{\"firstName\":\"Alice\",\"lastN\r\n" + 
				"ame\":\"Smith\",\"yearlyIncome\":200000,\"zipCode\":\"75012\",\"creditScore\":200,\"spouse\":\r\n" + 
				"null,\"latestBankruptcy\":null,\"latestBankruptcyDate\":null,\"latestBankruptcyReason\r\n" + 
				"\":null,\"latestBankruptcyChapter\":-1,\"birthDate\":191977200000,\"ssn\":{\"areaNumber\"\r\n" + 
				":\"800\",\"groupCode\":\"12\",\"serialNumber\":\"0234\"},\"SSN\":{\"areaNumber\":\"800\",\"groupC\r\n" + 
				"ode\":\"12\",\"serialNumber\":\"0234\"}},\"loan\":{\"numberOfMonthlyPayments\":48,\"startDat\r\n" + 
				"e\":1540822814178,\"amount\":10000,\"loanToValue\":1.2,\"duration\":4},\"validData\":true\r\n" + 
				",\"insuranceRequired\":true,\"insuranceRate\":0.02,\"approved\":true,\"messages\":[\"Low\r\n" + 
				"risk loan\",\"Congratulations! Your loan has been approved\"],\"yearlyInterestRate\":\r\n" + 
				"0.055,\"monthlyRepayment\":232.56475227242964,\"message\":\"Low risk loan\\nCongratula\r\n" + 
				"tions! Your loan has been approved\",\"insurance\":\"2%\",\"yearlyRepayment\":2790.7770\r\n" + 
				"27269156}\"";
		String str4 = "report\":{\"borrower\":{\"firstName\":\"Alice\",\"lastN\r\n" + 
				"ame\":\"Smith\",\"yearlyIncome\":200000,\"zipCode\":\"75012\",\"creditScore\":200,\"spouse\":\r\n" + 
				"null,\"latestBankruptcy\":null,\"latestBankruptcyDate\":null,\"latestBankruptcyReason\r\n" + 
				"\":null,\"latestBankruptcyChapter\":-1,\"birthDate\":191977200000,\"ssn\":{\"areaNumber\"\r\n" + 
				":\"800\",\"groupCode\":\"12\",\"serialNumber\":\"0234\"},\"SSN\":{\"areaNumber\":\"800\",\"groupC\r\n" + 
				"ode\":\"12\",\"serialNumber\":\"0234\"}},\"loan\":{\"numberOfMonthlyPayments\":48,\"startDat\r\n" + 
				"e\":1540822814178,\"amount\":10000,\"loanToValue\":1.2,\"duration\":4},\"validData\":true\r\n" + 
				",\"insuranceRequired\":true,\"insuranceRate\":0.02,\"approved\":true,\"messages\":[\"Low\r\n" + 
				"risk loan\",\"Congratulations! Your loan has been approved\"],\"yearlyInterestRate\":\r\n" + 
				"0.055,\"monthlyRepayment\":232.56475227242964,\"message\":\"Low risk loan\\nCongratula\r\n" + 
				"tions! Your loan has been approved\",\"insurance\":\"2%\",\"yearlyRepayment\":2790.7770\r\n" + 
				"27269156}\"";
		assertEquals(str3,str4);
	}
	
	@Test
	public void oneClientTwoBusinessApp() {
		// Create the client App
		
		// Create the Business App 1
		
		// Create the Business App 2
		
		// Assert that the number of messages received by BA 1 is superior to zero
		// Assert taht the number of messages received by BA 2 is superior to zero
		// Assert that the number of messages received by BA 1 + the number of messages received by BA 2 is equal to the sent one. 
	}
	
	public void brokeTest() {
		// Create the Client App
		// Create the BA APP 1
		// Create the BA APP 2
		// Count that we have received the right number of message
		// Stop the BA1
		// Create the Client App
		// Verify that we have the right amount of message.
	}

}
