package com.lihai.common.utils;

/**
 * Created by LiHai on 2017/3/18.
 */
public class StringUtil {

    /**
     * 判断是否为空
     * @param param
     * @return
     */
    public static boolean isEmpty(String param){

        if (param == null || "".equals(param)){
            return true;
        }
        return false;
    }
}
