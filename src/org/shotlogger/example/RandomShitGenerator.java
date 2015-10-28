package org.shotlogger.example;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.apache.commons.lang3.RandomStringUtils;
import org.shotlogger.Log;

/**
 *
 * @author shotbygun
 */
public class RandomShitGenerator implements Runnable {

        int loops, sleep, i;
        String name;
        
        public RandomShitGenerator(String name, int loops, int sleep) {
            this.name = name;
            this.loops = loops;
            this.sleep = sleep;
            i = 0;
        }
        
        
        @Override
        public void run() {
            
            String randomText;
            
            while(i++ < loops) {
                
                try {
                    //Thread.sleep(sleep);
                    
                    try {
                        randomText = RandomStringUtils.random(100, true, true);
                        
                        throw new Exception(randomText);
                    } catch (Exception ex) {
                        Log.log("testerRandomData", Log.DEBUG, getClass().getSimpleName(), "test", ex);
                    }
                    
                } catch (Exception uex) {
                    Log.log(name, Log.CRITICAL, getClass().getSimpleName(), "unexpected exception", uex);
                }
                
            }
            
            System.out.println(name + " done");
        }
        
        public void start() {
            Thread thread = new Thread(this);
            thread.setPriority(3);
            thread.setName(name);
            thread.setDaemon(true);
            thread.start();
        }
        
        
        
    }