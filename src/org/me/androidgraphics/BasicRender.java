/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.androidgraphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

import min3d.Shared;
import min3d.animation.AnimationObject3d;
import min3d.core.FacesBufferedList;
import min3d.core.TextureManager;
import min3d.parser.IParser;
import min3d.parser.Parser1;

import org.me.mesh.Background;
import org.me.mesh.ExplosionParticleSystem;
import org.me.mesh.GameBoardPlane;
import org.me.mesh.Group;
import org.me.mesh.Mesh;
import org.me.mesh.Path;
import org.me.mesh.Plane;
import org.me.mesh.PointMaker;
import org.me.mesh.Position;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * 
 * @author akebulan
 */
public class BasicRender implements GLSurfaceView.Renderer {

	private Group root = new Group();
	private static Context context;
	private static GL10 gl;
	private float zoomFactor = 1f;
	private float selectedZoomFactor = 1f;
	private float xFactor = 1;
	private float yFactor = 1;
	private float aspectRatio;
	private boolean stop = false;
	private int fps = 0;
	private long time = System.currentTimeMillis();
	private AnimationObject3d doomGuy;
	// private Background1 background;
	int troops = 35;
	private AnimationObject3d doomGuys[] = new AnimationObject3d[troops];
	// Our vertex buffer.
	protected FloatBuffer verticesBuffer = null;
	// Our index buffer.
	protected ShortBuffer indicesBuffer = null;
	protected FloatBuffer textBuffer;
	protected FloatBuffer normalBuffer;
	// The number of indices.
	protected int numOfIndices = -1;
	TextureManager _textureManager;
	public int testID1, testID2, snow, back, flare;
	private float[] lightAmbient = { 1f, 1f, 1f, 1.0f };
	private float[] lightDiffuse = { 1f, 1f, 1f, 1.0f };
	private float[] lightSpecular = { 1f, 1f, 1f, 1.0f };
	private float[] lightPosition = { 650f, 1200, 650.0f, 1.0f };
	/* The buffers for our light values */
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightSpecularBuffer;
	private FloatBuffer lightPositionBuffer;
	private float[] groundplane = { 0.0f, 1.0f, 0.0f, 1.0f };
	public ExplosionParticleSystem mParticleSystem;
	// BUFFER TESTING
	private boolean mContextSupportsFrameBufferObject;
	private int framebuffer1, framebuffer2, framebuffer3, framebuffer4;
	private int mFramebufferWidth = 256;
	private int mFramebufferHeight = 256;
	private int mSurfaceWidth;
	private int mSurfaceHeight;

	private int mTargetTexture1, mTargetTexture2, mTargetTexture3,
			mTargetTexture4;
	public static final String PREFIXSQUARE = "square";
	private static float uniqueID = 0.0f;
	private GameBoardPlane gameBoardPlane;
	Path path;
	Plane plane;
	Plane plane2;
	PointMaker point1;
	protected DrawVO explosionDrawVO;
	protected ExplosionParticleSystem particleSystem;
	Background ground1;
	float cameraX = 500;
	float cameraY = 500;
	float cameraZ = 500;

	float objectZ = 0f;
	int width;
	int height;
	public float xCamera = -350;
	public float yCamera = -350;
	public float zCamera = 700;
	public float xCameraLook = 350;
	public float yCameraLook = 350;
	public float zCameraLook = 700;
	private float camerSpeed = 10;
	private float camRotSpeed = 50;
	private MatrixTrackingGL matrixTrackingGL;

	public BasicRender() {
	}

