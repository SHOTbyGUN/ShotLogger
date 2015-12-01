package org.shotlogger;

import java.sql.Timestamp;

/**
 *
 * @author shotbygun
 */
public class LogPrinter {
    
    /**
     * 
     * @param logItem
     * @param delimiter
     * @param showTimestamp
     * @param showCategory
     * @param showStackTrace huge impact on performance
     * @return 
     */
    
    public static String stringBuilder(LogItem logItem, String delimiter, boolean showTimestamp, boolean showCategory, boolean showStackTrace) {
        
        StringBuilder textOut = new StringBuilder();
        
        // Timestamp
        if (showTimestamp) {
            textOut.append(new Timestamp(logItem.timestamp));
            textOut.append(delimiter);
        }
        
        // Category
        if (showCategory && logItem.category != null) {
            textOut.append(logItem.category);
            textOut.append(delimiter);
        }
        
        // Severity
        if (logItem.severity >= 0 && logItem.severity <= Log.severityText.length) {
            textOut.append(Log.severityText[logItem.severity]);
        } else {
            textOut.append("Unknown severity number: ");
            textOut.append(logItem.severity);
        }
        textOut.append(delimiter);
        
        // Source
        if (logItem.source != null) {
            textOut.append(logItem.source);
            textOut.append(delimiter);
        }
        
        // Message
        if (logItem.message != null) {
            textOut.append(logItem.message);
            textOut.append(delimiter);
        }
        
        // Exception
        if (logItem.exception != null) {
            textOut.append(logItem.exception.toString());
            textOut.append(delimiter);
            if (showStackTrace) {
                //textOut.append(ExceptionUtils.getStackTrace(logItem.exception));
                appendStackTrace(textOut, logItem.exception);
                textOut.append(delimiter);
            }
        }
        
        
        return textOut.toString();
    }
    
    /**
     * ExceptionUtils by Apache Commons Lang 3.3 is slow when calling it in loop
     * https://github.com/apache/commons-lang/commit/5292526e476ffbb19c6613a98464054236c86ace#L582
     * 
     * org.shotlogger.LoggerWorker.execute()
     *      - 31ms of 49ms @ ExceptionUtils.getStackTrace
     *      - 24ms of 39ms @ appendStackTrace
     * 
     * 
     * @param sb StringBuilder
     * @param item Exception
     */
    
    
    private static final String NEWLINE = System.getProperty("line.separator");
    private static StackTraceElement[] stackTrace;
    private static int i = 0;
    
    
    public static void appendStackTrace(StringBuilder sb, Throwable item) {
        stackTrace = item.getStackTrace();
        
        for (i = 0; i < stackTrace.length; i++) {
            sb.append(NEWLINE);
            sb.append(stackTrace[i].toString());
        }
    }
}
