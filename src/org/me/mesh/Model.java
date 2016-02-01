package org.me.mesh;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Model extends Mesh {

    boolean flip = false;

    public Model(ShortBuffer indicesBuffer, FloatBuffer verticesBuffer, FloatBuffer textBuffer, FloatBuffer normalBuffer, int numOfIndices) {
        this.indicesBuffer = indicesBuffer;
        this.verticesBuffer = verticesBuffer;
        this.textBuffer = textBuffer;
        this.normalBuffer = normalBuffer;
        this.numOfIndices = numOfIndices;

    }

    public void draw(GL10 gl) {

        // TEXTURE
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

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

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);

        if (normalBuffer != null) {
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
        }

        if (mAngleX == 45) {
//            mAngleX = 0;
            flip = true;
        } else if (mAngleX == -45) {
            flip = false;

        }

        if (flip) {
            mAngleX--;

        } else {
            mAngleX++;
        }

        gl.glLoadIdentity();

        gl.glTranslatef(x, y, z);

        gl.glPushMatrix();
        gl.glScalef(0.1f, 0.1f, 0.1f);
//        gl.glRotatef(y*100, 0, 1, 0);
        gl.glRotatef(mAngleX, 0, 1, 0);
//        gl.glRotatef(mAngleX++, 0, 0, 1);
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
