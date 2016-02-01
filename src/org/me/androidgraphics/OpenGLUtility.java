/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.androidgraphics;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

import min3d.Shared;
import min3d.animation.AnimationObject3d;
import min3d.core.Object3d;
import min3d.core.Object3dContainer;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.parser.Parser1;

import org.apache.commons.math.util.FastMath;
import org.me.mesh.Group;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;
import android.util.Log;


//import org.me.androidtowerdefense.R;

/**
 * 
 * @author akebulan
 */
public class OpenGLUtility {

	private static Context context;
	public static GL10 gl;
	public static GL11 gl11;

	private static int framebuffer;
	private static Group root;
	
	public static float cameraX = 500;
	public static float cameraY = 500;
	public static float cameraZ = 500;

	public OpenGLUtility() {
	}

	public OpenGLUtility(Context _context, GL10 _gl, Group _root) {
		context = _context;
		gl = _gl;
		root = _root;
	}

	final static int CHAR_SIZE = 2;

	public static void createBufferObjects(FloatBuffer verticesBuffer,
			ShortBuffer indicesBuffer, FloatBuffer textBuffer,
			FloatBuffer normalBuffer, int mVertexBufferObjectId,
			int mElementBufferObjectId) {
		// checkGLError(gl);
		// Generate a the vertex and element buffer IDs
		int[] vboIds = new int[2];
		GL11 gl11 = (GL11) gl;
		gl11.glGenBuffers(2, vboIds, 0);

		mVertexBufferObjectId = vboIds[0];
		mElementBufferObjectId = vboIds[1];

		// Upload the vertex data
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertexBufferObjectId);
		verticesBuffer.position(0);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, verticesBuffer.capacity(),
				verticesBuffer, GL11.GL_STATIC_DRAW);

		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mElementBufferObjectId);
		indicesBuffer.position(0);
		gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER,
				indicesBuffer.capacity() * CHAR_SIZE, indicesBuffer,
				GL11.GL_STATIC_DRAW);

		// We don't need the in-memory data any more
		verticesBuffer = null;
		indicesBuffer = null;
		// checkGLError(gl);
	}

	public static DrawVO createVBO(FloatBuffer verticesBuffer,
			ShortBuffer indicesBuffer, FloatBuffer textBuffer,
			FloatBuffer normalBuffer,int numOfIndices) {

		int[] buffer = new int[1];
		gl11.glGenBuffers(1, buffer, 0);
		int mVertexBufferObjectId = buffer[0];
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertexBufferObjectId);
		final int vertexSize = verticesBuffer.capacity() * 4;
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexSize, verticesBuffer,
				GL11.GL_STATIC_DRAW);
		Log.w("OpenGLUtility",
				" Generate vertex buffer GLError: " + gl11.glGetError());
		Log.w("OpenGLUtility",
		" createVBO mVertexBufferObjectId: " + mVertexBufferObjectId);
		
		// Allocate and fill the texture coordinate buffer.

		gl11.glGenBuffers(1, buffer, 0);
		int textureCoordBufferIndex = buffer[0];
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureCoordBufferIndex);
		final int texCoordSize = textBuffer.capacity() * 4;
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, texCoordSize, textBuffer,
				GL11.GL_STATIC_DRAW);
		Log.w("OpenGLUtility",
				" Generate texture buffer GLError: " + gl11.glGetError());
		Log.w("OpenGLUtility",
		" createVBO textureCoordBufferIndex: " + textureCoordBufferIndex);
		
		gl11.glGenBuffers(1, buffer, 0);
		int normalBufferIndex = buffer[0];
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalBufferIndex);
		final int normalSize = normalBuffer.capacity() * 4;
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, normalSize, normalBuffer,
				GL11.GL_STATIC_DRAW);
		Log.w("OpenGLUtility",
				" Generate normal buffer GLError: " + gl11.glGetError());
		Log.w("OpenGLUtility",
		" createVBO normalBufferIndex: " + normalBufferIndex);
		
		// Unbind the array buffer.
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

		gl11.glGenBuffers(1, buffer, 0);
		int indexBufferIndex = buffer[0];
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBufferIndex);
		final int indexSize = indicesBuffer.capacity() * 2;
		gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexSize,
				indicesBuffer, GL11.GL_STATIC_DRAW);		
		Log.w("OpenGLUtility",
				" Generate Index buffer GLError: " + gl11.glGetError());
		Log.w("OpenGLUtility",
		" createVBO indexBufferIndex: " + indexBufferIndex);
		
		// Unbind the element array buffer.
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		verticesBuffer = null;
		indicesBuffer = null;
		textBuffer=null;
		normalBuffer=null;
		
		return new DrawVO(mVertexBufferObjectId, textureCoordBufferIndex,
				indexBufferIndex, normalBufferIndex,numOfIndices);
	}
    public static void drawTestVBO(DrawVO drawVO ,int textureId)
    {
    		
    	gl11.glEnable(GL11.GL_TEXTURE_2D);
    	gl11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    	gl11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        gl11.glEnableClientState(GL10.GL_NORMAL_ARRAY);

    	gl11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    	gl11.glPushMatrix();

    	gl11.glTranslatef(drawVO.x, drawVO.y, drawVO.z);
        gl.glRotatef(drawVO.angle, 0, 0, 1);

        // draw using hardware buffers

//		Log.w("OpenGLUtility",
//		" drawTestVBO mVertexBufferObjectId: " + mVertexBufferObjectId);
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.mVertexBufferObjectId);
        gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

