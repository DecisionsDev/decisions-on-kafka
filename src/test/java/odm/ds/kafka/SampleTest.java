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
		myProducer1.producerInstance("localhost:9092", 2);
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
}
