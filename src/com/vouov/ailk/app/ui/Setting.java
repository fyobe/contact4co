package com.vouov.ailk.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TableRow;
import android.widget.ToggleButton;
import com.vouov.ailk.app.R;
import com.vouov.ailk.app.api.AppLocalApiClient;
import com.vouov.ailk.app.common.AppUpdateManager;
import com.vouov.ailk.app.model.User;

/**
 * User: yuml
 * Date: 13-1-23
 * Time: 下午10:33
 */
public class Setting extends BaseActivity {
    private ToggleButton autoLoginSettingToggleButton;
    private TableRow checkUpdateRow;
    private TableRow aboutRow;
    private TableRow feedbackRow;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        autoLoginSettingToggleButton = (ToggleButton) findViewById(R.id.btn_setting_auto_login);
        User storedUser = AppLocalApiClient.fetchUser(this);
        autoLoginSettingToggleButton.setChecked(storedUser == null ? false : storedUser.isAutoLogin());
        autoLoginSettingToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                User storedUser = AppLocalApiClient.fetchUser(Setting.this);
                storedUser.setAutoLogin(isChecked);
                AppLocalApiClient.saveUser(Setting.this, storedUser);
            }
        });
        checkUpdateRow = (TableRow) findViewById(R.id.menu_check_update);
        checkUpdateRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动更新任务
                AppUpdateManager updateManager = AppUpdateManager.getInstance(Setting.this);
                updateManager.update(true);
            }
        });
        aboutRow = (TableRow) findViewById(R.id.menu_about);
        aboutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting.this, About.class);
                startActivity(intent);
            }
        });
        feedbackRow = (TableRow) findViewById(R.id.menu_feedback);
        feedbackRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting.this, Feedback.class);
                startActivity(intent);
            }
        });
    }
}