//		Log.w("OpenGLUtility",
//		" drawTestVBO textureCoordBufferIndex: " + textureCoordBufferIndex);
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.textureCoordBufferIndex);
        gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.normalBufferIndex);      
        gl11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

//		Log.w("OpenGLUtility",
//		" drawTestVBO indexBufferIndex: " + indexBufferIndex);
        
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, drawVO.indexBufferIndex);
        gl11.glDrawElements(GL11.GL_TRIANGLES, drawVO.numOfIndices, GL11.GL_UNSIGNED_SHORT, 0);

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        gl11.glPopMatrix();

        gl11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        gl11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);        
        gl11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        gl11.glDisable(GL11.GL_TEXTURE_2D);
        
//		Log.w("OpenGLUtility",
//				" drawTestVBO GLError: " + gl11.glGetError());
    }

	
    public static void drawTestVBO2(DrawVO drawVO ,int textureId)
    {
    		
    	gl11.glEnable(GL11.GL_TEXTURE_2D);
    	gl11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    	gl11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        gl11.glEnableClientState(GL10.GL_NORMAL_ARRAY);

    	gl11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    	gl11.glPushMatrix();

    	gl11.glTranslatef(drawVO.x, drawVO.y, drawVO.z);
    	
		OpenGLUtility.billboard(drawVO.x,drawVO.y, drawVO.z);
		
       // gl.glRotatef(drawVO.angle, 0, 0, 1);
    //	gl11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

        gl.glColor4f(1, 1, 1, 1);

        // draw using hardware buffers

//		Log.w("OpenGLUtility",
//		" drawTestVBO mVertexBufferObjectId: " + mVertexBufferObjectId);
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.mVertexBufferObjectId);
        gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

//		Log.w("OpenGLUtility",
//		" drawTestVBO textureCoordBufferIndex: " + textureCoordBufferIndex);
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.textureCoordBufferIndex);
        gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.normalBufferIndex);      
        gl11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

//		Log.w("OpenGLUtility",
//		" drawTestVBO indexBufferIndex: " + indexBufferIndex);
        
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, drawVO.indexBufferIndex);
        gl11.glDrawElements(GL11.GL_TRIANGLES, drawVO.numOfIndices, GL11.GL_UNSIGNED_SHORT, 0);

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        
     //   gl11.glPopMatrix();

        gl11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        gl11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);        
        gl11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        gl11.glDisable(GL11.GL_TEXTURE_2D);
        
//		Log.w("OpenGLUtility",
//				" drawTestVBO GLError: " + gl11.glGetError());
    }

	
    public static void updateVBO(DrawVO drawVO,FloatBuffer verticesBuffer,
			ShortBuffer indicesBuffer, FloatBuffer textBuffer,
			FloatBuffer normalBuffer,int numOfIndices ) {

		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.mVertexBufferObjectId);
		final int vertexSize = verticesBuffer.capacity() * 4;
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexSize, verticesBuffer,
				GL11.GL_STATIC_DRAW);