	public BasicRender(Context context) {
		// this.context = context;
		Shared.context(context);
		_textureManager = new TextureManager();
		Shared.textureManager(_textureManager);
		// Shared.renderer(this);
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = byteBuf.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = byteBuf.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(lightPosition.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightPositionBuffer = byteBuf.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(lightSpecular.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightSpecularBuffer = byteBuf.asFloatBuffer();
		lightSpecularBuffer.put(lightSpecular);
		lightSpecularBuffer.position(0);
	}

	DrawVO planedrawVO;
	DrawVO doomdrawVO;

	public void testVBO() {
		short[] indices = { 0, 1, 2, 0, 2, 3 };

		float inc = 700 / 2;

		float vertices[] = { -inc, -inc, 0.0f, // 0, Bottom Left
				inc, -inc, 0.0f, // 1, Bottom Right
				inc, inc, 0.0f, // 2, Top Right
				-inc, inc, 0.0f, // 3, Top Left
		};

		float[] tex = { 1, 0, 1, 1, 0, 1, 0, 0, };

		float normals[] = { 0.0f, 0.0f, 1.0f };

		FloatBuffer verticesBuffer = null;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verticesBuffer = vbb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);

		FloatBuffer textBuffer;
		vbb = ByteBuffer.allocateDirect(tex.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		textBuffer = vbb.asFloatBuffer();
		textBuffer.put(tex);
		textBuffer.position(0);

		ShortBuffer indicesBuffer;
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indicesBuffer = ibb.asShortBuffer();
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
		numOfIndices = indices.length;

		FloatBuffer normalBuffer;
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		// OpenGLUtility op = new OpenGLUtility();
		planedrawVO = OpenGLUtility.createVBO(verticesBuffer, indicesBuffer,
				textBuffer, normalBuffer, numOfIndices);

	}

	public void initGame(GL10 gl_, Context context) {
		Log.w("BasicRenderer",
				"initGame GL11 " + OpenGLUtility.gl11.glGetError());
		// testVBO();

		testID2 = loadTexture(R.drawable.camo1);
		snow = loadTexture(R.drawable.halo);
		back = loadTexture(R.drawable.tilefloor1);
		flare = loadTexture(R.drawable.explode1);

		IParser parser = Parser1.createParser(Parser1.Type.MD2, Shared
				.context().getResources(),
				"org.me.androidgraphics:raw/small_attackerl", false);

		parser.parse();
		doomGuy = parser.getParsedAnimationObject();
		doomGuy.setName("doomGuy");
		doomGuy.setFps(100);
		doomGuy.setTextureId(testID2);
		doomGuy.play();

		doomGuy.generateHardwareBuffers();
		doomdrawVO = OpenGLUtility.createVBO(doomGuy.getVerticesBuffer(),
				doomGuy.getIndicesBuffer(), doomGuy.getTextBuffer(),
				doomGuy.getNormalBuffer(), doomGuy.faces().size()
						* FacesBufferedList.PROPERTIES_PER_ELEMENT);
		// root.add(doomGuy);
		// doomGuy.setParent(root);
		float x = 0;
		float y = 0;
		float z = 0;

		// doomGuy.generateHardwareBuffers(gl_);

		for (int i = 0; i < troops; i++) {
			if (x > 600) {
				x = 0;
				y = y + 100;
			}
			doomGuys[i] = (AnimationObject3d) doomGuy.clone(true);
			doomGuys[i].x = x;
			doomGuys[i].y = y;
			// doomGuys[i].generateHardwareBuffers(gl_);

			objectZ = z;
			Log.w("BasicRender ", "objectZ " + objectZ);

			doomGuys[i].z = objectZ;
			doomGuys[i].setTextureId(testID2);
			doomGuys[i].setName("doomGuys" + i);
			doomGuys[i].angle = 0;

			// doomdrawVO.setX(x);
			// doomdrawVO.setY(y);
			// doomdrawVO.setZ(z);
			// doomdrawVO.setAngle(0);

			// doomGuys[i].setParent(root);
			// root.add(doomGuys[i]);
			// doomGuys[i].setFps(100);
			// doomGuys[i].play();
			// doomGuys[i].stop();

			x = x + 100;
			// z = z + 100;
			// y = y - 100;

		}

		ArrayList<Position> locations = new ArrayList<Position>();
		locations.add(new Position(-13, 0, 13));
		locations.add(new Position(13, 0, 13));
		locations.add(new Position(13, 0, 10));
		locations.add(new Position(-13, 0, 10));

		point1 = new PointMaker(locations);
		point1.animationObject3dY = new Float(doomGuys[0].y).floatValue();
		point1.animationObject3dX = new Float(doomGuys[0].x).floatValue();

		// point1.setTextureId(testID2);

		gameBoardPlane = new GameBoardPlane(50, back, 700, root);

		planedrawVO = OpenGLUtility.createVBO(
				gameBoardPlane.displyPlane.getVerticesBuffer(),
				gameBoardPlane.displyPlane.getIndicesBuffer(),
				gameBoardPlane.displyPlane.getTextBuffer(),
				gameBoardPlane.displyPlane.getNormalBuffer(),
				gameBoardPlane.displyPlane.getNumOfIndices());

		planedrawVO.setX(300);
		planedrawVO.setY(300);
		planedrawVO.setZ(0);

		particleSystem = new ExplosionParticleSystem();
		particleSystem.setName("effectExplosion");
		particleSystem.setTextureId(flare);
		particleSystem.x = 300;
		particleSystem.y = 300;
		particleSystem.z = 0;

		explosionDrawVO = OpenGLUtility.createVBO(
				particleSystem.getVerticesBuffer(),
				particleSystem.getIndicesBuffer(),
				particleSystem.getTextBuffer(),
				particleSystem.getNormalBuffer(),
				particleSystem.getNumOfIndices());

		explosionDrawVO.setX(300);
		explosionDrawVO.setY(300);
		explosionDrawVO.setZ(100);

		particleSystem.setDrawVO(explosionDrawVO);

		// root.add(gameBoardPlane);
		// plane = new Plane();
		// plane.setTextureId(back);
		// plane.setX(300);
		// plane.setY(300);
		// plane.setZ(0);
		// ground1 = new Background();
		// ground1.z = -3;
		// ground1.x = 0.6f;
		// ground1.y = -1f;
		// background = new Background1(700, 14);
		// background.setTextureId(back);
		// background.setX(300);
		// background.setY(300);
		// background.setZ(0);

		// root.add(background);

		// ArrayList<Position> locations = new ArrayList<Position>();
		// locations.add(new Position(300, 300, 125));
		// locations.add(new Position(400, 400, 25));
		// locations.add(new Position(10, 101, 10));
		// locations.add(new Position(100, 101, 10));

		// path = new Path(locations);
	}

	public void onSurfaceCreated(GL10 gl_, EGLConfig config) {
		gl = gl_;

		if (gl_ instanceof GL11)

		{
			GL11 gl11 = (GL11) gl_;
			OpenGLUtility.gl11 = gl11;
			Log.w("BasicRenderer", "GL11 " + OpenGLUtility.gl11.glGetError());

		}

		matrixTrackingGL = new MatrixTrackingGL(gl);

		// OpenGLUtility openGLUtility =

		new OpenGLUtility(context, gl, root);

		// mContextSupportsFrameBufferObject =
		// checkIfContextSupportsFrameBufferObject(gl);
		// if (mContextSupportsFrameBufferObject) {
		// mTargetTexture1 = createTargetTexture(gl, mFramebufferWidth,
		// mFramebufferHeight);
		// framebuffer1 = createFrameBuffer(gl, mFramebufferWidth,
		// mFramebufferHeight, mTargetTexture1);
		//
		// mTargetTexture2 = createTargetTexture(gl, 256, 256);
		// framebuffer2 = createFrameBuffer(gl, 256, 256, mTargetTexture2);
		//
		// mTargetTexture3 = createTargetTexture(gl, 128, 128);
		// framebuffer3 = createFrameBuffer(gl, 128, 128, mTargetTexture3);
		//
		// mTargetTexture4 = createTargetTexture(gl, 64, 64);
		// framebuffer4 = createFrameBuffer(gl, 64, 64, mTargetTexture4);
		//
		// OpenGLUtility.setFramebuffer(framebuffer1);
		// }

		// And there'll be light!
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightSpecularBuffer);
		gl.glEnable(GL10.GL_LIGHT0); // Enable Light 0
		gl.glEnable(GL10.GL_LIGHTING);

		// Blending gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f); // Full Brightness.
		// 50% Alpha gl.glBlendFunc(GL10.GL_SRC_ALPHA,
		// GL10.GL_ONE_MINUS_SRC_ALPHA); // Set // The // Blending // Function
		// For // Translucency
		// Settings
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glDisable(GL10.GL_DITHER); // Disable dithering
		// gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping
		// gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		// gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // We'll Clear To The Color
		// Of
		// The Fog ( Modified )
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do
		// gl.glPointSize(5); // Set point size to 5x5

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		initGame(gl_, context);

		/*
		 * float fog[] = { 0.5f, 0.5f, 0.5f, 1.0f }; ByteBuffer byteBuf =
		 * ByteBuffer.allocateDirect(fog.length * 4);
		 * byteBuf.order(ByteOrder.nativeOrder()); FloatBuffer fogColorBuffer =
		 * byteBuf.asFloatBuffer(); fogColorBuffer.put(fog);
		 * fogColorBuffer.position(0); float fogStart = 1600;// cameraZ-objectZ
		 * - 300; float fogEnd = 1700;// cameraZ-objectZ + 300;
		 * gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
		 * gl.glFogf(GL10.GL_FOG_START, fogStart); gl.glFogf(GL10.GL_FOG_END,
		 * fogEnd); gl.glFogfv(GL10.GL_FOG_COLOR, fogColorBuffer);
		 * gl.glFogf(GL10.GL_FOG_DENSITY, 0.35f); gl.glHint(GL10.GL_FOG_HINT,
		 * GL10.GL_DONT_CARE); gl.glEnable(GL10.GL_FOG); Log.w("BasicRender ",
		 * "GL_FOG_START " + fogStart); Log.w("BasicRender ", "GL_FOG_END " +
		 * fogEnd);
		 */

		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE);
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		mSurfaceWidth = width;
		mSurfaceHeight = height;

		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, width, height); // Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// plane2 = new Plane();
		//
		// plane = new Plane();
		// plane.setTextureId(testID2);

		// Calculate The Aspect Ratio Of The Window
		aspectRatio = (float) width / height;

		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, aspectRatio, 1f, 2000f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
		gl.glLoadIdentity();
//		gl.glClearColor(0, 0, 1, 0);

