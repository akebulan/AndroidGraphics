package org.me.androidgraphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import min3d.core.FacesBufferedList;
import min3d.core.Object3d;
import min3d.core.Object3dContainer;
import min3d.parser.IParser;
import min3d.parser.Parser;
import org.me.events.OrientationListener;
import org.me.events.OrientationManager;
import org.me.mesh.Background;
import org.me.mesh.Cube;
import org.me.mesh.Group;
import org.me.mesh.Mesh;
import org.me.mesh.Model;
import org.me.mesh.Plane;
import org.me.particlesystem.ParticleSystem;
import org.me.particlesystem.ParticleSystem5;

/**
 * Android orientation sensor tutorial
 * @author antoine vianey
 * under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 */
public class Orientation extends Activity implements OrientationListener {

    private static Context CONTEXT;
    private ClearGLSurfaceView mGLView;
    protected Handler _initSceneHander;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _initSceneHander = new Handler();

        CONTEXT = this;
        mGLView = new ClearGLSurfaceView(this);
        setContentView(mGLView);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (OrientationManager.isListening()) {
            OrientationManager.stopListening();
        }

    }

    public static Context getContext() {
        return CONTEXT;
    }

    public void onOrientationChanged(float azimuth, float pitch, float roll) {
//        Toast.makeText(this, "onOrientationChanged azimuth "+azimuth+" "+" pitch "+pitch+" roll "+roll, Toast.LENGTH_SHORT).show();
    }

    public void onTopUp() {
//        Toast.makeText(this, "Top UP", Toast.LENGTH_SHORT).show();
//        float x = mGLView.getX();
//        mGLView.setX(x = x + 0.01f);
        float y = mGLView.getY();
        mGLView.setY(y = y - 0.01f);
    }

    public void onBottomUp() {
//        Toast.makeText(this, "Bottom UP", Toast.LENGTH_SHORT).show();
//        float x = mGLView.getX();
//        mGLView.setX(x = x - 0.01f);

        float y = mGLView.getY();
        mGLView.setY(y = y + 0.01f);
    }

    public void onLeftUp() {
//        Toast.makeText(this, "Left UP", Toast.LENGTH_SHORT).show();
//        float y = mGLView.getY();
//        mGLView.setY(y = y + 0.01f);
        float x = mGLView.getX();
        mGLView.setX(x = x + 0.01f);

    }

    public void onRightUp() {
//        Toast.makeText(this, "Right UP", Toast.LENGTH_SHORT).show();
//        float y = mGLView.getY();
//        mGLView.setY(y = y - 0.01f);
        float x = mGLView.getX();
        mGLView.setX(x = x - 0.01f);

    }

    class ClearGLSurfaceView extends GLSurfaceView {

        ClearRenderer mRenderer;
        float x, y;

        public float getX() {
            return mRenderer.model.x;
        }

        public void setX(float x) {
            this.x = x;
//            mRenderer.setX(x);
            mRenderer.model.x = x;
        }

        public float getY() {
            return mRenderer.model.y;
        }

        public void setY(float y) {
            this.y = y;
//            mRenderer.setY(y);
            mRenderer.model.y = y;


        }

        public ClearGLSurfaceView(Context context) {
            super(context);
            mRenderer = new ClearRenderer(context);
            setRenderer(mRenderer);
        }

        @Override
        public boolean onTouchEvent(final MotionEvent event) {
            mRenderer.setSize(this.getWidth(), this.getHeight());

            queueEvent(new Runnable() {

                public void run() {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (mRenderer.ammo > 0) {
                            mRenderer.bullet = new Plane();
                            mRenderer.bullet.setTextureId(mRenderer.snow);
                            mRenderer.group.add(mRenderer.bullet);
                            mRenderer.bullet.x = mRenderer.model.x + 0.06f;
                            mRenderer.bullet.y = mRenderer.model.y;
                            mRenderer.bullet.iterate = true;

                            int screenX = getWidth();
                            int screenY = getHeight();
                            mRenderer.setW(screenX);
                            mRenderer.setH(screenY);
                            mRenderer.ammo = mRenderer.ammo - 1;
                        }
//                    mRenderer.touchEvent(event.getX(), event.getY(), event.getAction());

                    }
                }
            });

//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            return true;
        }
    }

    class ClearRenderer implements GLSurfaceView.Renderer {

        private ParticleSystem[] mParticleSystems;
        private int systemCount = 0;
        private Mesh root;
        int box, snow, snow2, terrain, plane, cloud, hud1, text0, text1, text2, text3, icon1, icon2, gameover, clear;
        int ships = 3, enemy, ammo = 3;
        Context context;
        float mAngleX = 0;
        float x = 0;
        float y = 0;
        float w = 0;
        float h = 0;
        float cX = 0;
        float cY = 0;
        public Cube cube;
        public Model model, model2, model3, model4;
        public Plane bullet, point1, point2, point3, point4, point5, point6, point7, point8, point9, hud_icon_1, hud_icon_2, hud_1, hud_2, hud_text_1, hud_text_2, hud_text_3;
        public Background ground1, ground2, ground3, clouds1, clouds2, clouds3;
        public Group group;
        public boolean iterate;
        private int screenX, screenY;
        private boolean inCollosion, flip, timesUp;
        private long lastTime;
        float timeFrame = System.currentTimeMillis();

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
            // Initialize our cube.
            group = new Group();

            IParser parser = Parser.createParser(Parser.Type.OBJ,
                    getResources(), "org.me.androidgraphics:raw/plane2", true);

//                        IParser parser = Parser.createParser(Parser.Type.OBJ,
//                    getResources(), "org.me.androidgraphics:raw/missle1", true);
            parser.parse();

            Object3dContainer objModelContainer = parser.getParsedObject();
            Object3d object3d = objModelContainer.getChildAt(0);
            //indices
            ShortBuffer indicesBuffer = object3d.faces().buffer();
            indicesBuffer.position(0);

            // vertices
            FloatBuffer verticesBuffer = object3d.vertices().points().buffer();
            verticesBuffer.position(0);

            // tex
            FloatBuffer textBuffer = object3d.vertices().uvs().buffer();
            textBuffer.position(0);

            // normals
            FloatBuffer normalBuffer = object3d.vertices().normals().buffer();
            normalBuffer.position(0);

            int len = object3d.faces().size() * FacesBufferedList.PROPERTIES_PER_ELEMENT;

            model = new Model(indicesBuffer, verticesBuffer, textBuffer, normalBuffer, len);
            model.rx = 45;
            model.ry = 45;
            model.y = -0.3f;
            model.x = 0f;
            group.add(model);

            parser = Parser.createParser(Parser.Type.OBJ,
                    getResources(), "org.me.androidgraphics:raw/bomber2", true);
            parser.parse();

            objModelContainer = parser.getParsedObject();
            object3d = objModelContainer.getChildAt(0);
            //indices
            indicesBuffer = object3d.faces().buffer();
            indicesBuffer.position(0);

            // vertices
            verticesBuffer = object3d.vertices().points().buffer();
            verticesBuffer.position(0);

            // tex
            textBuffer = object3d.vertices().uvs().buffer();
            textBuffer.position(0);

            // normals
            normalBuffer = object3d.vertices().normals().buffer();
            normalBuffer.position(0);

            len = object3d.faces().size() * FacesBufferedList.PROPERTIES_PER_ELEMENT;

            model3 = new Model(indicesBuffer, verticesBuffer, textBuffer, normalBuffer, len);
            model3.rx = 45;
            model3.ry = 45;
            model3.y = 0.3f;
            group.add(model3);


            parser = Parser.createParser(Parser.Type.OBJ,
                    getResources(), "org.me.androidgraphics:raw/fighter", true);
            parser.parse();

            objModelContainer = parser.getParsedObject();
            object3d = objModelContainer.getChildAt(0);
            //indices
            indicesBuffer = object3d.faces().buffer();
            indicesBuffer.position(0);

            // vertices
            verticesBuffer = object3d.vertices().points().buffer();
            verticesBuffer.position(0);

            // tex
            textBuffer = object3d.vertices().uvs().buffer();
            textBuffer.position(0);

            // normals
            normalBuffer = object3d.vertices().normals().buffer();
            normalBuffer.position(0);

            len = object3d.faces().size() * FacesBufferedList.PROPERTIES_PER_ELEMENT;

            model4 = new Model(indicesBuffer, verticesBuffer, textBuffer, normalBuffer, len);
            model4.rx = 45;
            model4.ry = 45;
            model4.y = 0f;
            model4.x = 0f;
            group.add(model4);


            parser = Parser.createParser(Parser.Type.OBJ,
                    getResources(), "org.me.androidgraphics:raw/missle1", true);
            parser.parse();

            objModelContainer = parser.getParsedObject();
            object3d = objModelContainer.getChildAt(0);
            //indices
            indicesBuffer = object3d.faces().buffer();
            indicesBuffer.position(0);

            // vertices
            verticesBuffer = object3d.vertices().points().buffer();
            verticesBuffer.position(0);

            // tex
            textBuffer = object3d.vertices().uvs().buffer();
            textBuffer.position(0);

            // normals
            normalBuffer = object3d.vertices().normals().buffer();
            normalBuffer.position(0);

            len = object3d.faces().size() * FacesBufferedList.PROPERTIES_PER_ELEMENT;

            model2 = new Model(indicesBuffer, verticesBuffer, textBuffer, normalBuffer, len);
            model2.rx = 45;
            model2.ry = 45;
            model2.y = 0.3f;
            model2.x = -0.2f;
            group.add(model2);

            cube = new Cube(1, 1, 1);
            cube.rx = 45;
            cube.ry = 45;
            cube.y = 0.0f;
            group.add(cube);

            bullet = new Plane();
            group.add(bullet);

            ground1 = new Background();
            ground1.z = -2;
            ground1.x = 0.6f;
            ground1.y = -1f;
            group.add(ground1);

            ground2 = new Background();
            ground2.z = -2;
            ground2.x = 0.7f;
            ground2.y = 0f;
            group.add(ground2);

            ground3 = new Background();
            ground3.z = -2;
            ground3.x = 0.8f;
            ground3.y = 1f;
            group.add(ground3);


            clouds1 = new Background();
            clouds1.z = -1.99f;
            clouds1.x = 0.8f;
            clouds1.y = 1f;
            group.add(clouds1);

            clouds2 = new Background();
            clouds2.z = -1.99f;
            clouds2.x = 0.7f;
            clouds2.y = 2f;
            group.add(clouds2);

            clouds3 = new Background();
            clouds3.z = -1.99f;
            clouds3.x = 0.6f;
            clouds3.y = 3f;
            group.add(clouds3);

            // AIRCRAFT center
            point1 = new Plane();
            point1.y = model.y;
            point1.x = model.x;
            group.add(point1);
//
//            point2 = new Plane();
//            point2.y = model.y;
//            point2.x = model.x + 0.13f;
//            group.add(point2);
//
//            point3 = new Plane();
//            point3.y = model.y - 0.13f;
//            point3.x = model.x;
//            group.add(point3);
//
//            point4 = new Plane();
//            point4.y = model.y - 0.13f;
//            point4.x = model.x + 0.13f;
//            group.add(point4);

            // MISSLE TIP
            point5 = new Plane();
            point5.x = model2.x + 0.06f;
            point5.y = model2.y - 0.13f;
            group.add(point5);

            // CUBE CORNERS
//            point6 = new Plane();
//            point6.y = cube.y;
//            point6.x = cube.x;
//            group.add(point6);
//
//            point7 = new Plane();
//            point7.y = cube.y;
//            point7.x = cube.x + 0.13f;
//            group.add(point7);
//
//            point8 = new Plane();
//            point8.y = cube.y - 0.13f;
//            point8.x = cube.x;
//            group.add(point8);
//
//            point9 = new Plane();
//            point9.y = cube.y - 0.13f;
//            point9.x = cube.x + 0.13f;
//            group.add(point9);

            root = group;
            this.context = context;

            hud_1 = new Plane();
            hud_1.y = 0.3f;
            hud_1.x = -0.15f;

            hud_icon_1 = new Plane();
            hud_icon_1.y = 0.3f;
            hud_icon_1.x = -0.15f;

            hud_text_1 = new Plane();
            hud_text_1.y = 0.3f;
            hud_text_1.x = -0.15f;

            hud_2 = new Plane();
            hud_2.y = 0.3f;
            hud_2.x = 0.28f;

            hud_icon_2 = new Plane();
            hud_icon_2.y = 0.3f;
            hud_icon_2.x = 0.28f;

            hud_text_2 = new Plane();
            hud_text_2.y = 0.3f;
            hud_text_2.x = 0.28f;

            hud_text_3 = new Plane();
            hud_text_3.y = 0f;
            hud_text_3.x = 0.05f;

            mParticleSystems = new ParticleSystem[40];

        }

        public ClearRenderer() {
//            mParticleSystems = new ParticleSystem[40];
        }

        public void setSize(int width, int height) {
            this.screenX = width;
            this.screenY = height;
        }
        // init a new particle system on touch event

