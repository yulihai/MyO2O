package com.lihai.myo2o.utils;

/**
 * Created by LiHai on 2017/3/18.
 */

import android.animation.TypeEvaluator;

/**
 * 自定义插值器  属性动画
 */
public class  MyTypeEvaluator implements TypeEvaluator<int[]> {

    private int[] controlPoint;
    public MyTypeEvaluator(int[] controlPoint){

        this.controlPoint = controlPoint;
    }


    @Override
    public int[] evaluate(float t, int[]  p0, int[] p2) {

        //(1-t)2P0+2t(1-t)P1+t2P2   贝塞尔曲线   t：时间   p0 :始点  p2:终点  p1:控制点

        int[] result = new int[2];

        result[0] = (int) ((1-t)*(1-t)*p0[0] + 2*t*(1-t)*controlPoint[0] + t*t*p2[0]);
        result[1] = (int) ((1-t)*(1-t)*p0[1] + 2*t*(1-t)*controlPoint[1] + t*t*p2[1]);

        return result;
    }
}
