package org.shotlogger;

/**
 *
 * @author shotbygun
 */
public abstract class LoggerThread implements Runnable {

    private boolean keepRunning = false;
    private Thread thread;
    
    private final long targetSleepTime; // sleep time in milliseconds. eg 1000 = 1 second
    private long lastLoopTime = System.currentTimeMillis();
    private long currentTime = System.currentTimeMillis();
    private long sleepTime;
    
    public LoggerThread(int targetSleepTime) {
        this.targetSleepTime = targetSleepTime;
    }
    
    @Override
    public final void run() {
        
        while(keepRunning) {
            
            try {
                doSleep();
                execute();
            } catch (InterruptedException ex) {
                Log.log("logger", Log.WARNING, getClass().getSimpleName(), "thread was interrupted", ex);
            } catch (Exception ex) {
                Log.log("logger", Log.WARNING, getClass().getSimpleName(), "error while running", ex);
            }
            
        }
        
        // Exited while loop, do closing procedure
        tryClosing();
        
    }
    
    protected abstract void execute();
    
    protected void doSleep() throws Exception {
        
        if(targetSleepTime == 0)
            return;
        
        currentTime = System.currentTimeMillis();
        sleepTime = targetSleepTime - (currentTime - lastLoopTime);
        lastLoopTime = currentTime;
        
        // no time to sleep!
        if(sleepTime < 0)
            return;

        
        Thread.sleep(sleepTime);
    }
    
    public final void stop() {
        keepRunning = false;
    }
    
    public void start(String threadName) {
        keepRunning = true;
        thread = new Thread(this);
        thread.setName(threadName);
        thread.setDaemon(false);
        thread.start();
    }
    
    public abstract void tryClosing();
    
    protected Thread getThread() {
        return thread;
    }
    
}
