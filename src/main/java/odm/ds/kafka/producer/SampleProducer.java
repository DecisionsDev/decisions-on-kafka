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

import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SampleProducer {
	final Logger mylogger=Logger.getLogger(SampleProducer.class.getName());
	ResourceBundle mybundle=ResourceBundle.getBundle("MessagesBundle");
	
	/**
	 * Sets up kafka Producer with properties information.
	 * @param serverurl
	 * @param numberparam
	 * @return
	 * 
	 */
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
		Producer<String,String> producer=new KafkaProducer<String,String>(props);
		return producer;
	}
	
	/**
	 * Send an iterative message to a topic
	 * 
	 * @param producer
	 * @param topicname
	 */
	public void sendmessage(Producer<String, String> producer,String topicname){
		String topicName=topicname;
		for(int i=0;i<6;i++)
			producer.send(new ProducerRecord<String,String>(topicName,Integer.toString(i),Integer.toString(i)));
		mylogger.info(mybundle.getString("notif_sent"));
		producer.close();
		
	}
	
	/**
	 * Uses the KafProducer to send the provided string message to the provided topic.
	 * 
	 * @param producer
	 * @param topicname
	 * @param message
	 * 
	 */
	public void sendmessageString(Producer<String, String> producer,String topicName,String message){
		Date date=new Date();
		System.out.println(date.getTime());
		Random rand = new Random(); 
		int value = rand.nextInt(1000); 
		String tkey=""+date.getTime()+""+value;
		System.out.println(message);
		producer.send(new ProducerRecord<String,String>(topicName,tkey,message));
		mylogger.info(mybundle.getString("notif_sent")+" "+topicName);
		producer.close();
		
	}

}
