/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shotlogger;

/**
 *
 * @author shotbygun
 */
public abstract class LoggerThread implements Runnable {

    private boolean keepRunning = false;
    private Thread thread;
    
    // sleep is not super accurate, add some margin
    private static final int errorMargin = 5;
    
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
        
        currentTime = System.currentTimeMillis();
        sleepTime = targetSleepTime - (currentTime - lastLoopTime);
        lastLoopTime = currentTime;
        
        // no time to sleep!
        if(sleepTime < 0)
            return;
        
        if(sleepTime < errorMargin * 2)
            Thread.yield();
        else
            Thread.sleep(sleepTime - errorMargin);

        
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
    
}
