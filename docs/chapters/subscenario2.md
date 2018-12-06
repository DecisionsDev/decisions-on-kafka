## Scenario 2 :  Load balancing between two decision services

The goal of this scenario is to show the load balancing between two decision services.

![use case 2](../../docs/images/usecase2.png)

1. Run your first decision service, which puts its result in out1.txt:

    `
$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.decisionapp.DecisionService" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'requests' 'replies' 'test2'" -Dexec.classpathScope="test" > out1.txt
    `

2. Run your second decision service, which puts its result in out2.txt:

    `
$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.decisionapp.DecisionService" -Dexec.args="/test_deployment/loan_validation_with_score_and_grade 'localhost:9092' 'requests' 'replies' 'test2'" -Dexec.classpathScope="test" > out2.txt
    `
 
3. Run a client application that sends seven messages:

    `
$ mvn exec:java -Dexec.mainClass="odm.ds.kafka.odmjse.clientapp.ClientApplication" -Dexec.args="'{\"borrower\":{\"lastName\" : \"Smith\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"800-12-0234\",\"zipCode\":\"75012\",\"creditScore\":200,
 \"yearlyIncome\":55000},\"loanrequest\":{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":110000,\"loanToValue\":1.20}}' 'localhost:9092' 'requests' 'replies' 7 " -Dexec.classpathScope="test"
     `

4. When the client application terminates, and the decision services display the message "waiting for payload", stop both decision services and look at out1.txt and out2.txt. You see that the seven payloads have been split for execution between these two decision services.

[**Next** ![""](../../docs/images/next.jpg)](../../docs/chapters/subscenario3.md)

[![""](../../docs/images/home.jpg) **Back to home page**](../../Readme.md)
