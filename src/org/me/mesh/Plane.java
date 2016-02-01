package org.me.mesh;

import javax.microedition.khronos.opengles.GL10;

public class Plane extends Mesh {

    public boolean iterate;
    public boolean showing = true;

    ;

    public Plane() {
        short[] icoords = {0, 1, 2, 0, 2, 3};

        float coords[] = {
            -1.0f, 1.0f, 0.0f, // 0, Top Left
            -1.0f, 0.0f, 0.0f, // 1, Bottom Left
            0.0f, 0.0f, 0.0f, // 2, Bottom Right
            0.0f, 1.0f, 0.0f, // 3, Top Right
        };

        float[] tex = {
            0, 1,
            0, 0,
            1, 0,
            1, 1,};

        float normals[] = {
            0.0f, 0.0f, 1.0f};

        setIndices(icoords);
        setVertices(coords);
        setTextures(tex);
        setNormals(normals);
    }

    public Plane(float width, float height) {
        this(width, height, 1, 1);
    }

//    public Plane(float width, float height, int widthSegments,
//            int heightSegments) {
//        float[] vertices = new float[(widthSegments + 1) * (heightSegments + 1)
//                * 3];
//        short[] indices = new short[(widthSegments + 1) * (heightSegments + 1)
//                * 6];
//
//        float xOffset = width / -2;
//        float yOffset = height / -2;
//        float xWidth = width / (widthSegments);
//        float yHeight = height / (heightSegments);
//        int currentVertex = 0;
//        int currentIndex = 0;
//        short w = (short) (widthSegments + 1);
//        for (int y = 0; y < heightSegments + 1; y++) {
//            for (int x = 0; x < widthSegments + 1; x++) {
//                vertices[currentVertex] = xOffset + x * xWidth;
//                vertices[currentVertex + 1] = yOffset + y * yHeight;
//                vertices[currentVertex + 2] = 0;
//                currentVertex += 3;
//
//                int n = y * (widthSegments + 1) + x;
//
//                if (y < heightSegments && x < widthSegments) {
//                    // Face one
//                    indices[currentIndex] = (short) n;
//                    indices[currentIndex + 1] = (short) (n + 1);
//                    indices[currentIndex + 2] = (short) (n + w);
//                    // Face two
//                    indices[currentIndex + 3] = (short) (n + 1);
//                    indices[currentIndex + 4] = (short) (n + 1 + w);
//                    indices[currentIndex + 5] = (short) (n + 1 + w - 1);
//
//                    currentIndex += 6;
//                }
//            }
//        }
//
//        setIndices(indices);
//        setVertices(vertices);
//    }


	public Plane(float width, float heigth, int texc, int teb) {
		super();
		width = width - 10;
		heigth = heigth - 10;
		int pix = 128;
		
		float texWidth = width/pix;
		float texHeigth = heigth/pix;
		
		if(texWidth<1)texWidth=1;
		if(texHeigth<1)texHeigth=1;

		short[] indicesTop = {
				// TOP
				0, 1, 2, 0, 2, 3, };
		numOfIndices = indicesTop.length;

		float incWidth = width / 2;
		float incHeigth = heigth / 2;

		float verticesTop[] = {
				// TOP
				-incWidth, -incHeigth, 0.0f, // 0, Bottom Left
				incWidth, -incHeigth, 0.0f, // 1, Bottom Right
				incWidth, incHeigth, 0.0f, // 2, Top Right
				-incWidth, incHeigth, 0.0f, // 3, Top Left
		};


		float[] texTop = {
				// TOP
				texc, 0, texc, teb, 0, teb, 0, 0, };


		float normalsTop[] = {
				// TOP
				10.0f, 10.0f, 10.0f, };




		// setIndices(icoords);
		setIndices(indicesTop);

		// setVertices(coords);
		setVertices(verticesTop);

		setTextures(texTop);
		setNormals(normalsTop);
	}

	public void drawTest(GL10 gl) {
	    gl.glFrontFace(GL10.GL_CCW);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
	    gl.glEnable(GL10.GL_TEXTURE_2D);
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);
	    if(textureId!=0)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

	    gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, numOfIndices,
	        GL10.GL_UNSIGNED_SHORT, indicesBuffer);
	  }
    
    public void draw(GL10 gl) {

        if (iterate) {
            y = y + 0.01f;
            if (y > 1) {
                // skip drawing
                showing = false;
            }
        }

        if (showing) {

            // TEXTURE
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, 1);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
//        ((GL11Ext) gl).glDrawTexfOES(10, 10, 0, 64, 64);

            // Counter-clockwise winding.
//        gl.glFrontFace(GL10.GL_CCW);
//        // Enable face culling.
//        gl.glEnable(GL10.GL_CULL_FACE);
//        // What faces to remove with the face culling.
//        gl.glCullFace(GL10.GL_BACK);
            // Enabled the vertices buffer for writing and to be used during
            // rendering.
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            // Specifies the location and data format of an array of vertex
            // coordinates to use when rendering.
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
            // Set flat color
            gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
            // Smooth color
            if (colorBuffer != null) {
                // Enable the color array buffer to be used during rendering.
                gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
                gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
            }

//        gl.glBindTexture(GL10.GL_TEXTURE_2D, 1);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
            gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(gl.GL_BLEND);
            gl.glDisable(GL10.GL_DEPTH_TEST);

//        if (x > 1) {
//            x = 1;
//        }
//        if (x < 0) {
//            x = 0;
//        }
//
//        if (y > 1) {
//            y = 1;
//        }
//        if (y < 0) {
//            y = 0;
//        }


            gl.glLoadIdentity();

            gl.glTranslatef(x, y, z);

            gl.glPushMatrix();
            gl.glScalef(0.12f, 0.12f, 0.12f);
//        gl.glRotatef(mAngleX++, 1, 0, 0);
//        gl.glRotatef(mAngleX++, 0, 1, 0);
//        gl.glRotatef(mAngleX++, 0, 0, 1);
            gl.glRotatef(rx, 1, 0, 0);
            gl.glRotatef(ry, 0, 1, 0);
            gl.glRotatef(rz, 0, 0, 1);

            // Point out the where the color buffer is.
            gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
                    GL10.GL_UNSIGNED_SHORT, indicesBuffer);
            gl.glPopMatrix();

            // Disable the vertices buffer.
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
            // Disable face culling.
            gl.glDisable(GL10.GL_CULL_FACE);
            gl.glDisable(GL10.GL_NORMAL_ARRAY);
            gl.glDisable(gl.GL_BLEND);
//        gl.glDeleteTextures(textureId, null);
        }
    }
}
