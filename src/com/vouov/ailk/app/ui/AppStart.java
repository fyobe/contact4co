package com.vouov.ailk.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午7:52
 */
public class AppStart extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
        finish();
    }
}