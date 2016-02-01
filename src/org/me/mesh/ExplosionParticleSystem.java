package org.me.mesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.me.androidgraphics.OpenGLUtility;
import org.me.particlesystem.Particle;


/**
 *
 * @author lighthammer
 */
public class ExplosionParticleSystem extends Mesh {

    public Particle[] mParticles;
    private int PARTICLECOUNT = 1;
    // for use to draw the particle
//    private FloatBuffer mVertexBuffer;
//    private ShortBuffer mIndexBuffer;
//    private FloatBuffer mTextureBuffer;
    private Random gen;
    private long lastTime;

    public ExplosionParticleSystem() {
        super(false);
        mParticles = new Particle[PARTICLECOUNT];
        // setup the random number generator
        gen = new Random(System.currentTimeMillis());
        // loop through all the particles and create new instances of each one        gen = new Random(System.currentTimeMillis());

        for (int i = 0; i < PARTICLECOUNT; i++) {
//            mParticles[i] = new Particle(i, i, 0);
            mParticles[i] = new Particle(gen.nextFloat() * 10f, gen.nextFloat() * 10f, 0);
            initParticle(i);
        }

        short[] icoords = {0, 1, 2, 0, 2, 3};
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

        verticesBuffer = makeFloatBuffer(coords);
        textBuffer = makeFloatBuffer(tex);
        normalBuffer = makeFloatBuffer(normals);
        indicesBuffer = makeShortBuffer(icoords);
        numOfIndices = icoords.length;

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
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 2);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    @Override
    public void draw(GL10 gl) {

        update();
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
//        gl.glEnable(GL10.GL_TEXTURE_2D);


//        gl.glBlendFunc(gl.GL_ONE, gl.GL_ONE); // invisible
//        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);
//        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

        gl.glLoadIdentity();
        gl.glPushMatrix();
        
        gl.glTranslatef(x, y, z);

        for (int i = 0; i < PARTICLECOUNT; i++) {
            if (mParticles[i].timeToLive > 0f) {

                gl.glColor4f(mParticles[i].red, mParticles[i].green, mParticles[i].blue, mParticles[i].alpha);

                gl.glScalef(mParticles[i].scale, mParticles[i].scale, mParticles[i].scale);

//                gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);

                gl.glRotatef(rx, 1, 0, 0);
                gl.glRotatef(ry, 0, 1, 0);
                gl.glRotatef(rz, 0, 0, 1);

//                gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
        		OpenGLUtility.drawTestVBO2(drawVO,textureId);

            }
        }
        gl.glPopMatrix();

//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
//        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

    }

    
    public void drawO(GL10 gl) {

        update();
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);


//        gl.glBlendFunc(gl.GL_ONE, gl.GL_ONE); // invisible
//        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

        gl.glLoadIdentity();
        gl.glPushMatrix();
        
        gl.glTranslatef(x, y, z);

        for (int i = 0; i < PARTICLECOUNT; i++) {
            if (mParticles[i].timeToLive > 0f) {

                gl.glColor4f(mParticles[i].red, mParticles[i].green, mParticles[i].blue, mParticles[i].alpha);

                gl.glScalef(mParticles[i].scale, mParticles[i].scale, mParticles[i].scale);

//                gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);

                gl.glRotatef(rx, 1, 0, 0);
                gl.glRotatef(ry, 0, 1, 0);
                gl.glRotatef(rz, 0, 0, 1);

                gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT, indicesBuffer);

            }
        }
        gl.glPopMatrix();

//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

    }

    private void initParticle(int i) {

        float DOUBLESPEED = 500;
        mParticles[i].dx = (gen.nextFloat() * DOUBLESPEED) - (DOUBLESPEED / 2f);
        mParticles[i].dy = (gen.nextFloat() * DOUBLESPEED) - (DOUBLESPEED / 2f);
        mParticles[i].dz = (gen.nextFloat() * DOUBLESPEED) - (DOUBLESPEED / 2f);

        mParticles[i].alpha = 1f;
        mParticles[i].maxToLive = 1f;
        mParticles[i].timeToLive = 1f;
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
            // fourth decrement the time to live for the particle,
            // if it gets below zero, add to the dead count
            mParticles[i].timeToLive = mParticles[i].timeToLive - timeFrame;

//            float firstStage = mParticles[i].maxToLive - mParticles[i].maxToLive / 6;
//            float midStage = firstStage - mParticles[i].maxToLive / 3;

            mParticles[i].scale = mParticles[i].scale + 0.1f;
            // fade
            //mParticles[i].alpha = mParticles[i].alpha - 0.07f;

//            if (mParticles[i].timeToLive < mParticles[i].maxToLive
//                    && mParticles[i].timeToLive > firstStage) {
//                // yellow beging stage  of explosion
                mParticles[i].red = 1f;
                mParticles[i].green = 1f;
                mParticles[i].blue = 1f;
//
//            } else if (mParticles[i].timeToLive < firstStage
//                    && mParticles[i].timeToLive > midStage) {
//                // red midstage of explosion
//                mParticles[i].red = 1f;
//                mParticles[i].green = 0f;
//                mParticles[i].blue = 0f;
//
//            } else {
//                // gray ends stage  of explosion
//                mParticles[i].red = 1f;
//                mParticles[i].green = 0f;
//                mParticles[i].blue = 0f;
//
//            }

                if(mParticles[i].scale>10)
                {
                	mParticles[i].scale = 0;
                }
            if (mParticles[i].timeToLive < 0f) {
                deadCount++;
            }
        }
        if (deadCount == PARTICLECOUNT) {
//            this.getParentGroup().removeMesh(this);
        }
    }
}
