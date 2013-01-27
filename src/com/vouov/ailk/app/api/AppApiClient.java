package com.vouov.ailk.app.api;

import android.util.Log;
import com.vouov.ailk.app.config.AppApplication;
import com.vouov.ailk.app.model.Employee;
import com.vouov.ailk.app.model.Pagination;
import com.vouov.ailk.app.util.HttpUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午10:46
 */
public class AppApiClient {
    private static final String TAG = "ailk_api_client";
    private static final String AILK_CONTACT_QUERY_URL = "http://intra.asiainfo-linkage.com/misonline/address/employee_search.asp";
    private static final String AILK_QUERY_ENTRY_URL = "http://intra.asiainfo-linkage.com/misonline/address/employee_searchfrm.asp";
    private static AppApplication appApplication;

    public static void init(AppApplication application) {
        appApplication = application;
    }
    public static boolean login(String userName, String password) throws Exception{
       return  HttpUtil.basicAuthorize(AILK_QUERY_ENTRY_URL, userName, password);
    }

    public static Employee getEmployeeById(String id) throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("txtCmd", "search"));
        params.add(new BasicNameValuePair("txtCondition", " WHERE 1 = 1  AND upper(EI.EMPLOYEE_NUMBER) = '" + id + "' "));
        String result = HttpUtil.post(HttpUtil.getHttpClient(appApplication), AILK_CONTACT_QUERY_URL, params);
        Log.d(TAG, result);
        Pattern pattern = Pattern.compile("<tr height=32><td width='60px' class='tablecell'><abak language=javascript onclick=\"return detail\\('\\d+'\\)\">(.*?)</a></td><td  class='tablecell'>(\\d+)&nbsp;</td><td  class='tablecell'>(.*?)</td><td  class='tablecell'>(.*?)</td><td  class='tablecell'>(.*?)&nbsp;</td><td  class='tablecell'>(.*?)&nbsp;</td><td  class='tablecell'><a href=\"mailto:.*?\">(\\w+)</a>&nbsp;</td><td  class='tablecell'><a href=\"mailto:.*?\">(.*?)</a>&nbsp;</td><td  class='tablecell'>(.*?)\\((\\d*)\\)</td><td  class='tablecell'>(.*?)</td>");
        Matcher m = pattern.matcher(result);
        Employee employee=null;
        if (m.find()) {
            Log.d(TAG, m.group(0));
            employee = new Employee();
            employee.setId(m.group(2));
            employee.setAccount(m.group(7));
            employee.setName(m.group(1));
            employee.setDeptName(m.group(3));
            employee.setHomeCity(m.group(4));
            employee.setEmail(m.group(8));
            employee.setMobile(m.group(6));
            employee.setOfficePhone(m.group(5));
            employee.setParentEmployeeId(m.group(10));
            employee.setParentEmployeeName(m.group(9));
            employee.setDesc(m.group(11));
        }

        return employee;
    }

    public static Pagination<Employee> queryContacts(String condition, int currentPage) throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("txtCmd", "search"));
        params.add(new BasicNameValuePair("txtCondition", " WHERE 1 = 1  AND upper(EI.NT_ACCOUNT) LIKE '%" + condition + "%' "));
        if (currentPage > 1) {
            params.add(new BasicNameValuePair("txtPage", String.valueOf(currentPage)));
        }
        String result = HttpUtil.post(HttpUtil.getHttpClient(appApplication), AILK_CONTACT_QUERY_URL, params);
        Log.d(TAG, result);
        Pattern pattern = Pattern.compile("<tr height=32><td width='60px' class='tablecell'><abak language=javascript onclick=\"return detail\\('\\d+'\\)\">(.*?)</a></td><td  class='tablecell'>(\\d+)&nbsp;</td><td  class='tablecell'>(.*?)</td><td  class='tablecell'>(.*?)</td><td  class='tablecell'>(.*?)&nbsp;</td><td  class='tablecell'>(.*?)&nbsp;</td><td  class='tablecell'><a href=\"mailto:.*?\">(\\w+)</a>&nbsp;</td><td  class='tablecell'><a href=\"mailto:.*?\">(.*?)</a>&nbsp;</td><td  class='tablecell'>(.*?)\\((\\d*)\\)</td><td  class='tablecell'>(.*?)</td>");
        Matcher m = pattern.matcher(result);
        List<Employee> data = new ArrayList<Employee>();
        while (m.find()) {
            Log.d(TAG, m.group(0));
            Employee employee = new Employee();
            employee.setId(m.group(2));
            employee.setAccount(m.group(7));
            employee.setName(m.group(1));
            employee.setDeptName(m.group(3));
            employee.setHomeCity(m.group(4));
            employee.setEmail(m.group(8));
            employee.setMobile(m.group(6));
            employee.setOfficePhone(m.group(5));
            employee.setParentEmployeeId(m.group(10));
            employee.setParentEmployeeName(m.group(9));
            employee.setDesc(m.group(11));
            data.add(employee);
        }
        Pagination<Employee> pagination = new Pagination<Employee>();
        pagination.setCurrentPage(currentPage);
        pagination.setData(data);
        if (currentPage <= 1) {
            pattern = Pattern.compile("change_page\\('(\\d+)'\\)");
            m = pattern.matcher(result);
            int totalPage = 1;
            while (m.find()) {
                Log.d(TAG, m.group(0));
                String value = m.group(1);
                totalPage = Integer.parseInt(value);
            }
            pagination.setTotalPage(totalPage);
        }
        return pagination;
    }
}
