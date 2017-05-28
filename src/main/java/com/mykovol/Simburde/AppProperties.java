package com.mykovol.Simburde;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Created by MykoVol on 3/21/2017.
 */
public class AppProperties {
    private static Logger LOGGER = Logger.getLogger(AppProperties.class);
    private static URLConnection propURLConnection;
    private static String propFileName = "config.properties";

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

    public static void setMouseSpeed(int mouseSpeed) {
        AppProperties.mouseSpeed = mouseSpeed;
    }

    private static int leftUpperAngleX;
    private static int leftUpperAngleY;
    private static int rightBottomAngleX;
    private static int rightBottomAngleY;
    private static int mouseSpeed;
    private static int sleepInMouseClick;
    private static int sleepBeforeMouseMove;
    private static int mouseWheelMove;
    private static boolean interruptByMove;
    private static boolean pressCtrl;

    public static boolean isPressCtrl() {
        return pressCtrl;
    }

    public static void setPressCtrl(boolean pressCtrl) {
        AppProperties.pressCtrl = pressCtrl;
    }


    public static boolean isInterruptByMove() {
        return interruptByMove;
    }

    public static void setInterruptByMove(boolean interruptByMove) {
        AppProperties.interruptByMove = interruptByMove;
    }

    public static int getMouseWheelMove() {
        return mouseWheelMove;
    }

    public static void setMouseWheelMove(int mouseWheelMove) {
        AppProperties.mouseWheelMove = mouseWheelMove;
    }

    public static int getSleepBeforeMouseMove() {
        return sleepBeforeMouseMove;
    }

    public static void setSleepBeforeMouseMove(int sleepBeforeMouseMove) {
        AppProperties.sleepBeforeMouseMove = sleepBeforeMouseMove;
    }

    public static int getClickOption() {
        return clickOption;
    }

    public static void setClickOption(int clickOption) {
        AppProperties.clickOption = clickOption;
    }

    private static int clickOption; //1-one click,2-double click

    public static int getSleepInMouseClick() {
        return sleepInMouseClick;
    }

    public static void setSleepInMouseClick(int sleepInMouseClick) {
        AppProperties.sleepInMouseClick = sleepInMouseClick;
    }

    public static int getMouseSpeed() {
        return mouseSpeed;
    }

    private static String getPropFilePath(){
//        return AppProperties.class.getClassLoader().getResource(propFileName).getPath();
        return propFileName;
    }

    private static File getPropFile(){
        return new File(getPropFilePath());
    }

    public static void readProperties() {

        Properties prop = new Properties();

        try (InputStream inputStream = new FileInputStream(getPropFile())) {
            prop.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("property file '" + getPropFilePath() + "' not found and will be created with default values", e);
//            save default and reread
            SimburdeConfig.getInstance().saveSettings();
            try (InputStream inputStream = new FileInputStream(getPropFile())) {
                prop.load(inputStream);
            } catch (IOException e2) {
                LOGGER.error("property file '" + getPropFilePath() + "' cannot be created", e2);
                return;
            }
        }

        setLeftUpperAngleX(Integer.parseInt(prop.getProperty("leftUpperAngle.X")));
        setLeftUpperAngleY(Integer.parseInt(prop.getProperty("leftUpperAngle.Y")));
        setRightBottomAngleX(Integer.parseInt(prop.getProperty("rightBottomAngle.X")));
        setRightBottomAngleY(Integer.parseInt(prop.getProperty("rightBottomAngle.Y")));
        setMouseSpeed(Integer.parseInt(prop.getProperty("mouseSpeed")));
        setSleepInMouseClick(Integer.parseInt(prop.getProperty("sleepInMouseClick")));
        setSleepBeforeMouseMove(Integer.parseInt(prop.getProperty("sleepBeforeMouseMove")));
        setMouseWheelMove(Integer.parseInt(prop.getProperty("mouseWheelMove")));
        setClickOption(Integer.parseInt(prop.getProperty("clickOption")));
        setInterruptByMove(Boolean.parseBoolean(prop.getProperty("interruptByMove")));
        setPressCtrl(Boolean.parseBoolean(prop.getProperty("pressCtrl")));
    }

    public static void saveProperties() {
        Properties prop = new Properties();
        prop.setProperty("leftUpperAngle.X", Integer.toString(getLeftUpperAngleX()));
        prop.setProperty("leftUpperAngle.Y", Integer.toString(getLeftUpperAngleY()));
        prop.setProperty("rightBottomAngle.X", Integer.toString(getRightBottomAngleX()));
        prop.setProperty("rightBottomAngle.Y", Integer.toString(getRightBottomAngleY()));
        prop.setProperty("mouseSpeed", Integer.toString(getMouseSpeed()));
        prop.setProperty("sleepInMouseClick", Integer.toString(getSleepInMouseClick()));
        prop.setProperty("sleepBeforeMouseMove", Integer.toString(getSleepBeforeMouseMove()));
        prop.setProperty("mouseWheelMove", Integer.toString(getMouseWheelMove()));
        prop.setProperty("clickOption", Integer.toString(getClickOption()));
        prop.setProperty("interruptByMove", Boolean.toString(isInterruptByMove()));
        prop.setProperty("pressCtrl", Boolean.toString(isPressCtrl()));

        try (FileOutputStream outputStream = new FileOutputStream(getPropFile())) {
            prop.store(outputStream, null);
        } catch (IOException e) {
            LOGGER.error("property file '" + getPropFilePath() + "' not found in the classpath", e);
        }
    }
}

