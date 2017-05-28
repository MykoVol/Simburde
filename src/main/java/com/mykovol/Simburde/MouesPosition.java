package com.mykovol.Simburde;

import java.awt.*;

/**
 * Created by MykoVol on 5/28/2017.
 */
public class MouesPosition {
    private double X;
    private double Y;

    public MouesPosition() {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        X = b.getX();
        Y = b.getY();
    }

    public double  getX() {
        return X;
    }

    public double getY() {
        return Y;
    }
}
