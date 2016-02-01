package org.me.mesh;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import org.me.androidgraphics.BasicRender;
import org.me.androidgraphics.OpenGLUtility;

public class GameBoardPlane extends Mesh {

    private ArrayList<Mesh> allChildren = new ArrayList<Mesh>(1);
    private Iterator iter;
    public Background1 displyPlane;
    Background1 displyPlaneLevel1;
    Group root;

    public GameBoardPlane() {
        super();
    }

    public GameBoardPlane(int sqrs, int texture, int picSqr, Group root) {
        this.root = root;
        createPickSquares(sqrs);
        createDisplaySquare(texture, picSqr);
    }

    private void createPickSquares(int sqrs) {
//        Background1 plane;
        for (int xx = 0; xx < 14; xx++) {

            for (int yy = 1; yy < 14; yy++) {
                Background1 plane = new Background1(sqrs);
                plane.setName(BasicRender.PREFIXSQUARE + BasicRender.getUniqueID());
//                plane.generateHardwareBuffers(OpenGLUtility.gl);

//                plane.z = 0f;
//                plane.x = xx * 50;
//                plane.y = yy * 50;

                plane.setX(xx * 50f);
                plane.setY(yy * 50f);
                plane.setZ(0);
                allChildren.add(plane);
                root.add(plane);
            }
        }
    }

    private void createDisplaySquare(int texture, int picSqr) {
        displyPlane = new Background1(picSqr, 14);
        displyPlane.setName(BasicRender.PREFIXSQUARE + BasicRender.getUniqueID());
//        displyPlane.z = 0f;
//        displyPlane.x = 0;
//        displyPlane.y = 0;
        displyPlane.setX(300);
        displyPlane.setY(300);
        displyPlane.setZ(0);
        displyPlane.setTextureId(texture);
//        displyPlane.generateHardwareBuffers(OpenGLUtility.gl11);
//        displyPlaneLevel1 = new Background1(picSqr, 7);
//        displyPlaneLevel1.setName(GameBoard.PREFIXSQUARE + GameBoard.getUniqueID());
//        displyPlaneLevel1.z = -500f;
//        displyPlaneLevel1.x = 0;
//        displyPlaneLevel1.y = 700;
//        displyPlaneLevel1.setTextureId(texture);
    }

    @Override
    public void pick(GL10 gl) {
        super.pick(gl);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glDisable(gl.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_LIGHTING);

        Background1 plane;
        iter = allChildren.iterator();
        while (iter.hasNext()) {
            plane = (Background1) iter.next();

            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, plane.verticesBuffer);
//            gl.glNormalPointer(GL10.GL_FLOAT, 0, plane.normalBuffer);

//            gl.glLoadIdentity();
            gl.glPushMatrix();

            gl.glTranslatef(plane.getX(), plane.getY(), plane.getZ());
            gl.glScalef(scale, scale, scale);
            gl.glRotatef(rx, 1, 0, 0);
            gl.glRotatef(ry, 0, 1, 0);
            gl.glRotatef(rz, 0, 0, 1);

            gl.glColor4f(plane.rgba[0], plane.rgba[1], plane.rgba[2], plane.rgba[3]);

            gl.glDrawElements(GL10.GL_TRIANGLES, plane.numOfIndices, GL10.GL_UNSIGNED_SHORT, plane.indicesBuffer);
            gl.glPopMatrix();

        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_TEXTURE_2D);

    }

    @Override
    public void draw(GL10 gl) {
        super.draw(gl);
        displyPlane.draw(gl);
//        gl.glEnable(GL10.GL_TEXTURE_2D);
//
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
////        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
//
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, displyPlane.textureId);
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, displyPlane.verticesBuffer);
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, displyPlane.textBuffer);
////        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
////        gl.glLoadIdentity();
//        gl.glPushMatrix();
//        gl.glTranslatef(displyPlane.x, displyPlane.y, displyPlane.z);
//
//        // Point out the where the color buffer is.
//        gl.glDrawElements(GL10.GL_TRIANGLES, displyPlane.numOfIndices, GL10.GL_UNSIGNED_SHORT, displyPlane.indicesBuffer);
//        gl.glPopMatrix();
//
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

//    public void drawLevel(GL10 gl, Background1 plane) {
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, plane.textureId);
//
////        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
//
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, plane.verticesBuffer);
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, plane.textBuffer);
//        gl.glNormalPointer(GL10.GL_FLOAT, 0, plane.normalBuffer);
//
//        gl.glLoadIdentity();
//        gl.glPushMatrix();
//
//        gl.glColor4f(1, 1, 1, 1);
//
//        gl.glTranslatef(plane.x, plane.y, plane.z);
//        gl.glScalef(scale, scale, scale);
//        gl.glRotatef(rx, 1, 0, 0);
//        gl.glRotatef(ry, 0, 1, 0);
//        gl.glRotatef(rz, 0, 0, 1);
//
//        // Point out the where the color buffer is.
//        gl.glDrawElements(GL10.GL_TRIANGLES, plane.numOfIndices, GL10.GL_UNSIGNED_SHORT, plane.indicesBuffer);
//        gl.glPopMatrix();
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
//    }
    public ArrayList<Mesh> getAllChildren() {
        return allChildren;
    }

    public void setAllChildren(ArrayList<Mesh> allChildren) {
        this.allChildren = allChildren;
    }
}
