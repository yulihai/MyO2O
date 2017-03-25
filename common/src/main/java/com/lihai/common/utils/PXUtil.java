package com.lihai.common.utils;

import android.util.TypedValue;

import com.lihai.common.app.CommonApplication;


/**
 * Created by LiHai on 2017/3/9.
 */
public class PXUtil {

    /**
     * dp转像素
     * @param param
     * @return
     */
    public static int DP2PX(float param){

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,param, CommonApplication.resources.getDisplayMetrics());

    }


    public static int SP2PX(float param){

        return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,param, CommonApplication.resources.getDisplayMetrics());


    }

}
