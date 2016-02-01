package org.me.androidgraphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import org.me.particlesystem.ExplosionParticleSystem;

public class ParticleSystemDemo extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGLView = new ClearGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Visit bayninestudios.com");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        android.net.Uri.parse("http://www.bayninestudios.com"));
                startActivity(myIntent);
                return true;
        }
        return false;
    }
    private GLSurfaceView mGLView;

    class ClearGLSurfaceView extends GLSurfaceView {

        public ClearGLSurfaceView(Context context) {
            super(context);
            mRenderer = new ClearRenderer(context);
            setRenderer(mRenderer);
        }
        ClearRenderer mRenderer;

        @Override
        public boolean onTouchEvent(final MotionEvent event) {
//
            queueEvent(new Runnable() {

                public void run() {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Log.w("ParticleSystem", "onTouchEvent ");

                        mRenderer.mParticleSystem = new ExplosionParticleSystem();
                        mRenderer.mParticleSystem.x = 300;
                        mRenderer.mParticleSystem.y = 300;
                        mRenderer.mParticleSystem.setTextureId(mRenderer.snow);

                    }
                }
            });

            return true;
        }
    }

    class ClearRenderer implements GLSurfaceView.Renderer {

        public ExplosionParticleSystem mParticleSystem;
        public int box, snow;
        Context context;
        private int fps = 0;
        private long time = System.currentTimeMillis();

        public ClearRenderer() {
            mParticleSystem = new ExplosionParticleSystem();
        }

        public ClearRenderer(Context context) {
            mParticleSystem = new ExplosionParticleSystem();
            this.context = context;

        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            snow = loadTexture(gl, context, R.drawable.halo);
            mParticleSystem.setTextureId(snow);
            mParticleSystem.x = 300;
            mParticleSystem.y = 300;
            gl.glClearColor(0, 0, 1, 1.0f);

            // Enable Smooth Shading, default not really needed.
            gl.glShadeModel(GL10.GL_SMOOTH);
            // Depth buffer setup.
            gl.glClearDepthf(1.0f);
            // Enables depth testing.
//            gl.glEnable(GL10.GL_DEPTH_TEST);
            // The type of depth testing to do.
            gl.glDepthFunc(GL10.GL_LEQUAL);
            // Really nice perspective calculations.
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            gl.glEnable(GL10.GL_TEXTURE_2D);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnable(GL10.GL_NORMAL_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            gl.glEnable(gl.GL_BLEND);
//            GLU.gluPerspective(gl, 15.0f, 80.0f / 48.0f, 1, 100);
//            GLU.gluLookAt(gl, 0f, -17f, 5f, 0.0f, 0.0f, 1f, 0.0f, 1.0f, 0.0f);
//            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
//            gl.glViewport(0, 0, w, h);
            // Sets the current view port to the new size.
//            Log.w("BasicRender", "width " + width + " height " + height);
            gl.glViewport(0, 0, width, height);

            // Select the projection matrix
            gl.glMatrixMode(GL10.GL_PROJECTION);
            // Reset the projection matrix
            gl.glLoadIdentity();

            float aspectRatio = (float) width / height;

            // Calculate the aspect ratio of the window
            GLU.gluPerspective(gl, 45.0f, aspectRatio, 1f, 5000.0f);

//        gl.glFrustumf(-aspectRatio, aspectRatio, -1, 1, 1, 10);


            // TEMP SETINGS FOR A 700 X 700 GRID
            GLU.gluLookAt(gl, 300, 300, 500, // eye
                    300, 300, 0, // center
                    0.0f, 1.0f, 0.0f); // up


            // PROD SETTINGS
//        GLU.gluLookAt(gl, width / 2, height / 2, 4, // eye
//                width / 2, height / 2, 0, // center
//                0.0f, 1.0f, 0.0f); // up

            // These parameters will actually
            // Select the modelview matrix
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            // Reset the modelview matrix
            gl.glLoadIdentity();
        }

        public void onDrawFrame(GL10 gl) {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
//            mParticleSystem = new ExplosionParticleSystem();
//            mParticleSystem.x = 300;
//            mParticleSystem.y = 300;
//            mParticleSystem.setTextureId(snow);
            mParticleSystem.update();
            mParticleSystem.draw(gl);

            if (System.currentTimeMillis() - time >= 1000) {
                Log.w("BasicRender ", fps + " fps");
                fps = 0;
                time = System.currentTimeMillis();
            }
            fps++;
        }

        private int newTextureID(GL10 gl) {
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
}
