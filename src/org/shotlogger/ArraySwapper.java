package org.shotlogger;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author shotbygun
 */
public class ArraySwapper {
    
    // Locks
    private final ReentrantLock clientLock;
    private final Object masterLock;
    private final Object swapRequested;
    
    // Major variables
    private boolean masterHasAlpha;
    private final ArrayQueue alpha, beta;
    
    // Client variables
    private ArrayQueue clientPointer;
    
    public ArraySwapper(int arraySize) {
        alpha = new ArrayQueue(arraySize);
        beta = new ArrayQueue(arraySize);
        
        masterLock = new Object();
        clientLock = new ReentrantLock(false);
        
        masterHasAlpha = true;
        clientPointer = beta;
        
        swapRequested = new Object();
    }
    
    
    public void put(LogItem item) {
        
        try {
            
            // Obtain lock
            clientLock.lock();
            
            // Wait if queue is full
            
            /*
                Only one can of clients can try to obtain masterLock
                because if all threads were competing for masterLock, 
                then the consumer would have to compete vs everyone...
                And this class is supposed to be optimized for the one consumer
            */
            synchronized(masterLock) {
                while(!clientPointer.put(item)) {
                    masterLock.wait();
                }
            }
            
            
        } catch (Exception ex) {
            Log.failSafe(ShotLoggerInternal.INTERNAL_ERROR_CATEGORY, Log.CRITICAL, this.getClass().getSimpleName(), "log entry lost @ ArraySwapper.put()", ex);
        } finally {
            // Unlock everything
            clientLock.unlock();
        }
        
    }
    
    public ArrayQueue swap() {
        
        synchronized(masterLock) {
            
            if(masterHasAlpha) {
                alpha.clear();
                clientPointer = alpha;
            } else {
                beta.clear();
                clientPointer = beta;
            }

            masterHasAlpha = !masterHasAlpha;
            masterLock.notify();
        }
        
        if(masterHasAlpha)
            return alpha;
        else
            return beta;
    }
    
    /**
     * 
     * This is not thread safe operation
     * which means that values are estimate
     * 
     * @return int[2]
     */
    public int[] size() {
        int[] out = new int[2];
        out[0] = alpha.size();
        out[1] = beta.size();
        return out;
    }
    
}
