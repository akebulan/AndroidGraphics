package org.me.mesh;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.me.androidgraphics.OpenGLUtility;

import android.util.Log;

public class Background1 extends Mesh {

    public boolean iterate;
    public boolean rotate = false;
    float normals[];
//    public float x = 0;
//    public float y = 0;
//    public float z = -1;

    public Background1(int sqr) {
        super();
        short[] indices = {0, 1, 2, 0, 2, 3};
        float inc = sqr / 2;
        float vertices[] = {
            -inc, -inc, 0.0f, // 0, Bottom Left
            inc, -inc, 0.0f, // 1, Bottom Right
            inc, inc, 0.0f, // 2, Top Right
            -inc, inc, 0.0f, // 3, Top Left
        };

        float[] tex = {
            1, 0,
            1, 1,
            0, 1,
            0, 0,};

        float normals[] = {
            0.0f, 0.0f, 1.0f};

//        setIndices(icoords);
        setIndices(indices);

//        setVertices(coords);
        setVertices(vertices);

        setTextures(tex);
        setNormals(normals);
    }

    public Background1(int sqr, int texc) {
        super(false);
        short[] indices = {0, 1, 2, 0, 2, 3};
        float inc = sqr / 2;
        float vertices[] = {
            -inc, -inc, 0.0f, // 0, Bottom Left
            inc, -inc, 0.0f, // 1, Bottom Right
            inc, inc, 0.0f, // 2, Top Right
            -inc, inc, 0.0f, // 3, Top Left
        };

        float[] tex = {
            texc, 0,
            texc, texc,
            0, texc,
            0, 0,};

//        float normals[] = {
//            0.0f, 0.0f, 1.0f};

        float[] normals = {0.0f, 1.0f, 0.0f, 1.0f};

//        setIndices(icoords);
        setIndices(indices);

//        setVertices(coords);
        setVertices(vertices);

        setTextures(tex);
        setNormals(normals);
    }

    int particleVertBufferIndex = 0;
    
    int  aircraftVertBufferIndex = 0;

    int  textureCoordBufferIndex = 0;

    int  indexBufferIndex = 0;
    
    int  normalBufferIndex = 0;

    public void generateHardwareBuffers(GL11 gl)
    
    {
    	int error=0;
    	 
            if (gl instanceof GL11)
 
            {
 
                GL11 gl11 = OpenGLUtility.gl11;   
                
                 int[] buffer = new int[1]; 
               //  IntBuffer intBuffer = IntBuffer.allocate(1);
                 
               //gl11.glGenBuffers(1, buffer, 0);
               Log.w("Background1", " Generate vertex buffer GLError: " + gl11.glGetError());

 
                // Allocate and fill the vertex buffer.
                // Allocate and fill the vertex buffer.
  //              aircraftVertBufferIndex = buffer[0];
  //              gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, aircraftVertBufferIndex);
             //   final int vertexSize = verticesBuffer.capacity() * 4; 
             //   gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexSize, 
             //   		verticesBuffer, GL11.GL_STATIC_DRAW);
                
 
//                gl11.glGenBuffers(1, buffer, 0);
// 
//                aircraftVertBufferIndex = buffer[0];
// 
//                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, aircraftVertBufferIndex);
// 
//                final int vertex2Size = verticesBuffer.capacity()*4;
// 
//                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertex2Size, verticesBuffer, GL11.GL_STATIC_DRAW);
// 
  
//                error = gl11.glGetError();               
// 
//                if (error != GL10.GL_NO_ERROR)
// 
//                {
// 
//                    Log.e("AirDefender", " Generate vertex buffer GLError: " + error);
// 
//                }
// 
 
 
                // Allocate and fill the texture coordinate buffer.
 
                gl11.glGenBuffers(1, buffer, 0);
 
                textureCoordBufferIndex = buffer[0];
 
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureCoordBufferIndex);
 
                final int texCoordSize = textBuffer.capacity() * 4;
 
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, texCoordSize, textBuffer, GL11.GL_STATIC_DRAW);
 
 
 
                error = gl11.glGetError();
 
                if (error != GL10.GL_NO_ERROR)
 
                {
 
                    Log.e("AirDefender", "Generate texture buffer GLError: " + error);
 
                }
 
//                // Allocate and fill the texture coordinate buffer.
//                
//                gl11.glGenBuffers(1, buffer, 0);
// 
//                normalBufferIndex = buffer[0];
// 
//                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalBufferIndex);
// 
//                final int normalSize = normalBuffer.capacity() * 4;
// 
//                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, normalSize, normalBuffer, GL11.GL_STATIC_DRAW);
//  
// 
//                error = gl11.glGetError();
// 
//                if (error != GL10.GL_NO_ERROR)
// 
//                {
// 
//                    Log.e("AirDefender", "Generate normal buffer GLError: " + error);
// 
//                }
                
                // Unbind the array buffer.
 
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
 
 
 
                // Allocate and fill the index buffer.
 
                gl11.glGenBuffers(1, buffer, 0);
 
                indexBufferIndex = buffer[0];
 
                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBufferIndex);
 
                // A char is 2 bytes.
 
                final int indexSize = indicesBuffer.capacity() * 2;
 
                gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexSize, indicesBuffer, GL11.GL_STATIC_DRAW);
 
 
 
                error = gl11.glGetError();
 
                if (error != GL10.GL_NO_ERROR)
 
                {
 
                    Log.e("AirDefender", "Generate index buffer GLError: " + error);
 
                }
 
                // Unbind the element array buffer.
 
                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
 
              //  Log.e("AirDefender", "sucess: " + error);

            }
 
        
    }

    
    public void drawVBO(GL10 gl)
    {
        GL11 gl11 = (GL11) gl;
        
        // draw using hardware buffers

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, aircraftVertBufferIndex);
        gl11.glVertexPointer(3, GL10.GL_FIXED, 0, 0);

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureCoordBufferIndex);
        gl11.glTexCoordPointer(2, GL11.GL_FIXED, 0, 0);

//        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalBufferIndex);      
//        gl11.glNormalPointer(GL11.GL_FIXED, 0, 0);

        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBufferIndex);
        gl11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_SHORT, 0);

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        
    }

    
    
     @Override
    public void draw(GL10 gl) {

//        gl.glEnable(GL10.GL_TEXTURE_2D);
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
////        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
//
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);
////        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
//        gl.glPushMatrix();
//
//        gl.glTranslatef(x, y, z);
//
//        // Point out the where the color buffer is.
//        gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
////        drawVBO(gl);
//        gl.glPopMatrix();
//
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glDisable(gl.GL_TEXTURE_2D);

    }

//    @Override
//    public void pick(GL10 gl) {
//        super.pick(gl);
//
//        gl.glDisable(gl.GL_TEXTURE_2D);
//
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
//        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
//
////        gl.glLoadIdentity();
//        gl.glPushMatrix();
//
//        gl.glTranslatef(x, y, z);
//        gl.glScalef(scale, scale, scale);
//        gl.glRotatef(rx, 1, 0, 0);
//        gl.glRotatef(ry, 0, 1, 0);
//        gl.glRotatef(rz, 0, 0, 1);
//
//        gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
//
//        gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
//        gl.glPopMatrix();
//        gl.glEnable(GL10.GL_TEXTURE_2D);
//
//    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public float[] getNormals() {
        return normals;
    }

    
}
