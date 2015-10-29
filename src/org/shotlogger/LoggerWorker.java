package org.shotlogger;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.shotlogger.loglistener.LogListener;

/**
 *
 * @author shotbygun
 */
public class LoggerWorker extends LoggerThread {
    
    private LogItem currentItem;
    private final ArrayList<LogListener> listeners = new ArrayList<>();
    private final Lock lock = new ReentrantLock(); // Lock used to cover ArrayList

    public LoggerWorker(int targetSleepTime) {
        super(targetSleepTime);
    }

    @Override
    public void execute() {
        
        // Get log item if any
        currentItem = ShotLogger.currentLogItemQueue.poll();

        if(currentItem != null) {

            // Lock the listener list so we dont get interrupted
            lock.lock();

            try {

                while(currentItem != null) {

                    try {
                        for (LogListener listener : listeners) {
                            listener.consume(currentItem);
                        }

                    } catch (Exception ex) {
                        Log.log("LOGGER", Log.ERROR, getClass().getSimpleName(), "error on logger tick", ex);
                    } finally {
                        Log.trash(currentItem);
                    }

                    // Get new item for next round if there is any left
                    currentItem = ShotLogger.currentLogItemQueue.poll();
                }
            } finally {
                lock.unlock();
            }
        }
    }
    
    public void addListener(LogListener logListener) {
        try {
            
            if(logListener == null)
                throw new Exception("tied to add null logListener to LoggerWorker!");
            
            lock.lock();
            listeners.add(logListener);
        } catch (Exception ex) {
            Log.log("LOGGER", Log.ERROR, getClass().getSimpleName(), "error adding logger listener", ex);
        } finally {
            lock.unlock();
        }
    }
    
    public void removeListener(LogListener logListener) {
        try {
            lock.lock();
            listeners.remove(logListener);
        } catch (Exception ex) {
            Log.log("LOGGER", Log.ERROR, getClass().getSimpleName(), "error removing logger listener", ex);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void tryClosing() {
        // uhh? what do?
    }
    
    
    
}
