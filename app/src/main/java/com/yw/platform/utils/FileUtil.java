package com.yw.platform.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.ec.thread.DownloaderThread;
import com.yw.platform.global.Constants;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 文件工具类
 */
public class FileUtil {
    public static String TAG = "FileUtil";
    public static String cookieStr;

    /**
     * 检验SDcard状态
     * @return boolean
     */
    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 保存文件文件到目录
     * @param context
     * @return 文件保存的目录
     */
    public static String setMkdir(Context context) {
        String filePath;
        if (checkSDCard()) {
            filePath = Environment.getExternalStorageDirectory() + File.separator + "platform";
        } else {
            filePath = context.getCacheDir().getAbsolutePath() + File.separator + "platform";
        }
        File file = new File(filePath);
        if (!file.exists()) {
            boolean b = file.mkdirs();
        } else {
        }
        return filePath;
    }
    public static Properties loadPorperty(String filePath) {
        Properties properties = new Properties();
        try {
            FileInputStream is = new FileInputStream(filePath);
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
    /**
     * @param 将InputStream解析成字节数组
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
    public static void savePorperty(String filePath, Properties properties) {
        try {
            FileOutputStream os = new FileOutputStream(filePath, false);
            properties.store(os, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 保存zip文件文件到目录
     * @param context
     * @return 文件zip保存的目录
     */
    public static String checkAndMkFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            boolean b = file.mkdirs();
        } else {
        }
        return filePath;
    }
    /**
     * 检查文件是否存在
     */
    public static boolean checkFileExist(String resID) {
        String filePath;
        filePath = Environment.getExternalStorageDirectory() + File.separator + "platform" + File.separator + "apps/"
                + resID;
        File file = new File(filePath);
        boolean exists = file.exists();
        if (!file.exists()) {
            file.delete();
        }
        return file.exists();
    }
    /**
     * 得到文件的名称
     * @return
     * @throws IOException
     */
    public static String getFileName(String url) {
        String name = null;
        try {
            name = url.substring(url.lastIndexOf("/") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
    /**
     * * 递归删除目录下的所有文件及子目录下所有文件 * @param dir 将要删除的文件目录 * @return boolean Returns "true" if all
     * deletions were successful. * If a deletion fails, the method stops attempting to * delete and
     * returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    public static void unZip(String unZipfileName, String mDestPath) {// unZipfileName需要解压的zip文件全路经
        FileOutputStream fileOut;
        ZipInputStream zipIn;
        ZipEntry zipEntry;
        File file;
        int readedBytes;
        byte buf[] = new byte[4096];
        try {
            zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(unZipfileName)));
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                file = new File(mDestPath + File.separator + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    // 如果指定文件的目录不存在,则创建之.
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    fileOut = new FileOutputStream(file);
                    while ((readedBytes = zipIn.read(buf)) > 0) {
                        fileOut.write(buf, 0, readedBytes);
                    }
                    fileOut.close();
                }
                zipIn.closeEntry();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        // 解压完后把zip包删除
        File fileZip = new File(unZipfileName);
        if (fileZip.exists()) {
            fileZip.delete();
        }
    }
    // 得到json的内容
    public static String getStringJson(String strURL) {
        try {
            Log.i("getStringJson", strURL);
            URL url = new URL(strURL);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            String result = "http status code: " + ((HttpURLConnection) con).getResponseCode() + "\n";
            Log.i("getStringJson", "code=" + result);
            Log.i("getStringJson", "cookie=" + ((HttpURLConnection) con).getHeaderFields().get("set-cookie"));
            // cookieStr=((HttpURLConnection)
            // con).getHeaderFields().get("set-cookie").toString();
            // HttpURLConnection.HTTP_OK
            // 判断服务端返回码是否正确
            if (HttpURLConnection.HTTP_OK != ((HttpURLConnection) con).getResponseCode()) {
                Log.i("getStringJson", ((HttpURLConnection) con).getResponseCode() + "");
                return null;
            }
            InputStream is = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer bab = new ByteArrayBuffer(32);
            int current = 0;
            while ((current = bis.read()) != -1) {
                bab.append((byte) current);
            }
            result = EncodingUtils.getString(bab.toByteArray(), HTTP.UTF_8);
            bis.close();
            is.close();
            return result;
        } catch (Exception e) {
            Log.i("getStringJson", "数据获取失败！");
            e.printStackTrace();
        }
        return null;
    }
    public static String doHttpsGet(String serverURL) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
        HttpConnectionParams.setSoTimeout(httpParameters, 20000);
        HttpClient hc = initHttpClient(httpParameters);
        HttpGet get = new HttpGet(serverURL);
        get.addHeader("Content-Type", "text/xml");
        get.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(get);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            throw new Exception(e.getLocalizedMessage());
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }
    public static HttpClient initHttpClient(HttpParams params) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient(params);
        }
    }
    // 得到json的内容
    public static String getStringJson(String strURL, Context context) {
        String result = null;
        BufferedInputStream bis = null;
        try {
            Log.i("getStringJson", strURL);
            URL url = new URL(strURL);
            URLConnection con = url.openConnection();
            con.setRequestProperty("cookie", getCookie(context));
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            Log.i("getStringJson", "code=" + result);
            Log.i("getStringJson", "cookie=" + ((HttpURLConnection) con).getHeaderFields().get("set-cookie"));
            // HttpURLConnection.HTTP_OK
            // 判断服务端返回码是否正确
            if (HttpURLConnection.HTTP_OK != ((HttpURLConnection) con).getResponseCode()) {
                Log.i("getStringJson", ((HttpURLConnection) con).getResponseCode() + "");
                // return null;
            }
            bis = new BufferedInputStream(con.getInputStream());
            ByteArrayBuffer bab = new ByteArrayBuffer(32);
            int current = 0;
            while ((current = bis.read()) != -1) {
                bab.append((byte) current);
            }
            result = EncodingUtils.getString(bab.toByteArray(), HTTP.UTF_8);
            bis.close();
            return result;
        } catch (Exception e) {
            Log.i("getStringJson", "数据获取失败！");
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                    bis = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static String getCookie(Context context) {
        String cookie = cookieStr.substring(cookieStr.indexOf("GFASESSIONID"), cookieStr.indexOf(";"));
        return cookie;
    }
    public static boolean emailAdressFormat(String emailAdress) {
        String[] emails = emailAdress.split(",");
        for (int i = 0; i < emails.length; i++) {
            if (emails[i].endsWith(">")) {
                String email = emails[i].substring(emails[i].indexOf("<") + 1, emails[i].length() - 1);
                Log.i(TAG, "email=" + email);
                if (!emailFormat(email)) {
                    return false;
                }
            } else {
                if (!emailFormat(emails[i])) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 验证输入的邮箱格式是否符合
     * @param email
     * @return 是否合法
     */
    public static boolean emailFormat(String email) {
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher mc = pattern.matcher(email);
        return mc.matches();
    }
    public static String changeUrl(String url, String str) {
        if(url.substring(8).contains("/")){
            int index = url.indexOf('/', 8);
            String urlEnds = url.substring(index);
            url = "http://" + str + urlEnds;
        }else{
            url = "http://" + str;
        }
        return url;
    }
    public static String getIpanPort(String url) {
        String ipPort;
        if(url.substring(8).contains("/")){
            int index = url.indexOf('/', 8);
            if(url.contains("http")){
                ipPort=url.substring(url.indexOf("//"));
            }else{
                ipPort=url.substring(0, index);
            }
        }else{
            if(url.contains("http")){
                ipPort=url.substring(url.indexOf("//"));
            }else{
                ipPort=url;
            }
        }
        return ipPort;
    }
    /**
     * 判断字符串的编码
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }
    public static void attach_workedWithJson(String stringJson, String downloadUrl, Handler handler, Activity context,
            String name) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringJson);
            if (jsonObject.has("code")) {
                // String code = jsonObject.getString("code");
                String note = jsonObject.getString("note");
                Message msg = new Message();
                Bundle bundle = new Bundle();
                msg.what = 0;
                bundle.putString("code", "109000513");
                bundle.putString("note", note);
                msg.setData(bundle);
                handler.sendMessage(msg);
                // if (code.equalsIgnoreCase("100201006")) {
                // handler.sendEmptyMessage(502);
                // return;
                // }
                return;
            }
            String fileType = jsonObject.getString("fileType");
            if (fileType != null && !fileType.equalsIgnoreCase("")) {
                if (!fileType.equalsIgnoreCase("zip") && !fileType.equalsIgnoreCase("rar")
                        && !fileType.equalsIgnoreCase("cebx") && !fileType.equalsIgnoreCase("ceb")
                        && !fileType.equalsIgnoreCase("txt") && !fileType.equalsIgnoreCase("log")
                        && !FileUtil.isImageType(fileType) && !FileUtil.isPDFType(fileType)) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    msg.what = 0;
                    bundle.putString("code", "109000513");
                    bundle.putString("note", "不支持的文件格式");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } else if (fileType.equalsIgnoreCase("zip") || fileType.equalsIgnoreCase("rar")) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("json", stringJson);
                    msg.setData(bundle);
                    msg.what = 4;
                    handler.sendMessage(msg);
                } else if (fileType.equalsIgnoreCase("pdf")) {
                    // pdf
                    String fileName = jsonObject.getString("fileName");
                    // fileName=new
                    // String(fileName.getBytes(getEncoding(fileName)));
                    String totalNum = jsonObject.getString("totalNum");
                    // String fileUrl=jsonObject.getString("fileUrl");
                    // 如果要下载的附件没有传过来名字，则用现在的名字代替；
                    if (name.equalsIgnoreCase("")) {
                        name = fileName;
                    }
                    File file = new File(
                            FileUtil.checkAndMkFile(FileUtil.setMkdir(context) + File.separator + "attaches/")
                                    + getMD5Str(getDownloadUrl(downloadUrl)) + "." + fileType);
                    if (jsonObject.has("updateFlag")) {
                        if (jsonObject.getString("updateFlag").equalsIgnoreCase("") && file.exists()
                                && file.length() != 0) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("fileUrl", file.getPath());
                            bundle.putString("name", name);
                            bundle.putString("type", fileType);
                            msg.setData(bundle);
                            msg.what = 3;
                            handler.sendMessage(msg);
                            return;
                        } else {
                            // 下载
                            new DownloaderThread(handler, downloadUrl + "&gfa_pageNo=1",
                                    FileUtil.checkAndMkFile(FileUtil.setMkdir(context) + File.separator + "attaches/"),
                                    // changeType(fileName, fileType)).start();
                                    getMD5Str(getDownloadUrl(downloadUrl)) + "." + fileType, name).start();
                        }
                    }
                } else if (fileType.equalsIgnoreCase("cebx")) {
                    // pdf
                    String fileName = jsonObject.getString("fileName");
                    String totalNum = jsonObject.getString("totalNum");
                    // String fileUrl=jsonObject.getString("fileUrl");
                    if (name.equalsIgnoreCase("")) {
                        name = fileName;
                    }
                    File file = new File(
                            FileUtil.checkAndMkFile(FileUtil.setMkdir(context) + File.separator + "attaches/")
                                    + getMD5Str(getDownloadUrl(downloadUrl)) + "." + fileType);
                    if (jsonObject.has("updateFlag")) {
                        if (jsonObject.getString("updateFlag").equalsIgnoreCase("") && file.exists()
                                && file.length() != 0) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("fileUrl", file.getPath());
                            bundle.putString("name", name);
                            bundle.putString("type", fileType);
                            msg.setData(bundle);
                            msg.what = 3;
                            handler.sendMessage(msg);
                            return;
                        } else {
                            // 下载
                            new DownloaderThread(handler, downloadUrl + "&gfa_pageNo=1",
                                    FileUtil.checkAndMkFile(FileUtil.setMkdir(context) + File.separator + "attaches/"),
                                    // changeType(fileName, fileType)).start();
                                    getMD5Str(getDownloadUrl(downloadUrl)) + "." + fileType, name).start();
                        }
                    }
                } else if (fileType.equalsIgnoreCase("txt") || fileType.equalsIgnoreCase("log")) {
                    // 文本
                    String fileName = jsonObject.getString("fileName");
                    String totalNum = jsonObject.getString("totalNum");
                    // String fileUrl=jsonObject.getString("fileUrl");
                    if (name.equalsIgnoreCase("")) {
                        name = fileName;
                    }
                    File file = new File(
                            FileUtil.checkAndMkFile(FileUtil.setMkdir(context) + File.separator + "attaches/")
                                    + getMD5Str(getDownloadUrl(downloadUrl)) + "." + fileType);
                    if (jsonObject.has("updateFlag")) {
                        if (jsonObject.getString("updateFlag").equalsIgnoreCase("") && file.exists()
                                && file.length() != 0) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("fileUrl", file.getPath());
                            bundle.putString("name", name);
                            bundle.putString("type", fileType);
                            msg.setData(bundle);
                            msg.what = 3;
                            handler.sendMessage(msg);
                            return;
                        } else {
                            // 下载
                            new DownloaderThread(handler, downloadUrl + "&gfa_pageNo=1",
                                    checkAndMkFile(setMkdir(context) + File.separator + "attaches/"),
                                    getMD5Str(getDownloadUrl(downloadUrl)) + "." + fileType, name
                            // changeType(fileName, fileType)
                            ).start();
                        }
                    }
                } else {
                    // 图片
                    String fileName = jsonObject.getString("fileName");
                    String totalNum = jsonObject.getString("totalNum");
                    if (name.equalsIgnoreCase("")) {
                        name = fileName;
                    }
                    File file = new File(
                            FileUtil.checkAndMkFile(FileUtil.setMkdir(context) + File.separator + "attaches/")
                                    + getMD5Str(getDownloadUrl(downloadUrl)) + "." + fileType);
                    if (jsonObject.has("updateFlag")) {
                        if (jsonObject.getString("updateFlag").equalsIgnoreCase("") && file.exists()
                                && file.length() != 0) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("fileUrl", file.getPath());
                            bundle.putString("name", name);
                            bundle.putString("type", fileType);
                            msg.setData(bundle);
                            msg.what = 3;
                            handler.sendMessage(msg);
                            return;
                        } else {
                            // 下载
                            new DownloaderThread(handler, downloadUrl + "&gfa_pageNo=1",
                                    checkAndMkFile(setMkdir(context) + File.separator + "attaches/"),
                                    getMD5Str(getDownloadUrl(downloadUrl)) + "." + fileType, name
                            // changeType(fileName, fileType)
                            ).start();
                        }
                    }
                    // Message msg = new Message();
                    // Bundle bundle = new Bundle();
                    // bundle.putString("fileUrl", downloadUrl +
                    // "&gfa_pageNo=1");
                    // msg.setData(bundle);
                    // msg.what = 5;
                    // handler.sendMessage(msg);
                }
            } else {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                msg.what = 0;
                bundle.putString("code", "109000513");
                bundle.putString("note", "不支持的文件格式");
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Message msg = new Message();
            Bundle bundle = new Bundle();
            msg.what = 0;
            bundle.putString("code", "109000513");
            bundle.putString("note", "不支持的文件格式");
            msg.setData(bundle);
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }
    public static boolean isImageType(String type) {
        if (type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jif")
                || type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("bmp")) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isPDFType(String type) {
        if (type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx") || type.equalsIgnoreCase("docm")
                || type.equalsIgnoreCase("pdf") || type.equalsIgnoreCase("ppt") || type.equalsIgnoreCase("pps")
                || type.equalsIgnoreCase("pptx") || type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("xlsm")
                || type.equalsIgnoreCase("xlsx") || type.equalsIgnoreCase("ett") || type.equalsIgnoreCase("et")
                || type.equalsIgnoreCase("wps") || type.equalsIgnoreCase("wpt") || type.equalsIgnoreCase("xlt")
                || type.equalsIgnoreCase("dps") || type.equalsIgnoreCase("dpt")) {
            return true;
        } else {
            return false;
        }
    }
    public static void writeIpToSdCard(String string) {
        try {
            File file = new File(Constants.rootDir + "/ipConfig.txt");
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(string.getBytes());
            fos.close();
            System.out.println("写入成功：");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String changeType(String fileName, String type) {
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        fileName = name + "." + type;
        return fileName;
    }
    public static String getDownloadUrl(String s) {
        String download = s.substring(s.indexOf("url=")).trim();
        return download;
    }
    /**
     * 获取单个文件的MD5值！
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
    /*
     * MD5加密
     */
    private static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        // 16位加密，从第9位到25位
        return md5StrBuff.substring(8, 24).toString().toUpperCase();
    }
}
