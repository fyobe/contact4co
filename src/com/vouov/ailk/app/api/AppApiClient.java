package com.vouov.ailk.app.api;

import android.util.Log;
import com.vouov.ailk.app.common.AppApplication;
import com.vouov.ailk.app.model.Employee;
import com.vouov.ailk.app.model.Pagination;
import com.vouov.ailk.app.util.HttpUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
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
    private static AppApplication appApplication;

    public static void init(AppApplication application) {
        appApplication = application;
    }

    public static Employee login(String userName, String password) throws Exception {
        String sql = " WHERE 1 = 1  AND upper(EI.NT_ACCOUNT) = '" + userName + "' ";
        return selectEmployee(HttpUtil.getHttpClient("ailk\\"+userName, password), sql);
    }

    public static Employee getEmployeeById(String id) throws Exception {
        String sql = " WHERE 1 = 1  AND upper(EI.EMPLOYEE_NUMBER) = '" + id + "' ";
        return selectEmployee(HttpUtil.getHttpClient(appApplication), sql);
    }

    private static Employee selectEmployee(HttpClient httpClient, String sql) throws Exception {
        String result = requestContactResource(httpClient, sql, 0);
        List<Employee> data = parseEmployees(result);
        return data.isEmpty() ? null : data.get(0);
    }

    public static Pagination<Employee> queryContacts(String columnName, String condition, int currentPage) throws Exception {
       /*
        EI.EMPLOYEE_NUMBER	员工编号
        EI.LAST_NAME	员工姓名
        EI.OFFICE	分机号码
        EI.MOBILE	手机号码
        EI.NT_ACCOUNT	NT域帐号
        EI.ORG_NAME	部门名称
        */
        String sql = " WHERE 1 = 1  AND upper("+columnName+") LIKE '%" + condition + "%' ";
        Log.d(TAG, sql);
        String result = requestContactResource(HttpUtil.getHttpClient(appApplication), sql, currentPage);
        List<Employee> data = parseEmployees(result);
        Pagination<Employee> pagination = new Pagination<Employee>();
        pagination.setCurrentPage(currentPage);
        pagination.setData(data);
        if (currentPage <= 1) {
            int totalPage = parseTotalPage(result);
            pagination.setTotalPage(totalPage);
        }
        return pagination;
    }

    private static String requestContactResource(HttpClient httpClient, String condition, int currentPage) throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("txtCmd", "search"));
        params.add(new BasicNameValuePair("txtCondition", condition));
        if (currentPage > 1) {
            params.add(new BasicNameValuePair("txtPage", String.valueOf(currentPage)));
        }
        String result = HttpUtil.post(httpClient, AILK_CONTACT_QUERY_URL, params);
        Log.d(TAG, result);
        return result;
    }

    private static List<Employee> parseEmployees(String text) {
        Pattern pattern = Pattern.compile("<tr height=32><td width='60px' class='tablecell'><abak language=javascript onclick=\"return detail\\('\\d+'\\)\">(.*?)</a></td><td  class='tablecell'>(\\d+)&nbsp;</td><td  class='tablecell'>(.*?)</td><td  class='tablecell'>(.*?)</td><td  class='tablecell'>(.*?)&nbsp;</td><td  class='tablecell'>(.*?)&nbsp;</td><td  class='tablecell'><a href=\"mailto:.*?\">(\\w+)</a>&nbsp;</td><td  class='tablecell'><a href=\"mailto:.*?\">(.*?)</a>&nbsp;</td><td  class='tablecell'>(.*?)\\((\\d*)\\)</td><td  class='tablecell'>(.*?)</td>");
        Matcher m = pattern.matcher(text);
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
        return data;
    }

    private static int parseTotalPage(String text) {
        Pattern pattern = Pattern.compile("change_page\\('(\\d+)'\\)");
        Matcher m = pattern.matcher(text);
        int totalPage = 1;
        while (m.find()) {
            Log.d(TAG, m.group(0));
            String value = m.group(1);
            totalPage = Integer.parseInt(value);
        }
        return totalPage;
    }
}
