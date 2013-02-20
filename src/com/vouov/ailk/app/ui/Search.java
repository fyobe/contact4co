package com.vouov.ailk.app.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.vouov.ailk.app.R;
import com.vouov.ailk.app.adapter.SearchResultAdapter;
import com.vouov.ailk.app.api.AppApiClient;
import com.vouov.ailk.app.api.AppLocalApiClient;
import com.vouov.ailk.app.model.Employee;
import com.vouov.ailk.app.model.Pagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午7:53
 */
public class Search extends BaseActivity implements AbsListView.OnScrollListener {
    private static final String TAG = "ailk_ui_search";
    private Spinner conditionSpinner;
    private EditText conditionText;
    private Button searchButton;
    private SearchResultAdapter listAdapter;
    private Pagination<Employee> pagination;
    private View footerView;
    private String lastColumnName;
    private String columnName = "";
    private String condition;
    private LinearLayout searchHeader;

    private int lastPosition = 0;
    private boolean showSearchHeader = true;
    private Animation mShowAction = null;
    private Animation mHiddenAction = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        setTitle("员工信息查询");
        conditionSpinner = (Spinner) findViewById(R.id.spi_condition);
        conditionText = (EditText) findViewById(R.id.txt_condition);
        searchHeader = (LinearLayout) findViewById(R.id.search_header);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.condition_name, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        conditionSpinner.setAdapter(adapter);
        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                columnName = getResources().getStringArray(R.array.condition_value)[position];
                TextView tv = (TextView) view;
                tv.setTextSize(12.0f);    //设置大小
                tv.setGravity(Gravity.LEFT);   //设置居中
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchButton = (Button) findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = conditionText.getText().toString();
                if ((text != null && !"".equals(text))) {
                    if (!text.equals(condition) || !columnName.equals(lastColumnName)) {
                        isLoading = true;
                        lastColumnName = columnName;
                        condition = text;
                        listAdapter.getEmployees().clear();
                        listAdapter.notifyDataSetChanged();
                        new EmployeeQueryTask().execute(1);
                    }
                } else {
                    listAdapter.getEmployees().clear();
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
        ListView resultListView = (ListView) findViewById(R.id.list_search_result);
        listAdapter = new SearchResultAdapter(this, new ArrayList<Employee>());
        footerView = getLayoutInflater().inflate(R.layout.loading_footer, null);
        resultListView.addFooterView(footerView);
        resultListView.setFooterDividersEnabled(false);
        footerView.setVisibility(View.GONE);
        resultListView.setAdapter(listAdapter);
        resultListView.setOnScrollListener(this);
        // 动画
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(500);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //判断是否需要自动查询
        Intent intent = getIntent();
        boolean autoQuery = intent.getBooleanExtra("auto_query", false);
        if (autoQuery) {
            columnName = intent.getStringExtra("column_name");
            int spinnerPosition = Arrays.binarySearch(
                    getResources().getStringArray(R.array.condition_value),
                    columnName);
            //set the default according to value
            conditionSpinner.setSelection(spinnerPosition);
            conditionText.setText(intent.getStringExtra("condition"));
            searchButton.performClick();
        }
    }

    private boolean isLoading = false;
    private boolean isCompleted = false;

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            Log.d(TAG, "SCROLL_STATE_IDLE");
            int firstVisibleItem = absListView.getFirstVisiblePosition();
            int visibleItemCount = absListView.getChildCount();
            int totalItemCount = absListView.getCount();
            if (!showSearchHeader && (lastPosition - firstVisibleItem) > 0) {
                showSearchHeader = true;
                searchHeader.startAnimation(mShowAction);
                searchHeader.setVisibility(View.VISIBLE);
            } else if (showSearchHeader && (lastPosition - firstVisibleItem) < 0) {
                showSearchHeader = false;
                searchHeader.startAnimation(mHiddenAction);
                searchHeader.setVisibility(View.GONE);
            }
            lastPosition = firstVisibleItem;
            // 判断是否滑动到底部
            if (firstVisibleItem + visibleItemCount == totalItemCount && !isCompleted && !isLoading && pagination != null) {
                isLoading = true;
                new EmployeeQueryTask().execute(pagination.getCurrentPage() + 1);
            }
        } else {
            Log.d(TAG, "NO SCROLL_STATE_IDLE");
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private class EmployeeQueryTask extends AsyncTask<Integer, Integer, List<Employee>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            footerView.setVisibility(View.VISIBLE);
            listAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<Employee> s) {
            super.onPostExecute(s);
            footerView.setVisibility(View.GONE);
            if (s != null) listAdapter.getEmployees().addAll(s);
            listAdapter.notifyDataSetChanged();
            if (pagination.getCurrentPage() == pagination.getTotalPage()) isCompleted = true;
            isLoading = false;
        }

        @Override
        protected List<Employee> doInBackground(Integer... integers) {
            if (pagination != null) pagination.setCurrentPage(integers[0]);
            Pagination<Employee> result;
            try {
                result = AppApiClient.queryContacts(columnName, condition, integers[0]);
                if (!result.getData().isEmpty()) {
                    AppLocalApiClient.storeEmployees(Search.this, result.getData());
                }
            } catch (Exception e) {
                result = new Pagination<Employee>();
                Log.e(TAG, e.getMessage(), e);
            }
            if (integers[0] <= 1) {
                pagination = result;
            }
            return result.getData();
        }
    }
}