/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shotlogger.loglistener;

import com.shotbygun.collections.ArrayQueue;
import com.shotbygun.collections.ArraySwapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import org.shotlogger.Log;
import org.shotlogger.LogPrinter;
import org.shotlogger.LogItem;
import org.shotlogger.LoggerThread;
import org.shotlogger.ShotLoggerInternal;

/**
 *
 * @author shotbygun
 */
public class FileLogWriter extends LoggerThread implements LogListener {
    
    // Log file delimiter
    private String logDeLimiter;
    
    // Object used to write data to file
    private final String logDirectory;
    private final HashMap<String,FileLogHandle> handleMap;

    public FileLogWriter(int targetSleepTime, String logDirectory, String logDeLimiter) {
        super(targetSleepTime);
        this.logDirectory = logDirectory;
        this.logDeLimiter = logDeLimiter;
        
        handleMap = new HashMap<>();
    }
    
    @Override
    protected void execute() {
        for(FileLogHandle handle : handleMap.values()) {
            handle.writeAndFlush();
        }
    }

    @Override
    protected void tryClosing() {
        
        for(FileLogHandle handle : handleMap.values()) {
            handle.close();
        }
        
        handleMap.clear();
    }
    
    
    /**
     * @see this method is called from LoggerWorker
     * @param logItem 
     */
    @Override
    public void consume(LogItem logItem) {
        
        // TODO: make logitem stringbuilder easily replaceable
        
        // Get or Make file handle
        if(!handleMap.containsKey(logItem.threadName))
            handleMap.put(logItem.threadName, createFileHandle(logItem.threadName));
        
        // Write into to handle
        FileLogHandle handle = handleMap.get(logItem.threadName);
        handle.handleLogItems.put(LogPrinter.stringBuilder(logItem, logDeLimiter, true, false, true));
        //handleMap.get(logItem.category).handleLogItems.put(LogPrinter.stringBuilder(logItem, logDeLimiter, true, false, true));
    }

    private FileLogHandle createFileHandle(String handleName) {
        try {
            FileLogHandle handle = new FileLogHandle(logDirectory, handleName, logDeLimiter, notifyObject);
            return handle;
        } catch (Exception ex) {
            Log.log(ShotLoggerInternal.INTERNAL_ERROR_CATEGORY, Log.CRITICAL, getClass().getSimpleName(), "Unable to tap into log file: " + logDirectory, ex);
            return null;
        }
    }
    
}


class FileLogHandle {
    
    
    final String handleName;
    final PrintWriter handleWriter;
    // This queue holds string data waiting to get written to disk
    final ArraySwapper<String> handleLogItems;
    final String logDeLimiter;
    
    protected FileLogHandle(String logDirectory, String handleName, String logDeLimiter, Object notifyObject) throws Exception {
        this.handleName = handleName;
        this.handleWriter = new PrintWriter(new BufferedWriter(new FileWriter(logDirectory + File.separatorChar + handleName + ".log", true)));
        this.handleLogItems = new ArraySwapper<>(String.class, 1024*16, notifyObject);
        this.logDeLimiter = logDeLimiter;
    }
    
    protected void writeAndFlush() {
        
        String line;
        ArrayQueue<String> queue = handleLogItems.swap();
        
        while(true) {
            line = queue.pull();
            
            if(line == null)
                break;
            
            handleWriter.println(line);
        }
        
        handleWriter.flush();
    }
    
    protected void close() {
        handleWriter.close();
    }
}