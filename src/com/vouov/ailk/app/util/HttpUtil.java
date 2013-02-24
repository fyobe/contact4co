package com.vouov.ailk.app.util;

import com.vouov.ailk.app.common.AppApplication;
import com.vouov.ailk.app.common.AppException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * User: yuml
 * Date: 13-1-18
 * Time: 下午9:35
 */
public class HttpUtil {
    private static final String ENCODING = "GBK";

    public static DefaultHttpClient getHttpClient() {
        return getHttpClient(null, null);
    }

    public static DefaultHttpClient getHttpClient(AppApplication application) {
        return getHttpClient(application.getUserName(), application.getPassword());
    }

    public static DefaultHttpClient getHttpClient(String userName, String password) {
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 10 * 1024);
        params.setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17");
        DefaultHttpClient httpClient = new DefaultHttpClient(params);
        if (userName != null && !"".equals(userName))
            httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(userName, password));
        return httpClient;
    }

    public static String handleHttpResponse(HttpResponse response) throws IOException, AppException {
        HttpEntity httpEntity = response.getEntity();
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new AppException("返回相应的状态异常,异常代码:" + response.getStatusLine().getStatusCode() +
                    ",代码描述:" + response.getStatusLine().getReasonPhrase());
        }
        return EntityUtils.toString(httpEntity, ENCODING);
    }

    public static String post(HttpClient httpClient, String url, List<NameValuePair> params) throws IOException, AppException {
        try {
            HttpPost post = new HttpPost(url);
            HttpEntity formEntity = new UrlEncodedFormEntity(params, ENCODING);
            post.setEntity(formEntity);
            HttpResponse response = httpClient.execute(post);
            return handleHttpResponse(response);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

    }

    public static String get(HttpClient httpClient, String url) throws IOException, AppException {
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpClient.execute(get);
            return handleHttpResponse(response);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

}
