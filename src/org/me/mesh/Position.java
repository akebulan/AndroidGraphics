/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.mesh;

/**
 *
 * @author akebulan
 */
public class Position {

//    private double x;
//    private double y;
//    private double z;
    
    private Double dx,dy,dz;

    public Position() {
    	dx = new Double(0);
    	dy = new Double(0);
    	dz = new Double(0);
//
//        this.x = 0;
//        this.y = 0;
//        this.z = 0;
        
    }

    public Position(double x, double y, double z) {
//        this.x = x;
//        this.y = y;
//        this.z = z;
    	dx = new Double(x);
    	dy = new Double(y);
    	dz = new Double(z);
    	
 
    }

    public Position(Position position) {
        Position position1 = new Position(position.getX(), position.getY(), position.getZ());
//        this.x = position1.getX();
//        this.y = position1.getY();
//        this.z = position1.getZ();
    	dx = new Double(position1.getX());
    	dy = new Double(position1.getY());
    	dz = new Double(position1.getZ());

    }

    /**
     * Get the value of z
     *
     * @return the value of z
     */
    public double getZ() {
//        return z;
    	return dz.doubleValue();
    }

    /**
     * Set the value of z
     *
     * @param z new value of z
     */
    public void setZ(double z) {
    	dz = Double.valueOf(z);
//        this.z = z;
//        this.fz = new Float(this.z).floatValue();
       
    }

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public double getY() {
//        return y;
    	return dy.doubleValue();

    }

    /**
     * Set the value of y
     *
     * @param y new value of y
     */
    public void setY(double y) {
    	dy = Double.valueOf(y);

//        this.y = y;
//        this.fy = new Float(this.y).floatValue();
       
    }

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public double getX() {
//        return x;
    	return dx.doubleValue();

    }

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    public void setX(double x) {
//        this.x = x;
    	dx = Double.valueOf(x);

//        this.fx = new Float(this.x).floatValue();       
    }
    
    public float getFx(){
    	return dx.floatValue();
    }
    
    public float getFy(){
    	return dy.floatValue();
    }
    
    public float getFz(){
    	return  dz.floatValue();
    }    


    @Override
    public String toString() {
        return "Position{" + "x=" + dx.doubleValue() + " y=" + dy.doubleValue() + " z=" + dz.doubleValue() + '}';
    }


    
}