		// gl.glEnable(GL11.GL_TEXTURE_2D);
		// gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		// gl.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		// gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClearColor(0, 0, 1, 0);
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// // Setup our camera
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		//
		// // Reset the projection matrix
		// gl.glLoadIdentity();
		//
		// GLU.gluPerspective(gl, 45.0f, aspectRatio, 1f, 2000f);
		//
		// GLU.gluLookAt(gl, 0, 0, 10,
		// 0f, 0f, 1f,
		// 0f, 0.0f, 1.0f);
		//
		// // Select the modelview matrix
		// gl.glMatrixMode(GL10.GL_MODELVIEW);
		//
		// // Reset the modelview matrix
		// gl.glLoadIdentity();
		// GLU.gluLookAt(gl,
		// 0, 300, cameraZ,
		// 300, 300, 0f,
		// 0f, 1.0f, 0f);
		// rotated view

		followGesture("");

		setCamProjectionMatrix(gl);
		// GLU.gluLookAt(gl, xCamera, yCamera, zCamera * yFactor, // eye
		// xCameraLook, yCameraLook, 1, // center
		// 0.0f, 0.0f, 1.0f); // up
		// Draw our cube
		// for (int i = 0; i < 5; i++) {
		// doomGuys[i].draw(gl);
		// }

		point1.setAnimationObject3d(doomGuys[0]);
		point1.setX(doomGuys[0].x);
		point1.setY(doomGuys[0].y);
		point1.angle = doomGuys[0].angle;
		point1.draw(gl);

        gl.glColor4f(1,0,0,1);
//        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND);

//        gl.glEnable (GL10.GL_BLEND);
//        gl.glBlendFunc (GL10.GL_ONE, GL10.GL_ONE);
//		gl.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ZERO);

		for (int inc = 0; inc < troops; inc++) {
			doomdrawVO.setX(doomGuys[inc].x);
			doomdrawVO.setY(doomGuys[inc].y);
			doomdrawVO.setZ(doomGuys[inc].z);
			doomdrawVO.setAngle(doomGuys[inc].angle);
			doomGuys[inc].setDrawVO(doomdrawVO);
			OpenGLUtility.drawTestVBO(doomdrawVO, testID2);
		}
		doomGuys[0].update();
		
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

      gl.glPushMatrix();
//		gl.glTranslatef(planedrawVO.x, planedrawVO.y, planedrawVO.z);
//		OpenGLUtility.billboard(cameraX, cameraY, cameraZ, planedrawVO.x,
//				planedrawVO.y, planedrawVO.z);
		OpenGLUtility.drawTestVBO(planedrawVO, back);
      gl.glPopMatrix();		
		
//		gl.glTranslatef(explosionDrawVO.x, explosionDrawVO.y, explosionDrawVO.z);
//        gl.glScalef(particleSystem.mParticles[0].scale, particleSystem.mParticles[0].scale, particleSystem.mParticles[0].scale);
//
//		OpenGLUtility.billboard(cameraX, cameraY, cameraZ, explosionDrawVO.x,
//				explosionDrawVO.y, explosionDrawVO.z);
//		gl.glEnable(GL10.GL_BLEND);
//        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
//
		OpenGLUtility.drawTestVBO2(explosionDrawVO, flare);
		particleSystem.update();

		// restores the modelview matrix

		// for (int i = 0; i < 7; i++) {
		//
		// doomGuys[i].draw(gl);
		// }

		// gameBoardPlane.draw(gl);

		// int i, j=0;

		// save the current modelview matrix
		// matrixTrackingGL.glPushMatrix();
		//
		// // get the current modelview matrix
		// float mModelView[] = new float[16];
		// // gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
		// matrixTrackingGL.glMatrixMode(GL10.GL_MODELVIEW);
		// matrixTrackingGL.getMatrix(mModelView, 0);
		// // matrixTrackingGL.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		// matrixTrackingGL.glPopMatrix();

		// undo all rotations
		// beware all scaling is lost as well
		// for (int i = 0; i < 3; i++) {
		// for (int j = 0; j < 3; j++) {
		// if (i == j)
		// mModelView[i * 4 + j] = 1.0f;
		// else
		// mModelView[i * 4 + j] = 0.0f;
		// }
		// }

		// float right[] = new float[3];
		// right[0] = mModelView[0];
		// right[1] = mModelView[4];
		// right[2] = mModelView[8];
		//
		// float up[] = new float[3];
		// up[0] = mModelView[1];
		// up[1] = mModelView[5];
		// up[2] = mModelView[9];
		//
		// float upAux[] = new float[3];
		// upAux[0] = 0;
		// upAux[1] = 0;
		// upAux[2] = 0;
		//
		// float lookAt[] = new float[3];
		// lookAt[0] = mModelView[2];
		// lookAt[1] = mModelView[6];
		// lookAt[2] = mModelView[10];
		//
		// float objToCamProjX = this.cameraX - planedrawVO.getX();
		// float objToCamProjY = 0;
		// float objToCamProjZ = this.cameraZ - planedrawVO.getZ();
		//
		// // normalize both vectors to get the cosine directly afterwards
		// float d = new Double(FastMath.sqrt((objToCamProjX * objToCamProjX)
		// + (objToCamProjY * objToCamProjY)
		// + (objToCamProjZ * objToCamProjZ))).floatValue();
		// objToCamProjX = objToCamProjX / d;
		// objToCamProjY = objToCamProjY / d;
		// objToCamProjZ = objToCamProjZ / d;
		//
		// // easy fix to determine wether the angle is negative or positive
		// // for positive angles upAux will be a vector pointing in the
		// // positive y direction, otherwise upAux will point downwards
		// // effectively reversing the rotation.
		//
		// upAux[0] = lookAt[1] * objToCamProjZ - objToCamProjY * lookAt[2];
		// upAux[1] = lookAt[2] * objToCamProjX - objToCamProjZ * lookAt[0];
		// upAux[2] = lookAt[0] * objToCamProjY - objToCamProjX * lookAt[1];
		//
		// // compute the angle
		// float angleCosine = (lookAt[0] * objToCamProjX + lookAt[1]
		// * objToCamProjY + lookAt[2] * objToCamProjZ);
		//
		// // perform the rotation. The if statement is used for stability
		// reasons
		// // if the lookAt and v vectors are too close together then |aux|
		// could
		// // be bigger than 1 due to lack of precision
		// if ((angleCosine < 0.99990) && (angleCosine > -0.9999))
		// gl.glRotatef(new Double(FastMath.acos(angleCosine) * 180 / 3.14)
		// .floatValue(), upAux[0], upAux[1], upAux[2]);
		//
		// // The second part tilts the object so that it faces the camera
		// // objToCam is the vector in world coordinates from the local origin
		// to
		// // the camera
		// float objToCamX = this.cameraX - planedrawVO.getX();
		// float objToCamY = this.cameraY - planedrawVO.getY();
		// float objToCamZ = this.cameraZ - planedrawVO.getZ();
		//
		// // Normalize to get the cosine afterwards
		// d = new Double(FastMath.sqrt((objToCamX * objToCamX)
		// + (objToCamY * objToCamY) + (objToCamZ * objToCamZ)))
		// .floatValue();
		// objToCamX = objToCamX / d;
		// objToCamY = objToCamY / d;
		// objToCamZ = objToCamZ / d;
		//
		// // Compute the angle between v and v2, i.e. compute the
		// // required angle for the lookup vector
		// angleCosine = (objToCamProjX * objToCamX + objToCamProjY * objToCamY
		// + objToCamProjZ
		// * objToCamZ);
		//
		// // Tilt the object. The test is done to prevent instability when
		// // objToCam and objToCamProj have a very small
		// // angle between them
		// if ((angleCosine < 0.99990) && (angleCosine > -0.9999))
		// if (objToCamY < 0)
		// gl.glRotatef(
		// new Double(FastMath.acos(angleCosine) * 180 / 3.14)
		// .floatValue(), 1, 0, 0);
		// else
		// gl.glRotatef(
		// new Double(FastMath.acos(angleCosine) * 180 / 3.14)
		// .floatValue(), -1, 0, 0);

