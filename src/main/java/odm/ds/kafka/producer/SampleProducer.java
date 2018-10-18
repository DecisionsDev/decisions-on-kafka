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
package odm.ds.kafka.producer;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SampleProducer {
	final Logger mylogger=Logger.getLogger(SampleProducer.class.getName());
	ResourceBundle mybundle=ResourceBundle.getBundle("MessagesBundle");
	
	// Create an instance of Producer
	public Producer<String, String> producerInstance (String serverurl,int numberparam) 
	{
		mylogger.info("Current Locale: " + Locale.getDefault());
		if(numberparam==0){
			mylogger.severe(mybundle.getString("wrong_topic"));
			return null;
		}
		String server=serverurl;
		Properties props=new Properties();
		props.put("bootstrap.servers", server);
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("PARTITIONER_CLASS_CONFIG",DataPartitioner.class.getCanonicalName());
		props.put("partitions.0", "partition 0");
		props.put("partitions.1", "partition 1");
		Producer<String,String> producer=new KafkaProducer<String,String>(props);
		return producer;
	}
	
	// Send a message the iterrative message to a topic
	public void sendmessage(Producer<String, String> producer,String topicname){
		String topicName=topicname;
		for(int i=0;i<9;i++)
			producer.send(new ProducerRecord<String,String>(topicName,Integer.toString(i),Integer.toString(i)));
			mylogger.info(mybundle.getString("notif_sent"));
			producer.close();
		
	}
	
	// Send a String message to a topic
	public void sendmessageString(Producer<String, String> producer,String topicname,String message){
		String topicName=topicname;
			producer.send(new ProducerRecord<String,String>(topicName,"2",message));
			mylogger.info(mybundle.getString("notif_sent"));
			producer.close();
		
	}

	public static void main(String args[]){
		// Create an instance of producer
		SampleProducer myProducer=new SampleProducer();
		// Send a string message to a topic
		myProducer.sendmessageString(myProducer.producerInstance(args[1].toString(),args.length), args[0].toString(),"Hello");
	}

}
