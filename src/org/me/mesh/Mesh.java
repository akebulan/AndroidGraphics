package org.me.mesh;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.me.androidgraphics.DrawVO;

import android.util.Log;

public class Mesh {
    // Our vertex buffer.

    protected FloatBuffer verticesBuffer = null;
    // Our index buffer.
    protected ShortBuffer indicesBuffer = null;
    protected FloatBuffer textBuffer;
    protected FloatBuffer normalBuffer;
    // The number of indices.
    protected int numOfIndices = -1;
    // Flat Color
    protected float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    // Smooth Colors
    protected FloatBuffer colorBuffer = null;
    // Translate params.
    public float x = 0;
    public float y = 0;
    public float z = -1;
    // Rotate params.
    public float rx = 0;
    public float ry = 0;
    public float rz = 0;
    protected float mAngleX = 0;
    protected int textureId;
    public String name = "";
    public float scale = 1;
    protected static float red = 0.0f;
    protected static float green = 0.0f;
    protected static float blue = 0.0f;
    protected boolean pickable = true;
    public float angle = 0;
    protected DrawVO drawVO;

    public Mesh() {
        if (pickable) {
            red = red + 0.10f;
            if (red > 1.0f) {
                red = 0;
                green = green + 0.10f;
                if (green > 1.0f) {
                    green = 0;
                    blue = blue + 0.10f;
                }
            }

            BigDecimal bdRed = new BigDecimal(red);
            bdRed = bdRed.setScale(1, BigDecimal.ROUND_HALF_UP);

            BigDecimal bdGreen = new BigDecimal(green);
            bdGreen = bdGreen.setScale(1, BigDecimal.ROUND_HALF_UP);

            BigDecimal bdBlue = new BigDecimal(blue);
            bdBlue = bdBlue.setScale(1, BigDecimal.ROUND_HALF_UP);

//        BigDecimal bdAlpha = new BigDecimal(alpha / divideby);
//        bdAlpha = bdAlpha.setScale(1, BigDecimal.ROUND_HALF_UP);

            Log.w("Mesh", " r=" + bdRed.floatValue() + " g=" + bdGreen.floatValue() + " b=" + bdBlue.floatValue());

            rgba = new float[]{bdRed.floatValue(), bdGreen.floatValue(), bdBlue.floatValue(), 1.0f};

        }

    }

        public Mesh(boolean pickable) {
        if (pickable) {
            red = red + 0.10f;
            if (red > 1.0f) {
                red = 0;
                green = green + 0.10f;
                if (green > 1.0f) {
                    green = 0;
                    blue = blue + 0.10f;
                }
            }

            BigDecimal bdRed = new BigDecimal(red);
            bdRed = bdRed.setScale(1, BigDecimal.ROUND_HALF_UP);

            BigDecimal bdGreen = new BigDecimal(green);
            bdGreen = bdGreen.setScale(1, BigDecimal.ROUND_HALF_UP);

            BigDecimal bdBlue = new BigDecimal(blue);
            bdBlue = bdBlue.setScale(1, BigDecimal.ROUND_HALF_UP);

//        BigDecimal bdAlpha = new BigDecimal(alpha / divideby);
//        bdAlpha = bdAlpha.setScale(1, BigDecimal.ROUND_HALF_UP);

            Log.w("Mesh", " r=" + bdRed.floatValue() + " g=" + bdGreen.floatValue() + " b=" + bdBlue.floatValue());

            rgba = new float[]{bdRed.floatValue(), bdGreen.floatValue(), bdBlue.floatValue(), 1.0f};

        }

    }

    public void pick(GL10 gl) {
        
    }

