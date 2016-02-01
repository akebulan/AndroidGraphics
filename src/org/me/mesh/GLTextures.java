package org.me.mesh;

import javax.microedition.khronos.opengles.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;
import java.lang.Integer;

public class GLTextures {
    private java.util.HashMap<Integer, Integer> textureMap;
    private int[] textureFiles;
    private GL10 gl;
    private Context context;
    private int[] textures;

    public GLTextures(GL10 gl, Context context) {
        this.gl = gl;
        this.context = context;
        this.textureMap = new java.util.HashMap<Integer, Integer>();
    }

    public void loadTextures() {
        int[] tmp_tex = new int[textureFiles.length];
        gl.glGenTextures(textureFiles.length, tmp_tex, 0);
        textures = tmp_tex;
        for (int i = 0; i < textureFiles.length; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), textureFiles[i]);
            this.textureMap.put(new Integer(textureFiles[i]), new Integer(i));
            int tex = tmp_tex[i];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
            bmp.recycle();
        }
    }

    public void setTexture(int id) {
        try {
            int textureid = this.textureMap.get(new Integer(id)).intValue();
            gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[textureid]);
        } catch (Exception e) {
            return;
        }
    }

    public void add(int resource) {
        if (textureFiles == null) {
            textureFiles = new int[1];
            textureFiles[0] = resource;
        } else {
            int[] newarray = new int[textureFiles.length + 1];
            for (int i = 0; i < textureFiles.length; i++) {
                newarray[i] = textureFiles[i];
            }
            newarray[textureFiles.length] = resource;
            textureFiles = newarray;
        }
    }

  // Get a new texture id:
    private static int newTextureID(GL10 gl) {
        int[] temp = new int[1];
        gl.glGenTextures(1, temp, 0);
        return temp[0];
    }

    // Will load a texture out of a drawable resource file, and return an OpenGL texture ID:
    private int loadTexture(GL10 gl, Context context, int resource) {
        // In which ID will we be storing this texture?
        int id = newTextureID(gl);
        // We need to flip the textures vertically:
        Matrix flip = new Matrix();
        flip.postScale(1f, -1f);
        // This will tell the BitmapFactory to not scale based on the device's pixel density:
        // (Thanks to Matthew Marshall for this bit)
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        // Load up, and flip the texture:
        Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resource, opts);
        Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
        temp.recycle();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
        // Set all of our texture parameters:
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        // Generate, and load up all of the mipmaps:
        for (int level = 0, height = bmp.getHeight(), width = bmp.getWidth(); true; level++) {
            // Push the bitmap onto the GPU:
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bmp, 0);
            // We need to stop when the texture is 1x1:
            if (height == 1 && width == 1) {
                break;
            }
            // Resize, and let's go again:
            width >>= 1;
            height >>= 1;
            if (width < 1) {
                width = 1;
            }
            if (height < 1) {
                height = 1;
            }
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width, height, true);
            bmp.recycle();
            bmp = bmp2;
        }
        bmp.recycle();
        return id;
    }

}