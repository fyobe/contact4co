package com.vouov.ailk.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.vouov.ailk.app.R;
import com.vouov.ailk.app.api.AppApiClient;
import com.vouov.ailk.app.common.AppApplication;
import com.vouov.ailk.app.model.Employee;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午7:57
 */
public class Login extends Activity {
    private static final String TAG = "ailk_login";
    private EditText accountText;
    private EditText passwordText;
    private Button submitButton;
    private CheckBox rememberCheckBox;
    private CheckBox autoSubmitCheckBox;

    private String account;
    private String password;

    ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        accountText = (EditText) findViewById(R.id.txt_account);
        passwordText = (EditText) findViewById(R.id.txt_password);
        submitButton = (Button) findViewById(R.id.btn_submit);
        rememberCheckBox = (CheckBox) findViewById(R.id.cb_remember_pass);
        autoSubmitCheckBox = (CheckBox) findViewById(R.id.cb_auto_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = accountText.getText().toString();
                password = passwordText.getText().toString();
                if ("".equals(account.trim())) {
                    accountText.setSelected(true);
                    Toast.makeText(Login.this, "帐号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(password.trim())) {
                    passwordText.setSelected(true);
                    Toast.makeText(Login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncTask<String, Integer, Employee>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog = ProgressDialog.show(Login.this, null, "正在登录...", true);
                    }

                    @Override
                    protected void onPostExecute(Employee employee) {
                        super.onPostExecute(employee);
                        if (employee != null) {
                            //TODO 保存登录信息
                            AppApplication appApplication = (AppApplication) Login.this.getApplication();
                            appApplication.setUserName(account);
                            appApplication.setPassword(password);
                            appApplication.setUserInfo(employee);
                            Intent intent = new Intent(Login.this, Search.class);
                            Login.this.startActivity(intent);
                            Login.this.finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(Login.this, "登录失败，请检查登录信息", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    protected Employee doInBackground(String... strings) {
                        Employee result = null;
                        try {
                            result = AppApiClient.login(account, password);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                        return result;
                    }
                }.execute();
            }
        });
    }
}