    public void draw(GL10 gl) {
//        // Counter-clockwise winding.
//        gl.glFrontFace(GL10.GL_CCW);
//        // Enable face culling.
//        gl.glEnable(GL10.GL_CULL_FACE);
//        // What faces to remove with the face culling.
//        gl.glCullFace(GL10.GL_BACK);
//        // Enabled the vertices buffer for writing and to be used during
//        // rendering.
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
//        // Specifies the location and data format of an array of vertex
//        // coordinates to use when rendering.
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
//        // Set flat color
//        gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
//        // Smooth color
//        if (colorBuffer != null) {
//            // Enable the color array buffer to be used during rendering.
//            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
//        }
//
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, 1);
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);
//        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
//        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
//        gl.glEnable(gl.GL_BLEND);
//        if (mAngleX > 360) {
//            mAngleX = 0;
//        }
//        gl.glTranslatef(x, y, z);
//
//        gl.glPushMatrix();
//        gl.glScalef(0.25f, 0.25f, 0.25f);
////        gl.glRotatef(mAngleX++, 1, 0, 0);
////        gl.glRotatef(mAngleX++, 0, 1, 0);
////        gl.glRotatef(mAngleX++, 0, 0, 1);
//        gl.glRotatef(rx, 1, 0, 0);
//        gl.glRotatef(ry, 0, 1, 0);
//        gl.glRotatef(rz, 0, 0, 1);
//
//        // Point out the where the color buffer is.
//        gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
//                GL10.GL_UNSIGNED_SHORT, indicesBuffer);
//        gl.glPopMatrix();
//
//        // Disable the vertices buffer.
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
//        // Disable face culling.
//        gl.glDisable(GL10.GL_CULL_FACE);
//        gl.glDisable(GL10.GL_NORMAL_ARRAY);
    }

    protected void setVertices(float[] vertices) {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
    }

    protected void setTextures(float[] textCoords) {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(textCoords.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        textBuffer = vbb.asFloatBuffer();
        textBuffer.put(textCoords);
        textBuffer.position(0);
    }

    protected void setNormals(float[] normals) {
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer = nbb.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);

    }

    protected void setIndices(short[] indices) {
        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer = ibb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
        numOfIndices = indices.length;
    }

    public void setColor(float red, float green, float blue, float alpha) {
        // Setting the flat color.
        rgba[0] = red;
        rgba[1] = green;
        rgba[2] = blue;
        rgba[3] = alpha;
    }

    public void setColors(float[] colors) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }
    
    public float[] getRgba() {
        return rgba;
    }
    public int getTextureId() {
        return textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

	/**
	 * @return the verticesBuffer
	 */
	public FloatBuffer getVerticesBuffer() {
		return verticesBuffer;
	}

	/**
	 * @return the indicesBuffer
	 */
	public ShortBuffer getIndicesBuffer() {
		return indicesBuffer;
	}

	/**
	 * @return the textBuffer
	 */
	public FloatBuffer getTextBuffer() {
		return textBuffer;
	}

	/**
	 * @return the normalBuffer
	 */
	public FloatBuffer getNormalBuffer() {
		return normalBuffer;
	}

	/**
	 * @return the numOfIndices
	 */
	public int getNumOfIndices() {
		return numOfIndices;
	}

	/**
	 * @return the colorBuffer
	 */
	public FloatBuffer getColorBuffer() {
		return colorBuffer;
	}

	/**
	 * @param verticesBuffer the verticesBuffer to set
	 */
	public void setVerticesBuffer(FloatBuffer verticesBuffer) {
		this.verticesBuffer = verticesBuffer;
	}

	/**
	 * @param indicesBuffer the indicesBuffer to set
	 */
	public void setIndicesBuffer(ShortBuffer indicesBuffer) {
		this.indicesBuffer = indicesBuffer;
	}

	/**
	 * @param textBuffer the textBuffer to set
	 */
	public void setTextBuffer(FloatBuffer textBuffer) {
		this.textBuffer = textBuffer;
	}

	/**
	 * @param normalBuffer the normalBuffer to set
	 */
	public void setNormalBuffer(FloatBuffer normalBuffer) {
		this.normalBuffer = normalBuffer;
	}

	/**
	 * @param numOfIndices the numOfIndices to set
	 */
	public void setNumOfIndices(int numOfIndices) {
		this.numOfIndices = numOfIndices;
	}

	/**
	 * @param colorBuffer the colorBuffer to set
	 */
	public void setColorBuffer(FloatBuffer colorBuffer) {
		this.colorBuffer = colorBuffer;
	}

	/**
	 * @return the drawVO
	 */
	public DrawVO getDrawVO() {
		return drawVO;
	}

	/**
	 * @param drawVO the drawVO to set
	 */
	public void setDrawVO(DrawVO drawVO) {
		this.drawVO = drawVO;
	}


}
