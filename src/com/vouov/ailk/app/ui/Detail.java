package com.vouov.ailk.app.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.vouov.ailk.app.R;
import com.vouov.ailk.app.api.AppApiClient;
import com.vouov.ailk.app.api.AppLocalApiClient;
import com.vouov.ailk.app.model.Employee;

/**
 * User: yuml
 * Date: 13-1-19
 * Time: 下午11:12
 */
public class Detail extends BaseActivity {
    private static final String TAG = "ailk_detail";
    private TextView nameTextView;
    private TextView idTextView;
    private TextView accountTextView;
    private TextView deptNameTextView;
    private TextView homeCityTextView;
    private TextView officePhoneTextView;
    private TextView mobileTextView;
    private TextView emailTextView;
    private TextView parentEmployeeTextView;
    private TextView otherTextView;

    private ProgressDialog dialog;

    private Employee employee;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_detail);
        nameTextView = (TextView) findViewById(R.id.lbl_name);
        idTextView = (TextView) findViewById(R.id.lbl_employee_id);
        accountTextView = (TextView) findViewById(R.id.lbl_account);
        deptNameTextView = (TextView) findViewById(R.id.lbl_dept_name);
        homeCityTextView = (TextView) findViewById(R.id.lbl_home_city);
        officePhoneTextView = (TextView) findViewById(R.id.lbl_office_phone);
        mobileTextView = (TextView) findViewById(R.id.lbl_mobile);
        emailTextView = (TextView) findViewById(R.id.lbl_email);
        parentEmployeeTextView = (TextView) findViewById(R.id.lbl_parent_employee);
        otherTextView = (TextView) findViewById(R.id.lbl_other);
        Intent intent = getIntent();
        employee = (Employee) intent.getSerializableExtra("p_employe");
        nameTextView.setText(employee.getName());
        idTextView.setText(employee.getId());
        accountTextView.setText(employee.getAccount());
        deptNameTextView.setText(employee.getDeptName());
        homeCityTextView.setText(employee.getHomeCity());
        officePhoneTextView.setText(employee.getOfficePhone());
        mobileTextView.setText(employee.getMobile());
        emailTextView.setText(employee.getEmail());
        if (employee.getParentEmployeeId() != null && !"".equals(employee.getParentEmployeeId())) {
            parentEmployeeTextView.setText(Html.fromHtml("<u>" + employee.getParentEmployeeName() + " (" + employee.getParentEmployeeId() + ")<u>"));
            parentEmployeeTextView.setOnClickListener(new EmployeeLinkDetailClickListener(employee.getParentEmployeeId()));
        } else {
            parentEmployeeTextView.setText("");
        }
        if(employee.getDeptName()!=null && !"".equals(employee.getDeptName())){
            deptNameTextView.setText(Html.fromHtml("<u>"+employee.getDeptName()+"</u>"));
            deptNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Detail.this, Search.class);
                    i.putExtra("auto_query", true);
                    i.putExtra("column_name", "EI.ORG_NAME");
                    i.putExtra("condition", employee.getDeptName());
                    Detail.this.startActivity(i);
                }
            });
        }else {
            deptNameTextView.setText("");
        }
        otherTextView.setText(employee.getDesc());
        try {
            if (AppLocalApiClient.getEmployeeById(Detail.this, employee.getId()).isFavorite()) {
                findViewById(R.id.btn_add_fav).setVisibility(View.GONE);
            } else {
                findViewById(R.id.btn_add_fav).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //AppLocalApiClient.testTable(Detail.this);
                        AppLocalApiClient.addFavorites(Detail.this, employee);
                        findViewById(R.id.btn_add_fav).setVisibility(View.GONE);
                        Toast.makeText(Detail.this, "成功添加本地收藏", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class EmployeeLinkDetailClickListener implements View.OnClickListener {
        private String employeeId;

        public EmployeeLinkDetailClickListener(String employeeId) {
            this.employeeId = employeeId;
        }

        @Override
        public void onClick(View view) {
            new AsyncTask<String, Integer, Employee>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog = ProgressDialog.show(Detail.this, null, "请等待,数据加载中...", true);
                }

                @Override
                protected void onPostExecute(Employee employee) {
                    super.onPostExecute(employee);
                    if (employee != null) {
                        Intent intent = new Intent(Detail.this, Detail.class);
                        intent.putExtra("p_employe", employee);
                        Detail.this.startActivity(intent);
                    }
                    dialog.dismiss();
                }

                @Override
                protected Employee doInBackground(String... strings) {
                    Employee result = null;
                    try {
                        result = AppLocalApiClient.getEmployeeById(Detail.this, strings[0]);
                        if (result == null) {
                            result = AppApiClient.getEmployeeById(strings[0]);
                            if (result != null) {
                                AppLocalApiClient.storeEmployee(Detail.this, result);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    return result;
                }
            }.execute(this.employeeId);
        }
    }
}