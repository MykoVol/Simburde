package com.mykovol.Simburde;

import java.awt.*;

/**
 * Created by VMykolaichuk on 5/25/2017.
 */
public class Mouse {
    public void mouseGlide(int x1, int y1, int x2, int y2, int t, int n) {
        try {
            Robot r = new Robot();
            double dx = (x2 - x1) / ((double) n);
            double dy = (y2 - y1) / ((double) n);
            double dt = t / ((double) n);
            for (int step = 1; step <= n; step++) {
                r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
                Thread.sleep((int) dt);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
