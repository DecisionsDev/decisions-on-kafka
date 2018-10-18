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

package odm.ds.kafka;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.producer.SampleProducer;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

public class SampleTest extends Thread{
	
	@Test
	public void PublishAndConsumeOneMessage(){
		// Create a Producer instance
		SampleProducer myProducer1=new SampleProducer();
		System.out.println("Start test");
		//Send the String message 45
        myProducer1.sendmessageString(myProducer1.producerInstance("localhost:9092", 2),"moussatest","go");        
		// Create the consumer listening to the localhost and being part of CustomerGroup test2
		SampleConsumer myConsumer1=new SampleConsumer();
		KafkaConsumer<String, String> consumer1= myConsumer1.consumerInstance("localhost:9092", 3, "test2");
		//Soubscribe to the topic moussatest
		consumer1.subscribe(Arrays.asList("moussatest"),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		// Consumer the message 
		@SuppressWarnings("deprecation")
		ConsumerRecords<String,String> records=consumer1.poll(0100);
		String value=null;
		for(ConsumerRecord<String,String> record:records){
			System.out.printf("Offset=%d, key=%s,value=%s\n",record.offset(),record.key(),record.value());
			value=record.value();
		}
		// If the message is equal to 45 then assert True
		assertEquals("go",value);

	}
	

	public void MultiConsumerSingleTopic(){
		// Create a Producer instance
		SampleProducer myProducer2=new SampleProducer();
		System.out.println("Start server");
		//Send the String message 45
 //      myProducer2.sendmessageString(myProducer2.producerInstance("localhost:9092", 2),"partitiontest","20");
		myProducer2.sendmessage(myProducer2.producerInstance("localhost:9092", 2), "partitiontest");

//		Thread monthread=new Thread(()->{	
					// Create the consumer listening to the localhost and being part of CustomerGroup test2
			System.out.println("Start Thread 1");
			SampleConsumer myConsumer2=new SampleConsumer();
			
			KafkaConsumer<String, String> consumer2= myConsumer2.consumerInstance("localhost:9092", 3, "test2");
			//Soubscribe to the topic moussatest
			System.out.println("Parite 2");
			consumer2.subscribe(Arrays.asList("partitiontest"),new ConsumerRebalanceListener() {
	            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
	                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
	            }
	            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
	                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
	            }
	        
			});
			// Consumer the message 
			@SuppressWarnings("deprecation")
			ConsumerRecords<String,String> records2=consumer2.poll(0100);
			String value2=null;
			int i=0;
			for(ConsumerRecord<String,String> record2:records2){
				System.out.printf("Offset=%d, key=%s,value=%s\n",record2.offset(),record2.key(),record2.value());
				value2=record2.value();
				i++;
			}
			assertEquals(430,i);
	//		});

	//		monthread.start();

//			Thread monthread2=new Thread(()->{	
				System.out.println("Start Thread 2");
				//Send the String message 45
//		        myProducer2.sendmessageString(myProducer2.producerInstance("localhost:9092", 2),"partitiontest","20");
//		        myProducer2.sendmessage(myProducer2.producerInstance("localhost:9092", 2), "partitiontest");
				// Create the consumer listening to the localhost and being part of CustomerGroup test2
				SampleConsumer myConsumer3=new SampleConsumer();
				KafkaConsumer<String, String> consumer3= myConsumer3.consumerInstance("localhost:9092", 3, "test2");
				//Soubscribe to the topic moussatest
				consumer3.subscribe(Arrays.asList("partitiontest"),new ConsumerRebalanceListener() {
		            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
		            }
		            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
		            }
		        });
				// Consumer the message 
				@SuppressWarnings("deprecation")
				ConsumerRecords<String,String> records3=consumer3.poll(0100);
				String value3=null;
				int j=0;
				for(ConsumerRecord<String,String> record3:records3){
					System.out.printf("Offset=%d, key=%s,value=%s\n",record3.offset(),record3.key(),record3.value());
				//	value2=record2.value();
					j++;
				}
				// If the message is equal to 45 then assert True
				assertEquals(72,j);
