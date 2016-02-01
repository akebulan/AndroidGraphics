package org.me.particlesystem;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;
import org.me.mesh.Mesh;

/**
 *
 * @author lighthammer
 */
public class ExplosionParticleSystem extends Mesh {

    private Particle[] mParticles;
    private int PARTICLECOUNT = 1;
    // for use to draw the particle
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mIndexBuffer;
    private FloatBuffer mTextureBuffer;
//    private FloatBuffer normalBuffer;
//    private int textureId;
    // probably a good idea to add these two to the constructor
//    private int activeParticles = 0;
    private float GRAVITY = .2f;
//    private float AIR_RESISTANCE = 30.0f;
    private float AIR_RESISTANCE = 10.0f;
//    private float PARTICLESIZE = 0.02f;
    private Random gen;
    private long lastTime;
    // Rotate params.
//    public float rx = 0;
//    public float ry = 0;
//    public float rz = 0;

    public ExplosionParticleSystem() {
        mParticles = new Particle[PARTICLECOUNT];
        // setup the random number generator
        gen = new Random(System.currentTimeMillis());
        // loop through all the particles and create new instances of each one        gen = new Random(System.currentTimeMillis());

        for (int i = 0; i < PARTICLECOUNT; i++) {
            mParticles[i] = new Particle(i, i, 0);
//            mParticles[i] = new Particle(gen.nextFloat() * 10f, gen.nextFloat() * 10f, 0);

            initParticle(i);

        }

        short[] icoords = {0, 1, 2, 0, 2, 3};

////        float coords[] = {
////            -1.0f, 1.0f, 0.0f, // 0, Top Left
////            -1.0f, 0.0f, 0.0f, // 1, Bottom Left
////            0.0f, 0.0f, 0.0f, // 2, Bottom Right
////            0.0f, 1.0f, 0.0f, // 3, Top Right

        float coords[] = {
            -25.0f, -25.0f, 0.0f, // 0, Bottom Left
            25.0f, -25.0f, 0.0f, // 1, Bottom Right
            25.0f, 25.0f, 0.0f, // 2, Top Right
            -25.0f, 25.0f, 0.0f, // 3, Top Left
        };

        float[] tex = {0, 0,
            0, 1,
            1, 1,
            1, 0};

        float normals[] = {
            0.0f, 0.0f, 1.0f};

        mVertexBuffer = makeFloatBuffer(coords);
        mTextureBuffer = makeFloatBuffer(tex);
        normalBuffer = makeFloatBuffer(normals);
        mIndexBuffer = makeShortBuffer(icoords);
        lastTime = System.currentTimeMillis();

    }            // used to make native order float buffers

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
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }
//    protected FloatBuffer colorBuffer = null;
//    protected float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

