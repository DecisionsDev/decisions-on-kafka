# IBM ODM Decision Server in architecture driven message with kafka
[![Build Status](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka.svg?token=YUDWXbAcjsyzHsqNF4a8&branch=master)](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![GitHub last commit (branch)](https://img.shields.io/github/last-commit/ODMDev/odm-ondocker/dev.svg)](https://github.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Features


This sample shows how to use IBM Operational Decision Manager (ODM) with Kafka

![Sample Architecture](docs/images/architecture.png)

### Workflow Description

We demonstrate how to integrate Kafka into ODM by using the loan validation sample.
In this sample, we have Client Applications sending a loan request and Business Applications executing the loan request against a ruleset, for more information about the loan validation sample, see the References section.
We have one kafka broker and two topics in the sample architecture.
The first topic is for Client Applications to put their loan request, and the second topic is for replies where the Business Applications put the result after executing against a ruleset.
All the Business Applications have the same kafka consumer group, and Client Applications have different consumer groups.  

1. N Client applications act as kafka Producer and send their payload to the topic named Requests.

2. M Business Applications implementing ODM which act as a Kafka consumer and execute the payload.

3. After executing the payload against the ruleset, the Bussiness Applications act as a Kafka producer and put the json result in the topic named Replies.

4. The Client Applications act as Kafka consumer and get the message corresponding to the result of his request.

## Requirments

* Apache Kafka
* IBM Operational Decision Manager
* Apache Maven

## Before starting
* Make sure that you have kafka installed, and start kafka by launching zookeeper and kafka-server.
* Clone the project repository from github.
`$ git clone --branch=odm-integration git@github.ibm.com:MYattara/ODM-DecisionServer-JSE-Kafka.git`
* In the pom file, set the property `<ibm.odm.install.dir></ibm.odm.install.dir>` with your odm installation directory, ` For example : <ibm.odm.install.dir>C:\ODM8920</ibm.odm.install.dir>`

If you have a shell command line
* Create the kafka topic for request : `$ kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic requests`
* Create the kafka topic for replies : `$ kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic replies`


If you have a Windows command line

* Create the kafka topic for request : `$ kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic requests`
* Create the kafka topic for replies : `$ kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic replies`


## Building the source code
Use the following Maven command to build the source code.
`$ mvn clean install`

## Scenario Running

According to the sub-scenario we'll use several Client Applications sending one or many payload to several Business Applications.
the Client Application is a JSE Application that sends a payload with information about the borrower and the loan Request, and waits for the approval or a reject of his loan request.
The Business Application is a JSE ODM execution server in-memory persistence application, which executes the payload against ODM loan validation sample ruleset and returns a result (approved or rejected) to the JSE Client Application.

### Sub-scenario 1 : Two Client Applications sending payload to one Business Application and waiting for the result.
The goal of this sub-scenario is to show that each Client Application gets the right answer for his payload it sent to the Business Application.

![use case 1](docs/images/usecase1.png)


* Client Application command structure : 
```
$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="
<JsonPayload> <kafka server url> <topic for requests> <topic for replies> <number of message>"
 -Dexec.classpathScope="test"

```
`<JsonPayload>`  The loan request payload we want to evaluate.

`<kafka server url>` The kafka broker url. In the sample we use `localhost:9092` change it if necessary  if yours is different please change it.

`<topic for requests>` The topic where the Client Application puts loan requests and acts as a producer, and Business Application listens to it and acts as a kafka consumer.

`<topic for replies>` The topic where Business Application puts the result of the loan request execution against the decision service, The Business Application acts as a producer and the Client Application acts as a consumer
getting the message from the topic. 

`<number of message>` How many times we want to send the loan request payload for execution.

* Business Application command structure : 
```
$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjseclient.BusinessApplication" -Dexec.args="
<rulesetPath> <kafka server url> <topic for requests> <topic for replies> <Consumer Group> " 
-Dexec.classpathScope="test" -Dibm.odm.install.dir="C:\ODM8920" 

```

`<rulesetPath>` The IBM ODM ruleset path.

`<Consumer Group>` The kafka consumer group which the Business Application is part of.
 

1. Create the first Client Application : Open a command line in the project ODM-DecisionServer-JSE-Kafka root folder, and then run the command below. It sends a payload corresponding to the loan request. In this request the amount is 10000 and 
the yearlyIncome is 200000. 

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="'{\"borrower\":{\"lastName\" : \"Smith\",\"firstName\" : \"Alice\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":200000},
\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":10000,\"loanToValue\":1.20}}' 'localhost:9092' 'requests' 'replies' 1" -Dexec.classpathScope="test"`

'localhost:9092' is the broker url, if your broker url is different please change it accordingly,'requests' corresponds to the topic where loan requests are put, and 'replies' corresponds to the topic where the Business Application puts the execution
result. 1 is the number of loan request we want the Client Application sends to the Business Application.

 2. Create the second Client Application : Open a second command line in the root folder, and then run the following command. The second client Application sends a loan request with the yearlyIncome 55000 and the amount of loan 110000.
 
`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="'{\"borrower\":{\"lastName\" :\"Doe\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,
 \"yearlyIncome\":55000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":110000,\"loanToValue\":1.20}}' 'localhost:9092' 'requests' 'replies' 1" -Dexec.classpathScope="test"`
  
 3. Run the Business Application :
 
`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.businessapp.BusinessApplication" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'requests' 'replies' 'baconsumegroup'" -Dexec.classpathScope="test" -Dibm.odm.install.dir="C:\ODM8920" `
 
   'baconsumegroup' the consumer group in which is the Business Application.

 
4. Result : 
The loan request should be accepted in the first Client Application, and it should be rejected in the second Client Application.

5. Stop the Business Application before starting the Sub-scenario 2.

### Sub-scenario 2 :  1 Client Application Sending several payload to N Business Applications


The goal of this sub-scenario is to show the load balancing between two Business Applications.

![use case 2](docs/images/usecase2.png)

1. Run your first Business Application that puts its result in out1.txt.

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.businessapp.BusinessApplication" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'requests' 'replies' 'test2'" -Dexec.classpathScope="test"
 -Dibm.odm.install.dir="C:\ODM8920" > out1.txt `

2. Run your second Business Application that puts its result in out2.txt

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.businessapp.BusinessApplication" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'requests' 'replies' 'test2'" -Dexec.classpathScope="test"
 -Dibm.odm.install.dir="C:\ODM8920" > out2.txt`
 
3. Run a client Application that will send seven messages.

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="'{\"borrower\":{\"lastName\" : \"Smtih\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,
 \"yearlyIncome\":55000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":110000,\"loanToValue\":1.20}}' 'localhost:9092' 'requests' 'replies' 7 " -Dexec.classpathScope="test"`

4. When the Client Application terminates and the Business Applications are displaying the message "waiting for payload" stop both of your Business Applications and look at the files out1.txt and out2.txt. You will see that the seven payloads have been split for execution between these two Business Applications.

### Sub-scenario 3 : Availability after one Business Application is down
The goal of this sub-scenario is to see that if one Business Application is down, the others will still work.

![use case 3](docs/images/usecase3.png)

1. Run your first Business Application that puts its result in out1.txt.

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.businessapp.BusinessApplication" 
-Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'requests' 'replies' 'test2'" -Dexec.classpathScope="test" -Dibm.odm.install.dir="C:\ODM8920" > out1.txt`

2. Run your second Business Application which is going to put its result in out2.txt

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.businessapp.BusinessApplication" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'requests' 'replies' 'test2'" -Dexec.classpathScope="test"
 -Dibm.odm.install.dir="C:\ODM8920" > out2.txt`
 
3. Run a Client Application that will send 10 messages.

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="'{\"borrower\":{\"lastName\" : \"Smtih\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,
 \"yearlyIncome\":55000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":110000,\"loanToValue\":1.20}}' 'localhost:9092' 'requests' 'replies' 'test3' 10" -Dexec.classpathScope="test"`
 
4. Stop one of your Business Appplication.

5. Create a new Client Application that will send five messages. You'll see that the remaining Business Application handles the request :

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="'{\"borrower\":{\"lastName\" : \"Smtih\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,
 \"yearlyIncome\":55000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":110000,\"loanToValue\":1.20}}' 'localhost:9092' 'requests' 'replies' 'test3' 5" -Dexec.classpathScope="test"`

## Issues and contributions

To contribute or for any issue please use GitHub Issues tracker.

## References
* [IBM Operational Decision Manager Developer Center](https://developer.ibm.com/odm/)
* [Java EE rule session](https://www.ibm.com/support/knowledgecenter/en/SSQP76_8.9.2/com.ibm.odm.dserver.rules.samples/res_smp_topics/smp_res_javaee.html)
* [Loan Validation Sample](https://www.ibm.com/support/knowledgecenter/en/SSQP76_8.5.1/com.ibm.odm.dserver.rules.samples/designer_smp_topics/smp_rd_engineintmultproj_det.html)

## License
[Apache 2.0](LICENSE)
## Notice
© Copyright IBM Corporation 2018.

[![Build Status](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka.svg?token=YUDWXbAcjsyzHsqNF4a8&branch=master)](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![GitHub last commit (branch)](https://img.shields.io/github/last-commit/ODMDev/odm-ondocker/dev.svg)](https://github.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

