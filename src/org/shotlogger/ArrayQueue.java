package org.shotlogger;

/**
 *
 * @author shotbygun
 */
public class ArrayQueue {
    
    private final LogItem[] array;
    private final int size;
    private int setter = 0;
    private int getter = 0;
    
    public ArrayQueue(int size) {
        this.array = new LogItem[size];
        this.size = size;
    }
    
    /**
     * 
     * @param logItem
     * @return true if insert successful
     */
    public boolean put(LogItem logItem) {
        
        // if full, return false
        if(setter >= size)
            return false;
        
        // Insert item
        array[setter++] = logItem;
        
        // Insert successful
        return true;
    }
    
    public LogItem pull() {
        if(getter >= setter)
            return null;
        
        return array[getter++];
    }
    
    /**
     * Nothing is actually removed, pointers are just set to 0
     */
    public void clear() {
        setter = 0;
        getter = 0;
    }
    
    /**
     * 
     * @return setter = amount of "live" objects
     */
    public int size() {
        return setter;
    }
    
}
