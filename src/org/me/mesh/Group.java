package org.me.mesh;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class Group extends Mesh {

    protected ArrayList<Mesh> allChildren = new ArrayList<Mesh>(1);

    @Override
    public void draw(GL10 gl) {
        Mesh child;
        for (int i = 0; i < allChildren.size(); i++) {
            child = allChildren.get(i);
            if (child != null) {
                child.draw(gl);
            }
        }
    }

    public void pick(GL10 gl) {
        Mesh child;
        for (int i = 0; i < allChildren.size(); i++) {
            child = allChildren.get(i);
            if (child != null) {
                child.pick(gl);
            }
        }
    }

//    public void processPickResults(float[] pickResults) {
//        Log.w("processPickResults", "color is R=" +  pickResults[0] + " G=" +  pickResults[1] + " B=" +  pickResults[2] + " A=" +  pickResults[3]);
//
//        Mesh mesh;
//        for (int i = 0; i < allChildren.size(); i++) {
//            mesh = allChildren.get(i);
//            if (mesh != null) {
//                if (mesh.getRgba()[0] == pickResults[0] && mesh.getRgba()[1] == pickResults[1]
//                        && mesh.getRgba()[2] == pickResults[2]) {
//
//                    Log.w("Group", "processPickResults: X=" + mesh.x + " Y=" + mesh.y);
//                    int x = Float.valueOf(mesh.x).intValue();
//                    int y = Float.valueOf(mesh.y).intValue();
//
//                }
//            }
//        }
//
//
//    }

    /**
     * @param location
     * @param object
     * @see java.util.Vector#add(int, java.lang.Object)
     */
    public void add(int location, Mesh object) {
//		children.add(location, object);
    }

    /**
     * @param object
     * @return
     * @see java.util.Vector#add(java.lang.Object)
     */
    public boolean add(Mesh object) {
        return allChildren.add(object);
    }

    /**
     *
     * @see java.util.Vector#clear()
     */
    public void clear() {
        allChildren.clear();
    }

    /**
     * @param location
     * @return
     * @see java.util.Vector#get(int)
     */
//	public Mesh get(int location) {
//		return children.get(location);
//	}
    /**
     * @param location
     * @return
     * @see java.util.Vector#remove(int)
     */
//	public Mesh remove(int location) {
//		return children.remove(location);
//	}
    /**
     * @param object
     * @return
     * @see java.util.Vector#remove(java.lang.Object)
     */
    public boolean remove(Mesh object) {
        for (int x = 0; x < allChildren.size(); x++) {
            Mesh child = allChildren.get(x);
            if (object.getName().equals(child.getName())) {
                allChildren.remove(x);
            }
        }
        return true;
    }

    public ArrayList<Mesh> getAllChildren() {
        return allChildren;
    }

    public void setAllChildren(ArrayList<Mesh> allChildren) {
        this.allChildren = allChildren;
    }
    /**
     * @return
     * @see java.util.Vector#size()
     */
//	public int size() {
//		return children.size();
//	}


}
