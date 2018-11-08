package odm.ds.kafka.odmjse;

import static odm.ds.kafka.odmjse.MessageCode.SAMPLE_ERROR_INVALID_RULESET_PATH;
import static odm.ds.kafka.odmjse.MessageCode.SAMPLE_ERROR_MISSING_RULESET_PATH;
import static odm.ds.kafka.odmjse.MessageCode.SAMPLE_FOOTER;
import static odm.ds.kafka.odmjse.MessageCode.SAMPLE_FOOTER_TAB;
import static odm.ds.kafka.odmjse.MessageCode.SAMPLE_RULEAPP;
import static odm.ds.kafka.odmjse.MessageCode.SAMPLE_RULEAPP_DESCRIPTION;
import static odm.ds.kafka.odmjse.MessageCode.SAMPLE_RULESET_PATH;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;
import loan.LoanRequest;
import odm.ds.kafka.consumer.SampleConsumer;
import odm.ds.kafka.producer.SampleProducer;

public class Main 
{
	   
		private static final MessageFormatter formatter=new MessageFormatter();
		private static String RULEAPP=formatter.getMessage(SAMPLE_RULEAPP);
		private static String serverurl;
		private static String topicNameRq;
		private static String topicNameRp;
		private static String consumergroup;
		
	   
		private enum SampleOption {
		   RuleApp(
				   "r",
				   "ruleApp",
				   RULEAPP,
				   formatter.getMessage(SAMPLE_RULEAPP_DESCRIPTION),
				   null,
				   true,
				   false
				   );
		
		   private final Option option;
		   private final String defaultValue;
		   
		   public Option getOption() {
			   return option;
		   }
		   
		   public String getDefaultValue() {
			   return defaultValue;
		   }
	       SampleOption(String shortName, String longName, String argumentName, String description, String defaultValue, boolean 
	        		hasArgument, boolean required){
	        	this.option=Option.builder(shortName).argName(argumentName).longOpt(longName).desc(description).hasArg(hasArgument)
	        			.required(required).build();
	        	this.defaultValue=defaultValue;
	        }
	       
	      }
		private static final Options OPTIONS=new Options();
		
	public static void setUpkafkaParam(CommandLine commandLine, String[] arguments) {

		int nbOfArguments=arguments.length;
    	if(nbOfArguments!=0) {
    		List<String> unprocessedArguments=Arrays.asList(commandLine.getArgs());
    		if(!unprocessedArguments.isEmpty()) {
    			serverurl=arguments[2];
    			topicNameRq=arguments[3];
    			topicNameRp=arguments[4];
    			consumergroup=arguments[5];
    			
    		}
    		
    	}

	}
	public static String getPayload(CommandLine commandLine, String[] arguments) {
		int nbOfArguments=arguments.length;
    	if(nbOfArguments!=0) {
    		List<String> unprocessedArguments=Arrays.asList(commandLine.getArgs());
    		if(!unprocessedArguments.isEmpty()) {
    			String payloadAsString=arguments[1];
    			if(unprocessedArguments.contains(payloadAsString)) {
    				return payloadAsString;
    			}
    		}
    		
    	}
    	return null;
	}
    public static void main( String...arguments )
    {

        Main main=new Main();
        try {
        	 CommandLineParser parser=new DefaultParser();
             CommandLine commandLine = parser.parse(OPTIONS, arguments);
             IlrPath rulesetPath = main.getRulesetPath(commandLine, arguments);
             RESJSEExecution execution = new RESJSEExecution();
     		 int numberparam=2;
     		 String valeur=null;
     		 setUpkafkaParam(commandLine, arguments);
     		 for(int i=0;i<10;i++) {   		 
     			 ClientApplication.setUpClientApp(serverurl, numberparam, topicNameRq, getPayload(commandLine, arguments),i+"12", consumergroup, topicNameRp);
     		 System.out.println("i is "+i);
     		 }
              try {
            	  System.out.println("serverul "+serverurl);
            	  System.out.println("topicNameRq"+topicNameRq);
            	  System.out.println("topicNameRp "+topicNameRp);
            	  BusinessApplication.setUpBussinessApp(serverurl, numberparam, consumergroup, topicNameRq, rulesetPath, valeur);
            	  
              } finally {
            	  SampleConsumer myConsumer1=new SampleConsumer();
            	  myConsumer1.consumeMessage(myConsumer1.consumerInstance(serverurl, numberparam, consumergroup), topicNameRp);
            	  execution.release();
              }
        
        } catch (ParseException | IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            main.exitWithUsageMessage(OPTIONS);
        	
        } catch(Throwable exception) {
        	
        }
     
    }
    
