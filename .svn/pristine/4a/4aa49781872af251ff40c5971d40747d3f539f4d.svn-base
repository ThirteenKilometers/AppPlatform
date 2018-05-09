
package com.yw.platform.tools;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ApkCheckUtils {
    private static final String TAG = "ApkCheckUtils";
    private static Set<String> fileItemList = new HashSet<String>();
    static {
        fileItemList.add("META-INF/CERT.RSA");
        fileItemList.add("META-INF/CERT.SF");
        fileItemList.add("META-INF/MANIFEST.MF");
        fileItemList.add("AndroidManifest.xml");
        fileItemList.add("resources.arsc");
        fileItemList.add("classes.dex");
    }

    public static boolean isLegalApk(String apkPath) {
        boolean result = true;
        long start = System.currentTimeMillis();
        ZipEntry entry = null;
        ZipFile zipFile = null;
        ZipInputStream zipInput = null;
        try {
            File file = new File(apkPath);
            zipFile = new ZipFile(file);
            zipInput = new ZipInputStream(new FileInputStream(file));
            InputStream input = null;
            while ((entry = zipInput.getNextEntry()) != null) {
                if (fileItemList.contains(entry.getName())) {
                    input = zipFile.getInputStream(entry);
                    input.skip(1024 * 10000);
                    input.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "check apk file error: " + entry.getName());
            result = false;
            e.printStackTrace();
        } finally {
            if (zipInput != null) {
                try {
                    zipInput.close();
                } catch (IOException e) {
                    result = false;
                    e.printStackTrace();
                }
            }
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    result = false;
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "check used time:" + (System.currentTimeMillis() - start) + "ms.");
        }
        return result;
    }
}
