package com.lihai.request.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LiHai on 2017/3/4.
 */
public class RequestUtil {

    /*public static void post(String Url, Map<String ,String> header,Map<String ,String> param,final boolean isXML,final MyCallback myCallback){

        FormBody.Builder builder = new FormBody.Builder();

        if (param != null && !param.isEmpty()){

            Set<Map.Entry<String ,String>> entries = param.entrySet();
            for (Map.Entry<String ,String> entry:entries){
                builder.add(entry.getKey() , entry.getValue());
            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(Url).post(builder.build());

        if (header != null && !header.isEmpty()){
          //  Set<Map.Entry<String , String>> entries = header.entrySet();
            for (Map.Entry<String ,String> entry : header.entrySet()){
                requestBuilder.addHeader(entry.getKey() ,entry.getValue());
            }
        }

        Request request = requestBuilder.build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Call call =okHttpClient.newCall(request);

        call.enqueue(new Callback() {

            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {

                myCallback.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                myCallback.onSuccess(response.body().string());
                Message message = new Message();
                message.what = 1;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });

    }


    public interface MyCallback{
        void onFailure();
        void onSuccess(String result );
    }
*/


    public static void post(String Url, Map<String ,String> param, final Map<String ,String> header, final boolean isXML, final MyCallBack myCallBack){

        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder().url(Url);

        //头
        if (header != null && !header.isEmpty()){
            Set<Map.Entry<String , String>> entries = header.entrySet();
            for (Map.Entry<String ,String> entry : entries){
                builder.addHeader(entry.getKey() ,entry.getValue());
            }
        }

        //参数
        FormBody.Builder requestBuilder = new FormBody.Builder();

        if (param != null && !param.isEmpty()){
            Set<Map.Entry<String,String>> entries = param.entrySet();
            for (Map.Entry<String ,String> entry : entries)
                requestBuilder.add(entry.getKey() , entry.getValue());
        }

        Request request = builder.post(requestBuilder.build()).build();


        okHttpClient.newCall(request).enqueue(new Callback() {

            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {

                if (myCallBack != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myCallBack.onFailure("请求失败");
                        }
                    });
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(result)){
                            myCallBack.onFailure("请求失败");
                        }else {
                            if (!isXML){
                                if (myCallBack!= null){
                                    Map<String ,Object> map = JsonUtil.jsonToOject(result);
                                    if (map ==null){
                                        myCallBack.onFailure("请求失败");
                                    }
                                    myCallBack.onSuccess(map);
                                }
                            }else {
                                //TODO
                            }
                        }
                    }
                });
            }
        });
    }


    public  interface  MyCallBack{
        void onSuccess(Map<String ,Object> result);
        void onFailure(String error);
    }

}
