package org.me.mesh;

import javax.microedition.khronos.opengles.GL10;

public class Background extends Mesh {

    public boolean iterate;

    public Background() {
        short[] icoords = {0, 1, 2, 0, 2, 3};

        float coords[] = {
            -1.0f, 1.0f, 0.0f, // 0, Top Left
            -1.0f, 0.0f, 0.0f, // 1, Bottom Left
            0.0f, 0.0f, 0.0f, // 2, Bottom Right
            0.0f, 1.0f, 0.0f, // 3, Top Right
        };

        float[] tex = {
            1, 0,
            1, 1,
            0, 1,
            0, 0,};

        float normals[] = {
            0.0f, 0.0f, 1.0f};

        setIndices(icoords);
        setVertices(coords);
        setTextures(tex);
        setNormals(normals);
    }

    public Background(float width, float height) {
        this(width, height, 1, 1);
    }

    public Background(float width, float height, int widthSegments,
            int heightSegments) {
        float[] vertices = new float[(widthSegments + 1) * (heightSegments + 1)
                * 3];
        short[] indices = new short[(widthSegments + 1) * (heightSegments + 1)
                * 6];

        float xOffset = width / -2;
        float yOffset = height / -2;
        float xWidth = width / (widthSegments);
        float yHeight = height / (heightSegments);
        int currentVertex = 0;
        int currentIndex = 0;
        short w = (short) (widthSegments + 1);
        for (int y = 0; y < heightSegments + 1; y++) {
            for (int x = 0; x < widthSegments + 1; x++) {
                vertices[currentVertex] = xOffset + x * xWidth;
                vertices[currentVertex + 1] = yOffset + y * yHeight;
                vertices[currentVertex + 2] = 0;
                currentVertex += 3;

                int n = y * (widthSegments + 1) + x;

                if (y < heightSegments && x < widthSegments) {
                    // Face one
                    indices[currentIndex] = (short) n;
                    indices[currentIndex + 1] = (short) (n + 1);
                    indices[currentIndex + 2] = (short) (n + w);
                    // Face two
                    indices[currentIndex + 3] = (short) (n + 1);
                    indices[currentIndex + 4] = (short) (n + 1 + w);
                    indices[currentIndex + 5] = (short) (n + 1 + w - 1);

                    currentIndex += 6;
                }
            }
        }

        setIndices(indices);
        setVertices(vertices);
    }

    public void draw(GL10 gl) {
        // TEXTURE
//        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glDisable(gl.GL_TEXTURE_2D);
//        gl.glDisable(gl.GL_FOG);
//        gl.glDisable(gl.GL_LIGHTING);
//        gl.glDisable(gl.GL_DITHER);
//        gl.glDisable(gl.GL_BLEND);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
        // Set flat color
//        gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
        gl.glColor4f(1, 0, 0, 1);
        setColors(rgba);
        // Smooth color
//        if (colorBuffer != null) {
        // Enable the color array buffer to be used during rendering.
//            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
//        }

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
//        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
//        gl.glEnable(gl.GL_BLEND);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glLoadIdentity();
        gl.glTranslatef(x, y, z);
        gl.glPushMatrix();
        gl.glScalef(1.5f, 1.1f, 1f);
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
