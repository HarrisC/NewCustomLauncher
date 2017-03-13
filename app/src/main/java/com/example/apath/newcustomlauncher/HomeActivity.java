package com.example.apath.newcustomlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends Activity {

    private GestureDetectorCompat mDetector;
    private STT stt = new STT(1234);
    private static List<AppDetail> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        PackageManager pm = getPackageManager();
        mDetector = new GestureDetectorCompat(this, new OnSwipeListener(this, pm, stt));

        PackageManager manager = getPackageManager();
        apps = new ArrayList<AppDetail>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager).toString();
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }

    public static List<AppDetail> getAppsList() {
        return apps;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    public void showApps(View v) {
        Intent i = new Intent(this, AppsListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == stt.getRequestCode() && resultCode == RESULT_OK) {
            ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(matches.get(0));
            Log.e("Input: ", stringBuilder.toString());

            for (AppDetail app: apps) {
                if (stringBuilder.toString().equalsIgnoreCase(app.label)) {
                    Log.e("TEST: ", app.name);
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(app.name);
                    startActivity( LaunchIntent );
                    break;
                }
            }


            /*switch(stringBuilder.toString()) {
                case "camera":
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(i);
                    break;
                default:
                    Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}
