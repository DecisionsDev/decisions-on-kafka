# IBM ODM Decision Server in Message-driven Architecture with Apache Kafka
[![Build Status](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka.svg?token=YUDWXbAcjsyzHsqNF4a8&branch=master)](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![GitHub last commit (branch)](https://img.shields.io/github/last-commit/ODMDev/odm-ondocker/dev.svg)](https://github.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Introduction

Message driven architecture puts in interaction client applications with services replying to the requests from client applications.
Some advantages of message driven architecture are the scalability with load balancing, and the asynchronous communication.
This type of architecture is based on a broker allowing to subscribe to a topic and publish messages.

In this sample we show how to use IBM Operational Decision Manager (ODM) with Apache Kafka which is a distributed streaming platform allowing to setup a message driven architecture.
![Sample Architecture](docs/images/architecture.png)


In the sample architecture, we have Client Applications sending a loan request and Decision Services executing the loan request against a ruleset, for more information about the loan validation sample, see the References section.
Message driven architecture 
We have one Kafka broker and two topics in the sample architecture.
The first topic is for Client Applications to put their loan request, and the second topic is for replies where the Decision Services put the result after executing against a ruleset.
All the Decision Services have the same Kafka consumer group, and Client Applications have different consumer groups.  


### Workflow Description
The diagrame bellow describes the workflow

![Sample Architecture](docs/images/sequence.png)

1. Client application acts as Kafka Producer and send his payload to the topic named Requests.

2. Decision Service implementing ODM which acts as a Kafka consumer and execute the payload.

3. After executing the payload against the ruleset, the Decision Service acts as a Kafka producer and put the json result in the topic named Replies.

4. The Client Application acts as Kafka consumer and get the message corresponding to the result of his request.

## Requirements

* Apache Kafka 2.11
* IBM Operational Decision Manager 8.9.2
* Apache Maven 3

## Before starting
* Make sure that you have Kafka installed, and start Kafka by launching zookeeper and Kafka-server.
* Clone the project repository from github.
`$ git clone --branch=odm-integration git@github.ibm.com:MYattara/ODM-DecisionServer-JSE-Kafka.git`
* In the pom file, set the property `<ibm.odm.install.dir></ibm.odm.install.dir>` with your odm installation directory, ` For example : <ibm.odm.install.dir>C:\ODM8920</ibm.odm.install.dir>`

If you have a shell command line
* Create the Kafka topic for request : `$ Kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic requests`
* Create the Kafka topic for replies : `$ Kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic replies`


If you have a Windows command line

* Create the Kafka topic for request : `$ Kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic requests`
* Create the Kafka topic for replies : `$ Kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic replies`


## Building the source code
Use the following Maven command to build the source code.
`$ mvn clean install`

## Scenario Running

According to the scenario we'll use several Client Applications sending one or many payload to several Decision Services.
the Client Application is a JSE Application that sends a payload with information about the borrower and the loan request, and waits for the approval or a reject of his loan request.
The Decision Service is a JSE ODM execution server in-memory persistence application, which executes the payload against ODM loan validation sample ruleset and returns a result (approved or rejected) to the JSE Client Application.

* Client Application command structure: 
```
$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="
<JsonPayload> <Kafka server url> <topic for requests> <topic for replies> <number of message>"
 -Dexec.classpathScope="test"

```
`<JsonPayload>`  The loan request payload we want to evaluate.

`<Kafka server URL>` The Kafka broker URL. In the sample we use `localhost:9092` change it if necessary  if yours is different please change it.

`<topic for requests>` The topic where the Client Application puts loan requests and acts as a producer, and Decision Service listens to it and acts as a Kafka consumer.

`<topic for replies>` The topic where Decision Service puts the result of the loan request execution against the decision service, The Decision Service acts as a producer and the Client Application acts as a consumer
getting the message from the topic. 

`<number of message>` How many times we want to send the loan request payload for execution.

* Decision Service command structure: 
```
$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.decisionapp.DecisionService" -Dexec.args="
<rulesetPath> <Kafka server URL> <topic for requests> <topic for replies> <Consumer Group> " 
-Dexec.classpathScope="test" -Dibm.odm.install.dir="C:\ODM8920" 

```

`<rulesetPath>` The IBM ODM ruleset path.

`<Consumer Group>` The Kafka consumer group which the Decision Service is part of.
-   [Scenario 1 : Two Client Applications sending payload to one Decision Service and waiting for the result](docs/chapters/subscenario1.md)
-   [Scenario 2 : Load balancing between two Decision Services](docs/chapters/subscenario2.md)
-   [Scenario 3 : Availability after one Decision Service is down](docs/chapters/subscenario3.md)

## Issues and contributions

To contribute or for any issue please use GitHub Issues tracker.

## References
* [IBM Operational Decision Manager Developer Center](https://developer.ibm.com/odm/)
* [Java EE rule session](https://www.ibm.com/support/knowledgecenter/en/SSQP76_8.9.2/com.ibm.odm.dserver.rules.samples/res_smp_topics/smp_res_javaee.html)
* [Loan Validation Sample](https://www.ibm.com/support/knowledgecenter/en/SSQP76_8.5.1/com.ibm.odm.dserver.rules.samples/designer_smp_topics/smp_rd_engineintmultproj_det.html)
* [Apache Kafka](https://kafka.apache.org/)

## License
[Apache 2.0](LICENSE)

[**Next** ![""](docs/images/next.jpg)](docs/chapters/subscenario1.md)
## Notice
© Copyright IBM Corporation 2018.

[![Build Status](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka.svg?token=YUDWXbAcjsyzHsqNF4a8&branch=master)](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![GitHub last commit (branch)](https://img.shields.io/github/last-commit/ODMDev/odm-ondocker/dev.svg)](https://github.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