//        public void touchEvent(float x, float y, int eventCode) {
//            if (eventCode == MotionEvent.ACTION_UP) {
//                // only allow 40 explosions.
//                if (systemCount < 40) {
//                    mParticleSystems[systemCount] = new ParticleSystem();
//                    mParticleSystems[systemCount].x = model.x;
//                    mParticleSystems[systemCount].y = model.y;
//                    mParticleSystems[systemCount].z = model.z;
//
//                    mParticleSystems[systemCount].setTextureId(snow);
//                    systemCount++;
//                }
//            }
//        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // Load TEXTURE

            plane = loadTexture(gl, context, R.drawable.plane);
            model.setTextureId(plane);

            box = loadTexture(gl, context, R.drawable.red);
            cube.setTextureId(box);
            model2.setTextureId(box);
            model3.setTextureId(box);
            model4.setTextureId(box);

            snow = loadTexture(gl, context, R.drawable.flare1);
            bullet.setTextureId(snow);
            point1.setTextureId(snow);
//            point2.setTextureId(snow);
//            point3.setTextureId(snow);
//            point4.setTextureId(snow);

            point5.setTextureId(snow);

//            point6.setTextureId(snow);
//            point7.setTextureId(snow);
//            point8.setTextureId(snow);
//            point9.setTextureId(snow);

            snow2 = loadTexture(gl, context, R.drawable.flare2);


            terrain = loadTexture(gl, context, R.drawable.ground);
            cloud = loadTexture(gl, context, R.drawable.cloud);

            ground1.setTextureId(cloud);
            ground2.setTextureId(cloud);
            ground3.setTextureId(cloud);

            clouds1.setTextureId(cloud);
            clouds2.setTextureId(cloud);
            clouds3.setTextureId(cloud);

            hud1 = loadTexture(gl, context, R.drawable.container);
            hud_1.setTextureId(hud1);
            hud_2.setTextureId(hud1);

            icon1 = loadTexture(gl, context, R.drawable.f16);
            hud_icon_1.setTextureId(icon1);

            icon2 = loadTexture(gl, context, R.drawable.ammo);
            hud_icon_2.setTextureId(icon2);

            text0 = loadTexture(gl, context, R.drawable.text0);
            text1 = loadTexture(gl, context, R.drawable.text1);
            text2 = loadTexture(gl, context, R.drawable.text2);
            text3 = loadTexture(gl, context, R.drawable.text3);
            hud_text_1.setTextureId(text3);
            hud_text_2.setTextureId(text3);

            gameover = loadTexture(gl, context, R.drawable.gameover);
            clear = loadTexture(gl, context, R.drawable.gameoverblank);

            hud_text_3.setTextureId(clear);

            // Enable Lights
            createLight(gl);