    /**
     *  To get the RuleAppArchive
     *  @param commandLine
     *  @return
     * 
     */
    private String getRuleAppArchive( CommandLine commandLine) {
        SampleOption option = SampleOption.RuleApp;
        String optionValue = commandLine.getOptionValue(option.getOption().getOpt());
        return (optionValue == null) ? option.getDefaultValue() : optionValue;
    }
    
    /**
     *  Check if the rulesetPath provided as String is null or if the format is incorrect then throw the corresponding exception
     *  otherwise return an IlrPath corresponding to the path
     * @param commandLine
     * @param arguments
     * 
     * @return the rulesetPath
     * 
     */
    private IlrPath getRulesetPath(CommandLine commandLine, String[] arguments) throws IllegalArgumentException {
    	String rulesetPathArgumentAsString=getMandatoryRulesetPathArgument(commandLine, arguments);
    	if(rulesetPathArgumentAsString==null) {
    		String errorMessage=getMessage(SAMPLE_ERROR_MISSING_RULESET_PATH, getMessage(SAMPLE_ERROR_MISSING_RULESET_PATH));
    		throw new IllegalArgumentException(errorMessage);
    	}
    	try {
    		return IlrPath.parsePath(rulesetPathArgumentAsString);
    				
    	} catch (IlrFormatException exception) {
    		System.out.println(rulesetPathArgumentAsString);
    		String errorMessage=getMessage(SAMPLE_ERROR_INVALID_RULESET_PATH, rulesetPathArgumentAsString);
    		System.out.println(errorMessage);
    		throw new IllegalArgumentException(errorMessage);	
    	}
    }
    
    /**
     * To get the Message
     * 
     * @key key : the key of the message
     * @return message
     * 
     */
    private String getMessage(String key, Object... arguments) {
    	
    	return formatter.getMessage(key, arguments);
    }
    
    /**
     * The exit message with usage in case of error
     * Format the displayed message about the rulesetPath is not valid.
     * @param option
     * 
     */
    private void exitWithUsageMessage(Options options) {
    	HelpFormatter formater=new HelpFormatter();
    	String footer_tab = getMessage(SAMPLE_FOOTER_TAB);
    	String rulesetPath=getMessage(SAMPLE_RULESET_PATH);
    	String footer=getMessage(SAMPLE_FOOTER, rulesetPath, footer_tab);
    	String CommandLineSyntax=Main.class.getName()+ " [-j <" + RULEAPP + ">] <" + rulesetPath + ">";
    	formater.printHelp(120, CommandLineSyntax, null, options, footer);
    }
    
    /**
     * To get the rulesetPAth as a String
     * @param commandLine
     * @param arguments
     * 
     */
    private String getMandatoryRulesetPathArgument(CommandLine commandLine, String[] arguments) {
    	System.out.println("Inside getMandatory");
    	int nbOfArguments=arguments.length;
    	if(nbOfArguments!=0) {
    		List<String> unprocessedArguments=Arrays.asList(commandLine.getArgs());
    		if(!unprocessedArguments.isEmpty()) {
    			String rulesetPathArgumentAsString=arguments[0];
    			System.out.println("rulesetPathArgumentAsString "+arguments[0]);
    			System.out.println("lenght "+arguments[1]);
    			if(unprocessedArguments.contains(rulesetPathArgumentAsString)) {
    				return rulesetPathArgumentAsString;
    			}
    		}
    		
    	}
    	return null;
    }
    
	 public static loan.Borrower borrowerJson(){
		 ObjectMapper objectMapper=new ObjectMapper();
		 loan.Borrower borrower=null;
		 String loanJson =
				    "{ \"lastName\" : \"Smith\",\"firstName\" : \"John\", \"birthDate\":191977200000,\"SSN\":\"11243344\",\"zipCode\":\"75012\",\"creditScore\":200,\"yearlyIncome\":20000}";

			try {
				borrower=objectMapper.readValue(loanJson, loan.Borrower.class);				
			} catch(IOException e) {
				e.printStackTrace();
			}
			return borrower;
	 }
	 public static loan.LoanRequest loanRequestJson(){

		 ObjectMapper objectMapper=new ObjectMapper();
		 LoanRequest loanrequest=null;
		 String requestJson =
				    "{ \"numberOfMonthlyPayments\" : 48,\"startDate\" : 1540822814178, \"amount\":100000,\"loanToValue\":1.20}";

			try {
				loanrequest=objectMapper.readValue(requestJson, loan.LoanRequest.class);
				System.out.println("Loan Amout "+loanrequest.getAmount());
				System.out.println("Loan Duration "+loanrequest.getDuration());
				
			} catch(IOException e) {
				e.printStackTrace();
			}
			return loanrequest;

	 }


}
