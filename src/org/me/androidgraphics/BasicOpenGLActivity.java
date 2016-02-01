package org.me.androidgraphics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

public class BasicOpenGLActivity extends Activity {

    private PowerManager.WakeLock wl;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BasicGLSurfaceView view = new BasicGLSurfaceView(this);
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wl.release();

    }

    @Override
    protected void onResume() {
        super.onResume();
        wl.acquire();

    }
}