//            lastTime = System.currentTimeMillis();

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
            long currentTime = System.currentTimeMillis();
//            float timeFrame = (currentTime - lastTime) / 1000f;
            // replace the last time with the current time.
//            lastTime = currentTime;


            ground1.y = ground1.y - 0.01f;
            ground2.y = ground2.y - 0.01f;
            ground3.y = ground3.y - 0.01f;

            clouds1.y = clouds1.y - 0.03f;
            clouds2.y = clouds2.y - 0.03f;
            clouds3.y = clouds3.y - 0.03f;

            timeFrame = System.nanoTime() + 30000f;

            // resupply cube
            if (System.nanoTime() >= timeFrame) {
                timesUp = true;
            } else {
                timesUp = false;
            }

            if (cube.y > 0.1f) {
                timesUp = false;
                cube.y = -0.5f;
                timeFrame = System.nanoTime() + 30000f;

            }


            if (timesUp) {
                cube.y = cube.y + 0.01f;
            }



            // LANDSCAPE
            if (ground1.y < -2) {
                ground1.y = 1;
            }
            if (ground2.y < -2) {
                ground2.y = 1;
            }
            if (ground3.y < -2) {
                ground3.y = 1;
            }

            // CLOUDS
            if (clouds1.y < -2) {
                clouds1.y = 1;
            }
            if (clouds2.y < -2) {
                clouds2.y = 1;
            }
            if (clouds3.y < -2) {
                clouds3.y = 1;
            }


            // enemy bomber

            if (!flip) {
                model3.x = model3.x + 0.01f;
            } else {
                model3.x = model3.x - 0.01f;
            }


            if (model3.x > 0.4f) {
                flip = true;
            }
            if (model3.x < -0.4f) {
                flip = false;
            }

            // enemy fighter
            model4.x = model4.x + 0.01f;
            model4.y = model4.y - 0.01f;
            if (model4.y < -1) {
                model4.x = -0.2f;
                model4.y = 0.25f;
            }

            // AIRCRAFT
            if (model.y < -0.3f) {
                model.y = -0.25f;
            }

            if (model.y > 0.3f) {
                model.y = 0.25f;
            }

            if (model.x < -0.2f) {
                model.x = -0.15f;
            }

            if (model.x > 0.2f) {
                model.x = 0.15f;
            }

            // AIRCRAFT CORNERS
            point1.x = model.x + 0.065f;
            point1.y = model.y;
