package com.mykovol.Simburde;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by MykoVol on 3/21/2017.
 */
public class AppProperties {
    private static Logger LOGGER = Logger.getLogger(AppProperties.class);

    public static int getLeftUpperAngleX() {
        return leftUpperAngleX;
    }

    public static void setLeftUpperAngleX(int leftUpperAngleX) {
        AppProperties.leftUpperAngleX = leftUpperAngleX;
    }

    public static int getLeftUpperAngleY() {
        return leftUpperAngleY;
    }

    public static void setLeftUpperAngleY(int leftUpperAngleY) {
        AppProperties.leftUpperAngleY = leftUpperAngleY;
    }

    public static int getRightBottomAngleX() {
        return rightBottomAngleX;
    }

    public static void setRightBottomAngleX(int rightBottomAngleX) {
        AppProperties.rightBottomAngleX = rightBottomAngleX;
    }

    public static int getRightBottomAngleY() {
        return rightBottomAngleY;
    }

    public static void setRightBottomAngleY(int rightBottomAngleY) {
        AppProperties.rightBottomAngleY = rightBottomAngleY;
    }

    private static int leftUpperAngleX;
    private static int leftUpperAngleY;
    private static int rightBottomAngleX;
    private static int rightBottomAngleY;


    public static void getProperties() {
        InputStream inputStream = null;

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = AppProperties.class.getClassLoader().getResourceAsStream(propFileName);

            try {
                prop.load(inputStream);
            } catch (IOException e) {
                LOGGER.error("property file '" + propFileName + "' not found in the classpath", e);
            }

//            setLeftUpperAngleX((int)prop.get("leftUpperAngle.X"));
//            setLeftUpperAngleY((int)prop.get("leftUpperAngle.Y"));
//            setRightBottomAngleX((int)prop.get("rightBottomAngle.X"));
//            setRightBottomAngleY((int)prop.get("rightBottomAngle.Y"));

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error("inputStream close", e);
            }
        }
    }

}