		// set the modelview with no rotations and scaling
		// matrixTrackingGL.glMatrixMode(GL10.GL_MODELVIEW);
		// matrixTrackingGL.glLoadMatrixf(mModelView,0);
		// matrixTrackingGL.glLoadMatrixf(mModelView, 0);

		// gl.glLoadMatrixf(mModelView, 0);

		// gl.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		// gl.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		// gl.glDisable(GL11.GL_TEXTURE_2D);

		// GL11ExtensionPack gl11ep = (GL11ExtensionPack) gl;
		//
		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
		// 0);
		// drawOffscreenImage1(gl, mSurfaceWidth, mSurfaceHeight);

		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
		// framebuffer2);
		// drawOffscreenImage2(gl, 256, 256);

		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, 0);
		// drawOnscreen(gl, mSurfaceWidth, mSurfaceHeight);

		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, 0);
		// onDraw(gl);

		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
		// framebuffer1);
		// onDraw(gl);

		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
		// framebuffer2);
		// onDraw(gl);

		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
		// framebuffer3);
		// onDraw(gl);

		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
		// framebuffer4);
		// onDraw(gl);

		// gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

		// gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, 0);
		// plane.setTextureId(0);
		// plane.draw(gl);

		// gameBoardPlane.pick(gl);
		if (System.currentTimeMillis() - time >= 1000) {
			Log.w("BasicRender ", fps + " fps");

			// for (int i = 0; i < 3; i++) {
			// for (int j = 0; j < 3; j++) {
			// Log.w("BasicRender ", "mModelView: "
			// + mModelView[i * 4 + j]);
			//
			// }
			// }

			fps = 0;
			time = System.currentTimeMillis();
		}
		fps++;

	}

	public void onDraw(GL10 gl) {
		// doomGuy.update();
		path.update();

		gl.glLoadIdentity();
		// Setup our camera
		// GLU.gluLookAt(gl, 0, 0, 1200.0f,
		// 0, 0, 0f,
		// 0f, 1.0f, 0f);
		// GLU.gluLookAt(gl, 300, 300, 800.0f,
		// 300, 300, 0f,
		// 0f, 1.0f, 0f);
		GLU.gluLookAt(gl, 300, 0, cameraZ, 300, 300, 0f, 0f, 1.0f, 0f);
		//

		// GLU.gluLookAt(gl, 300, 0, 1000 * yFactor, // eye
		// 300, 300, 0, // center
		// 0.0f, 1.0f, 0.0f); // up
		// GLU.gluLookAt(gl, 300, -600, 100,
		// 300, 300, 0f,
		// 0f, 1.0f, 0f);
		// Turn off writing to the Color Buffer and Depth Buffer
		// We want to draw to the Stencil Buffer only
		gl.glColorMask(false, false, false, false);
		gl.glDepthMask(false);

		// Enable the Stencil Buffer
		gl.glEnable(GL10.GL_STENCIL_TEST);

		// Set 1 into the stencil buffer
		gl.glStencilFunc(GL10.GL_ALWAYS, 1, 0xFFFFFFFF);
		gl.glStencilOp(GL10.GL_REPLACE, GL10.GL_REPLACE, GL10.GL_REPLACE);

		// Draw the floor
		// ground1.draw(gl);
		// gameBoardPlane.pick(gl);
		gameBoardPlane.draw(gl);

		// Turn on Color Buffer and Depth Buffer
		gl.glColorMask(true, true, true, true);
		gl.glDepthMask(true);

		// Only write to the Stencil Buffer where 1 is set
		gl.glStencilFunc(GL10.GL_EQUAL, 1, 0xFFFFFFFF);
		// Keep the content of the Stencil Buffer
		gl.glStencilOp(GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_KEEP);

		// Draw the floor again
		// ground1.draw(gl);
		// gameBoardPlane.pick(gl);
		gameBoardPlane.draw(gl);

		// Push the current matrix to the stack
		gl.glPushMatrix();

		// Set our color
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);

		// Disable light
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_FOG);
		// Enable blending
		gl.glEnable(GL10.GL_BLEND);

		// Keep the content of the Stencil Buffer and increase when z passed
		// (not Carmacks fail test)
		gl.glStencilOp(GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_INCR);
		// Calculate the projected shadow
		float[] shadowMat = shadowmatrix(groundplane, lightPosition);
		gl.glMultMatrixf(shadowMat, 0);

		// Draw our cube
		for (int i = 0; i < 5; i++) {
			doomGuys[i].draw(gl);
		}
		// GLUT.glutWireSphere(gl, 0.5f, 12, 12);

		// Re-enable
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_FOG);
		// and Pop the matrix back from the stack
		gl.glPopMatrix();

		//
		gl.glPushMatrix();

		// Disable Stencil Buffer
		gl.glDisable(GL10.GL_STENCIL_TEST);

		// Draw our cube
		for (int i = 0; i < 5; i++) {
			doomGuys[i].draw(gl);
		}
		path.draw(gl);
		// ground1.draw(gl);

		// GLUT.glutWireSphere(gl, 2, 12, 12);

		// gameBoardPlane.pick(gl);
		// root.pick(gl);

		//
		gl.glPopMatrix();

		if (System.currentTimeMillis() - time >= 1000) {
			Log.w("BasicRender ", fps + " fps");
			fps = 0;
			time = System.currentTimeMillis();
		}
		fps++;
	}

	private void drawOnscreen(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		// float ratio = (float) width / height;
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		// gl.glLoadIdentity();
		// gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);

		gl.glClearColor(0, 0, 1, 0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// gl.glEnable(GL10.GL_BLEND);
		// gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);

		// gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		// gl.glBindTexture(GL10.GL_TEXTURE_2D, mTargetTexture1);

		// gl.glBindTexture(GL10.GL_TEXTURE_2D, mTargetTexture2);

		// gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
		// GL10.GL_REPLACE);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		GLU.gluLookAt(gl, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// GLU.gluLookAt(gl,
		// 300, 0, cameraZ,
		// 300, 300, 0f,
		// 0f, 1.0f, 0f);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glActiveTexture(GL10.GL_TEXTURE0);

		// long time = SystemClock.uptimeMillis() % 4000L;
		// float angle = 0.090f * ((int) time);

		// gl.glRotatef(angle, 0, 0, 1.0f);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTargetTexture1);
		plane2.drawTest(gl);

		// gl.glBindTexture(GL10.GL_TEXTURE_2D, mTargetTexture2);
		// plane2.drawTest(gl);
		// plane.drawTest(gl);

		// Restore default state so the other renderer is not affected.

		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_BLEND);

	}

	private float mAngle = 90;

	private void drawOffscreenImage1(GL10 gl, int width, int height) {

		// gl.glMatrixMode(GL10.GL_MODELVIEW);
		// gl.glLoadIdentity();

		// onDraw(gl);

		gl.glViewport(0, 0, width, height);
		// float ratio = (float) width / height;
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		// gl.glLoadIdentity();
		// gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

		// gl.glEnable(GL10.GL_CULL_FACE);
		// gl.glEnable(GL10.GL_DEPTH_TEST);

		gl.glClearColor(0, 1, 0, 0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();

		// gl.glRotatef(180, 0, 1, 0);
		// gl.glRotatef(mAngle * 0.25f, 1, 0, 0);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		// gl.glBindTexture(GL10.GL_TEXTURE_2D, flare);

		// Draw our cube
		// plane.drawTest(gl);
		gl.glTranslatef(0, 0, -3.0f);
		point1.draw(gl);

		// gl.glRotatef(mAngle * 2.0f, 0, 1, 1);
		// gl.glTranslatef(0.5f, 0.5f, 0.5f);

		// Draw our cube
		// plane.drawTest(gl);

		// mAngle += 1.2f;

		// gl.glTranslatef(0.5f, 0.5f, 0.5f);
		// Draw our cube
		// plane.drawTest(gl);

		// mAngle += 1.2f;

		gl.glPopMatrix();

		// Restore default state so the other renderer is not affected.
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

	}

	private void drawOffscreenImage2(GL10 gl, int width, int height) {

		// gl.glMatrixMode(GL10.GL_MODELVIEW);
		// gl.glLoadIdentity();

		// onDraw(gl);

		gl.glViewport(0, 0, width, height);
		// float ratio = (float) width / height;
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		// gl.glLoadIdentity();
		// gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

		// gl.glEnable(GL10.GL_CULL_FACE);
		// gl.glEnable(GL10.GL_DEPTH_TEST);

		gl.glClearColor(1, 0, 0, 0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();

		gl.glTranslatef(0, -1, -3.0f);
		gl.glRotatef(180, 0, 1, 0);
		// gl.glRotatef(mAngle * 0.25f, 1, 0, 0);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		// gl.glBindTexture(GL10.GL_TEXTURE_2D, flare);

		// Draw our cube
		plane.drawTest(gl);
		// point1.draw(gl);
		// gl.glRotatef(mAngle * 2.0f, 0, 1, 1);
		// gl.glTranslatef(0.5f, 0.5f, 0.5f);

		// Draw our cube
		// plane.drawTest(gl);

		// mAngle += 1.2f;

		// gl.glTranslatef(0.5f, 0.5f, 0.5f);
		// Draw our cube
		// plane.drawTest(gl);

		// mAngle += 1.2f;

		gl.glPopMatrix();

		// Restore default state so the other renderer is not affected.
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

	}

	public void processPickResults(float[] pickResults) {
		Log.w("processPickResults", "color is R=" + pickResults[0] + " G="
				+ pickResults[1] + " B=" + pickResults[2] + " A="
				+ pickResults[3]);

		Mesh mesh;
		for (int i = 0; i < root.getAllChildren().size(); i++) {
			mesh = root.getAllChildren().get(i);
			if (mesh != null) {
				if (mesh.getRgba()[0] == pickResults[0]
						&& mesh.getRgba()[1] == pickResults[1]
						&& mesh.getRgba()[2] == pickResults[2]) {

					Log.w("Group", "processPickResults: X=" + mesh.x + " Y="
							+ mesh.y);
					int x = Float.valueOf(mesh.x).intValue();
					int y = Float.valueOf(mesh.y).intValue();
					ArrayList<Position> locations = new ArrayList<Position>();
					locations.add(new Position(300, 300, 125));
					locations.add(new Position(x, y, 25));

					// path.setLocations(locations);
				}
			}
		}

	}

	protected float mAngleX = 0;

	// public void onDraw(GL10 gl) {
	// doomGuy.update();
	// // doomGuy.draw(gl);
	// //
	// // background.draw(gl);
	//
	// shadowfax(gl);
	// }
	// public void setProjectionMatrix(GL10 gl) {
	// // Select the projection matrix
	// gl.glMatrixMode(GL10.GL_PROJECTION);
	// // Reset the projection matrix
	// gl.glLoadIdentity();
	//
	//
	// GLU.gluPerspective(gl, 45.0f * zoomFactor, aspectRatio, 1f, 1300f);
	//
	// GLU.gluLookAt(gl, 300 * xFactor, 300 * yFactor, 500, // eye
	// 300 * xFactor, 300 * yFactor, 0, // center
	// 0.0f, 1.0f, 0.0f); // up
	// // Select the modelview matrix
	// gl.glMatrixMode(GL10.GL_MODELVIEW);
	// // Reset the modelview matrix
	// gl.glLoadIdentity();
	//
	// }
	// int uploadTextureAndReturnId(int resource, boolean $generateMipMap)
	// /*package-private*/ {
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inScaled = false;
	// Bitmap temp =
	// BitmapFactory.decodeResource(Shared.context().getResources(), resource,
	// opts);
	//
	// // int[] a = new int[1];
	// // gl.glGenTextures(1, a, 0); // create a 'texture name' and put it in
	// array element 0
	// // glTextureId = a[0
	//
	// int id = newTextureID(gl);
	//
	// gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
	//
	// if ($generateMipMap && gl instanceof GL11) {
	// gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP,
	// GL11.GL_TRUE);
	// } else {
	// gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP,
	// GL11.GL_FALSE);
	// }
	//
	// // 'upload' to gpu
	// GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, temp, 0);
	//
	// return id;
	// }
	public static int loadTexture(int resource) {
		// In which ID will we be storing this texture?
		int id = newTextureID(gl);
		// We need to flip the textures vertically:
		Matrix flip = new Matrix();
		flip.postScale(1f, -1f);
		flip.postRotate(180f);

		// This will tell the BitmapFactory to not scale based on the device's
		// pixel density:
		// (Thanks to Matthew Marshall for this bit)
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		// Load up, and flip the texture:
		Bitmap temp = BitmapFactory.decodeResource(Shared.context()
				.getResources(), resource, opts);
		Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
				temp.getHeight(), flip, true);
		temp.recycle();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
		// Set all of our texture parameters:
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		int[] crop = { 0, bmp.getWidth(), bmp.getHeight(), -bmp.getHeight() };

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

		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

		return id;
	}

	private static int newTextureID(GL10 gl) {
		int[] temp = new int[1];
		gl.glGenTextures(1, temp, 0);
		return temp[0];
	}

	public float[] shadowmatrix(float[] groundplane, float[] lightpos) {
		// Init
		// swapped Y an Z for our scene else shadow appear along y plane
		float[][] shadowMat = new float[4][4];
		int X = 0;
		int Y = 2;
		int Z = 1;
		int W = 3;

		// Find the dot product between the light position vector and the ground
		// plane normal
		float dot = groundplane[X] * lightpos[X] + groundplane[Y] * lightpos[Y]
				+ groundplane[Z] * lightpos[Z] + groundplane[W] * lightpos[W];

		// Calculate the floor shadow
		// based on the light position to the groundplane
		shadowMat[0][0] = dot - lightpos[X] * groundplane[X];
		shadowMat[1][0] = 0.f - lightpos[X] * groundplane[Y];
		shadowMat[2][0] = 0.f - lightpos[X] * groundplane[Z];
		shadowMat[3][0] = 0.f - lightpos[X] * groundplane[W];

		shadowMat[X][1] = 0.f - lightpos[Y] * groundplane[X];
		shadowMat[1][1] = dot - lightpos[Y] * groundplane[Y];
		shadowMat[2][1] = 0.f - lightpos[Y] * groundplane[Z];
		shadowMat[3][1] = 0.f - lightpos[Y] * groundplane[W];

		shadowMat[X][2] = 0.f - lightpos[Z] * groundplane[X];
		shadowMat[1][2] = 0.f - lightpos[Z] * groundplane[Y];
		shadowMat[2][2] = dot - lightpos[Z] * groundplane[Z];
		shadowMat[3][2] = 0.f - lightpos[Z] * groundplane[W];

		shadowMat[X][3] = 0.f - lightpos[W] * groundplane[X];
		shadowMat[1][3] = 0.f - lightpos[W] * groundplane[Y];
		shadowMat[2][3] = 0.f - lightpos[W] * groundplane[Z];
		shadowMat[3][3] = dot - lightpos[W] * groundplane[W];

		// Convert to single array
		float[] retVal = new float[4 * 4];
		int counter = 0;
		for (int i = 0; i < shadowMat.length; i++) {
			for (int j = 0; j < shadowMat[i].length; j++) {
				retVal[counter++] = shadowMat[i][j];
			}
		}

		//
		return retVal;
	}

	/*
	 * private int createFrameBuffer(GL10 gl, int width, int height, int
	 * targetTextureId) { GL11ExtensionPack gl11ep = (GL11ExtensionPack) gl; //
	 * int framebuffer; int[] framebuffers = new int[1];
	 * 
	 * // gl11ep.glGenFramebuffersOES(1, framebuffers, 0);
	 * 
	 * int framebuffer = framebuffers[0];
	 * 
	 * gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
	 * framebuffer);
	 * 
	 * // int depthbuffer; //// int[] renderbuffers = new int[1]; ////
	 * gl11ep.glGenRenderbuffersOES(1, renderbuffers, 0); // depthbuffer =
	 * renderbuffers[0];
	 * 
	 * // gl11ep.glBindRenderbufferOES(GL11ExtensionPack.GL_RENDERBUFFER_OES,
	 * depthbuffer);
	 * 
	 * // gl11ep.glRenderbufferStorageOES(GL11ExtensionPack.GL_RENDERBUFFER_OES,
	 * // GL11ExtensionPack.GL_DEPTH_COMPONENT16, width, height);
	 * 
	 * //
	 * gl11ep.glFramebufferRenderbufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
	 * // GL11ExtensionPack.GL_DEPTH_ATTACHMENT_OES, //
	 * GL11ExtensionPack.GL_RENDERBUFFER_OES, depthbuffer);
	 * 
	 * gl11ep.glFramebufferTexture2DOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
	 * GL11ExtensionPack.GL_COLOR_ATTACHMENT0_OES, GL10.GL_TEXTURE_2D,
	 * targetTextureId, 0);
	 * 
	 * int status =
	 * gl11ep.glCheckFramebufferStatusOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES);
	 * 
	 * if (status != GL11ExtensionPack.GL_FRAMEBUFFER_COMPLETE_OES) { throw new
	 * RuntimeException("Framebuffer is not complete: " +
	 * Integer.toHexString(status)); } //
	 * gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, 0);
	 * return framebuffer; }
	 */
	private int createFrameBuffer(GL10 gl, int width, int height,
			int targetTextureId) {
		GL11ExtensionPack gl11ep = (GL11ExtensionPack) gl;
		int framebuffer;
		int[] framebuffers = new int[1];
		gl11ep.glGenFramebuffersOES(1, framebuffers, 0);
		framebuffer = framebuffers[0];
		gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
				framebuffer);

		int depthbuffer;
		int[] renderbuffers = new int[1];
		gl11ep.glGenRenderbuffersOES(1, renderbuffers, 0);
		depthbuffer = renderbuffers[0];

		gl11ep.glBindRenderbufferOES(GL11ExtensionPack.GL_RENDERBUFFER_OES,
				depthbuffer);
		gl11ep.glRenderbufferStorageOES(GL11ExtensionPack.GL_RENDERBUFFER_OES,
				GL11ExtensionPack.GL_DEPTH_COMPONENT16, width, height);
		gl11ep.glFramebufferRenderbufferOES(
				GL11ExtensionPack.GL_FRAMEBUFFER_OES,
				GL11ExtensionPack.GL_DEPTH_ATTACHMENT_OES,
				GL11ExtensionPack.GL_RENDERBUFFER_OES, depthbuffer);

		gl11ep.glFramebufferTexture2DOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
				GL11ExtensionPack.GL_COLOR_ATTACHMENT0_OES, GL10.GL_TEXTURE_2D,
				targetTextureId, 0);
		int status = gl11ep
				.glCheckFramebufferStatusOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES);
		if (status != GL11ExtensionPack.GL_FRAMEBUFFER_COMPLETE_OES) {
			throw new RuntimeException("Framebuffer is not complete: "
					+ Integer.toHexString(status));
		}
		gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, 0);
		return framebuffer;
	}

	private int createTargetTexture(GL10 gl, int width, int height) {
		int texture;
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		texture = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, width, height, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, null);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		return texture;
	}

	private boolean checkIfContextSupportsFrameBufferObject(GL10 gl) {
		return checkIfContextSupportsExtension(gl, "GL_OES_framebuffer_object");
	}

	private boolean checkIfContextSupportsExtension(GL10 gl, String extension) {
		String extensions = " " + gl.glGetString(GL10.GL_EXTENSIONS) + " ";
		// The extensions string is padded with spaces between extensions, but
		// not
		// necessarily at the beginning or end. For simplicity, add spaces at
		// the
		// beginning and end of the extensions string and the extension string.
		// This means we can avoid special-case checks for the first or last
		// extension, as well as avoid special-case checks when an extension
		// name
		// is the same as the first part of another extension name.
		return extensions.indexOf(" " + extension + " ") >= 0;
	}

	// private void createLight(GL10 gl) {
	// float lightAmbient[] = {1.0f, 1.0f, 1.0f, 1.0f};
	// float lightDiffuse[] = {0f, 1.0f, 1.0f, 1.0f};
	// float lightSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};
	//
	// float matAmbient[] = {0.8f, 0.8f, 0.8f, 1.0f};
	// float matDiffuse[] = {0f, 0.8f, 0.8f, 1.0f};
	// float matSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};
	//
	// float light0Shininess = 128f;
	//
	// gl.glEnable(GL10.GL_LIGHTING);
	// gl.glEnable(GL10.GL_LIGHT0);
	//
	// gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, matAmbient, 0);
	// gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, matDiffuse, 0);
	// gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, matSpecular, 0);
	// gl.glMaterialf(GL10.GL_FRONT, GL10.GL_SHININESS, light0Shininess);
	//
	// gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
	// gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
	// gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightSpecular, 0);
	//
	// //// Define the position of the first light
	// float light0Position[] = {300 * xFactor, 300 * yFactor, 100};
	// gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Position, 0);
	// //
	// //// Define a direction vector for the light, this one points right down
	// the Z axis
	// // float light0Direction[] = {300 * xFactor, 300 * yFactor, 1200};
	// // gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, light0Direction,
	// 0);
	// }

	public Group getRoot() {
		return root;
	}

	public float getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(float zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public float getxFactor() {
		return xFactor;
	}

	public void setxFactor(float xFactor) {
		this.xFactor = xFactor;
	}

	public float getyFactor() {
		return yFactor;
	}

	public void setyFactor(float yFactor) {
		this.yFactor = yFactor;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public float getSelectedZoomFactor() {
		return selectedZoomFactor;
	}

	public void setSelectedZoomFactor(float selectedZoomFactor) {
		this.selectedZoomFactor = selectedZoomFactor;
	}

	public static float getUniqueID() {
		return uniqueID = uniqueID + 0.1f;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	float tempxFactor = 0;
	boolean travelX = false;
	boolean travelY = false;
	boolean travelXDirection = true;
	boolean travelYDirection = true;
	boolean lefttravelXDirection = true;
	boolean lefttravelYDirection = true;
	boolean righttravelXDirection = true;
	boolean righttravelYDirection = true;
	boolean activate = false;
	int spinCount = 28;
	int spinCountMax = 28;

	public void setCamProjectionMatrix(GL10 gl) {
		// Select the projection matrix
		matrixTrackingGL.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		matrixTrackingGL.glLoadIdentity();
		float roundZoom = OpenGLUtility.round(zoomFactor, 1);
		float roundSelect = OpenGLUtility.round(selectedZoomFactor, 1);

		if (roundZoom < roundSelect) {
			zoomFactor = zoomFactor + 0.1f;
		} else if (roundZoom > roundSelect) {
			zoomFactor = zoomFactor - 0.1f;
		} else {
			zoomFactor = selectedZoomFactor;
		}
		// // zoomFactor = zoomFactor - 0.01f;
		// effects jagged edges on intersecting polygons
		GLU.gluPerspective(gl, 45.0f * 0.5f, aspectRatio, 200f, 3000);
		// Log.w("BasicRender ", yFactor +
		// " zoomFactor perspective "+yFactor*3000);

		// translate contol view
		// GLU.gluLookAt(gl, 300 * xFactor, -300, 800 * yFactor, // eye
		// 300 * xFactor, 300, 1 * yFactor, // center
		// 0.0f, 0.0f, 1.0f); // up

		// rotated view
		this.cameraX = xCamera * xFactor;
		this.cameraY = yCamera;
		this.cameraZ = zCamera * yFactor;
		GLU.gluLookAt(gl, cameraX, cameraY, cameraZ, // eye
				xCameraLook, yCameraLook, 1, // center
				0.0f, 0.0f, 1.0f); // up

		// billboard attributes
		// OpenGLUtility.xFactor = xFactor;
		// OpenGLUtility.yFactor = yFactor;
		OpenGLUtility.cameraX=cameraX;
		OpenGLUtility.cameraY=cameraY;
		OpenGLUtility.cameraZ=cameraZ;
		
		// Select the modelview matrix
		matrixTrackingGL.glMatrixMode(GL10.GL_MODELVIEW);

		// rotated view
		// GLU.gluLookAt(gl,
		// -350, -350, 700 * yFactor, // eye
		// 350 , 350, 1 , // center
		// 0.0f, 0.0f, 1.0f); // up

		// Reset the modelview matrix
		matrixTrackingGL.glLoadIdentity();
	}

	public void followGesture(String direction) {
		double x = 0;
		double y = 0;
		double xMax = 1050;
		double xMin = -350;
		double yMax = 1050;
		double yMin = -350;

		if (xCamera >= xMax) {
			xCamera = Double.valueOf(xMax).floatValue();
			// xCamera = new Float(xMax).floatValue();
		}
		if (xCamera <= xMin) {
			xCamera = Double.valueOf(xMin).floatValue();
			// xCamera = new Float(xMin).floatValue();
		}
		if (yCamera >= yMax) {
			yCamera = Double.valueOf(yMax).floatValue();
			// yCamera = new Float(yMax).floatValue();
		}
		if (yCamera <= yMin) {
			yCamera = Double.valueOf(yMin).floatValue();
			// yCamera = new Float(yMin).floatValue();
		}

		// if(gameManager.getPickResults()!=null)
		// {
		// float r = gameManager.getPickResults()[0];
		// float g = gameManager.getPickResults()[1];
		// float b = gameManager.getPickResults()[2];
		// float a = gameManager.getPickResults()[3];
		//
		// if(r==0.5 && g==0.5&&b==0.5&&a==1.0)
		// followGesture xFactor add to cameraxCamera -350.0 yCamera -350.0
		// travelXDirection false travelYDirection true travelX true travelY
		// true
		/*
		 * if (direction.equals("left")) { // tempxFactor = xFactor; if (xCamera
		 * < xMax && travelXDirection) { travelXDirection = false; travelX =
		 * true; } if (xCamera > xMin && !travelXDirection) { travelXDirection =
		 * true; travelX = true; }
		 * 
		 * if (yCamera < yMax && travelYDirection) { travelYDirection = true;
		 * travelX = true; } if (yCamera > yMin && !travelYDirection) {
		 * travelYDirection = false; travelX = true; }
		 * 
		 * // travelXDirection = true; // travelYDirection =true; activate =
		 * true; spinCount = 0; // Log.w("BasicRender", //
		 * " followGesture xFactor add to cameraxCamera "
		 * +xCamera+" yCamera "+yCamera+ //
		 * " travelXDirection "+travelXDirection
		 * +" travelYDirection "+travelYDirection
		 * +" travelX "+travelX+" travelY "+travelY); if (xCamera >= xMax) {
		 * xCamera = Double.valueOf(xMax - 1).floatValue(); //xCamera = new
		 * Float(xMax - 1).floatValue(); travelY = true; travelX = false; if
		 * (travelYDirection) { travelYDirection = false; } } if (xCamera <=
		 * xMin) { xCamera = Double.valueOf(xMin + 1).floatValue(); // xCamera =
		 * new Float(xMin + 1).floatValue(); travelY = true; travelX = false; if
		 * (!travelYDirection) { travelYDirection = true; } } if (yCamera >=
		 * yMax) { yCamera = Double.valueOf(yMax - 1).floatValue(); // yCamera =
		 * new Float(yMax - 1).floatValue(); travelX = true; travelY = false; if
		 * (!travelXDirection) { travelXDirection = true; } } if (yCamera <=
		 * yMin) { yCamera = Double.valueOf(yMin + 1).floatValue(); //yCamera =
		 * new Float(yMin + 1).floatValue(); travelX = true; travelY = false; if
		 * (travelXDirection) { travelXDirection = false; } }
		 * 
		 * } else if (direction.equals("right")) { // tempxFactor = xFactor; if
		 * (xCamera < xMax && travelXDirection) { travelXDirection = true;
		 * travelX = true; } if (xCamera > xMin && !travelXDirection) {
		 * travelXDirection = false; travelX = true; }
		 * 
		 * if (yCamera < yMax && travelYDirection) { travelYDirection = true;
		 * travelX = true; } if (yCamera > yMin && !travelYDirection) {
		 * travelYDirection = false; travelX = true; } // travelXDirection =
		 * false; // travelYDirection =false; activate = true; spinCount = 0; //
		 * Log.w("BasicRender", //
		 * " followGesture xFactor subtract from cameraxCamera "
		 * +xCamera+" yCamera "
		 * +yCamera+" travelXDirection "+travelXDirection+" travelX "
		 * +travelX+" travelY "+travelY);
		 * 
		 * if (xCamera >= xMax) { xCamera = Double.valueOf(xMax -
		 * 1).floatValue(); //xCamera = new Float(xMax - 1).floatValue();
		 * travelY = true; travelX = false; if (travelXDirection) {
		 * travelXDirection = false; } } if (xCamera <= xMin) { xCamera =
		 * Double.valueOf(xMin + 1).floatValue(); //xCamera = new Float(xMin +
		 * 1).floatValue(); travelY = true; travelX = false; if
		 * (!travelXDirection) { travelXDirection = true; } } if (yCamera >=
		 * yMax) { yCamera = Double.valueOf(yMax - 1).floatValue(); //yCamera =
		 * new Float(yMax - 1).floatValue(); travelX = true; travelY = false; if
		 * (travelYDirection) { travelYDirection = false; } } if (yCamera <=
		 * yMin) { yCamera = Double.valueOf(yMin + 1).floatValue();
		 * 
		 * //yCamera = new Float(yMin + 1).floatValue();
		 * 
		 * travelX = true; travelY = false; if (!travelYDirection) {
		 * travelYDirection = true; } }
		 * 
		 * } else { spinCount++;
		 * 
		 * if (spinCount > spinCountMax) { activate = false; } else { activate =
		 * true; }
		 * 
		 * if (xCamera >= xMax) { xCamera = Double.valueOf(xMax).floatValue();
		 * //xCamera = new Float(xMax).floatValue(); } if (xCamera <= xMin) {
		 * xCamera = Double.valueOf(xMin).floatValue(); // xCamera = new
		 * Float(xMin).floatValue(); } if (yCamera >= yMax) { yCamera =
		 * Double.valueOf(yMax).floatValue(); //yCamera = new
		 * Float(yMax).floatValue(); } if (yCamera <= yMin) { yCamera =
		 * Double.valueOf(yMin).floatValue(); //yCamera = new
		 * Float(yMin).floatValue(); }
		 * 
		 * } // followGesture xFactor add to cameraxCamera -350.0 yCamera -350.0
		 * // travelXDirection false travelYDirection true travelX false travelY
		 * // true
		 * 
		 * // travelX=false; // travelY=true; // travelYDirection=true;
		 * 
		 * // if(xCamera>=xMax){xCamera=new //
		 * Float(xMax).floatValue();travelY=true
		 * ;travelX=false;travelXDirection=false;} //
		 * if(xCamera<=xMin){xCamera=new //
		 * Float(xMin).floatValue();travelY=true
		 * ;travelX=false;travelXDirection=true;} //
		 * if(yCamera>=yMax){yCamera=new //
		 * Float(yMax).floatValue();travelX=true
		 * ;travelY=false;travelYDirection=false;} //
		 * if(yCamera<=yMin){yCamera=new //
		 * Float(yMin).floatValue();travelX=true
		 * ;travelY=false;travelYDirection=true;} // if(xCamera>=xMax &&
		 * !travelXDirection){xCamera=new //
		 * Float(xMax).floatValue();travelY=true
		 * ;travelX=false;travelXDirection=false;} // if(xCamera>=xMax &&
		 * travelXDirection){xCamera=new //
		 * Float(xMax).floatValue();travelY=true
		 * ;travelX=false;travelXDirection=true;} // // if(xCamera<=xMin &&
		 * !travelXDirection){xCamera=new //
		 * Float(xMin).floatValue();travelY=true
		 * ;travelX=false;travelXDirection=false;} // if(xCamera<=xMin &&
		 * travelXDirection){xCamera=new //
		 * Float(xMin).floatValue();travelY=true
		 * ;travelX=false;travelXDirection=true;} // // if(yCamera>=yMax &&
		 * !travelYDirection){yCamera=new //
		 * Float(yMax).floatValue();travelX=true
		 * ;travelY=false;travelYDirection=false;} // if(yCamera>=yMax &&
		 * travelYDirection){yCamera=new //
		 * Float(yMax).floatValue();travelX=true
		 * ;travelY=false;travelYDirection=true;} // // if(yCamera<=yMin &&
		 * !travelYDirection){yCamera=new //
		 * Float(yMin).floatValue();travelX=true
		 * ;travelY=false;travelYDirection=false;} // if(yCamera<=yMin &&
		 * travelYDirection){yCamera=new //
		 * Float(yMin).floatValue();travelX=true
		 * ;travelY=false;travelYDirection=true;} //
		 * 
		 * // if(xCamera>=xMax && //
		 * yCamera>=yMax){travelYDirection=false;travelXDirection=false;} //
		 * if(xCamera<=xMin && //
		 * yCamera<=yMin){travelYDirection=true;travelXDirection=true;} //
		 * if(xCamera>=xMax && //
		 * yCamera<=yMin){travelYDirection=true;travelXDirection=false;} //
		 * if(xCamera<=xMin && //
		 * yCamera>=yMax){travelYDirection=false;travelXDirection=true;}
		 * 
		 * // Log.w("BasicRender", //
		 * " followGesture case 1a xCamera "+xCamera+" yCamera "
		 * +yCamera+" travelX "+travelX+ //
		 * " travelY "+travelY+" travelXDirection "
		 * +travelXDirection+" travelYDirection "
		 * +travelYDirection+" spinCount "+spinCount);
		 * 
		 * if (travelX) { if (activate) { // Log.w("BasicRender", //
		 * " travelX xCamera "
		 * +xCamera+" yCamera "+yCamera+" travelXDirection "+travelXDirection
		 * +" travelYDirection "
		 * +travelYDirection+" travelX "+travelX+" travelY "+travelY);
		 * 
		 * if (travelXDirection) { xCamera = xCamera + camRotSpeed; } else {
		 * xCamera = xCamera - camRotSpeed; }
		 * 
		 * if (xCamera == xMax && travelXDirection) { travelY = true; travelX =
		 * false; travelXDirection = true; } else if (xCamera == xMax &&
		 * !travelXDirection) { travelY = false; travelX = true;
		 * travelXDirection = false; } else if (xCamera == xMin &&
		 * !travelXDirection) { travelY = true; travelX = false;
		 * travelXDirection = false; } else if (xCamera == xMin &&
		 * travelXDirection) { travelY = false; travelX = true; travelXDirection
		 * = true; }
		 * 
		 * } }
		 * 
		 * if (travelY) {
		 * 
		 * if (activate) {
		 * 
		 * // Log.w("BasicRender", //
		 * " travelY xCamera "+xCamera+" yCamera "+yCamera
		 * +" travelXDirection "+travelXDirection
		 * +" travelYDirection "+travelYDirection
		 * +" travelX "+travelX+" travelY "+travelY);
		 * 
		 * if (travelYDirection) { yCamera = yCamera + camRotSpeed; } else {
		 * yCamera = yCamera - camRotSpeed; }
		 * 
		 * if (yCamera == yMax && travelYDirection) { travelX = true; travelY =
		 * false; travelYDirection = true; } else if (yCamera == yMax &&
		 * !travelYDirection) { travelX = false; travelY = true;
		 * travelYDirection = false; } else if (yCamera == yMin &&
		 * !travelYDirection) { travelX = true; travelY = false;
		 * travelYDirection = false; } else if (yCamera == yMin &&
		 * travelYDirection) { travelX = false; travelY = true; travelYDirection
		 * = true; }
		 * 
		 * } } // } // }
		 */
	}

}
