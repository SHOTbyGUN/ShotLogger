package org.shotlogger.example;

import java.util.Scanner;
import org.shotlogger.Log;
import org.shotlogger.ShotLogger;

/**
 *
 * @author shotbygun
 */
public class Tester {
    
    private static int rsgCounter = 0;
    
    public static void main(String[] args) {
        
        /*
            Testing exceptions before initializing ShotLogger
        */
        
        Log.info("tester", Tester.class.getSimpleName(), "logger pre init message", null);
        
        try {
            throw new Exception("a wild exception appears!");
        } catch (Exception ex) {
            Log.info("tester", "main", "exception format test", ex);
        }
        
        
        ShotLogger shotLogger = ShotLogger.getShotLogger();
        shotLogger.startBasic("/raid/shotbygun/temp/log");
        
        long startTime, endTime;
        
        try {
            
            startTime = System.currentTimeMillis();
            
            Log.info("tester", Tester.class.getSimpleName(), "logger after init message", null);
            
            // Wait logger to settle
            Thread.sleep(1000);
            
            shotLogger.printPoolSizes();
            trashSpeedTest(1);
            
            //Thread.sleep(1000);
            
            shotLogger.printPoolSizes();
            trashSpeedTest(1);
            
            //Thread.sleep(1000);
            
            shotLogger.printPoolSizes();
            trashSpeedTest(2);
            
            //Thread.sleep(1000);
            
            shotLogger.printPoolSizes();
            trashSpeedTest(3);
            
            //Thread.sleep(1000);
            
            shotLogger.printPoolSizes();
            trashSpeedTest(4);
            
            //Thread.sleep(1000);
            
            shotLogger.printPoolSizes();
            trashSpeedTest(5);
            
            shotLogger.printPoolSizes();
            
            //Thread.sleep(1000);
            
            shotLogger.printPoolSizes();
            
            Thread.sleep(1000);
            
            shotLogger.printPoolSizes();
            
            
            endTime = System.currentTimeMillis();
            System.out.println("shit generation time:" + (endTime - startTime));
            Log.info("tester", null, "shit generation done", null);
            
            Thread.sleep(1000);
            
            endTime = System.currentTimeMillis();
            System.out.println("total time:" + (endTime - startTime));
            // Finally stop the logger
            
            
        } catch (Exception ex) {
            // if tester fails, print exception traditionally
            System.out.println("tester error");
            ex.printStackTrace();
        } finally {
            // Shut down logger
            // they are NOT daemons
            shotLogger.stopAndWait();
        }
    }
    
    public static void trashSpeedTest(int numThreads) {
        
        //shotLogger.log("tester", shotLogger.ERROR, Tester.class.getSimpleName(), "starting RandomShitGenerators", null);
        System.out.println("test with " + numThreads + " workers");
        
        long startTime, endTime, deltaTime;
        
        startTime = System.currentTimeMillis();
        
        for(int i = 1 ; i < numThreads; i++) {
            RandomShitGenerator rsg = new RandomShitGenerator("RSG-" + rsgCounter++, 100000, 10);
            rsg.start();
        }
        
        RandomShitGenerator rsg = new RandomShitGenerator("RSG-main", 100000, 10);
        rsg.run();
        
        endTime = System.currentTimeMillis();
        deltaTime = endTime - startTime;
        System.out.println("RSG-main generation: " + deltaTime + " milliseconds");
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
                ShotLogger.getShotLogger().printPoolSizes();
            }

        }
    }
    
}
