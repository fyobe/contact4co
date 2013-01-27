package com.vouov.ailk.app.util;

import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * User: yuml
 * Date: 13-1-25
 * Time: 下午9:57
 */
public class FileUtils {
    public static String formatSize(long size) {
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return decimalFormat.format(new BigDecimal(size).divide(new BigDecimal(1024), 2,BigDecimal.ROUND_HALF_UP).doubleValue()) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            return decimalFormat.format(new BigDecimal(size).divide(new BigDecimal(1024 * 1024), 2,BigDecimal.ROUND_HALF_UP).doubleValue()) + "MB";
        } else {
            return decimalFormat.format(new BigDecimal(size).divide(new BigDecimal(1024 * 1024 * 1024), 2,BigDecimal.ROUND_HALF_UP).doubleValue()) + "GB";
        }
    }

    public static File getSDFile(String filePath){
        File file= null;
        //判断是否挂载了SD卡
        String storageState = Environment.getExternalStorageState();
        if(storageState.equals(Environment.MEDIA_MOUNTED)){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + filePath;
            file = new File(path);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    public static File getSDFileDir(String dirPath){
        File file= null;
        //判断是否挂载了SD卡
        String storageState = Environment.getExternalStorageState();
        if(storageState.equals(Environment.MEDIA_MOUNTED)){
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + dirPath;
            file = new File(filePath);
            if(!file.exists()){
                file.mkdirs();
            }
        }
        return file;
    }

    public static String getExtension(File file) {
        return (file != null) ? getExtension(file.getName()) : "";
    }

    public static String getExtension(String filename) {
        return getExtension(filename, "");
    }

    public static String getExtension(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');
            if(i!=-1) return filename.substring(i + 1);
        }
        return defExt;
    }

    public static String getNoExtName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');
            if(i!=-1) filename.substring(0, i);
        }
        return filename;
    }

    public static void main(String[] args) {
        System.out.println(formatSize(1129));
    }
}
