package org.me.mesh;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import min3d.animation.AnimationObject3d;
import android.util.Log;

public class ProjectileFX extends Mesh {

	AnimationObject3d animationObject3d;
	public float animationObject3dY,animationObject3dX;
	ArrayList<Position> locations;
	int numberPoints = 0;
	public boolean iterate;
	// private double incX = 0;
	// private double incY = 0;
	// private double incZ = 0;

	private Double incX = new Double(0);
	private Double incY = new Double(0);
	private Double incZ = new Double(0);

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

	public ProjectileFX() {
		super();

	}

	public ProjectileFX(ArrayList<Position> locations) {
		super(false);
		this.locations = locations;
		Object object[] = locations.toArray();
		float vertices[] = new float[object.length * 3];
		int j = 0;
		initLocation();
		// Position position = (Position) object[0];
		// lx = new Float(position.getX()).floatValue();
		// ly = new Float(position.getY()).floatValue();
		// lz = new Float(position.getZ()).floatValue();
		//
		// position = (Position) object[object.length - 1];
		// dx = new Float(position.getX()).floatValue();
		// dy = new Float(position.getY()).floatValue();
		// dz = new Float(position.getZ()).floatValue();

		for (int i = 0; i < object.length; i++) {

			Position position = (Position) object[i];

			vertices[j++] = position.getFx();// new
												// Float(position.getX()).floatValue();
			vertices[j++] = position.getFy();// new
												// Float(position.getY()).floatValue();
			vertices[j++] = position.getFz();// new
												// Float(position.getZ()).floatValue();
		}

		setVertices(vertices);
		numberPoints = object.length;

		float[] tex = { 0, 0, 0, 1, 1, 1, 1, 0 };
		setTextures(tex);

		float normals[] = { 0.0f, 0.0f, 1.0f };
		setNormals(normals);

		// float emission[] = {0.0f, 0.4f, 0.0f, 1.0f};
		// setMaterial(emission);

		// for (int i = 0; i < vertices.length; i++) {
		//
		// System.out.println("vertices " + vertices[i]);
		// }

	}

	public void initLocation() {

		// Log.w("Path", "initLocation=");
		lifeTime = 0;
		// incX = 0;
		// incY = 0;
		// incZ = 0;

		incX = new Double(0);
		incY = new Double(0);
		incZ = new Double(0);

		Object object[] = locations.toArray();
		Position position = (Position) object[0];
		lx = position.getFx();// new Float(position.getX()).floatValue();
		ly = position.getFy();// new Float(position.getY()).floatValue();
		lz = position.getFz();// new Float(position.getZ()).floatValue();

		position = (Position) object[object.length - 1];
		dx = position.getFx();// new Float(position.getX()).floatValue();
		dy = position.getFy();// new Float(position.getY()).floatValue();
		dz = position.getFz();// new Float(position.getZ()).floatValue();
	}

	@Override
	public void pick(GL10 gl) {
		super.pick(gl);
	}

	float inc = 125;
	float incSize = 10;
	float incRot = 0;
	float incScale = 1;
	float range = 125;
	float rateOfFire = range/50;

	public void backlash()
	{
		float backlash=3;
		if(angle==180)
			animationObject3d.y=animationObject3d.y-backlash;
		else if(angle==0)
			animationObject3d.y=animationObject3d.y+backlash;
		else if(angle==90)
			animationObject3d.x=animationObject3d.x-backlash;
		else if(angle==270)
			animationObject3d.x=animationObject3d.x+backlash;	
		
	}
	
	public void trajectory()
	{
        // range of tank
		if (inc > range) {
			inc = 0;
			incSize = 1;
			incRot = 0;
			incScale = 1;
			backlash();
		}

		// optional
		incSize =incSize+0.5f;
		if(incSize>50)
		{
			incSize = 0;
		}
		
		
		// backlash reset
		if (inc > range/10) {
			animationObject3d.y =animationObject3dY;
			animationObject3d.x =animationObject3dX;
			//Log.w("PointMaker","animationObject3d.y "+animationObject3d.y );
		}
		
		// rotation
		if (inc > range/3) {
			incRot = incRot +10;
		}
		
		if(inc > range/2){
			incScale = incScale - 0.03f;

		}
		
		if(incRot>360)
		{
			incRot = 0;

		}
		
		if(incScale<0.1f)
		{
			incScale = 0.1f;

		}
		
		// rate of fire of tank
		inc = inc + rateOfFire;		
	}
	@Override
	public void draw(GL10 gl) {
		
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_FOG);
        
