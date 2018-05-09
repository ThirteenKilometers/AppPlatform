package com.yw.platform.utils;

import com.yw.platform.global.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HttpClient {

    DefaultHttpClient client;
    private static HttpClient httpClient;
    private String strCookies = "";

    private HttpClient() {
        client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
    }

    public static HttpClient getInstance() {
        if (httpClient == null) {
            httpClient = new HttpClient();
        }
        return httpClient;
    }

    public String executeGet(String url) {
        String result = null;
        BufferedReader reader = null;
        try {
            //DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpConnectionParams.setSoTimeout(httpParams, 30000);
            client.setParams(httpParams);
            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            reader.close();
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//		finally {
//			if (reader != null) {
//				try {
//					reader.close();
//					reader = null;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
        return result;
    }


    public String executeGet(String url, String strCookie) {
        String result = null;
        BufferedReader reader = null;
        setCookies(strCookie);
        try {
            // DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            request.setHeader("Cookie", strCookie);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpConnectionParams.setSoTimeout(httpParams, 30000);
            client.setParams(httpParams);

            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            reader.close();
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//		finally {
//			if (reader != null) {
//				try {
//					reader.close();
//					reader = null;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
        return result;
    }

    public List<Cookie> getAllCookies() {
        return client.getCookieStore().getCookies();
    }

    public CookieStore getCookieStore() {
        return client.getCookieStore();
    }

    // 下载附件时用
    public String getCookies() {
        return strCookies;
    }

    public void setCookies(String strCookie) {
        this.strCookies = strCookie;
    }

    //证书认证接口
    public String executePost_verify(String p7, String ip, String port, String userId) {
        String result = null;
        BufferedReader reader = null;
        try {
//			DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI("http://" + ip + ":" + port + "/platform/authentication"));
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

            // 接收参数json列表
            JSONObject jsonParam1 = new JSONObject();
            jsonParam1.put("ip", "127.0.0.1");
            jsonParam1.put("logonName", userId);
            jsonParam1.put("p7", p7);


            JSONObject jsonParam = new JSONObject();
            jsonParam.put("authnMethod", "X509SIGN");
            jsonParam.put("parameters", jsonParam1);

            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            request.setEntity(entity);


            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //认证接口
    public String executePost_verifyWithoutVPN(String ip, String port, String account, String password, String deviceId) {
        String result = null;
        BufferedReader reader = null;
        try {
//			DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI("http://" + ip + ":" + port + "/platform/authentication"));
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

            // 接收参数json列表
            JSONObject jsonParam1 = new JSONObject();
            jsonParam1.put("ip", "127.0.0.1");
            jsonParam1.put("logonName", account);
            jsonParam1.put("password", password);
            jsonParam1.put("deviceID", deviceId);


            JSONObject jsonParam = new JSONObject();
            jsonParam.put("authnMethod", "PASSWORD");
            jsonParam.put("parameters", jsonParam1);

            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            request.setEntity(entity);


            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //设备注册接口
    public String executePost_Regist(String ip, String port, String account, String deviceID, String deviceModel) {
        String result = null;
        BufferedReader reader = null;
        try {
            //DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI("http://" + ip + ":" + port + "/platform/device/enroll"));
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            // 接收参数json列表
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("logonName", account);
            jsonParam.put("deviceID", deviceID);
            jsonParam.put("deviceType", "android");
            jsonParam.put("deviceModel", deviceModel);

            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            request.setEntity(entity);


            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //日志接口
    public String executePost_logSend(JSONObject jsonParam, String ip, String port) {
        String result = null;
        BufferedReader reader = null;
        try {
            HttpPost request = new HttpPost();
            request.setURI(new URI("http://" + ip + ":" + port + "/platform/logging/trace"));
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);

            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            request.setEntity(entity);


            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //获取验证码接口
    public String getAuthCode(String phone) {
        String result = null;
        BufferedReader reader = null;
        try {
//			DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            request.setURI(new URI("http://" + Constants.platIpRegister + ":" + Constants.platPortRegister
                    + "/CloudPlatform/back/EmpRegisterAction_getVerifyCode.do"));
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("userCode", phone));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //用户注册接口
    public String registerAccount(String company, String phone, String password, String name, String authCode) {
        String result = null;
        BufferedReader reader = null;
        try {
//			DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            request.setURI(new URI("http://" + Constants.platIpRegister + ":" + Constants.platPortRegister
                    + "/CloudPlatform/back/EmpRegisterAction_empRegister.do"));
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("comUserCode", company));
            postParameters.add(new BasicNameValuePair("userCode", phone));
            postParameters.add(new BasicNameValuePair("passWord", password));
            postParameters.add(new BasicNameValuePair("name", name));
            postParameters.add(new BasicNameValuePair("verifyCode", authCode));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters, HTTP.UTF_8);
            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //获取策略接口
    public String post_getAdressConfig(String ip, String port) {
        String result = null;
        BufferedReader reader = null;
        try {
//			DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            request.setURI(new URI("http://" + ip + ":" + port + "/platform/policy/global"));
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("deviceType", "ANDROIDPHONE"));
            postParameters.add(new BasicNameValuePair("deviceOsType", "ANDROID"));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //消息注册接口
    public String post_messageRegist(String url, String userId, String code) {
        String result = null;
        BufferedReader reader = null;
        try {
//			DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            request.setURI(new URI(url));
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("userID", userId));
            postParameters.add(new BasicNameValuePair("osType", "1"));
            postParameters.add(new BasicNameValuePair("subscriberCode", code));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);


            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //获取角标接口
    public String post_messageNumber(String url, String userId) {
        String result = null;
        BufferedReader reader = null;
        try {
//			DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            request.setURI(new URI(url));
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("userID", userId));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);


            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //获取应用列表
    public String post_getAppList(String UserId, String ip, String port) {
        String result = null;
        BufferedReader reader = null;
        try {
//				DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            request.setURI(new URI("http://" + ip + ":" + port + "/platform/app"));
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("deviceType", "ANDROIDPHONE"));
            postParameters.add(new BasicNameValuePair("userID", UserId));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);


            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
