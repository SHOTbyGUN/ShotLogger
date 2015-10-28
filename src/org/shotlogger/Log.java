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
    public static final String[] severityText = {"DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"};
    
    
    
    
    
    public static void log(String category, short severity, String source, String message, Exception exception) {
        
        LogItem reusable = ShotLogger.trashLogItemPool.poll();
        
        if(reusable == null) {
            reusable = new LogItem();
        }
        
        reusable.set(category, severity, source, message, exception);
        ShotLogger.currentLogItemPool.add(reusable);
    }
    
    
    protected static void trash(LogItem item) {

        if(ShotLogger.trashLogItemPool.size() < ShotLogger.trashSizeLimit)
            ShotLogger.trashLogItemPool.add(item);
    }
    
    public static void printPoolSizes() {
        
    }
    
}
