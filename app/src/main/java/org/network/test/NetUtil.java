package org.network.test;

import android.accounts.NetworkErrorException;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/3/2.
 */

public class NetUtil {


    public static Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private static int name = 1;

    //post方法
    public static String post(String url, String content) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            //post请求的参数
            String request_data = content;
            OutputStream out = conn.getOutputStream();
            out.write(request_data.getBytes());
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String response = getString(is);
                return response;
            } else {
                throw new NetworkErrorException("response status Code:" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }

    //get方法
    public static String get(String url) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String response = getString(is);
                return response;
            } else {
                throw new NetworkErrorException("response status Code:" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }

    public static String getString(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int num;
        while ((num = is.read(buffer)) != -1) {
            os.write(buffer, 0, num);
        }
        is.close();
        String str = os.toString();
        os.close();
        return str;
    }

    public static Long getFile(String param) {
        HttpURLConnection conn = null;
        name++;
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ccc");
        File file = new File(dir.getAbsolutePath() + File.separator + name + ".apk");
        if (!dir.exists()) dir.mkdir();
        byte[] bytes;
        try {
            file.createNewFile();
            URL murl = new URL(param);
            conn = (HttpURLConnection) murl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(150000);
            conn.setReadTimeout(5000);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()));
                bytes = new byte[1024];
                int len;
                while ((len = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                }
                bis.close();
                bos.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return file != null ? file.length() : 0;
    }
}
