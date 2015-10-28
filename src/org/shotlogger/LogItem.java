/*
 * LogItem = one log entry
 * this class provides easy handling for any logListeners
 */
package org.shotlogger;

import org.apache.commons.lang3.exception.ExceptionUtils;

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
    
    public void set(String category, short severity, String source, String message, Exception exception) {
        this.timestamp = System.currentTimeMillis();
        this.category = category;
        this.severity = severity;
        this.source = source;
        this.message = message;
        this.exception = exception;
    }
    
    public String defaultStringBuilder(String delimiter) {
        String textOut = "";
        
        if(category != null) {
            textOut += category;
            textOut += delimiter;
        }
        
        if(severity >= 0 && severity <= Log.severityText.length) {
            textOut += Log.severityText[severity];
        } else {
            textOut += "Unknown severity number: " + severity;
        }
        textOut += delimiter;
        
        if(source != null) {
            textOut += source;
            textOut += delimiter;
        }
        
        if(message != null) {
            textOut += message;
            textOut += delimiter;
        }
        
        if(exception != null) {
            textOut += exception.toString();
            textOut += delimiter;
            textOut += ExceptionUtils.getStackTrace(exception);
            textOut += delimiter;
        }
        
        return textOut;
    }
    
    public String fileLoggerStringBuilder(String delimiter) {
        String textOut = "";
        
        /* 
            removed category, because it is the filename
        
            if(category != null) {
                textOut += category;
                textOut += delimiter;
            }
        */
        
        if(severity >= 0 && severity <= Log.severityText.length) {
            textOut += Log.severityText[severity];
        } else {
            textOut += "Unknown severity number: " + severity;
        }
        textOut += delimiter;
        
        if(source != null) {
            textOut += source;
            textOut += delimiter;
        }
        
        if(message != null) {
            textOut += message;
            textOut += delimiter;
        }
        
        if(exception != null) {
            textOut += exception.toString();
            textOut += delimiter;
            textOut += ExceptionUtils.getStackTrace(exception);
            textOut += delimiter;
        }
        
        return textOut;
    }
    
}
