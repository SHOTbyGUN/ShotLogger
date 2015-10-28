/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shotlogger.loglistener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.shotlogger.Log;
import org.shotlogger.LogItem;
import org.shotlogger.LoggerThread;

/**
 *
 * @author shotbygun
 */
public class FileLogWriter extends LoggerThread implements LogListener {
    
    // Log file delimiter
    private String logDeLimiter;
    
    // Reusable timestamp object
    private Timestamp time = new Timestamp(System.currentTimeMillis());
    
    // Object used to write data to file
    private String logDirectory;
    private HashMap<String,FileLogHandle> handleMap;
    
    private String line;

    public FileLogWriter(int targetSleepTime, String logDirectory, String logDeLimiter) {
        super(targetSleepTime);
        this.logDirectory = logDirectory;
        this.logDeLimiter = logDeLimiter;
        handleMap = new HashMap<>();
        //initialize();
    }
    
    @Override
    protected void execute() {
        for(FileLogHandle handle : handleMap.values()) {
            handle.writeAndFlush();
        }
    }

    @Override
    public void tryClosing() {
        
        for(FileLogHandle handle : handleMap.values()) {
            handle.close();
        }
        
        handleMap.clear();
    }
    
    

    @Override
    public void consume(LogItem logItem) {
        
        // Read timestamp
        time.setTime(logItem.timestamp);
        
        // TODO: make logitem stringbuilder easily replaceable
        
        if(!handleMap.containsKey(logItem.category))
            handleMap.put(logItem.category, createFileHandle(logItem.category));
        
        // Write into to handle
        handleMap.get(logItem.category).handleDataBuffer.add(time.toString() + logDeLimiter + logItem.fileLoggerStringBuilder(logDeLimiter));
    }

    private FileLogHandle createFileHandle(String handleName) {
        try {
            FileLogHandle handle = new FileLogHandle(logDirectory, handleName);
            return handle;
        } catch (Exception ex) {
            Log.log("shotlogger", Log.CRITICAL, getClass().getSimpleName(), "Unable to tap into log file: " + logDirectory, ex);
            return null;
        }
    }
    
}


class FileLogHandle {
    
    final String handleName;
    final PrintWriter handleWriter;
    // This queue holds string data waiting to get written to disk
    final ConcurrentLinkedQueue<String> handleDataBuffer;
    
    // Temp variables
    String line;
    
    protected FileLogHandle(String logDirectory, String handleName) throws Exception {
        this.handleName = handleName;
        this.handleWriter = new PrintWriter(new BufferedWriter(new FileWriter(logDirectory + File.separatorChar + handleName + ".log", true)));
        handleDataBuffer = new ConcurrentLinkedQueue<>();
    }
    
    protected void writeAndFlush() {
        line = handleDataBuffer.poll();
        while(line != null) {
            handleWriter.println(line);
            line = handleDataBuffer.poll();
        }
        handleWriter.flush();
    }
    
    protected void close() {
        handleWriter.close();
        handleDataBuffer.clear();
    }
}