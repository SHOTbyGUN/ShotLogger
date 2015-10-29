/*
    The manager class of this logging system
    - use this class to init logging system
    - use this class to shutdown logging system
    - use this class to modify logging settings
 */
package org.shotlogger;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.shotlogger.loglistener.ConsoleLogListener;
import org.shotlogger.loglistener.FileLogWriter;

/**
 *
 * @author shotbygun
 */
public class ShotLogger {
    
    
    // TODO: consider moving pools away from this class
    protected static final ConcurrentLinkedQueue<LogItem> currentLogItemQueue = new ConcurrentLinkedQueue<>();
    protected static final ConcurrentLinkedQueue<LogItem> trashLogItemQueue = new ConcurrentLinkedQueue<>();
    /* log items are recycled */
    protected static final int trashSizeLimit = 4096;
    
    private static ShotLogger shotLogger;
    
    private LoggerWorker loggerWorker;
    private ConsoleLogListener consoleLog;
    private FileLogWriter fileLog;
    
    
    private ShotLogger() {
        // Create this class only with getShotLogger = prevent multiple instances
    }
    
    public static ShotLogger getShotLogger() {
        if(shotLogger == null) {
            shotLogger = new ShotLogger();
        }
        
        return shotLogger;
    }
    
    public void startBasic(String logDirectoryPath) {
        
        // Create loggerWorker
        loggerWorker = new LoggerWorker(50);
        
        // Create listeners
        consoleLog = new ConsoleLogListener(" ");
        fileLog = new FileLogWriter(1000, getOrCreateDirectory(logDirectoryPath), ";");
        
        // Start
        loggerWorker.addListener(fileLog);
        loggerWorker.addListener(consoleLog);
        loggerWorker.start("shotlogger");
        fileLog.start("FileShotLogger");
    }
    
    public void stop() {
        fileLog.stop();
        loggerWorker.stop();
    }
    
    private String getOrCreateDirectory(String directoryPath) {
        try {
            File file = new File(directoryPath);
            
            // try to create directory structure if not existing
            if(!file.exists())
                file.mkdirs();
            
            if(file.isDirectory())
                return directoryPath;
            else
                throw new Exception("given logfilepath points to a file, not a directory:" + directoryPath);
            
            
        } catch (Exception ex) {
            Log.log("logger", Log.CRITICAL, getClass().getSimpleName(), "Cannot create logfile: " + directoryPath, ex);
            return null;
        }
    }
    
    public String printPoolSizes() {
        return (currentLogItemQueue.size() + " / " + trashLogItemQueue.size());
    }
    
}
