/*
    The manager class of this logging system
    - use this class to init logging system
    - use this class to shutdown logging system
    - use this class to modify logging settings
 */
package org.shotlogger;

import java.io.File;
import org.shotlogger.loglistener.ConsoleLogListener;
import org.shotlogger.loglistener.FileLogWriter;

/**
 *
 * @author shotbygun
 */
public class ShotLogger {
        
    // Main pool of LogItems
    protected static final ArraySwapper currentLogItemQueue = new ArraySwapper(4096);
    
    // Trash pool
    //protected static final TryDeque<LogItem> trashLogItemQueue = new TryDeque<>(1024);
    
    // Singleton
    private static ShotLogger shotLogger;
    
    // Default systems
    private LoggerWorker loggerWorker;
    private ConsoleLogListener consoleLog;
    private FileLogWriter fileLog;
    
    
    private ShotLogger() {
        // Create this class only with getShotLogger
    }
    
    public static ShotLogger getShotLogger() {
        if(shotLogger == null) {
            shotLogger = new ShotLogger();
        }
        
        return shotLogger;
    }
    
    /**
     * 
     * @param logDirectoryPath this is log DIRECTORY, not logfile. Category = filename + .log inside the given log DIRECTORY
     */
    public void startBasic(String logDirectoryPath) {
        
        // Create loggerWorker
        loggerWorker = new LoggerWorker();
        
        // Create listeners
        consoleLog = new ConsoleLogListener(" ");
        fileLog = new FileLogWriter(1000, getOrCreateDirectory(logDirectoryPath), ";");
        
        // Start
        loggerWorker.addListener(fileLog);
        loggerWorker.addListener(consoleLog);
        loggerWorker.start("LoggerWorker");
        fileLog.start("FileShotLogger");
    }

    
    public void stop() {
        
        loggerWorker.stop();
        fileLog.stop();
        
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
    
    public void printPoolSizes() {
        
        int[] sizes = currentLogItemQueue.size();
        
        System.out.println(sizes[0] + "/" + sizes[1] + " trash: " + 0);
    }
    
    public void stopAndWait() {
        try {
            
            System.out.println("waiting for loggerWorker to stop");
            loggerWorker.stop();
            loggerWorker.getThread().join();
            
            System.out.println("waiting for fileLog to stop");
            fileLog.stop();
            fileLog.getThread().join();
            
            System.out.println("logger stopped");
            
        } catch (Exception ex) {
            System.out.println("stopAndWait() @ " + ShotLogger.class.getSimpleName() + " " + ex.toString());
            ex.printStackTrace();
            
            // if exception, try to make sure they were asked to stop
            loggerWorker.stop();
            fileLog.stop();
        }
    }

    public LoggerWorker getLoggerWorker() {
        return loggerWorker;
    }

    public ConsoleLogListener getConsoleLog() {
        return consoleLog;
    }

    public FileLogWriter getFileLog() {
        return fileLog;
    }
    
}
