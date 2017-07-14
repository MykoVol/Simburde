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

    private int getRandom(int min, int max) {
        Random r = new Random();
        int res = 0;
        if (max >= 0 && min <= 0) res = r.nextInt((max + Math.abs(min)) + 1) + min;
        else if (max >= 0 && min >= 0) res = r.nextInt((max - min) + 1) + min;
        else if (max <= 0 && min <= 0) res = -r.nextInt((Math.abs(min) - Math.abs(max)) + 1) + Math.abs(max);
//        LOGGER.info("Rand = " + res + "(" + min + "/" + max + ")");
        return res;
    }


    private int getRandom(int max) {
        return getRandom(0, max);
    }


    public void mouseGlide() {
        try {
            setInterrupt(false);//init set
            humanMouseMove();
        } catch (AWTException e) {
            LOGGER.error(e);
        }
    }

    public void humanMouseMove() throws AWTException {
        Robot robot = new Robot();
//       current position
        Point pointStart = MouseInfo.getPointerInfo().getLocation();
//        new random point in range
        int endX = getRandom(AppProperties.getLeftUpperAngleX(), AppProperties.getRightBottomAngleX());
        int endY = getRandom(AppProperties.getLeftUpperAngleY(), AppProperties.getRightBottomAngleY());
        Point pointEnd = new Point(endX, endY);

        int speed = AppProperties.getMouseSpeed();

        Point[] cowardList;
        double t;    //the time interval
        double k = .025;
        cowardList = new Point[4];

        //set the beginning and end points
        cowardList[0] = pointStart;
        cowardList[3] = new Point(getRandom(AppProperties.getLeftUpperAngleX(), AppProperties.getRightBottomAngleX()), (getRandom(AppProperties.getLeftUpperAngleY(), AppProperties.getRightBottomAngleY())));

        int xout = Math.abs(pointEnd.x - pointStart.x) / 10;
        int yout = Math.abs(pointEnd.y - pointStart.y) / 10;

        int x = 0, y = 0;
//        intermediate points
        x = pointStart.x < pointEnd.x
                ? pointStart.x + ((xout > 0) ? getRandom(1, xout) : 1)
                : pointStart.x - ((xout > 0) ? getRandom(1, xout) : 1);
        y = pointStart.y < pointEnd.y
                ? pointStart.y + ((yout > 0) ? getRandom(1, yout) : 1)
                : pointStart.y - ((yout > 0) ? getRandom(1, yout) : 1);
        cowardList[1] = new Point(x, y);

        x = pointEnd.x < pointStart.x
                ? pointEnd.x + ((xout > 0) ? getRandom(1, xout) : 1)
                : pointEnd.x - ((xout > 0) ? getRandom(1, xout) : 1);
        y = pointEnd.y < pointStart.y
                ? pointEnd.y + ((yout > 0) ? getRandom(1, yout) : 1)
                : pointEnd.y - ((yout > 0) ? getRandom(1, yout) : 1);
        cowardList[2] = new Point(x, y);

        double px = 0, py = 0;
        for (t = k; t <= 1 + k; t += k) {
            //use Berstein polynomials
            px = (cowardList[0].x + t * (-cowardList[0].x * 3 + t * (3 * cowardList[0].x -
                    cowardList[0].x * t))) + t * (3 * cowardList[1].x + t * (-6 * cowardList[1].x +
                    cowardList[1].x * 3 * t)) + t * t * (cowardList[2].x * 3 - cowardList[2].x * 3 * t) +
                    cowardList[3].x * t * t * t;
            py = (cowardList[0].y + t * (-cowardList[0].y * 3 + t * (3 * cowardList[0].y -
                    cowardList[0].y * t))) + t * (3 * cowardList[1].y + t * (-6 * cowardList[1].y +
                    cowardList[1].y * 3 * t)) + t * t * (cowardList[2].y * 3 - cowardList[2].y * 3 * t) +
                    cowardList[3].y * t * t * t;
            robot.mouseMove((int) px, (int) py);
            robot.delay(getRandom(speed, speed * 2));
            if (isNeedBreak((int) px, (int) py)) return;
        }
        mouseClick(robot);
        robot.delay(getRandom(AppProperties.getSleepBeforeMouseMove()));
        humanMouseMove();
    }

    private void mouseClick(Robot r) {
        if (AppProperties.isPressCtrl()) {
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_CONTROL);
        }
        if (AppProperties.getClickOption() == 1) {
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.delay(getRandom(AppProperties.getSleepInMouseClick()));
            r.mouseRelease(InputEvent.BUTTON1_MASK);
        }
        r.mouseWheel(getRandom(-AppProperties.getMouseWheelMove(), AppProperties.getMouseWheelMove()));
    }

    private boolean isNeedBreak(double X, double Y) {
        Boolean positionChanged = false;
        if (AppProperties.isInterruptByMove()) {
            MouesPosition mouseNow = new MouesPosition();
            positionChanged = (int) mouseNow.getX() != (int) X || (int) mouseNow.getY() != (int) Y;

//            if (positionChanged){
////                temp take cur position one move time
//                LOGGER.debug("Mouse interrupted. Second try");
//                LOGGER.debug("Was int X,Y"+Integer.toString((int)X)+"; "+Integer.toString((int)Y));
//                LOGGER.debug("Was dbl X,Y"+Double.toString(X)+"; "+Double.toString(Y));
//                LOGGER.debug("Cur dbl X,Y"+Double.toString(mouseNow.getX())+"; "+Double.toString(mouseNow.getY()));
//                mouseNow = new MouesPosition();
//                positionChanged = (int) mouseNow.getX() != (int) X || (int) mouseNow.getY() != (int) Y;
//                LOGGER.debug("Second results");
//                LOGGER.debug("Was int X,Y"+Integer.toString((int)X)+"; "+Integer.toString((int)Y));
//                LOGGER.debug("Was dbl X,Y"+Double.toString(X)+"; "+Double.toString(Y));
//                LOGGER.debug("Cuwr dbl X,Y"+Double.toString(mouseNow.getX())+"; "+Double.toString(mouseNow.getY()));
//            }
        }

        return (positionChanged || getInterrupt());
    }
}
