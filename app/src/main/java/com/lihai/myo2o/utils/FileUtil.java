package com.lihai.myo2o.utils;

import android.os.Environment;

/**
 * Created by yulihai-海 on 2016/12/11.
 */
public class FileUtil {
    public static String getSDPath(){

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return null;
        }else{
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }
}
