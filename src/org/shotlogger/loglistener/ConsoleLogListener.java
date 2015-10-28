/*
 * This will basically just println all logitems
 */
package org.shotlogger.loglistener;

import org.shotlogger.Log;
import org.shotlogger.LogItem;



/**
 *
 * @author shotbygun
 */
public class ConsoleLogListener implements LogListener {
    
    private String delimiter;
    private String selectedCategory = null;
    private int selectedLogLevel = Log.INFO;
    
    public ConsoleLogListener(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public void consume(LogItem logItem) {
        
        if(selectedLogLevel > logItem.severity)
            return;
        
        if(selectedCategory == null) {
            System.out.println(logItem.defaultStringBuilder(delimiter));
        } else {
            if(selectedCategory.equals(logItem.category))
                System.out.println(logItem.defaultStringBuilder(delimiter));
        }
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public int getSelectedLogLevel() {
        return selectedLogLevel;
    }

    public void setSelectedLogLevel(int selectedLogLevel) {
        this.selectedLogLevel = selectedLogLevel;
    }
    
    
    
}