//				});
//				monthread2.start();
//				Thread monthread3=new Thread(()->{	
					System.out.println("Start Thread 3");
					SampleConsumer myConsumer4=new SampleConsumer();
					KafkaConsumer<String, String> consumer4= myConsumer4.consumerInstance("localhost:9092", 3, "test2");
					//Soubscribe to the topic moussatest
					consumer3.subscribe(Arrays.asList("partitiontest"),new ConsumerRebalanceListener() {
			            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
			            }
			            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
			                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
			            }
			        });
					// Consumer the message 
					@SuppressWarnings("deprecation")
					ConsumerRecords<String,String> records4=consumer4.poll(0100);
					String value4=null;
					int v=0;
					for(ConsumerRecord<String,String> record3:records3){
						System.out.printf("Offset=%d, key=%s,value=%s\n",record3.offset(),record3.key(),record3.value());
						value3=record3.value();
						v++;
					}
					assertEquals(74,v);
					// If the message is equal to 45 then assert True
				//	assertEquals("30",value3);
//					});
//					monthread3.start();
		
    //    SampleProducer myProducer3=new SampleProducer();
    //    myProducer3.sendmessageString(myProducer3.producerInstance("localhost:9092", 2),"partitiontest","30");
				

		

	}
	
	public void MultiConsumerMultiTopic(){
		//Create a Producer instance
		SampleProducer myProducer2=new SampleProducer();
		myProducer2.sendmessage(myProducer2.producerInstance("localhost:9092", 2), "partitiontest");
		// Create the first Customer
    	// Create the consumer listening to the localhost and being part of CustomerGroup test3
		SampleConsumer myConsumer1=new SampleConsumer();
		KafkaConsumer<String, String> consumer1= myConsumer1.consumerInstance("localhost:9092", 3, "test3");
		//Soubscribe to the topic moussatest
		consumer1.subscribe(Arrays.asList("partitiontest"),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		// Consumer the message 
		@SuppressWarnings("deprecation")
		ConsumerRecords<String,String> records=consumer1.poll(0100);
		int count=0;
		for(ConsumerRecord<String,String> record:records){
			System.out.printf("Offset=%d, key=%s,value=%s\n",record.offset(),record.key(),record.value());
			count++;
		}
		// If the message is equal to 45 then assert True
		assertEquals(3,count);
		SampleConsumer myConsumer2=new SampleConsumer();
		KafkaConsumer<String, String> consumer2= myConsumer2.consumerInstance("localhost:9092", 3, "test3");
		consumer2.subscribe(Arrays.asList("partitiontest"),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		// Consumer the message 
		@SuppressWarnings("deprecation")
		ConsumerRecords<String,String> records2=consumer2.poll(0100);
		int count2=0;
		for(ConsumerRecord<String,String> record2:records2){
			System.out.printf("Offset=%d, key=%s,value=%s\n",record2.offset(),record2.key(),record2.value());
			count2++;
		}
		// If the message is equal to 45 then assert True
		assertEquals(3,count2);
		SampleConsumer myConsumer3=new SampleConsumer();
		KafkaConsumer<String, String> consumer3= myConsumer3.consumerInstance("localhost:9092", 3, "test3");

		consumer3.subscribe(Arrays.asList("partitiontest"),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		// Consumer the message 
		@SuppressWarnings("deprecation")
		ConsumerRecords<String,String> records3=consumer3.poll(0100);
		int count3=0;
		for(ConsumerRecord<String,String> record3:records3){
			System.out.printf("Offset=%d, key=%s,value=%s\n",record3.offset(),record3.key(),record3.value());
			count3++;
		}
		// If the message is equal to 45 then assert True
		assertEquals(3,count3);


		
	}
	
}
