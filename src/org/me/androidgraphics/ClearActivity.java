/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.androidgraphics;

/**
 *
 * @author lighthammer
 */
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import org.me.events.OrientationListener;
import org.me.events.OrientationManager;
import org.me.mesh.Cube;
import org.me.mesh.Group;
import org.me.mesh.Mesh;
import org.me.mesh.Plane;
import org.me.particlesystem.ParticleSystem5;

public class ClearActivity extends Activity implements OrientationListener {

    private GLSurfaceView mGLView;
    private static Context CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        mGLView = new ClearGLSurfaceView(this);
//        setContentView(mGLView);
        CONTEXT = this;


    }

//    @Override
//    public boolean onTouchEvent(final MotionEvent event) {
//
//        Toast.makeText(this.getContext(), "TAP!", Toast.LENGTH_SHORT).show();
//
//        try {
//            Thread.sleep(20);
//        } catch (InterruptedException e) {
//        }
//        return true;
//    }
    @Override
    protected void onPause() {
        super.onPause();

        if (mGLView != null) {
            mGLView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (OrientationManager.isSupported()) {
            OrientationManager.startListening(this);
        }

        if (mGLView != null) {
            mGLView.onResume();
        }

    }

    protected void onDestroy() {
        super.onDestroy();

        if (OrientationManager.isListening()) {
            OrientationManager.stopListening();
        }
    }

    public static Context getContext() {
        return CONTEXT;
    }

    public void onOrientationChanged(float azimuth,
            float pitch, float roll) {
    }

    public void onBottomUp() {
        Toast.makeText(this, "Bottom UP", 1000).show();
    }

    public void onLeftUp() {
        Toast.makeText(this, "Left UP", 1000).show();
    }

    public void onRightUp() {
        Toast.makeText(this, "Right UP", 1000).show();
    }

    public void onTopUp() {
        Toast.makeText(this, "Top UP", 1000).show();
    }
}

class ClearGLSurfaceView extends GLSurfaceView {

    ClearRenderer mRenderer;

    public ClearGLSurfaceView(Context context) {
        super(context);
        mRenderer = new ClearRenderer(context);
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        queueEvent(new Runnable() {

            public void run() {

                int screenX = getWidth();
                int screenY = getHeight();
                float x = (event.getX() - (screenX / 2)) / (screenX / 2);
                float y = (event.getY() - (screenY / 2)) / (screenY / -2);
                float xx = ((float) ((int) (100 * x))) / 100;
                float yy = ((float) ((int) (100 * y))) / 100;
                mRenderer.setX(xx);
                mRenderer.setY(yy);
                mRenderer.setW(screenX);
                mRenderer.setH(screenY);



            }
        });


        Toast.makeText(this.getContext(), " x= " + mRenderer.getX() + " y= " + mRenderer.getY(), Toast.LENGTH_SHORT).show();

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}

class ClearRenderer implements GLSurfaceView.Renderer {

    private ParticleSystem5 mParticleSystem;
    private Mesh root;
    int box, snow;
    Context context;
    float mAngleX = 0;
    float x = 0;
    float y = 0;
    float w = 0;
    float h = 0;
    float cX = 0;
    float cY = 0;
    Cube cube;
    Plane plane;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public ClearRenderer(Context context) {
        mParticleSystem = new ParticleSystem5();
        // Initialize our cube.
        Group group = new Group();
        cube = new Cube(1, 1, 1);
        cube.rx = 45;
        cube.ry = 45;

        plane = new Plane();
        group.add(cube);
        group.add(plane);

        root = group;
        this.context = context;
    }

    public ClearRenderer() {
        mParticleSystem = new ParticleSystem5();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Load TEXTURE
        box = loadTexture(gl, context, R.drawable.red);
        cube.setTextureId(box);

        snow = loadTexture(gl, context, R.drawable.flare1);
        plane.setTextureId(snow);

        // Enable Lights
        createLight(gl);

        // Set Background color
        gl.glClearColor(0, 0, 1f, 1.0f);
        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);
        // Depth buffer setup.
        gl.glClearDepthf(1.0f);
        // Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // Really nice perspective calculations.
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Sets the current view port to the new size.
        gl.glViewport(0, 0, width, height);
        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // Reset the projection matrix
        gl.glLoadIdentity();
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
//        GLU.gluLookAt(gl, 0f, -10f, 10f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // Reset the modelview matrix
        gl.glLoadIdentity();
    }

