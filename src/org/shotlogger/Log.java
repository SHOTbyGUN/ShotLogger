package org.shotlogger;

/**
 *
 * @author shotbygun
 */
public class Log {
    
    // Severity constants
    
    public static final short DEBUG = 0;
    public static final short INFO = 1;
    public static final short WARNING = 2;
    public static final short ERROR = 3;
    public static final short CRITICAL = 4;
    public static final short FATAL = 5;
    public static final String[] severityText = {"DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL", "FATAL"};
    
    
    public static void log(String category, short severity, String source, String message, Exception exception) {
        /*
        LogItem reusable = ShotLogger.trashLogItemQueue.poll();
        
        if(reusable == null) {
            reusable = new LogItem();
        }
        */
        LogItem reusable = new LogItem();
        reusable.set(category, severity, source, message, exception);
        ShotLogger.currentLogItemQueue.put(reusable);
    }
    
    
    // These are just "overloading" methods of the above
    
    public static void debug(String category, String source, String message, Exception exception) {
        log(category, DEBUG, source, message, exception);
    }
    
    public static void info(String category, String source, String message, Exception exception) {
        log(category, INFO, source, message, exception);
    }
    
    public static void warning(String category, String source, String message, Exception exception) {
        log(category, WARNING, source, message, exception);
    }
    
    public static void error(String category, String source, String message, Exception exception) {
        log(category, ERROR, source, message, exception);
    }
    
    public static void critical(String category, String source, String message, Exception exception) {
        log(category, CRITICAL, source, message, exception);
    }
    
    public static void fatal(String category, String source, String message, Exception exception) {
        log(category, FATAL, source, message, exception);
    }
    
    // FailSafe will only work if FileLogWriter is initialized and running
    
    private static final String FAILSAFEDELIMITER = ";";
    
    protected static void failSafe(String category, short severity, String source, String message, Exception exception) {
        
        // Create logitem out of the normal flow
        LogItem logItem = new LogItem();
        logItem.set(category, severity, source, message, exception);
        
        // Generate string
        String errorLine = LogPrinter.stringBuilder(logItem, FAILSAFEDELIMITER, true, true, true);
        
        // Output the error
        System.err.println(errorLine);
        System.out.println(errorLine);
        
        // Send error string to FileLogWriter
        failSafePlainText(errorLine);
    }
    
    protected static void failSafePlainText(String failSafeErrorMessage) {
        ShotLogger.getShotLogger().getFileLog().failSafe(failSafeErrorMessage);
    }
    
}
