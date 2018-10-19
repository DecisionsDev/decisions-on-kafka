package odm.ds.kafka.main;
import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.producer.SampleProducer;

public class SampleMain {
	
	public static void main(String...args) {
		
		SampleProducer myProducer=new SampleProducer();
		// Send a string message to a topic
		myProducer.sendmessageString(myProducer.producerInstance(args[0].toString(),args.length), args[1].toString(),args[2].toString());
		// Create an instance of Consumer
		SampleConsumer myConsumer=new SampleConsumer();
		// Consume the message
		myConsumer.consumeMessage(myConsumer.consumerInstance(args[0].toString(), args.length,args[2].toString()), args[1].toString());
	
	}

}
