package com.lihai.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by LiHai on 2017/2/28.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int setLayout();

    protected abstract void initView();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        initView();
    }
}
