package odm.ds.kafka;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

public class MockedKafkaTest {
    private static final String TOPIC = "topic-1";
    private static final String BROKERHOST = "127.0.0.1";
    private static final String BROKERPORT = "9092";
    private static final String ZKPORT = "2181";

    private String nodeId = "0";
    private String zkConnect = "localhost:" + ZKPORT;
    private KafkaServerStartable server;
    KafkaProducer<String, String> producer;
    KafkaConsumer<String, String> consumer;
    public static final Logger mylogger=Logger.getLogger(MockedKafkaTest.class.getName());    

    @Before
    public void setup() throws IOException {
        //zookeeper
    //    startZK();
        //start kafka
    //    startKafka();
        // setup producer
   //     setupProducer();
        // setup consumer
  //      setupConsumer();
        
        //Consumer listening
  //      liveConsumer();
    }

    @After
    public void tearDown() throws Exception {
    //	liveConsumer();
        server.shutdown();
        server.awaitShutdown();
    }

    private static void startZK() throws IOException {
    	mylogger.info("Zookeepr Started");
        final File zkTmpDir = File.createTempFile("zookeeper", "test");
        zkTmpDir.delete();
        zkTmpDir.mkdir();

        new Thread() {
            
            public void run() {
                ZooKeeperServerMain.main(new String [] {ZKPORT,  zkTmpDir.getAbsolutePath()});
            }
        }.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    private void startKafka() {
        Properties props = new Properties();
        props.put("broker.id", nodeId);
        props.put("port", BROKERPORT);
        props.put("zookeeper.connect", zkConnect);
        props.put("host.name", "127.0.0.1");
        props.put("offsets.topic.replication.factor", "1");
        KafkaConfig conf = new KafkaConfig(props);
        server = new KafkaServerStartable(conf);
        server.startup();
        mylogger.info("Kafka started");
    }

    private void setupProducer() {
        Properties producerProps = new Properties();
        producerProps.setProperty("bootstrap.servers", BROKERHOST + ":" + BROKERPORT);
        producerProps.setProperty("acks", "all");
        producerProps.setProperty("retries", "0");
        producerProps.setProperty("batch.size"," 16384");
        producerProps.setProperty("linger.ms", "1");
        producerProps.setProperty("buffer.memory", "33554432");
        producerProps.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        producerProps.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(producerProps);
        mylogger.info("Producer started");
      
    }

    private void setupConsumer() {
        Properties consumerProps = new Properties();
        consumerProps.setProperty("bootstrap.servers", BROKERHOST + ":" + BROKERPORT);
        consumerProps.setProperty("group.id", "group0");
        consumerProps.setProperty("client.id", "consumer0");
        consumerProps.setProperty("enable.auto.commit", "true");
        consumerProps.setProperty("enable.auto.commit.interval.ms", "1000");
        consumerProps.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("auto.offset.reset", "earliest");
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Arrays.asList(TOPIC));
        mylogger.info("Consumer started");

    }
    public void liveConsumer(){
  		consumer.subscribe(Arrays.asList(TOPIC));
  		System.out.println("Subscribed to topic"+TOPIC);
          try {
  		while(true){
  		@SuppressWarnings("deprecation")
		ConsumerRecords<String,String> records=consumer.poll(0100);
  		for(ConsumerRecord<String,String> record:records)
  			System.out.printf("Offset=%d, key=%s,value=%s\n",record.offset(),record.key(),record.value());
  				}
          	
      		}
  		 finally 
  			{
  				consumer.close();}
  			}

    public void testProducerConsumer() throws Exception {
//    	ProducerRecord<String, String > record=new ProducerRecord<String, String>(TOPIC,Integer.toString(0),Integer.toString(0));
  //  	ProducerRecord<Integer, byte> record=new ProducerRecord<Integer, byte[]>(1,new Byte[]{1,2});
   // 	ProducerRecord<K, V>
    	consumer.subscribe(Arrays.asList(TOPIC));
        mylogger.info("Start test");
  //      Send the message produced

  //      for(int i=0;i<8;i++)
  //			producer.send(new ProducerRecord<String,String>(TOPIC,Integer.toString(i),Integer.toString(i)));
			producer.send(new ProducerRecord<String,String>(TOPIC,"12","15"));
			mylogger.info("Message Sent");
			producer.close(); 
		    @SuppressWarnings("deprecation")
			ConsumerRecords<String, String> records = consumer.poll(50000);
		    
//		    assertEquals(54,records.count());
		    Iterator<ConsumerRecord<String, String>> recordIterator = records.iterator();
		    ConsumerRecord<String, String> record = recordIterator.next();
		    System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
//		    assertEquals(12, record.key());
		 //   assertEquals("15",record.value());

    	}
  // Consume the message
}
