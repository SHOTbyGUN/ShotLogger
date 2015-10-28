package org.shotlogger.loglistener;

import org.shotlogger.LogItem;



/**
 *
 * @author shotbygun
 */
public interface LogListener {
    
    public abstract void consume(LogItem logitem);
}
