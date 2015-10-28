package org.shotlogger.example;



import java.util.Scanner;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.shotlogger.Log;
import org.shotlogger.ShotLogger;

/**
 *
 * @author shotbygun
 */
public class Tester {
    
    public static void main(String[] args) {
        
        Log.log("tester", Log.DEBUG, Tester.class.getSimpleName(), "logger tester pre init message", null);
        
        ShotLogger shotLogger = ShotLogger.getShotLogger();
        shotLogger.startBasic("/home/shotbygun/dev/shotlog/log");
        
        long startTime, endTime;
        
        try {
            
            startTime = System.currentTimeMillis();
            
            Log.log("tester", Log.INFO, Tester.class.getSimpleName(), "logger tester message 1", null);
            
            // Wait logger to settle
            //Thread.sleep(1000);
            
            Log.printPoolSizes();
            trashSpeedTest(1);
            
            //Thread.sleep(1000);
            
            Log.printPoolSizes();
            trashSpeedTest(1);
            
            //Thread.sleep(1000);
            
            Log.printPoolSizes();
            trashSpeedTest(2);
            
            //Thread.sleep(1000);
            
            Log.printPoolSizes();
            trashSpeedTest(3);
            
            //Thread.sleep(1000);
            
            Log.printPoolSizes();
            trashSpeedTest(4);
            
            //Thread.sleep(1000);
            
            Log.printPoolSizes();
            trashSpeedTest(5);
            
            Log.printPoolSizes();
            
            //Thread.sleep(1000);
            
            Log.printPoolSizes();
            
            Thread.sleep(1000);
            
            Log.printPoolSizes();



            //consoleTrap();
            
            endTime = System.currentTimeMillis();
            
            System.out.println("total time:" + (endTime - startTime));
            
            // Finally stop the logger
            
            
        } catch (Exception ex) {
            // if tester fails, print exception traditionally
            System.out.println("tester error");
            ExceptionUtils.printRootCauseStackTrace(ex);
        } finally {
            // Shut down logger
            // they are NOT daemons
            shotLogger.stop();
        }
    }
    
    public static void trashSpeedTest(int numThreads) {
        
        //Log.log("tester", Log.ERROR, Tester.class.getSimpleName(), "starting RandomShitGenerators", null);
        System.out.println("test with " + numThreads + " workers");
        
        long startTime, endTime, deltaTime;
        
        startTime = System.currentTimeMillis();
        
        for(int i = 1 ; i < numThreads; i++) {
            RandomShitGenerator rsg = new RandomShitGenerator("RSG-" + i, 100000, 10);
            rsg.start();
        }
        
        RandomShitGenerator rsg = new RandomShitGenerator("RSG-main", 100000, 10);
        rsg.run();
        
        endTime = System.currentTimeMillis();
        deltaTime = endTime - startTime;
        System.out.println("first test: " + deltaTime + " milliseconds");
    }
    
    
    public static void consoleTrap() {
        // wait before close
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        String input;
        while(!quit) {
            input = scanner.nextLine();
            if(input.equalsIgnoreCase("quit")) {
                Log.log("system.in", Log.INFO, Tester.class.getSimpleName(), "quit requested from console", null);
                quit = true;
            } else {
                Log.log("system.in", Log.INFO, Tester.class.getSimpleName(), input, null);
                Log.printPoolSizes();
            }

        }
    }
    
}
