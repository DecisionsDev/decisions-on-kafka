package odm.ds.kafka.ODMJ2SEclient;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import ilog.rules.res.model.IlrPath;

/**
 * Hello world!
 *
 */
public class Main 
{
	   
	   
	   private enum SampleOption{
       	
		
	//	   private final Option option;
	//	   private final String defaultValue;
	        SampleOption(){
	        	
	        }
	        }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Main main=new Main();
        CommandLineParser parser=new DefaultParser();
        
        try {
        	
        
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
     *  To get the RulesetPath
     * @param commandLine
     * @param arguments
     * 
     * @return the rulesetPath
     */
    private IlrPath getRulesetPath(CommandLine commandLine, String[] arguments) throws IllegalArgumentException {
    	
    	return null;
    }
    
    /**
     * To get the Message
     * 
     * @key key : the key of the message
     * @return message
     * 
     */
    private String getMessage(String key, Object... arguments) {
    	
    	return null;
    }
    
    /**
     * The exit message with usage in case of error
     * @param option
     * 
     */
    private void exitWithUsageMessage(Options options) {
    	
    }
    
    /**
     * To get MandatoryRulesetPathArgument
     * @param commandLine
     * @param arguments
     * 
     */
    private String getMandatoryRulesetPathArgument(CommandLine commandLine, String[] arguments) {
    	
    	return null;
    }
}
