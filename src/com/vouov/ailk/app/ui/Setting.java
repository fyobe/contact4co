package com.vouov.ailk.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import com.vouov.ailk.app.R;
import com.vouov.ailk.app.common.AppUpdateManager;

/**
 * User: yuml
 * Date: 13-1-23
 * Time: 下午10:33
 */
public class Setting extends BaseActivity {
    private TableRow checkUpdateRow;
    private TableRow aboutRow;
    private TableRow feedbackRow;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
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