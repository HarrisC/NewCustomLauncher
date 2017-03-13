package com.example.apath.newcustomlauncher;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class OnSwipeListener extends GestureDetector.SimpleOnGestureListener {

    Context context;
    PackageManager pm;
    STT stt;
    ReadRss readRss;

    public OnSwipeListener(Context context, PackageManager pm, STT stt) {
        this.context = context;
        this.pm = pm;
        this.stt= stt;
    }

    private static final String DEBUG_TAG = "Scroll";

    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {

        Intent I;

        switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
                Log.d(DEBUG_TAG, "Up");
                I = new Intent(Intent.ACTION_DIAL);
                context.startActivity(I);
                return true;
            case 2:
                Log.d(DEBUG_TAG, "left");
                I = AppsListActivity.manager.getLaunchIntentForPackage("com.htc.sense.mms");
                context.startActivity(I);
                return true;
            case 3:
                Log.d(DEBUG_TAG, "down");
                Intent i = new Intent(context, NewsReader.class);
                context.startActivity(i);
                return true;
            case 4:
                Log.d(DEBUG_TAG, "right");
                I = AppsListActivity.manager.getLaunchIntentForPackage("com.android.settings");
                context.startActivity(I);
                return true;
        }
        return false;
    }

    public static int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return 2;
        if (angle < -45 && angle >= -135)
            // down
            return 3;
        if (angle > -45 && angle <= 45)
            // right
            return 4;
        return 0;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(DEBUG_TAG, "Long pressed");
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(DEBUG_TAG, "Double Tap");
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        Log.e("Test", activities.toString());
        if (activities.size() != 0) {
            ((Activity) context).startActivityForResult(stt.getIntent(), stt.getRequestCode());
        }
        return true;
    }
}
