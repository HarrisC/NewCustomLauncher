package com.example.apath.newcustomlauncher;

import android.content.Intent;
import android.speech.RecognizerIntent;

public class STT {
    Intent intent;
    int requestCode;

    public STT (int requestCode) {
        this.requestCode = requestCode;
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh_HK");
    }

    public Intent getIntent() {
        return intent;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
