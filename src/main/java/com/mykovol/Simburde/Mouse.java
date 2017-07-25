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
    private static MouseStatus mouseStatus = MouseStatus.INACTIVE;

    public static void setStatus(MouseStatus currentStatus) {
//        synchronized (Mouse.mouseStatus) {
        mouseStatus = currentStatus;
//            return !Mouse.mouseStatus;
//        }
    }

    public static MouseStatus getStatus() {
//        synchronized (Mouse.mouseStatus) {
        return mouseStatus;
//        }
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
            Mouse.setStatus(MouseStatus.ACTIVE);
            Robot robot = new Robot();
            //move mouse from point to point till it is stopped
            while (Mouse.getStatus()==MouseStatus.ACTIVE) humanMouseMove(robot);
        } catch (AWTException e) {
            LOGGER.error(e);
        } catch (InterruptedException a) {
        } finally {
            Mouse.setStatus(MouseStatus.INACTIVE);
        }
    }

    public void humanMouseMove(Robot robot) throws AWTException, InterruptedException {
        if (Mouse.getStatus() != mouseStatus.ACTIVE) throw new InterruptedException();
//       current position
        Point pointStart = MouseInfo.getPointerInfo().getLocation();
//        new random point in range
        int endX = getRandom(AppProperties.getLeftUpperAngleX(), AppProperties.getRightBottomAngleX());
        int endY = getRandom(AppProperties.getLeftUpperAngleY(), AppProperties.getRightBottomAngleY());
        Point pointEnd = new Point(endX, endY);

        int speed = AppProperties.getMouseSpeed();

        Point[] cowardList;
        double t;    //the time interval
        double k =.0005*speed;// .025;
        if  (k>.025) k = .025;
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
            robot.delay(getRandom(0, Math.round(1000/speed)));
            if (isNeedBreak((int) px, (int) py)) throw new InterruptedException();
        }
        mouseClick(robot);
        if (Mouse.getStatus() != mouseStatus.ACTIVE) throw new InterruptedException();
        robot.delay(getRandom(AppProperties.getSleepBeforeMouseMove()));
        printCharacters(robot);
        if (Mouse.getStatus() != mouseStatus.ACTIVE) throw new InterruptedException();
    }

    private void mouseClick(Robot r) {
        if (AppProperties.isPressCtrl()) {
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_CONTROL);
        }
        if (AppProperties.getClickOption() == 1) {
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);
            r.keyPress(KeyEvent.VK_ESCAPE);
            r.keyRelease(KeyEvent.VK_ESCAPE);
        }
        r.mouseWheel(getRandom(-AppProperties.getMouseWheelMove(), AppProperties.getMouseWheelMove()));
    }

    private void printCharacters(Robot r) {
        if (!AppProperties.getKeyCharacters().isEmpty()) {
            r.keyPress(KeyEvent.VK_V);
            r.keyRelease(KeyEvent.VK_V);
            r.delay(1000);
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_Z);
            r.keyRelease(KeyEvent.VK_Z);
            r.keyRelease(KeyEvent.VK_CONTROL);
        }
    }

    private boolean isNeedBreak(double X, double Y) {
        Boolean positionChanged = false;
        if (AppProperties.isInterruptByMove()) {
            MouesPosition mouseNow = new MouesPosition();
//            mouse moved more than for 5 px
            positionChanged = (Math.abs(mouseNow.getX() - X) > 5) || (Math.abs(mouseNow.getY() - Y)>5);
        }
        return (positionChanged || Mouse.getStatus() != MouseStatus.ACTIVE);
    }
}
