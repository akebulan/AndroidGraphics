/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.androidgraphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 *
 * @author akebulan
 */
public class BasicGLSurfaceView extends GLSurfaceView {

    private BasicRender mRenderer;
    Context context;
    private BasicGestureListener basicGestureListener;
    private GestureDetector mGestureDetector;

    public BasicGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setFocusable(true);
        mRenderer = new BasicRender(context);
        setRenderer(mRenderer);
        basicGestureListener = new BasicGestureListener();
        mGestureDetector = new GestureDetector(context, basicGestureListener);
    }

    public BasicGLSurfaceView(Context context) {
        super(context);

        setFocusable(true);

        mRenderer = new BasicRender(context);
        setRenderer(mRenderer);
        basicGestureListener = new BasicGestureListener();
        mGestureDetector = new GestureDetector(context, basicGestureListener);


    }

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {


        queueEvent(new Runnable() {

 //           int screenX = getWidth();
//            int screenY = getHeight();

//            public void run() {
//                if (ev.getAction() == MotionEvent.ACTION_UP) {
//                    Log.w("ParticleSystem", "onTouchEvent ");
//                    float[] pickResults = OpenGLUtility.pickColor((int) ev.getX(), (int) ev.getY(), screenX, screenY);
//                    mRenderer.processPickResults(pickResults);
//                }
//            }

            public void run() {
                basicGestureListener.setBasicRender(mRenderer);
                basicGestureListener.setHeight(getHeight());
                basicGestureListener.setWidth(getWidth());

                try {
                    if (mGestureDetector.onTouchEvent(ev)) {
//                        sounds.play(sExplosion, 1.0f, 1.0f, 0, 0, 1.5f);
                        Log.w("BasicGLSurfaceView", "onTouchEvent");


                        // return true;
                    } else {
                        // return false;
                    }
                } catch (java.lang.IllegalArgumentException e) {
                    Log.w("BasicGLSurfaceView", e);

                }

            }
        });
        
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
      }

    @Override
    public void onPause() {
        super.onPause();
//        mRenderer.getRoot().clearAll();

    }

    @Override
    public void onResume() {
        super.onResume();
//        mRenderer.getRoot().clearAll();

    }

    public BasicRender getmRenderer() {
        return mRenderer;
    }

    public void setmRenderer(BasicRender mRenderer) {
        this.mRenderer = mRenderer;
    }
}
