package com.mykovol.Simburde;

import com.melloware.jintellitype.JIntellitype;


/**
 * Created by MykoVol on 2/20/2017.
 */
public class HotKey {
    void init() {

        if (System.getProperty("sun.arch.data.model").equals("32"))
            JIntellitype.setLibraryLocation("lib/JIntellitype.dll");
        else JIntellitype.setLibraryLocation("lib/JIntellitype64.dll");


//        JIntellitype.getInstance().registerHotKey(1, JIntellitype. MOD_CONTROL + JIntellitype.MOD_ALT, (int) '8');
        JIntellitype.getInstance().registerHotKey(1, 0, 117);
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, (int) '9');
        JIntellitype.getInstance().registerHotKey(3, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, (int) '0');
        JIntellitype.getInstance().registerHotKey(4, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, (int) '8');
        JIntellitype.getInstance().addHotKeyListener(arg0 -> {
            if (arg0 == 1)
//                mouse in new thread to interrupt it than by the same key
                if (Mouse.isInMove()) Mouse.setInterrupt(true);
                else {
                    Thread mouseMoveThread = new Thread(() -> new Mouse().mouseGlide());
                    mouseMoveThread.start();
                }
            else if (arg0 == 2) {
                SimburdeConfig.getInstance().setMouseLeftUpperAngle();
            } else if (arg0 == 3) {
                SimburdeConfig.getInstance().setMouseRightBottomAngle();
            } else if (arg0 == 4) {
                SimburdeConfig.getInstance().showForm();

            }

        });

        SimburdeConfig.getInstance().showForm();

    }

}