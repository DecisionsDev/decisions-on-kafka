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

import java.io.IOException;
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

import odm.ds.loanvalidation.Reply;

public class SampleConsumer {

	final Logger myLogger=Logger.getLogger(SampleConsumer.class.getName());
	ResourceBundle mybundle = ResourceBundle.getBundle("messages");
	
	/**
	Create an instance of KafkaConsumer using provided information about the server and additional information needed for the
	configuration of kafka.
	@param serverurl
	@param numberparam
	@param consumergroup
	@return consumer
	*
	*/
	public KafkaConsumer<String, String> consumerInstance(String serverurl, int numberparam, String consumergroup) {

		myLogger.info("Current Locale: " + Locale.getDefault());
		if (numberparam == 0) {
			myLogger.severe(mybundle.getString("NO_TOPIC_NAME"));
		}
		Properties props = new Properties();
		props.put("bootstrap.servers", serverurl);
		props.put("group.id", consumergroup);
		props.put("enable.auto.commit", "true");
		props.put("enable.auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "8000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		return consumer;
	}


	/**
	 * Listen to all messages sent to the provided Kafka consumer on the provided topic, but display 
	 * only the message with key value equal to key
	 * @param consumer
	 * @param key
	 * @param topicName
	 * 
	 */
	public String consumeMessage(KafkaConsumer<String, String> consumer, String key, String topicName) {
		String data=null;
		consumer.subscribe(Arrays.asList(topicName), new ConsumerRebalanceListener() {
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				System.out.printf("%s topic-partitions are revoked from this consumer\n",
						Arrays.toString(partitions.toArray()));
			}

			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				System.out.printf("%s topic-partitions are assigned to this consumer\n",
						Arrays.toString(partitions.toArray()));
			}
		});
		myLogger.info(mybundle.getString("TOPIC_NAME") + " " + topicName);
		boolean gotmessage=false;
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			myLogger.info(mybundle.getString("WAITING"));
			if (!records.isEmpty()) {
				for (ConsumerRecord<String, String> record : records) {
					try {
						Reply reply=new Reply();
						if (key.equals(reply.ExtractKeyFromJson(record.value()))) {
							myLogger.info(record.value());
							myLogger.info(mybundle.getString("receive_key") + reply.ExtractKeyFromJson(record.value()));
							gotmessage=true;
							data=record.value();
							break;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						myLogger.severe(mybundle.getString("issue_message")+e.getMessage());
					}
				}
				if (gotmessage==true) break;
			}
			
			
		}
		consumer.close();
		return data;

	}

	


}
