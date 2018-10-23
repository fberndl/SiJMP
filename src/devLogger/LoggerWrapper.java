package devLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Simple Logging singelton class for logging to console and files.
 * 
 * @author Florian Berndl
 */
public class LoggerWrapper {

	// Log file Name
	private static final String		filename	= "logger.log";
	// Number of log files
	private static final int			count			= 1;
	// Size of each log file
	private static final int			limit			= 1024 * 100;							// 100 Kb
	// Append into log file
	private static final boolean	append		= true;

	private final Logger					myLogger	= Logger.getLogger("Test");

	public Logger getMyLogger() {
		return myLogger;
	}

	private static LoggerWrapper	instance	= null;

	protected LoggerWrapper() {
		// Exists only to defeat instantiation.
	}

	public static LoggerWrapper getInstance() {
		if (instance == null) {
			instance = new LoggerWrapper();
			instance.prepareLogger();
		}
		return instance;
	}

	private void prepareLogger() {
		try {
			// FileHandler file name with max size and number of log files limit
			Handler fileHandler = new FileHandler(filename, limit, count, append);
			fileHandler.setFormatter(new MyFormatter());
			try {
				LogManager.getLogManager().readConfiguration(new FileInputStream("mylogging.properties"));
			} catch (SecurityException | IOException e1) {
				e1.printStackTrace();
				System.err.println("mylogging.properties not found!!!!");
			}
			myLogger.addHandler(new ConsoleHandler());
			myLogger.addHandler(fileHandler);

			// adding custom handler
			// myLogger.addHandler(new FileHandler());
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

}
