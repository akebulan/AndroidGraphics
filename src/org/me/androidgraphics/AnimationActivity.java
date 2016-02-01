/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.me.androidgraphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import min3d.Shared;
import min3d.animation.AnimationObject3d;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;

public class AnimationActivity extends RendererActivity {

    private AnimationObject3d doomGuy;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        super.onCreate(savedInstanceState);
//    }
    @Override
    public void initScene() {
        IParser parser = Parser.createParser(Parser.Type.MD2, getResources(),
                "org.me.androidgraphics:raw/radar_1", true);
        parser.parse();
        doomGuy = parser.getParsedAnimationObject();
        doomGuy.scale().x = doomGuy.scale().y = doomGuy.scale().z = .1f;
        scene.addChild(doomGuy);

        doomGuy.rotation().z = -90;
        doomGuy.rotation().x = -90;
//        doomGuy.position().x = 0;
//        doomGuy.position().z = 0;
//        doomGuy.position().y = 0;
        doomGuy.setFps(20);
        doomGuy.play();

//        parser = Parser.createParser(Parser.Type.OBJ, getResources(),
//                "org.me.androidgraphics:raw/fighter", true);
//

//        parser.parse();
//        Object3dContainer objModel = parser.getParsedObject();
//        objModel.scale().x = objModel.scale().y = objModel.scale().z = .7f;
//        scene.addChild(objModel);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // -- create a shallow copy
                AnimationObject3d doomGuyClone = (AnimationObject3d) doomGuy.clone(false);
                scene.addChild(doomGuyClone);
                doomGuyClone.position().x -= 3 * i;
                doomGuyClone.position().z -= 3 * j;
                if (i % 2 == 1) {
                    doomGuyClone.position().z += .5;
                }
//                doomGuyClone.setFps(20);
//                doomGuyClone.play();
                // -- prevent this object from updating the vertices.
//                doomGuyClone.setUpdateVertices(false);
            }
        }

        scene.camera().position.x = -10;
        scene.camera().position.z = 10;

        loadTexture(R.drawable.turret01);

    }

    @Override
    public void updateScene() {
    }

    public static int loadTexture(int resource) {
        // In which ID will we be storing this texture?
        int id = newTextureID(Shared.renderer().gl());
        // We need to flip the textures vertically:
        Matrix flip = new Matrix();
        flip.postScale(1f, -1f);
        flip.postRotate(180f);

        // This will tell the BitmapFactory to not scale based on the device's pixel density:
        // (Thanks to Matthew Marshall for this bit)
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        // Load up, and flip the texture:
        Bitmap temp = BitmapFactory.decodeResource(Shared.context().getResources(), resource, opts);
        Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
        temp.recycle();
        Shared.renderer().gl().glBindTexture(GL10.GL_TEXTURE_2D, id);
        // Set all of our texture parameters:
        Shared.renderer().gl().glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
        Shared.renderer().gl().glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
        Shared.renderer().gl().glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        Shared.renderer().gl().glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        int[] crop = {0, bmp.getWidth(), bmp.getHeight(), -bmp.getHeight()};

        ((GL11) Shared.renderer().gl()).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, crop, 0);


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

    private static int newTextureID(GL10 gl) {
        int[] temp = new int[1];
        gl.glGenTextures(1, temp, 0);
        return temp[0];
    }
}
