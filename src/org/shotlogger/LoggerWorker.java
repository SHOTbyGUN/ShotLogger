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
    
    private ArrayQueue jobList;

    public LoggerWorker() {
        super(0);
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
    public void execute() {

        try {

            jobList = ShotLogger.currentLogItemQueue.swap();

            while(true) {

                // Get an item from empty
                currentItem = jobList.pull();

                // if null, the queue is empty
                if(currentItem == null)
                    break;

                // Process the item
                try {
                    for (LogListener listener : listeners) {
                        listener.consume(currentItem);
                    }
                } catch (Exception ex) {
                    Log.failSafe(ShotLoggerInternal.INTERNAL_ERROR_CATEGORY, Log.CRITICAL, this.getClass().getSimpleName(), "error 1", ex);
                } finally {
                    //ShotLogger.trashLogItemQueue.offer(currentItem);
                }
            }

            // Implement sleeping mechanism here!
            //ShotLogger.currentLogItemQueue.wait(100);
            Thread.sleep(1);
            
        } catch (Exception ex) {
            Log.failSafe(ShotLoggerInternal.INTERNAL_ERROR_CATEGORY, Log.CRITICAL, this.getClass().getSimpleName(), "error 2", ex);
        }
        
    }

    @Override
    public void tryClosing() {
        
    }
    
    
    
}
