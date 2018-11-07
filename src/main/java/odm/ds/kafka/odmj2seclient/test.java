package odm.ds.kafka.odmj2seclient;



import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import loan.LoanRequest;
import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.producer.SampleProducer;
import java.util.UUID;


public class test {

	public static void main(String[] args){
		Date date=new Date();
		System.out.println(date.getTime());
		System.out.println(date.getTime());
	//	Random rand = new Random();
		int random = (int )(Math.random() * 50 + 1);
		String key=""+date.getTime()+""+random;
		random = (int )(Math.random() * 50 + 1);
		System.out.println("the key "+key);
		key=""+date.getTime()+""+random;
		System.out.println("the key "+key);
		key=""+date.getTime()+""+random;
		System.out.println("the key "+key);
		UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        System.out.println("Random UUID String = " + randomUUIDString);
		carJsonSample();
		loanjsonSample();
		loanRequestJson();
		loanJson();
		
		SampleProducer myProducer=new SampleProducer();
		// Send a string message to a topic
		 String serverurl="localhost:9092";
		 String topicName="moussatest";
		 int numberparam=2;
		 String consumergroup="test2";
		 String loanJson =
				 "{\"borrower\":{\"firstName\" : \"Johnny\", \"lastName\" : \"Smith\",\"birthDate\":191977200000,\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":20000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":100000,\"loanToValue\":1.20}}";

		 SampleProducer myproducer=new SampleProducer();
		 myproducer.sendmessageString(myproducer.producerInstance(serverurl, numberparam), topicName, loanJson);
		// Create an instance of Consumer
		SampleConsumer myConsumer=new SampleConsumer();
		 myConsumer.consumeMessage(myConsumer.consumerInstance(serverurl, numberparam, consumergroup), topicName);
		// Consume the message
	//	myConsumer.consumeMessage(myConsumer.consumerInstance(args[0].toString(), args.length,args[2].toString()), args[1].toString());

	}
	public static void carJsonSample() {
		ObjectMapper objectMapper = new ObjectMapper();

		String carJson =
		    "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

		try {
		    Car car = objectMapper.readValue(carJson, Car.class);

		    System.out.println("car brand = " + car.getBrand());
		    System.out.println("car doors = " + car.getDoors());
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static void loanjsonSample() {
		ObjectMapper objectMapper=new ObjectMapper();
		String loanJson =
			    "{ \"firstName\" : \"John\", \"lastName\" : \"Smith\",\"birthDate\":191977200000,\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":20000}";

		try {
			loan.Borrower borrower=objectMapper.readValue(loanJson, loan.Borrower.class);
			System.out.println("Borrower lastname "+borrower.getLastName());
			System.out.println("Borrower firstname "+borrower.getFirstName());
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	 public static loan.LoanRequest loanRequestJson(){

		 ObjectMapper objectMapper=new ObjectMapper();
		 LoanRequest loanrequest=null;
		 String requestJson =
				    "{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":100000,\"loanToValue\":1.20}";

			try {
				loanrequest=objectMapper.readValue(requestJson, loan.LoanRequest.class);
				System.out.println("Loan Amout "+loanrequest.getAmount());
				System.out.println("Loan Duration "+loanrequest.getDuration());
				
			} catch(IOException e) {
				e.printStackTrace();
			}
			return loanrequest;

	 }
	 public static Loan loanJson() {
		 
		 ObjectMapper objectMapper=new ObjectMapper();
		 Loan loan=null;
		 String loanJson =
			 "{\"borrower\":{\"firstName\" : \"John\", \"lastName\" : \"Smith\",\"birthDate\":191977200000,\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":20000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":100000,\"loanToValue\":1.20}}";
				 

			try {
				loan=objectMapper.readValue(loanJson, Loan.class);
				System.out.println("Loan Borrower "+loan.getBorrower());
				System.out.println("Loan Request "+loan.getLoanrequest());
				
			} catch(IOException e) {
				e.printStackTrace();
			}
			return loan;
	
	 }
}
