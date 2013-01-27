package com.vouov.ailk.app.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.vouov.ailk.app.R;
import com.vouov.ailk.app.common.AppException;
import com.vouov.ailk.app.util.FileUtils;
import com.vouov.ailk.app.util.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * User: yuml
 * Date: 13-1-24
 * Time: 下午9:59
 */
public class FileDownloadTask extends AsyncTask<String, Long, File> {
    private static final String TAG = "ailk_FileDownloadTask";
    private HttpGet httpGet;
    private long contentLength;
    protected Context mContext;
    private String mUrl;
    protected File mSaveFile;
    private String mProgressTitle;
    private ProgressBar mProgressBar;
    private TextView mProgressText;
    private int lastProgress = -1;
    private long lastProgressTime = Calendar.getInstance().getTimeInMillis();
    //下载对话框
    private Dialog mDownloadDialog;

    public FileDownloadTask(Context context, String url, File saveFile) {
        this(context, url, saveFile, null);
    }

    public FileDownloadTask(Context context, String url, File saveFile, String progressTitle) {
        super();
        this.mContext = context;
        this.mUrl = url;
        this.mSaveFile = saveFile;
        this.mProgressTitle = progressTitle;
    }


    /**
     * 显示下载对话框
     */
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mProgressTitle == null ? "正在下载文件" : mProgressTitle);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.file_download_progress, null);
        mProgressBar = (ProgressBar) v.findViewById(R.id.update_progress);
        mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
        builder.setView(v);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FileDownloadTask.this.cancel(true);
                dialog.dismiss();

            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                FileDownloadTask.this.cancel(true);
                dialog.dismiss();
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showDownloadDialog();
        httpGet = new HttpGet(this.mUrl);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (file != null && mSaveFile != null) {
            file.renameTo(mSaveFile);
        }
        mDownloadDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        int progress = new BigDecimal(values[0] * mProgressBar.getMax()).divide(new BigDecimal(contentLength), 0, BigDecimal.ROUND_HALF_DOWN).intValue();
        if (lastProgress != progress) {
            mProgressBar.setProgress(progress);
            lastProgress = progress;
        }
        //1s更新一次
        if (lastProgressTime + 1000 < Calendar.getInstance().getTimeInMillis()) {
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            numberFormat.setMaximumFractionDigits(2);
            StringBuffer sb = new StringBuffer(numberFormat.format(new BigDecimal(values[0]).divide(new BigDecimal(contentLength), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue()));
            sb.append("\t\t\t");
            sb.append(FileUtils.formatSize(values[0])).append("/").append(FileUtils.formatSize(contentLength));
            mProgressText.setText(sb.toString());
            lastProgressTime = Calendar.getInstance().getTimeInMillis();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d(TAG, "Request URL cancelled: " + mUrl);
        if (httpGet != null && !httpGet.isAborted()) {
            httpGet.abort();
        }
    }

    @Override
    protected File doInBackground(String... args) {
        FileOutputStream fos = null;
        InputStream is = null;
        HttpClient httpClient = null;
        File tempFile = null;
        try {
            Log.d(TAG, "Download URL: " + mUrl);
            httpClient = HttpUtil.getHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new AppException("返回相应的状态异常,异常代码:" + httpResponse.getStatusLine().getStatusCode() +
                        ",代码描述:" + httpResponse.getStatusLine().getReasonPhrase());
            }
            contentLength = httpEntity.getContentLength();
            publishProgress(0L);
            tempFile = new File(mSaveFile.getPath() + ".tmp");
            fos = new FileOutputStream(tempFile);
            is = httpEntity.getContent();
            byte[] buffer = new byte[1024];
            int len;
            long downloadedLen = 0;
            while (!isCancelled() && (len = is.read(buffer)) != -1) {
                downloadedLen += len;
                fos.write(buffer, 0, len);
                publishProgress(downloadedLen);
            }
            if (isCancelled()) {
                Log.d(TAG, "Abort the request");
                httpGet.abort();
            } else {
                return tempFile;
            }
        } catch (Exception e) {
            Log.e(TAG, "下载文件出现异常", e);
            httpGet.abort();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        if (isCancelled() && tempFile != null) {
            Log.d(TAG, "因为取消动作，删除未完成的缓存文件");
            tempFile.delete();
        }
        return null;
    }
}
