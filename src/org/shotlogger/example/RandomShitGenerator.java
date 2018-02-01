package org.shotlogger.example;

import org.shotlogger.Log;

/**
 *
 * @author shotbygun
 */
public class RandomShitGenerator implements Runnable {

    int loops, i;
    String name;
    Thread thread;

    public RandomShitGenerator(String name, int loops) {
        this.name = name;
        this.loops = loops;
        i = 0;
    }


    @Override
    public void run() {

        String randomText;

        while(i++ < loops) {

            try {

                try {
                    //randomText = RandomStringUtils.random(100, true, true);
                    randomText = Long.toHexString(System.currentTimeMillis()+i);

                    throw new Exception(randomText);
                } catch (Exception ex) {
                    Log.log("testerRandomData", Log.DEBUG, getClass().getSimpleName(), name, ex);
                }

            } catch (Exception uex) {
                Log.log(name, Log.CRITICAL, getClass().getSimpleName(), "unexpected exception", uex);
            }

        }

        System.out.println(name + " done");
    }

    public void start() {
        thread = new Thread(this);
        thread.setPriority(3);
        thread.setName(name);
        thread.setDaemon(true);
        thread.start();
    }
}