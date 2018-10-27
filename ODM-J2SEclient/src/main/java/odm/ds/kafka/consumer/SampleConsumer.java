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
package odm.ds.kafka.consumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class SampleConsumer {

	final Logger myLogger=Logger.getLogger(SampleConsumer.class.getName());
	ResourceBundle mybundle = ResourceBundle.getBundle("MessagesBundle");
	
	/**
	Create an Instance of a Consumer
	@param serverurl
	@param numberparam
	@param consumergroup
	@return consumer
	*
	*/
	public KafkaConsumer<String, String> consumerInstance(String serverurl, int numberparam, String consumergroup){
		
		myLogger.info("Current Locale: " + Locale.getDefault());
		System.out.println("Hello 0");
		if(numberparam==0){
			myLogger.severe(mybundle.getString("no_topic_name"));
		}
		System.out.println("Hello 1");
		Properties props=new Properties();
		props.put("bootstrap.servers", serverurl);
		//"localhost:9092"
		props.put("group.id", consumergroup);
		props.put("enable.auto.commit", "true");
		props.put("enable.auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "8000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String,String> consumer=new KafkaConsumer<String,String>(props);
		myLogger.info("Groud id "+consumergroup);
		System.out.println("Fin");
		//consumer.subscribe(Arrays.asList(topicName));
			return consumer;
		}
		
	/**
	 * Listen to a topic and Consume coming messages
	 *  @param consumer
	 *  @param topicName
	 *  
	 */
	
	public void consumeMessage(KafkaConsumer<String, String> consumer, String topicName){
		consumer.subscribe(Arrays.asList(topicName),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		myLogger.info(mybundle.getString("topic_name")+" "+topicName);
		long endTimeMillis = System.currentTimeMillis() + 1000;
//		while(true){
		@SuppressWarnings("deprecation")
		ConsumerRecords<String,String> records=consumer.poll(0100);
		for(ConsumerRecord<String,String> record:records)

//			System.out.printf("Offset=%d, key=%s,value=%s\n",record.offset(),record.key(),record.value());
			//myLogger.info("Offset=%d, key=%s,value=%s\n "+record.offset()+record.key()+record.value());
			myLogger.info(record.value());
//		if (System.currentTimeMillis() > endTimeMillis) {
            // do some clean-up
  //          return;
//		}
//	}

		
	}

}
