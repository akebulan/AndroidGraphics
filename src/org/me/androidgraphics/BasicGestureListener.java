/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.androidgraphics;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 *
 * @author akebulan
 */
public class BasicGestureListener extends GestureDetector.SimpleOnGestureListener {

    private int width;
    private int height;
    private BasicRender basicRender;

    public BasicGestureListener() {
        super();
    }
//
//    public BasicGestureListener(BasicRender basicRender, int width, int height) {
//        super();
//        this.basicRender = basicRender;
//        this.width = width;
//        this.height = height;
//    }

    @Override
    public boolean onSingleTapUp(MotionEvent ev) {

//        Log.w("onSingleTapUp", ev.toString());
//        int screenX = getWidth();
//        int screenY = getHeight();
//
//        float[] pickResults = OpenGLUtility.pickColor((int) ev.getX(), (int) ev.getY(), screenX, screenY);
//
//        basicRender.getGameManager().setPickResults(pickResults);
//        basicRender.getGameManager().processPickResults();

        return true;

    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
//        Log.w("onDoubleTap", e.toString());
//        basicRender.setZoomFactor(basicRender.getZoomFactor() - 0.1f);
//        int screenX = getWidth();
//        int screenY = getHeight();
//
//        float[] pickResults = OpenGLUtility.pickColor((int) ev.getX(), (int) ev.getY(), screenX, screenY);
//
//        basicRender.getGameManager().setPickResults(pickResults);
//        basicRender.getGameManager().processPickResults();

        return true;
    }

    @Override
    public void onShowPress(MotionEvent ev) {

//        Log.w("onShowPress", ev.toString());
////        basicRender.setZoomFactor(basicRender.getZoomFactor() + 0.1f);
//        int screenX = getWidth();
//        int screenY = getHeight();
//
//        float[] pickResults = OpenGLUtility.pickColor((int) ev.getX(), (int) ev.getY(), screenX, screenY);
//
//        basicRender.getGameManager().setPickResults(pickResults);
//        basicRender.getGameManager().processPickResults();


    }

    @Override
    public void onLongPress(MotionEvent ev) {

//        Log.w("onLongPress", ev.toString());
//        int screenX = getWidth();
//        int screenY = getHeight();
//
//        float[] pickResults = OpenGLUtility.pickColor((int) ev.getX(), (int) ev.getY(), screenX, screenY);
//
//        basicRender.getGameManager().setPickResults(pickResults);
//        basicRender.getGameManager().processPickResults();


    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

//        Log.w("onScroll e1", e1.toString());
//        Log.w("onScroll e2", e2.toString());
//        Log.w("onScroll distanceX/distanceY", distanceX + "/" + distanceY);

//        if (distanceX > 0) {
//            basicRender.setZoomFactor(basicRender.getZoomFactor() + 0.1f);
//        } else if (distanceX < 0) {
//            basicRender.setZoomFactor(basicRender.getZoomFactor() - 0.1f);
//        }

        float incr = 0.03f;
        if (distanceX > 0) {
            if (basicRender.getxFactor() < 2) {
                basicRender.setxFactor(basicRender.getxFactor() + incr);
                // for cam setting
//                basicRender.setyFactor(basicRender.getyFactor() - incr);
            }
        } else if (distanceX < 0) {
            if (basicRender.getxFactor() > 0) {
                basicRender.setxFactor(basicRender.getxFactor() - incr);
                // for cam setting
//                basicRender.setyFactor(basicRender.getyFactor() + incr);
            }
        }

        if (distanceY > 0) {
            if (basicRender.getyFactor() > 0) {
                basicRender.setyFactor(basicRender.getyFactor() - incr);
                // for cam setting
//                basicRender.setxFactor(basicRender.getxFactor() + incr);
            }
        } else if (distanceY < 0) {
            if (basicRender.getyFactor() < 2) {
                basicRender.setyFactor(basicRender.getyFactor() + incr);
                // for cam setting
//                basicRender.setxFactor(basicRender.getxFactor() - incr);
            }
        }

//        Log.w("BasicGestureListener", "onScroll: yFactor =" + basicRender.getyFactor());
//        Log.w("BasicGestureListener", "onScroll: xFactor =" + basicRender.getxFactor());


        return true;

    }

    @Override
    public boolean onDown(MotionEvent ev) {

//        Log.w("onDownd", ev.toString());
        int screenX = getWidth();
        int screenY = getHeight();

 //       float[] pickResults = OpenGLUtility.pickColor((int) ev.getX(), (int) ev.getY(), screenX, screenY);

//        basicRender.getGameManager().setPickResults(pickResults);
//        basicRender.getGameManager().processPickResults();

        return true;

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

//        Log.w("onFling e1", e1.toString());
//
//        Log.w("onFling e2", e2.toString());
//
//        Log.w("onFling velocityX/velocityY", velocityX + "/" + velocityY);
//
//        float incr = 1f;
////
//        if (velocityX > 0) {
//                basicRender.setxFactor(basicRender.getxFactor() + incr);
//        } else if (velocityX < 0) {
//                basicRender.setxFactor(basicRender.getxFactor() - incr);
//        }

//        float incr = 0.1f;
//
//        if (velocityX > 0) {
//            basicRender.setxFactor(basicRender.getxFactor() - incr);
//        } else if (velocityX < 0) {
//            basicRender.setxFactor(basicRender.getxFactor() + incr);
//        }
//
//        if (velocityY > 0) {
//            basicRender.setyFactor(basicRender.getyFactor() + incr);
//        } else if (velocityY < 0) {
//            basicRender.setyFactor(basicRender.getyFactor() - incr);
//        }

        return true;

    }


    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public BasicRender getBasicRender() {
        return basicRender;
    }

    public void setBasicRender(BasicRender basicRender) {
        this.basicRender = basicRender;
    }
}
