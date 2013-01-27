package com.vouov.ailk.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.vouov.ailk.app.R;
import com.vouov.ailk.app.model.Employee;
import com.vouov.ailk.app.ui.Detail;

import java.util.List;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午8:39
 */
public class SearchResultAdapter extends BaseAdapter {
    Context context;
    List<Employee> employees;
    LayoutInflater layoutInflater;

    public SearchResultAdapter(Context context, List<Employee> employees) {
        this.context = context;
        this.employees = employees;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public int getCount() {
        return this.employees.size();
    }

    @Override
    public Object getItem(int i) {
        return this.employees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = this.layoutInflater.inflate(R.layout.search_result_item, null);
            viewHolder.lblEmployeeId = (TextView) view.findViewById(R.id.lbl_employee_id);
            viewHolder.lblMobile = (TextView) view.findViewById(R.id.lbl_mobile);
            viewHolder.lblDeptName = (TextView) view.findViewById(R.id.lbl_dept_name);
            viewHolder.lblName = (TextView) view.findViewById(R.id.lbl_name);
            viewHolder.lblNtAccount = (TextView) view.findViewById(R.id.lbl_nt_account);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.lblName.setText(employees.get(i).getName());
        viewHolder.lblEmployeeId.setText(employees.get(i).getId());
        viewHolder.lblMobile.setText(employees.get(i).getMobile());
        viewHolder.lblNtAccount.setText(employees.get(i).getAccount());
        viewHolder.lblDeptName.setText(employees.get(i).getDeptName());
        view.setOnClickListener(new ResultItemClickListener(employees.get(i)));
        return view;
    }

    private class ResultItemClickListener implements View.OnClickListener{
        private Employee employee;
        public ResultItemClickListener(Employee employee) {
            this.employee = employee;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, Detail.class);
            intent.putExtra("p_employe", this.employee);
            context.startActivity(intent);
        }
    }

    private class ViewHolder {
        public TextView lblEmployeeId;
        public TextView lblMobile;
        public TextView lblDeptName;
        public TextView lblName;
        public TextView lblNtAccount;
    }
}