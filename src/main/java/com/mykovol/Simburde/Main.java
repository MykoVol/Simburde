package com.mykovol.Simburde;

import org.apache.log4j.Logger;

import static com.sun.jna.NativeLibrary.getProcess;

/**
 * Created by MykoVol on 2/21/2017.
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        AppProperties.readProperties();
//        second part of a DB passwords is provided by parameter (DB pass = configPass + paramPass)
        try {
            new HotKey().init();
        } catch (Exception e) {
            LOGGER.error("Hot key will not work. Library did not loaded on " + System.getProperty("sun.arch.data.model") + "bit system",e);
        }


    }
}
