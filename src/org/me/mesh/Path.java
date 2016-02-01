package org.me.mesh;

import android.util.Log;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import org.me.androidgraphics.Position;

public class Path extends Mesh {

    ArrayList<Position> locations;
    int numberPoints = 0;
    public boolean iterate;
    private double incX = 0;
    private double incY = 0;
    private double incZ = 0;
    // LOCATIONS
    double lx = 0;
    double ly = 0;
    double lz = 0;
    // DESTINATION
    double dx = 0;
    double dy = 0;
    double dz = 0;
    protected double rate = 10;
    private int lifeTime = 0;
    protected double maxRange = 125;

    public Path() {
        super();

    }

    public Path(ArrayList<Position> locations) {
        super(false);
        this.locations = locations;
        Object object[] = locations.toArray();
        float vertices[] = new float[object.length * 3];
        int j = 0;
        initLocation();
//        Position position = (Position) object[0];
//        lx = new Float(position.getX()).floatValue();
//        ly = new Float(position.getY()).floatValue();
//        lz = new Float(position.getZ()).floatValue();
//
//        position = (Position) object[object.length - 1];
//        dx = new Float(position.getX()).floatValue();
//        dy = new Float(position.getY()).floatValue();
//        dz = new Float(position.getZ()).floatValue();

        for (int i = 0; i < object.length; i++) {

            Position position = (Position) object[i];

            vertices[j++] = new Float(position.getX()).floatValue();
            vertices[j++] = new Float(position.getY()).floatValue();
            vertices[j++] = new Float(position.getZ()).floatValue();
        }

        setVertices(vertices);
        numberPoints = object.length;

        for (int i = 0; i < vertices.length; i++) {

            System.out.println("vertices " + vertices[i]);
        }

    }

    public void initLocation() {

//        Log.w("Path", "initLocation=");
        lifeTime = 0;
        incX = 0;
        incY = 0;
        incZ = 0;

        Object object[] = locations.toArray();
        Position position = (Position) object[0];
        lx = new Float(position.getX()).floatValue();
        ly = new Float(position.getY()).floatValue();
        lz = new Float(position.getZ()).floatValue();

        position = (Position) object[object.length - 1];
        dx = new Float(position.getX()).floatValue();
        dy = new Float(position.getY()).floatValue();
        dz = new Float(position.getZ()).floatValue();
    }

    @Override
    public void pick(GL10 gl) {
        super.pick(gl);
    }

	public void drawTest(GL10 gl) {
	    gl.glFrontFace(GL10.GL_CCW);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
	    gl.glEnable(GL10.GL_TEXTURE_2D);
	   // gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, numberPoints);

	  }
	
    @Override
    public void draw(GL10 gl) {
        super.draw(gl);

//        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glDisable(gl.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);

        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glColor4f(1, 0.3f, 0, 1f);
        gl.glLineWidth(5.0f);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, numberPoints);

        gl.glPopMatrix();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnable(GL10.GL_LIGHTING);

    }

    public void update() {
        lifeTime = lifeTime + 5;
        if (lifeTime > maxRange) {
            initLocation();
        }

        if (lx < dx) {
            // LOCATION X IS LESS THAN DESTINATION X
            incX = incX + rate;
        } else if (lx > dx) {
            // LOCATION X IS GREATER THAN DESTINATION X
            incX = incX - rate;
        }

        if (ly < dy) {
            // LOCATION Y IS LESS THAN DESTINATION Y
            incY = incY + rate;
        } else if (ly > dy) {
            // LOCATION Y IS GREATER THAN DESTINATION Y
            incY = incY - rate;
        }

        if (lz < dz) {
            // LOCATION Z IS LESS THAN DESTINATION Z
            incZ = incZ + rate;
        } else if (lz > dz) {
            // LOCATION Z IS GREATER THAN DESTINATION Z
            incZ = incZ - rate;
        }

        this.x = new Float(incX).floatValue();
        this.y = new Float(incY).floatValue();
        this.z = new Float(incZ).floatValue();
    }

    public ArrayList<Position> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Position> locations) {
        this.locations = locations;
        Object object[] = locations.toArray();
        float vertices[] = new float[object.length * 3];
        int j = 0;
        initLocation();

        for (int i = 0; i < object.length; i++) {

            Position position = (Position) object[i];

            vertices[j++] = new Float(position.getX()).floatValue();
            vertices[j++] = new Float(position.getY()).floatValue();
            vertices[j++] = new Float(position.getZ()).floatValue();
        }

        setVertices(vertices);
        numberPoints = object.length;
    }
}
