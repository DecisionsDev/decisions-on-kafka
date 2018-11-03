# ODM Decision Server with kafka
[![Build Status](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka.svg?token=YUDWXbAcjsyzHsqNF4a8&branch=master)](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![GitHub last commit (branch)](https://img.shields.io/github/last-commit/ODMDev/odm-ondocker/dev.svg)](https://github.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Features


This sample show how to use IBM ODM with Kafka

![Sample Architecture](ODM-J2SEclient/docs/images/architecture.png)

### Workflow Description

1. We have n client application which reacte as kafka Producer and send their payload to the kafka topic named Requests

2. We have n Business Application implementing ODM Xu which reate as Consumer and execute the payload.

3. 
## Requirments

* Kafka
* IBM ODM
* Maven


## Dependencies
- Compile time
- Test

## Deployment
* Clone the repository from github.
`$ git clone --branch=master git@github.ibm.com:MYattara/ODM-DecisionServer-Kafka.git MYattara/ODM-DecisionServer-Kafka`
* Run the project locally.
`$ mvn clean install -Dmessage="yourMessage" -DtopicName="topicName" -Dserverurl="yourkafkahost:9092" -Dgroupid="ConsumerGroupid"`
* To run the main class SampleMain :$ `mvn exec:java -Dexec.mainClass="odm.ds.kafka.main.SampleMain" -Dexec.args="yourkafkahost:port topicname message" -Dmaven.test.skip=true`
* To run the integration test : `$ mvn -Dtest=SampleTest -Dmessage="yourMessage" -DtopicName="topicName" -Dserverurl="yourkafkahost:port" test`
* To run the Main Application of the J2SEclient Application : 
`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmj2seclient.Main" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 
'{\"borrower\":{\"lastName\" : \"Yattara\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"11243344\",\"zipCode\":\"75012\"
,\"creditScore\":200,\"yearlyIncome\":20000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178,
\"amount\":100000,\"loanToValue\":1.20}}' 'localhost:9092' 'multipart' 'repliestest' 'test2'" -Dexec.classpathScope="test" 
-Dibm.odm.install.dir="C:\ODM8920"`

* To run the Business Application : `$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmj2seclient.BusinessApplication" 
-Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'multipart' 'repliestest' 'test2'" -Dexec.classpathScope="test"
 -Dibm.odm.install.dir="C:\ODM8920" `
 
 * To run the Client Application : `$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmj2seclient.ClientApplication" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 
 '{\"borrower\":{\"lastName\" : \"Yattara\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"11243344\",\"zipCode\":\"75012\",\"creditScore\":200,
 \"yearlyIncome\":20000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":100000,\"loanToValue\":1.20}}' 'localhost:9092' 
 'multipart' 'repliestest' 'test2'" -Dexec.classpathScope="test" -Dibm.odm.install.dir="C:\ODM8920"`


## Contributing

## References
* [IBM Operational Decision Manager Developer Center](https://developer.ibm.com/odm/)
* [Java EE rule session](https://www.ibm.com/support/knowledgecenter/en/SSQP76_8.9.2/com.ibm.odm.dserver.rules.samples/res_smp_topics/smp_res_javaee.html)

## Issues and contributions

## License
[Apache 2.0](LICENSE)
## Notice
© Copyright IBM Corporation 2018.

[![Build Status](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka.svg?token=YUDWXbAcjsyzHsqNF4a8&branch=master)](https://travis.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![GitHub last commit (branch)](https://img.shields.io/github/last-commit/ODMDev/odm-ondocker/dev.svg)](https://github.ibm.com/MYattara/ODM-DecisionServer-Kafka)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

