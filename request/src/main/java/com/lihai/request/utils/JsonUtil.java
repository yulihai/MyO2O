package com.lihai.request.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * Created by LiHai on 2017/3/4.
 */
public class JsonUtil {


    /**
     * 泛型反序列化
     * @param jsonStr
     * @param <T>
     * @return
     */
    public static <T> T jsonToOject (String jsonStr){

        try {
            return JSON.parseObject(jsonStr, new TypeReference<T>() {
            });

        }catch (Exception e){
            return null;
        }
    }


    /**
     * 泛型反序列化
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr ,Class clazz){

        return JSON.parseArray(jsonStr,clazz);
    }


}