    public void onDrawFrame(GL10 gl) {
//        if (mAngleX > 360) {
//            mAngleX = 0;
//        }

//        // TEXTURE
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, 1);
//        gl.glEnable(GL10.GL_TEXTURE_2D);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, snow);
//        ((GL11Ext) gl).glDrawTexfOES(10, 10, 0, 64, 64);

//        gl.glBindTexture(GL10.GL_TEXTURE_2D, snow);
//        ((GL11Ext) gl).glDrawTexfOES(10, 10, 0, 64, 64);
//        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
//        gl.glEnable(gl.GL_BLEND);

        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // Replace the current matrix with the identity matrix
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -1);

        // Translates CUBE TO TOUCH POINT.
        if (cube.x < x) {
            cube.x = cube.x + 0.01f;
        } else if (cube.x > x) {
            cube.x = cube.x - 0.01f;
        }

        if (cube.y < y) {
            cube.y = cube.y + 0.01f;
        } else if (cube.y > y) {
            cube.y = cube.y - 0.01f;
        }

//        gl.glTranslatef(cX, cY, -10);
//        gl.glScalef(0.1f, 0.1f, 0.1f);

//        gl.glPushMatrix();
//        gl.glScalef(1f, 1f, 1f);

//        gl.glRotatef(mAngleX++, 0, 1, 0);
        // Draw our scene.
        root.draw(gl);
//        gl.glPopMatrix();
//        mParticleSystem.update();
//        mParticleSystem.draw(gl);
    }

    private void createLight(GL10 gl) {
        float lightAmbient[] = {0.2f, 0.0f, 0.0f, 1.0f};
        float lightDiffuse[] = {0.5f, 0.0f, 0.0f, 1.0f};
        float lightSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};

        float matAmbient[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float matDiffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float matSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};

        float light0Shininess = 1f;

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, matSpecular, 0);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, light0Shininess);

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightSpecular, 0);

        // Define the position of the first light
        float light0Position[] = {0.0f, 10.0f, 5.0f, 0.0f};
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Position, 0);

        // Define a direction vector for the light, this one points right down the Z axis
        float light0Direction[] = {0.0f, 0.0f, -1.0f};
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, light0Direction, 0);
    }
    // Get a new texture id:

    private static int newTextureID(GL10 gl) {
        int[] temp = new int[1];
        gl.glGenTextures(1, temp, 0);
        return temp[0];
    }

    // Will load a texture out of a drawable resource file, and return an OpenGL texture ID:
    private int loadTexture(GL10 gl, Context context, int resource) {
        // In which ID will we be storing this texture?
        int id = newTextureID(gl);
        // We need to flip the textures vertically:
        Matrix flip = new Matrix();
        flip.postScale(1f, -1f);
        // This will tell the BitmapFactory to not scale based on the device's pixel density:
        // (Thanks to Matthew Marshall for this bit)
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        // Load up, and flip the texture:
        Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resource, opts);
        Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
        temp.recycle();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
        // Set all of our texture parameters:
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        int[] crop = {0, bmp.getWidth(), bmp.getHeight(), -bmp.getHeight()};

        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, crop, 0);


        // Generate, and load up all of the mipmaps:
        for (int level = 0, height = bmp.getHeight(), width = bmp.getWidth(); true; level++) {
            // Push the bitmap onto the GPU:
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bmp, 0);
            // We need to stop when the texture is 1x1:
            if (height == 1 && width == 1) {
                break;
            }
            // Resize, and let's go again:
            width >>= 1;
            height >>= 1;
            if (width < 1) {
                width = 1;
            }
            if (height < 1) {
                height = 1;
            }
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width, height, true);
            bmp.recycle();
            bmp = bmp2;
        }
        bmp.recycle();
        return id;
    }
}


