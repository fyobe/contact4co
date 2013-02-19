package com.vouov.ailk.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.vouov.ailk.app.R;
import com.vouov.ailk.app.common.AppManager;

/**
 * User: yuml
 * Date: 13-1-23
 * Time: 下午10:36
 */
public abstract class BaseActivity extends Activity {
    private static final String TAG = "ailk_ui_base";
    protected int currentMenuId;

    public void setCurrentMenuId(int currentMenuId) {
        this.currentMenuId = currentMenuId;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "menu create");
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(this.currentMenuId);
        if (item != null) {
            Log.d(TAG, "menu disabled:" + item.getTitle());
            item.setEnabled(false);
        } else {
            Log.d(TAG, "menu not found:" + this.currentMenuId);
        }
        return super.onCreateOptionsMenu(menu);
    }
    protected void favorite() {
        Intent intent = new Intent(this, Favorite.class);
        startActivity(intent);
    }
    protected void settings() {
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }

    protected void quit() {
        AppManager.getInstance().exitApp(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "menu selected: " + item.getTitle());
        switch (item.getItemId()) {
            case R.id.menu_fav:
                Log.d(TAG, "menu selected: fav");
                favorite();
                break;
            case R.id.menu_setting:
                Log.d(TAG, "menu selected: setting");
                settings();
                break;
            case R.id.menu_exit:
                Log.d(TAG, "menu selected: exit");
                quit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        AppManager.getInstance().finishActivity(this);
        super.onDestroy();
    }
}