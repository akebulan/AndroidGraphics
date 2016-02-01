/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.androidgraphics;

/**
 *
 * @author akebulan
 */
public class Position {

    private double x;
    private double y;
    private double z;

    public Position() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get the value of z
     *
     * @return the value of z
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the value of z
     *
     * @param z new value of z
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public double getY() {
        return y;
    }

    /**
     * Set the value of y
     *
     * @param y new value of y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public double getX() {
        return x;
    }

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Position{" + "x=" + x + " y=" + y + " z=" + z + '}';
    }
}
