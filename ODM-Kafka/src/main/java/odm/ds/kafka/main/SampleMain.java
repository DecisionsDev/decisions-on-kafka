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
package odm.ds.kafka.main;
import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.producer.SampleProducer;

public class SampleMain {
	
	/**
	 *  A sample Main show the creation of a producer and a consumer
	 * 
	 * 
	 */
	public static void main(String...args) {
		
		SampleProducer myProducer=new SampleProducer();
		// Send a string message to a topic
		myProducer.sendmessageString(myProducer.producerInstance(args[0].toString(),args.length), args[1].toString(),args[2].toString());
		// Create an instance of Consumer
		SampleConsumer myConsumer=new SampleConsumer();
		// Consume the message
		myConsumer.consumeMessage(myConsumer.consumerInstance(args[0].toString(), args.length,args[2].toString()), args[1].toString());
//	
	}

}
