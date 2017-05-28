package com.mykovol.Simburde;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by VMykolaichuk on 5/25/2017.
 */
public class Mouse {
    private static final Logger LOGGER = Logger.getLogger(Mouse.class);
    private static Boolean interruptMouseMove = true;

    public static Boolean isInMove() {
        synchronized (Mouse.interruptMouseMove) {
            return !Mouse.interruptMouseMove;
        }
    }

    public static Boolean getInterrupt() {
        synchronized (Mouse.interruptMouseMove) {
            return Mouse.interruptMouseMove;
        }
    }

    public static void setInterrupt(boolean interruptMouseMove) {
        synchronized (Mouse.interruptMouseMove) {
            Mouse.interruptMouseMove = interruptMouseMove;
        }
    }

    private int getRandomBetween(int min, int max) {
        Random r = new Random();
        if (min > 0) return r.nextInt(max - min) + min;
        else return r.nextInt(max + max) + min;
    }

    public void mouseGlide() {
        try {
            setInterrupt(false);
            moveToRandomPoint();
        } catch (AWTException e) {
            LOGGER.error(e);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }


    private void moveToRandomPoint() throws InterruptedException, AWTException {
//        current position
        MouesPosition mouseNow = new MouesPosition();
        int beginX = (int) mouseNow.getX();
        int beginY = (int) mouseNow.getY();
//        new random pint in range
        int endX = getRandomBetween(AppProperties.getLeftUpperAngleX(), AppProperties.getRightBottomAngleX());
        int endY = getRandomBetween(AppProperties.getLeftUpperAngleY(), AppProperties.getRightBottomAngleY());
//        init settings
        Robot r = new Robot();
        int t = AppProperties.getMouseSpeed();
        int n = AppProperties.getMouseSpeed();
//        delta move
        double dx = (endX - beginX) / ((double) n);
        double dy = (endY - beginY) / ((double) n);
        double dt = t / ((double) n);
        double nextX;
        double nextY;
        for (int step = 1; step <= n; step++) {
            nextX = (beginX + dx * step);
            nextY = (beginY + dy * step);
            r.mouseMove((int) nextX, (int) nextY);
            Thread.sleep((int) dt);
            if (isNeedBreak(nextX, nextY)) break;
//        if end point is reached than click and start moving to next point
            if (nextX == endX && nextY == endY) {
                mouseClick(r);
                Thread.sleep(AppProperties.getSleepBeforeMouseMove());
                if (isNeedBreak(nextX, nextY)) break;
                moveToRandomPoint();
            }
        }
    }

    private void mouseClick(Robot r) throws InterruptedException {
        if (AppProperties.isPressCtrl()) {
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_CONTROL);
        }
        if (AppProperties.getClickOption() == 1) {
            r.mousePress(InputEvent.BUTTON1_MASK);
            Thread.sleep(AppProperties.getSleepInMouseClick());
            r.mouseRelease(InputEvent.BUTTON1_MASK);
            r.mouseWheel(getRandomBetween(-AppProperties.getMouseWheelMove(), AppProperties.getMouseWheelMove()));
        } else if (AppProperties.getClickOption() == 2) {
            r.keyPress(KeyEvent.VK_ESCAPE);
            r.keyRelease(KeyEvent.VK_ESCAPE);

            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);

            r.mouseWheel(getRandomBetween(-AppProperties.getMouseWheelMove(), AppProperties.getMouseWheelMove()));
        }
    }

    private boolean isNeedBreak(double X, double Y) {
        Boolean positionChanged = false;
        if (AppProperties.isInterruptByMove()) {
            MouesPosition mouseNow = new MouesPosition();
            positionChanged = (int) mouseNow.getX() != (int) X || (int) mouseNow.getY() != (int) Y;

            if (positionChanged){
                LOGGER.debug("Mouse interrupted");
                LOGGER.debug("Was int X,Y"+Integer.toString((int)X)+"; "+Integer.toString((int)Y));
                LOGGER.debug("Was dbl X,Y"+Double.toString(X)+"; "+Double.toString(Y));
                LOGGER.debug("Cur dbl X,Y"+Double.toString(mouseNow.getX())+"; "+Double.toString(mouseNow.getY()));
            }
        }


        return (positionChanged || getInterrupt());
    }
}
