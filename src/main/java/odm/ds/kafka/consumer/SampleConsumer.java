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
import odm.ds.kafka.odmj2seclient.Reply;

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
		if(numberparam==0){
			myLogger.severe(mybundle.getString("no_topic_name"));
		}
		Properties props=new Properties();
		props.put("bootstrap.servers", serverurl);
		props.put("group.id", consumergroup);
		props.put("enable.auto.commit", "true");
		props.put("enable.auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "8000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String,String> consumer=new KafkaConsumer<String,String>(props);
			return consumer;
		}
		
	/**
	 * Listen to a topic and Consume coming messages
	 *  @param consumer
	 *  @param topicName
	 *  
	 */
	
	public String consumeMessage(KafkaConsumer<String, String> consumer, String topicName){
		String data=null;
		consumer.subscribe(Arrays.asList(topicName),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		myLogger.info(mybundle.getString("topic_name")+" "+topicName);
		myLogger.info(" Waiting messages from topic "+topicName);
		while(true){
		@SuppressWarnings("deprecation")
		ConsumerRecords<String,String> records=consumer.poll(1000);
		if(!records.isEmpty()) {
		for(ConsumerRecord<String,String> record:records) {
			myLogger.info(record.value());
			myLogger.info("partition numero %i "+record.partition());
			data=record.value();
			}
		}
		break;
	
		}
		consumer.close();
		return data;
		
	}
	
	/**
	 * 
	 * @param consumer
	 * @param topicName
	 * @return
	 * 
	 */
	public String[] consumeMessage2(KafkaConsumer<String, String> consumer, String topicName){
		String[] data = new String[10];
		consumer.subscribe(Arrays.asList(topicName),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		myLogger.info(mybundle.getString("topic_name")+" "+topicName);
		int i=0;
		while(true){
		ConsumerRecords<String,String> records=consumer.poll(1000);
		if(!records.isEmpty()) {
		for(ConsumerRecord<String,String> record:records) {
			myLogger.info(record.value());
			data[i]=record.value();
			i++;
			}
		}
		break;
	
		}
		consumer.close();
		return data;
		
	}
	
	/**
	 * 
	 * @param consumer
	 * @param key
	 * @param topicName
	 * 
	 */
	public void consumeMessage3(KafkaConsumer<String, String> consumer, String key, String topicName){
		consumer.subscribe(Arrays.asList(topicName),new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
            }
        });
		myLogger.info(mybundle.getString("topic_name")+" "+topicName);
		while(true){
		ConsumerRecords<String,String> records=consumer.poll(1000);
		System.out.println("Waiting for payload ");
		if(!records.isEmpty()) {
		for(ConsumerRecord<String,String> record:records) {
			if(key.equals(Reply.ExtractkeyFromJson(record.value()))) 
			{
			myLogger.info(record.value());
			System.out.println("The receive key is "+Reply.ExtractkeyFromJson(record.value()));
				}
			}
		}
	
		}
		
	}

	


}