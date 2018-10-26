package odm.ds.kafka.odmj2seclient;

import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_RULEAPP;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_ERROR_MISSING_RULESET_PATH;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_ERROR_INVALID_RULESET_PATH;
import static odm.ds.kafka.odmj2seclient.MessageCode.SAMPLE_RULEAPP_DESCRIPTION;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

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
		
		
    public static void main( String...arguments )
    {
        System.out.println( "Hello World!" );
        Main main=new Main();
        try {
        	  CommandLineParser parser=new DefaultParser();
              CommandLine commandLine = parser.parse(OPTIONS, arguments);
              IlrPath rulesetPath = main.getRulesetPath(commandLine, arguments);
              String ruleAppArchive = main.getRuleAppArchive(commandLine);
              RESJSEExecution execution = new RESJSEExecution();
              try {
            	  execution.loadRuleApp(ruleAppArchive);
            	  execution.executeRuleset(rulesetPath);
            	  
              } finally {
            	  
              }
        
        } catch (Throwable exception) {
        	
        }
     
    }
    
    /**
     *  To get the RuleAppArchive
     *  @param commandLine
     *  @return
     * 
     */
    private String getRuleAppArchive( CommandLine commandLine) {
    	
    	return null;
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
    		String errorMessage=getMessage(SAMPLE_ERROR_INVALID_RULESET_PATH, rulesetPathArgumentAsString);
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
     * @param option
     * 
     */
    private void exitWithUsageMessage(Options options) {
    	
    }
    
    /**
     * To get the rulesetPAth as a String
     * @param commandLine
     * @param arguments
     * 
     */
    private String getMandatoryRulesetPathArgument(CommandLine commandLine, String[] arguments) {
    	int nbOfArguments=arguments.length;
    	if(nbOfArguments!=0) {
    		List<String> unprocessedArguments=Arrays.asList(commandLine.getArgs());
    		if(!unprocessedArguments.isEmpty()) {
    			String rulesetPathArgumentAsString=arguments[nbOfArguments-1];
    			if(unprocessedArguments.contains(rulesetPathArgumentAsString)) {
    				return rulesetPathArgumentAsString;
    			}
    		}
    		
    	}
    	return null;
    }
}
