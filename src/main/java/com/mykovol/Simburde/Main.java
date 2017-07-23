package com.mykovol.Simburde;

import org.apache.log4j.Logger;

import static com.sun.jna.NativeLibrary.getProcess;

/**
 * Created by MykoVol on 2/21/2017.
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        if (args.length>0) {
            if (Integer.parseInt(args[0])<4) SimburdeConfig.getInstance().setDefaultMsg("Successfully updated");
            else SimburdeConfig.getInstance().setDefaultMsg("Update failed!");
        }


        AppProperties.readProperties();
        try {
            new HotKey().init();
        } catch (Exception e) {
            LOGGER.error("Hot key will not work. Library did not loaded on " + System.getProperty("sun.arch.data.model") + "bit system",e);
        }


    }
}
