/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.androidgraphics;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

/**
 *
 * @author lighthammer
 */
public class MainActivity extends Activity {

    Animation hyperspaceJumpAnimation;
    TranslateAnimation translateAnimation;
    ImageView spaceshipImage;
    float x,y=0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);


        spaceshipImage = (ImageView) findViewById(R.id.turretangle1);
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(MainActivity.this, "x= " + event.getX() + " y= " + event.getY(), Toast.LENGTH_SHORT).show();
            translateAnimation = new TranslateAnimation(x, event.getX(), y, event.getY());
//            translateAnimation.setRepeatMode(TranslateAnimation.REVERSE);
//            translateAnimation.setRepeatCount(2);
            translateAnimation.setDuration(700);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            translateAnimation.setFillAfter(true);
            x=event.getX();
            y=event.getY();
            spaceshipImage.startAnimation(translateAnimation);
            return true;
        }
        return super.onTouchEvent(event);
    }
}
