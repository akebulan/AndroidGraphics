package min3d.animation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.util.Log;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import min3d.core.FacesBufferedList;
import min3d.core.Object3d;
import min3d.core.TextureList;
import min3d.core.Vertices;

import org.me.androidgraphics.OpenGLUtility;
import org.me.mesh.Group;

public class AnimationObject3d extends Object3d {

    private int numFrames;
    private KeyFrame[] frames;
    private int currentFrameIndex;
    private long startTime;
    private long currentTime;
    private boolean isPlaying;
    private float interpolation;
    private float fps = 70;
    private boolean updateVertices = true;
    private String currentFrameName;
    private int loopStartIndex;
    private boolean loop = false;
    private Group parent;
    private float alpha = 1;
    public float x = 0, y = 0;    
    public float angle = 0;

    public float scalex, scaley, scalez = 1;

    public AnimationObject3d(int $maxVertices, int $maxFaces, int $numFrames) {
        super($maxVertices, $maxFaces);
        this.numFrames = $numFrames;
        this.frames = new KeyFrame[numFrames];
        this.currentFrameIndex = 0;
        this.isPlaying = false;
        this.interpolation = 0;
        this._animationEnabled = true;
    }

    public AnimationObject3d(Vertices $vertices, FacesBufferedList $faces, TextureList $textures, KeyFrame[] $frames) {
        super($vertices, $faces, $textures);
        numFrames = $frames.length;
        frames = $frames;
    }

    public int getCurrentFrame() {
        return currentFrameIndex;
    }

    public void addFrame(KeyFrame frame) {
        frames[currentFrameIndex++] = frame;
    }

    public void setFrames(KeyFrame[] frames) {
        this.frames = frames;
    }

    public void play() {
        startTime = System.currentTimeMillis();
        isPlaying = true;
        currentFrameName = null;
        loop = false;
    }

    public void play(String name) {
        currentFrameIndex = 0;
        currentFrameName = name;

        for (int i = 0; i < numFrames; i++) {
            if (frames[i].getName().equals(name)) {
                loopStartIndex = currentFrameIndex = i;
                break;
            }
        }

        startTime = System.currentTimeMillis();
        isPlaying = true;
    }

    public void play(String name, boolean loop) {
        this.loop = loop;
        play(name);
    }

    public void stop() {
        isPlaying = false;
        currentFrameIndex = 0;
    }

    public void pause() {
        isPlaying = false;
    }

    public void update() {


//        if (!isPlaying || !updateVertices) {
//            return;
//        }
        
//        Log.w("AnimationObject3d", getCurrentFrame() + " frame");

        currentTime = System.currentTimeMillis();
        KeyFrame currentFrame = frames[currentFrameIndex];
        KeyFrame nextFrame = frames[(currentFrameIndex + 1) % numFrames];

        if (currentFrameName != null && !currentFrameName.equals(currentFrame.getName())) {
            if (!loop) {
                stop();
            } else {
                currentFrameIndex = loopStartIndex;
            }
            return;
        }

        
        float[] currentVerts = currentFrame.getVertices();
        float[] nextVerts = nextFrame.getVertices();
        float[] currentNormals = currentFrame.getNormals();
        float[] nextNormals = nextFrame.getNormals();
        int numVerts = currentVerts.length;

        float[] interPolatedVerts = new float[numVerts];
        float[] interPolatedNormals = new float[numVerts];

        for (int i = 0; i < numVerts; i += 3) {
            interPolatedVerts[i] = currentVerts[i] + interpolation * (nextVerts[i] - currentVerts[i]);
            interPolatedVerts[i + 1] = currentVerts[i + 1] + interpolation * (nextVerts[i + 1] - currentVerts[i + 1]);
            interPolatedVerts[i + 2] = currentVerts[i + 2] + interpolation * (nextVerts[i + 2] - currentVerts[i + 2]);
            interPolatedNormals[i] = currentNormals[i] + interpolation * (nextNormals[i] - currentNormals[i]);
            interPolatedNormals[i + 1] = currentNormals[i + 1] + interpolation * (nextNormals[i + 1] - currentNormals[i + 1]);
            interPolatedNormals[i + 2] = currentNormals[i + 2] + interpolation * (nextNormals[i + 2] - currentNormals[i + 2]);
        }

        interpolation += fps * (currentTime - startTime) / 1000;

        vertices().overwriteNormals(interPolatedNormals);
        vertices().overwriteVerts(interPolatedVerts);

        if (interpolation > 1) {
            interpolation = 0;
            currentFrameIndex++;

            if (currentFrameIndex >= numFrames) {
                currentFrameIndex = 0;
            }
        }
        
        this.generateHardwareBuffers();
        OpenGLUtility.updateVBO(drawVO, verticesBuffer, indicesBuffer, textBuffer, normalBuffer, numVerts);
        
        startTime = System.currentTimeMillis();

    }

