package com.lihai.request.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LiHai on 2017/3/4.
 */
public class RequestUtil {

     private static OkHttpClient okHttpClient;

   static {

       OkHttpClient.Builder builder = new OkHttpClient.Builder();
       builder.interceptors().add(new Interceptor() {   //拦截器
           @Override
           public Response intercept(Chain chain) throws IOException {

               Request request = chain.request();
               Response response = chain.proceed(request);

               //判断是否要登录
               String result = response.body().string();
               Map<String ,Object> resultMap = JsonUtil.jsonToOject(result);

                //-91 要登录
               if (resultMap.containsKey("status") && Integer.parseInt(resultMap.get("status")+ "") == -91){

                   //TODO  跳转到登录界面

               }

               return response;
           }
       });

       builder.cookieJar(new CookieJar() {   //保存cookie
           List<Cookie> myCookie = new ArrayList<Cookie>();
           @Override
           public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
               myCookie.addAll(cookies);
           }

           //请求要携带cookie,
           @Override
           public List<Cookie> loadForRequest(HttpUrl url) {
               return myCookie;
           }
       });

        okHttpClient =builder.build();
    }


    /**
     * post请求
     * @param Url  地址
     * @param param  请求参数
     * @param header  请求头
     * @param isXML  是否xml
     * @param myCallBack  回调
     */
    public static void post(String Url, Map<String ,String> param, final Map<String ,String> header, final boolean isXML, final MyCallBack myCallBack){


        okHttpClient = new OkHttpClient();
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


    /**
     * get请求
     * @param Url  地址
     * @param param  请求参数
     * @param header  请求头
     * @param isXML  是否xml
     * @param myCallBack  回调
     */
    public static void get(String Url, Map<String ,String> param, final Map<String ,String> header, final boolean isXML, final MyCallBack myCallBack){


        okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        //头
        if (header != null && !header.isEmpty()){
            Set<Map.Entry<String , String>> entries = header.entrySet();
            for (Map.Entry<String ,String> entry : entries){
                builder.addHeader(entry.getKey() ,entry.getValue());
            }
        }

        //参数

        if (param != null && !param.isEmpty()){
            StringBuilder SB = new StringBuilder("?");
            Set<Map.Entry<String,String>> entries = param.entrySet();
            for (Map.Entry<String ,String> entry : entries) {
                SB.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
            SB.deleteCharAt(SB.length()-1);  // 减去最后一个 &
            Url = Url  + SB.toString() ;
        }

        Request request = builder.get().url(Url).build();


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
