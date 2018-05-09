package com.yw.platform.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LocationUtils {
    private static String TAG = "LocationUtils";
    private static String assetsFlag = "assets";
    private static String dataPath = "";
    private static String filePath = "";
    private static String filePath1 = "";
    static Context con;

    /**
     * copy all the data in assets to filePath
     *
     * @param context
     * @return
     */
    public static boolean copyAsset(Context context) {
        con = context;
        String apkPath = context.getPackageCodePath();
        Log.i(TAG, apkPath);
        dataPath = "data/data/" + context.getPackageName();
        filePath = FileUtil.setMkdir(context) + File.separator + "app";
        filePath1 = FileUtil.setMkdir(context) + File.separator;
        File destFile = new File(filePath);
//		if (destFile != null && destFile.exists()) {
//			Log.i(TAG, "本地oa包已经存在,删除。。。");
//			FileUtil.deleteDir(destFile);
//		}
        // 同样删除JS文件
        File jsFile = new File(FileUtil.setMkdir(context) + File.separator + "js");
        if (jsFile.exists()) {
            Log.i(TAG, "本地插件已经存在,删除。。。");
            FileUtil.deleteDir(jsFile);
        }
        File destPath;
        ZipInputStream zis = null;
        ZipEntry zipent = null;
        BufferedOutputStream bos = null;
        byte[] buf = null;
        int bufLen = 2048;
        int count = -1;
        try {
            zis = new ZipInputStream(new FileInputStream(apkPath));
            buf = new byte[bufLen];
            while ((zipent = zis.getNextEntry()) != null) {
                String entName = zipent.getName();
                if (!entName.startsWith(assetsFlag)) {
                    continue;
                }
                entName = entName.substring(entName.indexOf(assetsFlag)
                        + assetsFlag.length());
                if (entName == null || entName.trim().equalsIgnoreCase("")) {
                    continue;
                }
                String outFile = filePath1 + entName;
                String outPath = outFile.substring(0, outFile
                        .lastIndexOf(File.separator));
                destPath = new File(outPath);
                if (!destPath.exists()) {
                    if (!destPath.mkdirs()) {
                        continue;
                    }
                }
                destFile = new File(outFile);
                if (destFile.exists()) {
                    continue;
                }
                if (!destFile.createNewFile()) {
                    continue;
                }

                bos = new BufferedOutputStream(new FileOutputStream(destFile),
                        bufLen);
                while ((count = zis.read(buf, 0, bufLen)) != -1) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
                bos.close();
            }
            zis.close();
        } catch (IOException e) {
            return false;
        }
        destFile = new File(filePath);
        if (destFile.exists()) {
            return true;
        }
        return true;
    }
}
