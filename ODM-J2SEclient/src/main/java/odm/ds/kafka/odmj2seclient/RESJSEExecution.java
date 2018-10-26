package odm.ds.kafka.odmj2seclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import ilog.rules.res.model.IlrAlreadyExistException;
import ilog.rules.res.model.IlrFormatException;
import ilog.rules.res.model.IlrPath;
import ilog.rules.res.model.archive.IlrArchiveException;
import ilog.rules.res.session.IlrJ2SESessionFactory;
import ilog.rules.res.session.IlrSessionCreationException;
import ilog.rules.res.session.IlrSessionException;

public class RESJSEExecution {
	
	
	/**
	 * 
	 * @return an IlrSessionFactory
	 */
	
	 private static IlrJ2SESessionFactory createJ2SESessionFactory() {
	
		 return null;
	 }
	 /**
	  *  Execute the rulePath using his path
	  *  @param rulesetPath : The path of the ruleset
	  *  
	  */
	 public void executeRuleset(IlrPath rulesetPath) throws IlrFormatException,
     IlrSessionCreationException,
     IlrSessionException {
		 
	 }
	 
	 /**
	  *  To get the url of the RuleAppArchive
	  * @param ruleAppArchiveName
	  * @return
	  * @throws MalformedURLException
	  */
	 private URL getRuleAppArchiveURL(String ruleAppArchiveName) throws MalformedURLException {
		 
		 return null;
	 }
	 
	 /**
	  *  To get the warning
	  * @param key
	  * @param arguments
	  * 
	  */
	 private void warning(String key, Object... arguments) {
		 
	 }
	 
	 /**
	  *  To get the Message
	  * @param key
	  * @param arguments
	  * @return
	  * 
	  */
	 private String getMessage(String key, Object... arguments) {
		 
		 return null;
	 }
	 
	 /**
	  * 
	  * @param level
	  * @param key
	  * @param arguments
	  */
	 private void log(Level level, String key, Object... arguments) {
		 
	 }
	 
	 /**
	  *  To get info
	  * @param key
	  * @param arguments
	  * 
	  */
	 private void info(String key, Object... arguments) {
		 
	 }
	 /**
	  *  To load the RuleApp
	  *  
	  * @param ruleAppArchiveName
	  * @throws IlrSessionCreationException
	  * @throws IlrSessionException
	  * @throws IOException
	  * @throws IlrArchiveException
	  * @throws IlrAlreadyExistException
	  * @throws IlrFormatException
	  * 
	  */
	 public void loadRuleApp(String ruleAppArchiveName) throws IlrSessionCreationException,
     IlrSessionException,
     IOException,
     IlrArchiveException,
     IlrAlreadyExistException,
     IlrFormatException {
		 
	 }
	 
	 /**
	  * 
	  */
	 public void release() {
		 
	 }
	 
	 /**
	  * Constructor for RESJSEExecution
	  * @param factory
	  * 
	  */
	 private RESJSEExecution(IlrJ2SESessionFactory factory) {
		 
	 }
	 
	 /**
	  * Constructor without argument
	  */
	 public RESJSEExecution() {
		 
	 }
}