//
//            point2.x = model.x + 0.13f;
//            point2.y = model.y;
//
//            point3.x = model.x;
//            point3.y = model.y - 0.13f;
//
//            point4.x = model.x + 0.13f;
//            point4.y = model.y - 0.13f;

            // ENEMY CORNERS
//            point6.y = model3.y;
//            point6.x = model3.x;
//
//            point7.x = model3.x + 0.13f;
//            point7.y = model3.y;
//
//            point8.x = model3.x;
//            point8.y = model3.y - 0.13f;
//
//            point9.x = model3.x + 0.13f;
//            point9.y = model3.y - 0.13f;

            // MISSILE
            model2.y = model2.y - 0.015f;

            if (model2.y < -2) {
                model2.y = 1;
//                inCollosion = false;

                model2.x = model2.x + 0.02f;
                if (model2.x > 0.2f) {
                    model2.x = -0.2f;
                }
            }

            point5.x = model2.x + 0.06f;
            point5.y = model2.y - 0.13f;

            // MISSLE COILLISION
            if (point5.x > model.x && point5.x < model.x + 0.13f) {
                if (point5.y < model.y && point5.y > model.y - 0.13f) {
                    if (!inCollosion) {


                        mParticleSystems[systemCount] = new ParticleSystem();
                        mParticleSystems[systemCount].x = model.x;
                        mParticleSystems[systemCount].y = model.y;
                        mParticleSystems[systemCount].z = model.z;
                        mParticleSystems[systemCount].setTextureId(snow2);
                        systemCount++;
//                        inCollosion = true;
                        // remove aircraft
                        ships = ships - 1;
                        model.y = -0.3f;
                        model.x = 0f;
                        // reset ammo
                        ammo = 3;
                        // remove missle
                        model2.y = -3f;

                        if (ships < 0) {
                            //reset
                            ships = 3;
                            ammo = 3;
                            hud_text_3.setTextureId(clear);

                        }

                        if (ships == 0) {
                            hud_text_1.setTextureId(text0);
                            hud_text_3.setTextureId(gameover);
                        }

                        if (ships == 1) {
                            hud_text_1.setTextureId(text1);
                        }

                        if (ships == 2) {
                            hud_text_1.setTextureId(text2);
                        }

                        if (ships == 3) {
                            hud_text_1.setTextureId(text3);
                        }
                    }
                }
            }

            // BULLET COLLISION  WITH CUBE
            if (bullet.x > cube.x && bullet.x < cube.x + 0.13f) {
                if (bullet.y < cube.y && bullet.y > cube.y - 0.13f) {
                    if (systemCount < 40) {
                        mParticleSystems[systemCount] = new ParticleSystem();
                        mParticleSystems[systemCount].x = bullet.x;
                        mParticleSystems[systemCount].y = bullet.y;
                        mParticleSystems[systemCount].z = bullet.z;
                        mParticleSystems[systemCount].setTextureId(snow2);
                        systemCount++;
                        bullet.showing = false;
//                        inCollosion = true;
                    }
                    cube.y = 0.5f;
                    ammo = 3;
                }
            }

            // SHIP COLLISION  WITH CUBE
            if (point1.x > cube.x && point1.x < cube.x + 0.13f) {
                if (point1.y < cube.y && point1.y > cube.y - 0.13f) {
                    if (systemCount < 40) {
                        mParticleSystems[systemCount] = new ParticleSystem();
                        mParticleSystems[systemCount].x = cube.x;
                        mParticleSystems[systemCount].y = cube.y;
                        mParticleSystems[systemCount].z = cube.z;
                        mParticleSystems[systemCount].setTextureId(snow2);
                        systemCount++;
//                        bullet.showing = false;
//                        inCollosion = true;
                    }
                    cube.y = 0.5f;
                    ammo = 3;
                }
            }

            // BULLET COLITION WITH ENEMY
            if (bullet.x > model3.x && bullet.x < model3.x + 0.13f) {
                if (bullet.y < model3.y && bullet.y > model3.y - 0.13f) {
                    if (systemCount < 40) {
                        mParticleSystems[systemCount] = new ParticleSystem();
                        mParticleSystems[systemCount].x = bullet.x;
                        mParticleSystems[systemCount].y = bullet.y;
                        mParticleSystems[systemCount].z = bullet.z;
                        mParticleSystems[systemCount].setTextureId(snow2);
                        systemCount++;
                        bullet.showing = false;
                        model3.x = 0.4f;
//                        inCollosion = true;
                    }

                    ammo = 3;
                }
            }

            if (ammo == 0) {
                hud_text_2.setTextureId(text0);
            }

            if (ammo == 1) {
                hud_text_2.setTextureId(text1);
            }

            if (ammo == 2) {
                hud_text_2.setTextureId(text2);
            }

            if (ammo == 3) {
                hud_text_2.setTextureId(text3);
            }