//    public void setColors(float[] colors) {
//        // float has 4 bytes.
//        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
//        cbb.order(ByteOrder.nativeOrder());
//        colorBuffer = cbb.asFloatBuffer();
//        colorBuffer.put(colors);
//        colorBuffer.position(0);
//    }
    @Override
    public void draw(GL10 gl) {
//        Log.w("ParticleSystem", "draw");

//        update();
        gl.glEnable(GL10.GL_TEXTURE_2D);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
//
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnable(GL10.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//
//        gl.glEnable(gl.GL_BLEND);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

//        gl.glBlendFunc(gl.GL_ONE, gl.GL_ONE); // invisible
//        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);


        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

//        gl.glLoadIdentity();
        gl.glTranslatef(x, y, z);

        for (int i = 0; i < PARTICLECOUNT; i++) {
            if (mParticles[i].timeToLive > 0f) {
//                gl.glPushMatrix();

                gl.glColor4f(mParticles[i].red, mParticles[i].green, mParticles[i].blue, mParticles[i].alpha);

                gl.glScalef(mParticles[i].scale, mParticles[i].scale, mParticles[i].scale);

                gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);

                gl.glRotatef(rx, 1, 0, 0);
                gl.glRotatef(ry, 0, 1, 0);
                gl.glRotatef(rz, 0, 0, 1);

                gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

//                gl.glPopMatrix();
            }
        }

        gl.glDisable(GL10.GL_TEXTURE);
        gl.glDisable(GL10.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(gl.GL_BLEND);

    }

    private void initParticle(int i) {

        float DOUBLESPEED = 500;
        mParticles[i].dx = (gen.nextFloat() * DOUBLESPEED) - (DOUBLESPEED / 2f);
        mParticles[i].dy = (gen.nextFloat() * DOUBLESPEED) - (DOUBLESPEED / 2f);
        mParticles[i].dz = (gen.nextFloat() * DOUBLESPEED) - (DOUBLESPEED / 2f);

        mParticles[i].alpha = 1f;
//        mParticles[i].maxToLive = (gen.nextFloat() * 1.5f) + 0f;
//        mParticles[i].timeToLive = mParticles[i].maxToLive;
        mParticles[i].maxToLive = 5f;
        mParticles[i].timeToLive = 5f;
    }

    // update the particle system, move everything
    public void update() {
        // calculate time between frames in seconds
        long currentTime = System.currentTimeMillis();
        float timeFrame = (currentTime - lastTime) / 1000f;
        // replace the last time with the current time.
        lastTime = currentTime;
        // dead particle count, kill the system if all dead
        int deadCount = 0;

        // move the particles
        for (int i = 0; i < PARTICLECOUNT; i++) {
            // first apply a gravity to the z speed, in this case

//            // apply air resistance
//            mParticles[i].dx = mParticles[i].dx - (mParticles[i].dx * AIR_RESISTANCE * timeFrame);
//            mParticles[i].dy = mParticles[i].dy - (mParticles[i].dy * AIR_RESISTANCE * timeFrame);
//            mParticles[i].dz = mParticles[i].dz - (mParticles[i].dz * AIR_RESISTANCE * timeFrame);
//
//            // second move the particle according to it's speed
//            mParticles[i].x = mParticles[i].x + (mParticles[i].dx * timeFrame);
//            mParticles[i].y = mParticles[i].y + (mParticles[i].dy * timeFrame);
//            mParticles[i].z = mParticles[i].z + (mParticles[i].dz * timeFrame);

            // add a gravity effect
//            mParticles[i].z = mParticles[i].z - (GRAVITY * timeFrame);

            // fourth decrement the time to live for the particle,
            // if it gets below zero, add to the dead count
            mParticles[i].timeToLive = mParticles[i].timeToLive - timeFrame;

            float firstStage = mParticles[i].maxToLive - mParticles[i].maxToLive / 6;
            float midStage = firstStage - mParticles[i].maxToLive / 3;

            mParticles[i].scale = mParticles[i].scale + 0.2f;
            mParticles[i].alpha = mParticles[i].alpha - 0.02f;

            if (mParticles[i].timeToLive < mParticles[i].maxToLive
                    && mParticles[i].timeToLive > firstStage) {
                // yellow beging stage  of explosion
                mParticles[i].red = 1f;
                mParticles[i].green = 1f;
                mParticles[i].blue = 1f;
//                mParticles[i].alpha = 1f;

            } else if (mParticles[i].timeToLive < firstStage
                    && mParticles[i].timeToLive > midStage) {
                // red midstage of explosion
                mParticles[i].red = 1f;
                mParticles[i].green = 1f;
                mParticles[i].blue = 0f;
//                mParticles[i].alpha = 1f;

            } else {
                // gray ends stage  of explosion
                mParticles[i].red = 1f;
                mParticles[i].green = 1f;
                mParticles[i].blue = 0f;
                mParticles[i].alpha = 0.1f;

            }

            if (mParticles[i].timeToLive < 0f) {
                deadCount++;
            } else {
//                Log.w("ParticleSystem", "mParticles[i].scale " + mParticles[i].alpha);
            }
        }
        if (deadCount == PARTICLECOUNT) {
//    		systemAlive = false;
//            Log.w("ParticleSystem", "deadCount " + deadCount);
//            this.getParentGroup().remove(this);
        }
    }
//    public int getTextureId() {
//        return textureId;
//    }
//
//    public void setTextureId(int textureId) {
//        this.textureId = textureId;
//    }
}
