package com.example.apath.newcustomlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class NewsReader extends Activity {

    private TextToSpeech tts;
    private static ArrayList<FeedItem> feedItems;
    private GestureDetectorCompat mDetector;
    private int feedCount = 0;
    private String toSpeak;

    public static void setFeedItems(ArrayList<FeedItem> feedList) {
        feedItems = feedList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_home);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(new Locale("yue", "HK"));
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        toSpeak = "新聞";
        speak(toSpeak);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {

            switch (OnSwipeListener.getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
                case 1:
                    Log.d(DEBUG_TAG, "Up");
                    return true;
                case 2:
                    // next news feed
                    Log.d(DEBUG_TAG, "left");
                    if (feedCount >= feedItems.size()) {
                        toSpeak = "最後一則新聞";
                        speak(toSpeak);
                    } else {
                        toSpeak = "下一則新聞";
                        speak(toSpeak);
                        feedCount++;
                    }
                    return true;
                case 3:
                    Log.d(DEBUG_TAG, "down");
                    return true;
                case 4:
                    //previous news feed
                    Log.d(DEBUG_TAG, "right");
                    if (feedCount == 0) {
                        toSpeak = "第一則新聞";
                        speak(toSpeak);
                    } else {
                        toSpeak = "上一則新聞";
                        speak(toSpeak);
                        feedCount--;
                    }
                    return true;
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            // speak the title
            Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
            String toSpeak = feedItems.get(feedCount).getTitle();
            speak(toSpeak);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            //speak the descciption
            Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
            String toSpeak = feedItems.get(feedCount).getDescription();
            speak(toSpeak);
            return true;
        }
    }

    private void speak(String toSpeak) {
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "");
    }

    @Override
    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
}