    public float getFps() {
        return fps;
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public Object3d clone(boolean cloneData) {
        Vertices v = cloneData ? _vertices.clone() : _vertices;
        FacesBufferedList f = cloneData ? _faces.clone() : _faces;
        //KeyFrame[] fr = cloneData ? getClonedFrames() : frames;

        AnimationObject3d clone = new AnimationObject3d(v, f, _textures, frames);
        clone.position().x = position().x;
        clone.position().y = position().y;
        clone.position().z = position().z;
        clone.rotation().x = rotation().x;
        clone.rotation().y = rotation().y;
        clone.rotation().z = rotation().z;
        clone.scale().x = scale().x;
        clone.scale().y = scale().y;
        clone.scale().z = scale().z;
        clone.setFps(fps);
        clone.animationEnabled(animationEnabled());
        return clone;
    }

    public KeyFrame[] getClonedFrames() {
        int len = frames.length;
        KeyFrame[] cl = new KeyFrame[len];

        for (int i = 0; i < len; i++) {
            cl[i] = frames[i].clone();
        }

        return cl;
    }

    public boolean getUpdateVertices() {
        return updateVertices;
    }

    public void setUpdateVertices(boolean updateVertices) {
        this.updateVertices = updateVertices;
    }

    public void fade() {
        alpha = alpha - 0.1f;
    }

    public void remove() {
        if (getCurrentFrame() > 28) {
            parent.remove(this);
            Log.w("AnimationObject3d remove ", getCurrentFrame() + " frame");
        }


    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

   int particleVertBufferIndex = 0;
    
   int  aircraftVertBufferIndex = 0;

   int  textureCoordBufferIndex = 0;

   int  indexBufferIndex = 0;
   
   int  normalBufferIndex = 0;

    
   public void drawVBO(GL10 gl)
   {
       GL11 gl11 = (GL11) gl;
       
       // draw using hardware buffers

       gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, aircraftVertBufferIndex);
       gl11.glVertexPointer(3, GL10.GL_FIXED, 0, 0);

       gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureCoordBufferIndex);
       gl11.glTexCoordPointer(2, GL11.GL_FIXED, 0, 0);

       gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalBufferIndex);      
       gl11.glNormalPointer(GL11.GL_FIXED, 0, 0);

       gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBufferIndex);
       gl11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_SHORT, 0);

       gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
       gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
       
   }
   
   protected FloatBuffer getBuffer(float[] textCoords) {
       // a float is 4 bytes, therefore we multiply the number if
       // vertices with 4.
       // Our index buffer.
       FloatBuffer textBuffer;


       ByteBuffer vbb = ByteBuffer.allocateDirect(textCoords.length * 4);
       vbb.order(ByteOrder.nativeOrder());
       textBuffer = vbb.asFloatBuffer();
       textBuffer.put(textCoords);
       textBuffer.position(0);
       
       return textBuffer;
   }
    public void generateHardwareBuffers()
    
    {
    	int error=0;
    	
        indicesBuffer = faces().buffer();
        indicesBuffer.position(0);

        // vertices
        verticesBuffer = vertices().points().buffer();
        verticesBuffer.position(0);

        // tex
        textBuffer = vertices().uvs().buffer();
        textBuffer.position(0);

        // normals
        normalBuffer = vertices().normals().buffer();
        normalBuffer.position(0);

        numOfIndices = faces().size() * FacesBufferedList.PROPERTIES_PER_ELEMENT;


// 
//            if (gl instanceof GL11)
// 
//            {
// 
//                GL11 gl11 = (GL11) gl;
// 
//                int[] buffer = new int[1]; 
// 
//                // Allocate and fill the vertex buffer.
// 
//                gl11.glGenBuffers(1, buffer, 0);
// 
//                aircraftVertBufferIndex = buffer[0];
// 
//                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, aircraftVertBufferIndex);
// 
//                final int vertex2Size = verticesBuffer.capacity() * 4;
// 
//                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertex2Size, verticesBuffer, GL11.GL_STATIC_DRAW);
// 
//  
//                error = gl11.glGetError();               
// 
//                if (error != GL10.GL_NO_ERROR)
// 
//                {
// 
//                    Log.e("AirDefender", "Generate vertex buffer GLError: " + error);
// 
//                }
// 
// 
// 
//                // Allocate and fill the texture coordinate buffer.
// 
//                gl11.glGenBuffers(1, buffer, 0);
// 
//                textureCoordBufferIndex = buffer[0];
// 
//                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureCoordBufferIndex);
// 
//                final int texCoordSize = textBuffer.capacity() * 4;
// 
//                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, texCoordSize, textBuffer, GL11.GL_STATIC_DRAW);
// 
// 
// 
//                error = gl11.glGetError();
// 
//                if (error != GL10.GL_NO_ERROR)
// 
//                {
// 
//                    Log.e("AirDefender", "Generate texture buffer GLError: " + error);
// 
//                }
// 
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
//                
//                // Unbind the array buffer.
// 
//                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
// 
// 
// 
//                // Allocate and fill the index buffer.
// 
//                gl11.glGenBuffers(1, buffer, 0);
// 
//                indexBufferIndex = buffer[0];
// 
//                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBufferIndex);
// 
//                // A char is 2 bytes.
// 
//                final int indexSize = indicesBuffer.capacity() * 2;
// 
//                gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexSize, indicesBuffer, GL11.GL_STATIC_DRAW);
// 
// 
// 
//                error = gl11.glGetError();
// 
//                if (error != GL10.GL_NO_ERROR)
// 
//                {
// 
//                    Log.e("AirDefender", "Generate index buffer GLError: " + error);
// 
//                }
// 
//                // Unbind the element array buffer.
// 
//                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
// 
//                Log.e("AirDefender", "sucess: " + error);
//
//            }
// 
        
    }
 
 


    @Override
    public void draw(GL10 gl) {

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        indicesBuffer = faces().buffer();
        indicesBuffer.position(0);

        // vertices
        verticesBuffer = vertices().points().buffer();
        verticesBuffer.position(0);

        // tex
        textBuffer = vertices().uvs().buffer();
        textBuffer.position(0);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textBuffer);

        // normals
        normalBuffer = vertices().normals().buffer();
        normalBuffer.position(0);

        numOfIndices = faces().size() * FacesBufferedList.PROPERTIES_PER_ELEMENT;

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);

        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
//        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glRotatef(angle, 0, 0, 1);

        gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT,
                indicesBuffer);
        
//        drawVBO(gl);
        
        gl.glPopMatrix();

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

    }
}