//            if (ground.y < 0) {
//                ground.y = 1;
//            }

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


//            plane.x = x;
//            plane.y = y;
            // Translates CUBE TO TOUCH POINT.
//            if (plane.x < x) {
//                plane.x = plane.x + 0.01f;
//            } else if (plane.x > x) {
//                plane.x = plane.x - 0.01f;
//            }
//
//            if (plane.y < y) {
//            if (iterate) {
//                plane.y = plane.y + 0.01f;
//            }
//            } else if (cube.y > y) {
//                plane.y = plane.y - 0.01f;
//            }

//        gl.glTranslatef(cX, cY, -10);
//        gl.glScalef(0.1f, 0.1f, 0.1f);

//        gl.glPushMatrix();
//        gl.glScalef(1f, 1f, 1f);

//        gl.glRotatef(mAngleX++, 0, 1, 0);
            // Draw our scene.
            root.draw(gl);
            for (int i = 0; i < systemCount; i++) {
                mParticleSystems[i].update();
                mParticleSystems[i].draw(gl);
            }

//            switchToOrtho(gl);
            hud_icon_1.draw(gl);
            hud_icon_2.draw(gl);
            hud_1.draw(gl);
            hud_2.draw(gl);
            hud_text_1.draw(gl);
            hud_text_2.draw(gl);
            hud_text_3.draw(gl);

//            switchToFustrum(gl);
//        gl.glPopMatrix();
//        mParticleSystem.update();
//        mParticleSystem.draw(gl);
        }

        public void switchToOrtho(GL10 gl) {
            gl.glDisable(GL10.GL_DEPTH_TEST);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glOrthof(0, this.screenX, 0, this.screenY, 1, 10);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();

        }

        public void switchToFustrum(GL10 gl) {
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glPopMatrix();
            gl.glMatrixMode(GL10.GL_MODELVIEW);
        }

        private void createLight(GL10 gl) {
            float lightAmbient[] = {0.5f, 0.5f, 0.5f, 1.0f};
            float lightDiffuse[] = {0.5f, 0.5f, 0.5f, 1.0f};
            float lightSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};

            float matAmbient[] = {0.5f, 0.5f, 0.5f, 1.0f};
            float matDiffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
            float matSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};

            float light0Shininess = 0.5f;

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