		//gl.glEnable(GL10.GL_BLEND);
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);

		gl.glLoadIdentity();

		gl.glPushMatrix();

		// location of tank object
		gl.glTranslatef(x, y, 25);
		// rotation of tank object
        gl.glRotatef(180+angle, 0, 0, 1);

        // effect projectile flight path
        trajectory();
		
		// round edges of point
		gl.glEnable(GL10.GL_POINT_SMOOTH);

		// yellow offset slightly from other colors
		gl.glPointSize(incSize/4);	
		gl.glTranslatef(0, inc, 0);
        gl.glRotatef(incRot, 0, 1, 0);
		gl.glScalef(incScale, incScale, incScale);
        
		gl.glColor4f(1, 1, 0, 1f);
		gl.glDrawArrays(GL10.GL_POINTS, 0, numberPoints);

		// red offset slightly from other colors
		gl.glPointSize(incSize/3);	
		gl.glTranslatef(0, 5, 0);
		gl.glColor4f(1, 0, 0, 1f);
		gl.glDrawArrays(GL10.GL_POINTS, 0, numberPoints);

		// black offset slightly from other colors
		gl.glPointSize(4);				
		gl.glTranslatef(0, 5, 0);
		gl.glColor4f(0f, 0f, 0f, 1f);
		gl.glDrawArrays(GL10.GL_POINTS, 0, numberPoints);

		gl.glPopMatrix();
		
      
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_BLEND);

	}

	public void update() {
		lifeTime = lifeTime + 5;
		if (lifeTime > maxRange) {
			initLocation();
		}

		if (lx < dx) {
			// LOCATION X IS LESS THAN DESTINATION X
			// incX = incX + rate;
			incX = Double.valueOf(incX.doubleValue() + rate);

		} else if (lx > dx) {
			// LOCATION X IS GREATER THAN DESTINATION X
			// incX = incX - rate;
			incX = Double.valueOf(incX.doubleValue() - rate);

		}

		if (ly < dy) {
			// LOCATION Y IS LESS THAN DESTINATION Y
			// incY = incY + rate;
			incY = Double.valueOf(incY.doubleValue() + rate);

		} else if (ly > dy) {
			// LOCATION Y IS GREATER THAN DESTINATION Y
			// incY = incY - rate;
			incY = Double.valueOf(incY.doubleValue() - rate);

		}

		if (lz < dz) {
			// LOCATION Z IS LESS THAN DESTINATION Z
			// incZ = incZ + rate;
			incZ = Double.valueOf(incZ.doubleValue() + rate);

		} else if (lz > dz) {
			// LOCATION Z IS GREATER THAN DESTINATION Z
			// incZ = incZ - rate;
			incZ = Double.valueOf(incZ.doubleValue() - rate);

		}

		this.x = incX.floatValue();// new Float(incX).floatValue();
		this.y = incY.floatValue();// new Float(incY).floatValue();
		this.z = incZ.floatValue();// new Float(incZ).floatValue();
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

			vertices[j++] = position.getFx();// new
												// Float(position.getX()).floatValue();
			vertices[j++] = position.getFy();// new
												// Float(position.getY()).floatValue();
			vertices[j++] = position.getFz();// new
												// Float(position.getZ()).floatValue();
		}

		setVertices(vertices);
		numberPoints = object.length;
	}

	/**
	 * @return the animationObject3d
	 */
	public AnimationObject3d getAnimationObject3d() {
		return animationObject3d;
	}

	/**
	 * @param animationObject3d the animationObject3d to set
	 */
	public void setAnimationObject3d(AnimationObject3d animationObject3d) {
		this.animationObject3d = animationObject3d;
	}
	
	
}