//		Log.w("OpenGLUtility",
//				" Generate vertex buffer GLError: " + gl11.glGetError());
//		Log.w("OpenGLUtility",
//		" updateVBO mVertexBufferObjectId: " + drawVO.mVertexBufferObjectId);
		
		// Allocate and fill the texture coordinate buffer.

		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.textureCoordBufferIndex);
		final int texCoordSize = textBuffer.capacity() * 4;
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, texCoordSize, textBuffer,
				GL11.GL_STATIC_DRAW);
//		Log.w("OpenGLUtility",
//				" Generate texture buffer GLError: " + gl11.glGetError());
//		Log.w("OpenGLUtility",
//		" updateVBO textureCoordBufferIndex: " + drawVO.textureCoordBufferIndex);
		
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, drawVO.normalBufferIndex);
		final int normalSize = normalBuffer.capacity() * 4;
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, normalSize, normalBuffer,
				GL11.GL_STATIC_DRAW);
//		Log.w("OpenGLUtility",
//				" Generate normal buffer GLError: " + gl11.glGetError());
//		Log.w("OpenGLUtility",
//		" updateVBO normalBufferIndex: " + drawVO.normalBufferIndex);
		
		// Unbind the array buffer.
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, drawVO.indexBufferIndex);
		final int indexSize = indicesBuffer.capacity() * 2;
		gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexSize,
				indicesBuffer, GL11.GL_STATIC_DRAW);		
