package org.me.particlesystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;
import org.me.particlesystem.Particle;

public class ParticleSystem5 {

    private Particle[] mParticles;

    // probably a good idea to add these two to the constructor
    private int PARTICLECOUNT = 200;
    private int activeParticles = 0;

    // gravity is 10, no real reason, but gotta start with something
    // and 10m/s sounds good
    private float GRAVITY = 10f;
    private float PARTICLESIZE = 0.03f;
    // don't let particles go below this z value
    private float FLOOR = 0.0f;
    // this is used to track the time of the last update so that
    // we can calculate a frame rate to find out how far a particle
    // has moved between updates
    private long lastTime;

    // for use to draw the particle
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mIndexBuffer;
    private FloatBuffer mTextureBuffer;
    int textureId;
    private Random gen;
    short[] icoords = {0, 1, 2, 0, 2, 3};

    public ParticleSystem5() {
        mParticles = new Particle[PARTICLECOUNT];

        // setup the random number generator
        gen = new Random(System.currentTimeMillis());
        // loop through all the particles and create new instances of each one
        for (int i = 0; i < PARTICLECOUNT; i++) {
            mParticles[i] = new Particle();
            initParticle(i);
        }

        // a simple triangle, kinda like this ^
//        float[] coords = {
//            -PARTICLESIZE, 0.0f, 0.0f,
//            PARTICLESIZE, 0.0f, 0.0f,
//            0.0f, 0.0f, PARTICLESIZE};


//        float[] coords = {-0.1f, 0.0f, 0.0f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f};
//
//        short[] icoords = {0, 1, 2};

//        float coords[] = {
//            -1.0f, 1.0f, 0.0f, // 0, Top Left
//            -1.0f, -1.0f, 0.0f, // 1, Bottom Left
//            1.0f, -1.0f, 0.0f, // 2, Bottom Right
//            1.0f, 1.0f, 0.0f, // 3, Top Right
//        };

        float coords[] = {
            -1.0f, 1.0f, 0.0f, // 0, Top Left
            -1.0f, 0.0f, 0.0f, // 1, Bottom Left
            0.0f, 0.0f, 0.0f, // 2, Bottom Right
            0.0f, 1.0f, 0.0f, // 3, Top Right
        };

        float[] tex = {0, 0,
            0, 1,
            1, 1,
            1, 0};

//        float[] tex = {0, 1,
//            0, 0,
//            1, 0,
//            1, 1,};

        mVertexBuffer = makeFloatBuffer(coords);
        mIndexBuffer = makeShortBuffer(icoords);
        mTextureBuffer = makeFloatBuffer(tex);

        lastTime = System.currentTimeMillis();
    }

    // used to make native order float buffers
    private FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    // used to make native order short buffers
    private ShortBuffer makeShortBuffer(short[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 2);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_TEXTURE);
        gl.glEnable(GL10.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(gl.GL_BLEND);
        gl.glDisable(GL10.GL_DEPTH_TEST);
//                gl.glTranslatef(0, 0, -5);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        ((GL11Ext) gl).glDrawTexfOES(10, 10, 0, 64, 64);

        for (int i = 0; i < PARTICLECOUNT; i++) {
            gl.glPushMatrix();
            gl.glColor4f(mParticles[i].red, mParticles[i].green, mParticles[i].blue, 1.0f);
            gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);
            gl.glScalef(1f, 1f, 1f);
            gl.glDrawElements(GL10.GL_TRIANGLES, icoords.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
            gl.glPopMatrix();
        }

        gl.glDisable(GL10.GL_TEXTURE);
        gl.glDisable(GL10.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(gl.GL_BLEND);
    // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
    }

    private void initParticle(int i) {
        // loop through all the particles and create new instances of each one
        mParticles[i].x = -2f;
        mParticles[i].y = 0f;
        mParticles[i].z = 0f;
        // random x and y speed between -1.0 and 1.0
        mParticles[i].dx = (gen.nextFloat() * 2f) + 2f;
        mParticles[i].dy = (gen.nextFloat() * 2f) - 1f;
        // random z speed between 4.0 and 7.0
        mParticles[i].dz = (gen.nextFloat() * 3) + 4f;

        // set color (mostly blue, for water)
        mParticles[i].blue = (gen.nextFloat() + 1f) / 2f;
        mParticles[i].red = mParticles[i].blue * .8f;
        mParticles[i].green = mParticles[i].blue * .8f;

        // set time to live
        mParticles[i].timeToLive = gen.nextFloat() * 1f + 0.6f;
    }

    // update the particle system, move everything
    public void update() {
        // calculate time between frames in seconds
        long currentTime = System.currentTimeMillis();
        float timeFrame = (currentTime - lastTime) / 1000f;
        // replace the last time with the current time.
        lastTime = currentTime;

        // add the particles slowly
        if (activeParticles < PARTICLECOUNT) {
            // calculate how many particles per frame.  I have set
            // the particles per second at 100
            int addParticleCount = new Float(100f * timeFrame).intValue();
            // always be adding at least one particle
            if (addParticleCount < 1) {
                addParticleCount = 1;
            }
            activeParticles = activeParticles + addParticleCount;
            if (activeParticles > PARTICLECOUNT) {
                activeParticles = PARTICLECOUNT;
            }
        }

        // move the particles
        for (int i = 0; i < activeParticles; i++) {
            // first apply a gravity to the z speed, in this case
            mParticles[i].dz = mParticles[i].dz - (GRAVITY * timeFrame);

            // second move the particle according to it's speed
            mParticles[i].x = mParticles[i].x + (mParticles[i].dx * timeFrame);
            mParticles[i].y = mParticles[i].y + (mParticles[i].dy * timeFrame);
            mParticles[i].z = mParticles[i].z + (mParticles[i].dz * timeFrame);

            // third if the particle hits the 'floor' reverse
            // the z speed and cut in half
            if (mParticles[i].z <= FLOOR) {
                mParticles[i].z = FLOOR;
//				mParticles[i].dx = 0f;
//				mParticles[i].dy = 0f;
                mParticles[i].dz = mParticles[i].dz * -.5f;
            }

            // fourth decrement the time to live for the particle,
            // if it gets below zero, respawn it
            mParticles[i].timeToLive = mParticles[i].timeToLive - timeFrame;
            if (mParticles[i].timeToLive < 0f) {
                initParticle(i);
            }
        }
    }

    public int getTextureId() {
        return textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }
}
