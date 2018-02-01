/*
 * LogItem = one log entry
 * this class provides easy handling for any logListeners
 */
package org.shotlogger;


/**
 *
 * @author shotbygun
 */
public class LogItem {
    
    public long timestamp;
    public String category;
    public short severity;
    public String source;
    public String message;
    public Exception exception;
    public String threadName;
    
    /**
     * @see timestamp is taken when set() is called
     * @param category each category has its own logfile
     * @param severity
     * @param source suggestion: this.getClass().getSimpleName()
     * @param message
     * @param exception OPTIONAL, you can use null
     */
    public void set(String category, short severity, String source, String message, Exception exception, String threadName) {
        this.timestamp = System.currentTimeMillis();
        this.category = category;
        this.severity = severity;
        this.source = source;
        this.message = message;
        this.exception = exception;
        this.threadName = threadName;
    }
    
}
