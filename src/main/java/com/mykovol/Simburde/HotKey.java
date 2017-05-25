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

//        show sync with db summary screen on Ctrl+Alt+T

        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, (int) '8');
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, (int) '9');
        JIntellitype.getInstance().registerHotKey(3, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, (int) '0');
        JIntellitype.getInstance().registerHotKey(4, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, (int) '7');
        JIntellitype.getInstance().addHotKeyListener(arg0 -> {
            if (arg0 == 1)
                SimburdeConfig.getInstance().showForm();
            else if (arg0 == 2) {
                SimburdeConfig.getInstance().setMouseLeftUpperAngle();
            } else if (arg0 == 3) {
                SimburdeConfig.getInstance().setMouseRightBottomAngle();
            } else if (arg0 == 4) {
                new Mouse().mouseGlide(AppProperties.getLeftUpperAngleX(), AppProperties.getLeftUpperAngleY(), AppProperties.getRightBottomAngleX(), AppProperties.getRightBottomAngleY(), 10000, 10000);
            }

        });

    }

}