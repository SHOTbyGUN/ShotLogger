/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shotlogger.strategy;

import com.shotbygun.collections.ArraySwapper;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.shotlogger.Log;
import org.shotlogger.LogItem;
import org.shotlogger.ShotLoggerInternal;

/**
 *
 * @author shotbygun
 */
public class NThreadedStrategy implements ShotLoggerStrategy {
    
    private final ConcurrentHashMap<Thread,ArraySwapper<LogItem>> logItemQueueMap = new ConcurrentHashMap<>();
    private int logPoolSizePerThread;
    
    public NThreadedStrategy(Integer size) {
        if(size == null)
            logPoolSizePerThread = 512;
        else
            logPoolSizePerThread = size;
    }
    
    @Override
    public ArraySwapper getCurrentLogItemSwapper() {
        
        Thread thread = Thread.currentThread();
        ArraySwapper arraySwapper = logItemQueueMap.get(thread);
        if(arraySwapper == null) {
            arraySwapper = new ArraySwapper(LogItem.class, logPoolSizePerThread);
            logItemQueueMap.put(thread, arraySwapper);
        }
        return arraySwapper;
    }
    
    protected void removeLogItemPoolThread(Thread thread) {
        logItemQueueMap.remove(thread);
    }
    
    @Override
    public Collection<ArraySwapper<LogItem>> getAllSwappers() {
        return logItemQueueMap.values();
    }
    
    protected Set<Map.Entry<Thread, ArraySwapper<LogItem>>> getEntrySet() {
        return logItemQueueMap.entrySet();
    }
    
    @Override
    public int getSize() {
        
        //int[] sizes = currentLogItemQueue.size();
        int size = 0;
        for(ArraySwapper swapper : getAllSwappers()) {
            size += swapper.size();
        }
        
        return size;
    }
    
    private void trimLogItemPool() {
        
        // For each log entry pool
        for(Entry<Thread, ArraySwapper<LogItem>> entry : getEntrySet()) {
            
            // if thread is dead
            if(!entry.getKey().isAlive()) {
                
                // if all log items have been processed
                if(entry.getValue().size() == 0) {
                    Log.log(ShotLoggerInternal.INTERNAL_ERROR_CATEGORY, 
                            Log.DEBUG, 
                            "trimLogItemPool()", "removing logItemPool from dead thread: " + entry.getKey().getName() + " " + entry.getKey().getId(), 
                            null);
                    removeLogItemPoolThread(entry.getKey());
                }
            }
        }
    }
    
}
