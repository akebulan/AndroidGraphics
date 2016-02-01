/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.mesh;

import java.util.Arrays;

/**
 *
 * @author akebulan
 */
public class ModelData3D {

    public float[] vertices;
    public float[] tex;
    public float normals[];
    public short[] indices;
    public int vertexCount;

    public void print() {
        System.out.println("vertices=" + Arrays.toString(vertices));
        System.out.println("tex=" + Arrays.toString(tex));
        System.out.println("normals=" + Arrays.toString(normals));
        System.out.println("indices=" + Arrays.toString(indices));
    }
}
