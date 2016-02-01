package org.me.mesh;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cube extends Mesh {

    public Cube(ShortBuffer indicesBuffer, FloatBuffer verticesBuffer, FloatBuffer textBuffer, FloatBuffer normalBuffer, int numOfIndices) {
        this.indicesBuffer = indicesBuffer;
        this.verticesBuffer = verticesBuffer;
        this.textBuffer = textBuffer;
        this.normalBuffer = normalBuffer;
        this.numOfIndices = numOfIndices;

    }

//    public Cube(short indices[], float vertices[], float texCoords[], float normals[]) {
//        setIndices(indices);
//        setVertices(vertices);
//        setTextures(texCoords);
////        setNormals(normals);
//    }
    public Cube(float width, float height, float depth) {
        width /= 2;
        height /= 2;
        depth /= 2;

        float vertices[] = {
            // FRONT
            -1.0f, 1.0f, 1.0f, // 0
            -1.0f, -1.0f, 1.0f, // 1
            1.0f, -1.0f, 1.0f, // 2
            1.0f, 1.0f, 1.0f, // 3

            // BACK
            -1.0f, 1.0f, -1.0f, // 4
            -1.0f, -1.0f, -1.0f, // 5
            1.0f, -1.0f, -1.0f, // 6
            1.0f, 1.0f, -1.0f, // 7

            // LEFT
            -1.0f, 1.0f, 1.0f, // 8
            -1.0f, -1.0f, 1.0f, // 9
            -1.0f, -1.0f, -1.0f, // 10
            -1.0f, 1.0f, -1.0f, // 11

            // RIGHT
            1.0f, -1.0f, 1.0f, // 12
            1.0f, -1.0f, -1.0f, // 13
            1.0f, 1.0f, -1.0f, // 14
            1.0f, 1.0f, 1.0f, // 15

            // TOP
            -1.0f, 1.0f, 1.0f, // 16
            1.0f, 1.0f, 1.0f, // 19
            1.0f, 1.0f, -1.0f, // 18
            -1.0f, 1.0f, -1.0f, // 17

            // BOTTOM
            -1.0f, -1.0f, 1.0f, // 20
            -1.0f, -1.0f, -1.0f, // 21
            1.0f, -1.0f, -1.0f, // 22
            1.0f, -1.0f, 1.0f, // 23
        };

        short indices[] = {
            //FRONT
            0, 1, 2,
            0, 2, 3,
            //BACK
            4, 7, 6,
            4, 6, 5,
            // LEFT
            11, 10, 9,
            11, 9, 8,
            // RIGHT
            12, 13, 14,
            12, 14, 15,
            // TOP
            16, 17, 18,
            16, 18, 19,
            //BOTTOM
            20, 21, 22,
            20, 22, 23,};

        float normals[] = {
            //FRONT
            0.0f, 0.0f, 1.0f,
            //BACK
            0.0f, 0.0f, -1.0f,
            // LEFT
            -1.0f, 0.0f, 0.0f,
            // RIGHT
            1.0f, 0.0f, 0.0f,
            // TOP
            0.0f, 1.0f, 0.0f,
            //BOTTOM
            0.0f, -1.0f, 0.0f,};

        float texCoords[] = {
            // FRONT
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            // BACK
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            // LEFT
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            // RIGHT
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            // TOP
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            // BOTTOM
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,};

        setIndices(indices);
        setVertices(vertices);
        setTextures(texCoords);
        setNormals(normals);
    }

    public void draw(GL10 gl) {

        // TEXTURE
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
//        ((GL11Ext) gl).glDrawTexfOES(10, 10, 0, 64, 64);

        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
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

        if (normalBuffer != null) {
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
        }

        if (mAngleX > 360) {
            mAngleX = 0;
        }
        gl.glLoadIdentity();

        gl.glTranslatef(x, y, z);

        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.05f, 0.05f);
//        gl.glRotatef(y*100, 0, 1, 0);
        gl.glRotatef(mAngleX++, 0, 1, 0);
        gl.glRotatef(mAngleX++, 0, 0, 1);
//        gl.glRotatef(rx, 1, 0, 0);
//        gl.glRotatef(ry, 0, 1, 0);
//        gl.glRotatef(rz, 0, 0, 1);

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

    }
}
