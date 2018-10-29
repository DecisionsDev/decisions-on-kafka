package odm.ds.kafka.odmj2seclient;

import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_RULEAPP;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_ERROR_MISSING_RULESET_PATH;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_ERROR_INVALID_RULESET_PATH;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_RULEAPP_DESCRIPTION;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_FOOTER_TAB;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_FOOTER;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_RULESET_PATH;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;

/**
 * Hello world!
 *
 */
public class Main 
{
	   
		private static final MessageFormatter formatter=new MessageFormatter();
		private static String RULEAPP=formatter.getMessage(SAMPLE_RULEAPP);
	   
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
        System.out.println( "Hello World!" );
        Main main=new Main();
        try {
        	  CommandLineParser parser=new DefaultParser();
        	  System.out.println("partie 1");
              CommandLine commandLine = parser.parse(OPTIONS, arguments);
              System.out.println("partie 2");
              IlrPath rulesetPath = main.getRulesetPath(commandLine, arguments);
              System.out.println("partie 3");
              String ruleAppArchive = main.getRuleAppArchive(commandLine);
              System.out.println("partie 4");
              RESJSEExecution execution = new RESJSEExecution();
              try {
            	  execution.loadRuleApp(ruleAppArchive);
            	  System.out.println("partie 5");
            	  execution.executeRuleset(rulesetPath);
            	  System.out.println("partie 6");
            	  
              } finally {
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
    	System.out.println("partie 2-1");
    	if(rulesetPathArgumentAsString==null) {
    		String errorMessage=getMessage(SAMPLE_ERROR_MISSING_RULESET_PATH, getMessage(SAMPLE_ERROR_MISSING_RULESET_PATH));
    		System.out.println("partie 2-2");
    		throw new IllegalArgumentException(errorMessage);
    	}
    	try {
    		System.out.println("partie 2-3");
    		return IlrPath.parsePath(rulesetPathArgumentAsString);
    				
    	} catch (IlrFormatException exception) {
    		System.out.println("partie 2-4");
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
}
