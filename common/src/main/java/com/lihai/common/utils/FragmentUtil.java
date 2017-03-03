package com.lihai.common.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by LiHai on 2017/2/28.
 */
public class FragmentUtil {

    /**
     *
     * @param fragmentManager
     * @param contentLayoutId
     * @param content
     * @param tab
     */
    public static void showFragment(FragmentManager fragmentManager, int contentLayoutId, Fragment content,String tab){

        //事务中。。。
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment temp = fragmentManager.findFragmentByTag(tab);   //临时变量

        if (temp!= null){
            fragmentTransaction.show(temp);
        }else{
            fragmentTransaction.add(contentLayoutId,content,tab);
        }
        fragmentTransaction.commit();

    }


    public static void hideFragment(FragmentManager fragmentManager,Fragment content){

        if (content == null){
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(content);
        fragmentTransaction.commit();
    }
}