//		Log.w("OpenGLUtility",
//				" Generate Index buffer GLError: " + gl11.glGetError());
//		Log.w("OpenGLUtility",
//		" updateVBO indexBufferIndex: " + drawVO.indexBufferIndex);
		
		// Unbind the element array buffer.
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		verticesBuffer = null;
		indicesBuffer = null;
		textBuffer=null;
		normalBuffer=null;

	}


	public static Object3d loadOBJ(String model) {
		IParser parser = Parser.createParser(Parser.Type.OBJ,
				context.getResources(), model, true);

		parser.parse();

		Object3dContainer objModelContainer = parser.getParsedObject();
		Object3d object3d = objModelContainer.getChildAt(0);

		return object3d;
	}

	public static AnimationObject3d loadMD2(String model) {
		IParser parser = Parser1.createParser(Parser1.Type.MD2, Shared
				.context().getResources(), model, false);
		parser.parse();

		AnimationObject3d object3d = parser.getParsedAnimationObject();
		object3d.setFps(100);
		// object3d.play();
		// object3d.stop();
		return object3d;

	}

	public static int loadTexture(int resource) {
		// In which ID will we be storing this texture?
		int id = newTextureID(gl);
		// We need to flip the textures vertically:
		Matrix flip = new Matrix();
		flip.postScale(1f, -1f);
		// This will tell the BitmapFactory to not scale based on the device's
		// pixel density:
		// (Thanks to Matthew Marshall for this bit)
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		// Load up, and flip the texture:
		Bitmap temp = BitmapFactory.decodeResource(context.getResources(),
				resource, opts);
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
		return id;
	}

	public static int loadGLTexture(int resource) {
		// Generate one texture pointer...
		// int[] textures = new int[1];
		// gl.glGenTextures(1, textures, 0);
		// mTextureId = textures[0];

		// In which ID will we be storing this texture?
		int mTextureId = newTextureID(gl);
		// We need to flip the textures vertically:
		Matrix flip = new Matrix();
		flip.postScale(1f, -1f);
		// This will tell the BitmapFactory to not scale based on the device's
		// pixel density:
		// (Thanks to Matthew Marshall for this bit)

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		// Load up, and flip the texture:
		Bitmap temp = BitmapFactory.decodeResource(context.getResources(),
				resource, opts);
		Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
				temp.getHeight(), flip, true);
		temp.recycle();

		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_NEAREST);

		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

		return mTextureId;
	}

	private static int newTextureID(GL10 gl) {
		int[] temp = new int[1];
		gl.glGenTextures(1, temp, 0);
		return temp[0];
	}

	public static float[] pickColor(int x, int y, int width, int height) {

		GL11ExtensionPack gl11ep = (GL11ExtensionPack) gl;
		gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
				framebuffer);

		int opglY = (int) (height - y);
		gl.glDisable(gl.GL_TEXTURE_2D);
		gl.glDisable(gl.GL_LIGHTING);
		gl.glDisable(GL10.GL_STENCIL_TEST);
		gl.glDisable(GL10.GL_BLEND);

		root.pick(gl);

		ByteBuffer bb = ByteBuffer.allocateDirect(4);
		bb.order(ByteOrder.nativeOrder());
		bb.position(0);

		gl.glReadPixels(x, opglY, 1, 1, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, bb);

		double divideby = 255;
		double addTo = 256;
		double red = bb.get();
		double green = bb.get();
		double blue = bb.get();
		double alpha = bb.get();

		if (red < 0) {
			red = red + addTo;
		}

		if (green < 0) {
			green = green + addTo;
		}

		if (blue < 0) {
			blue = blue + addTo;
		}

		if (alpha < 0) {
			alpha = alpha + addTo;
		}

		BigDecimal bdRed = new BigDecimal(red / divideby);
		bdRed = bdRed.setScale(1, BigDecimal.ROUND_HALF_UP);

		BigDecimal bdGreen = new BigDecimal(green / divideby);
		bdGreen = bdGreen.setScale(1, BigDecimal.ROUND_HALF_UP);

		BigDecimal bdBlue = new BigDecimal(blue / divideby);
		bdBlue = bdBlue.setScale(1, BigDecimal.ROUND_HALF_UP);

		BigDecimal bdAlpha = new BigDecimal(alpha / divideby);
		bdAlpha = bdAlpha.setScale(1, BigDecimal.ROUND_HALF_UP);

		// Log.w("pickColor", "onTouchEvent: X=" + x + " Y=" + y + " W=" + width
		// + " H=" + height);
		// Log.w("pickColor", "color is R=" + bdRed.floatValue() + " G=" +
		// bdGreen.floatValue() + " B=" + bdBlue.floatValue() + " A=" +
		// bdAlpha.floatValue());
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_LIGHTING);

		return new float[] { bdRed.floatValue(), bdGreen.floatValue(),
				bdBlue.floatValue(), bdAlpha.floatValue() };
	}

	// public static double getAngle(Position p1, Position p2) {
	// if (p1 != null & p2 != null) {
	// double angle = Math.toDegrees(Math.atan2(p2.getY() - p1.getY(), p2.getX()
	// - p1.getX()));
	//
	// if (angle < 0) {
	// angle = 360 + angle;
	// }
	//
	// // Log.w("Defender", " from " + p1.toString() + " to " + p2.toString() +
	// " angle " + angle);
	// return angle;
	// }
	//
	// return 0;
	// }
	public static int getFramebuffer() {
		return framebuffer;
	}

	public static void setFramebuffer(int framebuffer) {
		OpenGLUtility.framebuffer = framebuffer;
	}

	/**
	 * Round a double value to a specified number of decimal places.
	 * 
	 * @param val
	 *            the value to be rounded.
	 * @param places
	 *            the number of decimal places to round to.
	 * @return val rounded to places decimal places.
	 */
	public static double round(double val, int places) {
		long factor = (long) Math.pow(10, places);

		// Shift the decimal the correct number of places
		// to the right.
		val = val * factor;

		// Round to the nearest integer.
		long tmp = Math.round(val);

		// Shift the decimal the correct number of places
		// back to the left.
		return (double) tmp / factor;
	}

	/**
	 * Round a float value to a specified number of decimal places.
	 * 
	 * @param val
	 *            the value to be rounded.
	 * @param places
	 *            the number of decimal places to round to.
	 * @return val rounded to places decimal places.
	 */
	public static float round(float val, int places) {
		return (float) round((double) val, places);
	}

	public static float[] shadowmatrix(float[] groundplane, float[] lightpos) {
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
	
	public static void billboard(
			float objectX, float objectY, float objectZ) {
		
		MatrixTrackingGL matrixTrackingGL = new MatrixTrackingGL(gl);
		matrixTrackingGL.glPushMatrix();
		float mModelView[] = new float[16];
		matrixTrackingGL.glMatrixMode(GL10.GL_MODELVIEW);
		matrixTrackingGL.getMatrix(mModelView, 0);
		matrixTrackingGL.glPopMatrix();
		

		float right[] = new float[3];
		right[0] = mModelView[0];
		right[1] = mModelView[4];
		right[2] = mModelView[8];

		float up[] = new float[3];
		up[0] = mModelView[1];
		up[1] = mModelView[5];
		up[2] = mModelView[9];

		float upAux[] = new float[3];
		upAux[0] = 0;
		upAux[1] = 0;
		upAux[2] = 0;

		float lookAt[] = new float[3];
		lookAt[0] = mModelView[2];
		lookAt[1] = mModelView[6];
		lookAt[2] = mModelView[10];

		float objToCamProjX = cameraX - objectX;
		float objToCamProjY = 0;
		float objToCamProjZ = cameraZ - objectZ;

		// normalize both vectors to get the cosine directly afterwards
		float d = new Double(FastMath.sqrt((objToCamProjX * objToCamProjX)
				+ (objToCamProjY * objToCamProjY)
				+ (objToCamProjZ * objToCamProjZ))).floatValue();
		objToCamProjX = objToCamProjX / d;
		objToCamProjY = objToCamProjY / d;
		objToCamProjZ = objToCamProjZ / d;

		// easy fix to determine wether the angle is negative or positive
		// for positive angles upAux will be a vector pointing in the
		// positive y direction, otherwise upAux will point downwards
		// effectively reversing the rotation.

		upAux[0] = lookAt[1] * objToCamProjZ - objToCamProjY * lookAt[2];
		upAux[1] = lookAt[2] * objToCamProjX - objToCamProjZ * lookAt[0];
		upAux[2] = lookAt[0] * objToCamProjY - objToCamProjX * lookAt[1];

		// compute the angle
		float angleCosine = (lookAt[0] * objToCamProjX + lookAt[1]
				* objToCamProjY + lookAt[2] * objToCamProjZ);

		// perform the rotation. The if statement is used for stability reasons
		// if the lookAt and v vectors are too close together then |aux| could
		// be bigger than 1 due to lack of precision
		if ((angleCosine < 0.99990) && (angleCosine > -0.9999))
			gl.glRotatef(new Double(FastMath.acos(angleCosine) * 180 / 3.14)
					.floatValue(), upAux[0], upAux[1], upAux[2]);

		// The second part tilts the object so that it faces the camera
		// objToCam is the vector in world coordinates from the local origin to
		// the camera
		float objToCamX = cameraX - objectX;
		float objToCamY = cameraY - objectY;
		float objToCamZ = cameraZ - objectZ;

		// Normalize to get the cosine afterwards
		d = new Double(FastMath.sqrt((objToCamX * objToCamX)
				+ (objToCamY * objToCamY) + (objToCamZ * objToCamZ)))
				.floatValue();
		objToCamX = objToCamX / d;
		objToCamY = objToCamY / d;
		objToCamZ = objToCamZ / d;

		// Compute the angle between v and v2, i.e. compute the
		// required angle for the lookup vector
		angleCosine = (objToCamProjX * objToCamX + objToCamProjY * objToCamY + objToCamProjZ
				* objToCamZ);

		// Tilt the object. The test is done to prevent instability when
		// objToCam and objToCamProj have a very small
		// angle between them
		if ((angleCosine < 0.99990) && (angleCosine > -0.9999))
			if (objToCamY < 0)
				gl.glRotatef(
						new Double(FastMath.acos(angleCosine) * 180 / 3.14)
								.floatValue(), 1, 0, 0);
			else
				gl.glRotatef(
						new Double(FastMath.acos(angleCosine) * 180 / 3.14)
								.floatValue(), -1, 0, 0);
		

		
	}
	


}
