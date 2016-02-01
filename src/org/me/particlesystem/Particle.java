/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.particlesystem;

import java.util.Random;

/**
 *
 * @author lighthammer
 */
public class Particle {

    // location
    public float x, y, z;
    // velocity
    public float dx, dy, dz;
    // color
    public float red, green, blue, alpha;
    //size
    public float scale = 0;
    // time to live
    public float timeToLive;
    public float maxToLive;

    public Particle() {
    }

    // the constructor which also assigns location
    public Particle(float newx, float newy, float newz) {
        super();
        this.x = newx;
        this.y = newy;
        this.z = newz;
    }

    // constructor to assign location and velocity
    public Particle(float newx, float newy, float newz, float newdx, float newdy, float newdz) {
        super();
        this.x = newx;
        this.y = newy;
        this.z = newz;
        this.dx = newdx;
        this.dy = newdy;
        this.dz = newdz;
    }
}
