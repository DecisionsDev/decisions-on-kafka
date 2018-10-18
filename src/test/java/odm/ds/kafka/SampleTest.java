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

public class SampleTest {
	
	@Test
	public void PublishAndConsumeOneMessage(){
		// Create a Producer instance
		SampleProducer myProducer1=new SampleProducer();
		System.out.println("Start test");
		//Send the String message 45
        myProducer1.sendmessageString(myProducer1.producerInstance("localhost:9092", 2),"moussatest","45");        
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
		assertEquals("45",value);

	}

	public void MultiConsumerSingleTopic(){
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
	
	@Test
	public void MultiConsumerMultiTopic() {
		
		SampleProducer myProducer2=new SampleProducer();
	    myProducer2.sendmessageString(myProducer2.producerInstance("localhost:9092", 2),"odmtest1","20");
	    myProducer2.sendmessageString(myProducer2.producerInstance("localhost:9092", 2),"odmtest2","30");
		SampleConsumer myConsumer2=new SampleConsumer();
		KafkaConsumer<String, String> consumer2= myConsumer2.consumerInstance("localhost:9092", 3, "test4");
		//Soubscribe to the topic moussatest
		consumer2.subscribe(Arrays.asList("odmtest1"),new ConsumerRebalanceListener() {
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
		for(ConsumerRecord<String,String> record2:records2){
			System.out.printf("Offset=%d, key=%s,value=%s\n",record2.offset(),record2.key(),record2.value());
			value2=record2.value();
		}
		// If the message is equal to 45 then assert True
		//assertEquals("20",value2);
		SampleConsumer myConsumer3=new SampleConsumer();
		KafkaConsumer<String, String> consumer3= myConsumer3.consumerInstance("localhost:9092", 3, "test4");
		//Soubscribe to the topic moussatest
		consumer3.subscribe(Arrays.asList("odmtest2"),new ConsumerRebalanceListener() {
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
		for(ConsumerRecord<String,String> record3:records3){
			System.out.printf("Offset=%d, key=%s,value=%s\n",record3.offset(),record3.key(),record3.value());
			value2=record3.value();
		}
		// If the message is equal to 45 then assert True
		assertEquals("20",value3);

	}
}
