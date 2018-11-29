## Scenario 1 : Two Client Applications sending payload to one Decision Service and waiting for the result.
The goal of this scenario is to show that each Client Application gets the right answer for his payload it sent to the Decision Service.

![use case 1](../../docs/images/usecase1.png)
 

1. Create the first Client Application: Open a command line in the project ODM-DecisionServer-JSE-Kafka root folder, and then run the command below. It sends a payload corresponding to the loan request. In this request the amount is 10000 and 
the yearlyIncome is 200000. 

`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="'{\"borrower\":{\"lastName\" : \"Smith\",\"firstName\" : \"Alice\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":200000},
\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":10000,\"loanToValue\":1.20}}' 'localhost:9092' 'requests' 'replies' 1" -Dexec.classpathScope="test"`

'localhost:9092' is the broker url, if your broker url is different please change it accordingly,'requests' corresponds to the topic where loan requests are put, and 'replies' corresponds to the topic where the Decision Service puts the execution
result. 1 is the number of loan request we want the Client Application sends to the Decision Service.

 2. Create the second Client Application : Open a second command line in the root folder, and then run the following command. The second client Application sends a loan request with the yearlyIncome 55000 and the amount of loan 110000.
 
`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="'{\"borrower\":{\"lastName\" :\"Doe\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,
 \"yearlyIncome\":55000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":110000,\"loanToValue\":1.20}}' 'localhost:9092' 'requests' 'replies' 1" -Dexec.classpathScope="test"`
  
 3. Run the Decision Service:
 
`$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.decisionapp.DecisionService" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'requests' 'replies' 'baconsumegroup'" -Dexec.classpathScope="test" -Dibm.odm.install.dir="C:\ODM8920" `
 
   'baconsumegroup' the consumer group in which is the Decision Service.

 
4. Result: 
The loan request should be accepted in the first Client Application, and it should be rejected in the second Client Application.

5. Stop the Decision Service before starting the scenario 2.

[**Next** ![""](../../docs/images/next.jpg)](../../docs/chapters/subscenario2.md)

[![""](../../docs/images/home.jpg) **Back to home page**](../../Readme.md)