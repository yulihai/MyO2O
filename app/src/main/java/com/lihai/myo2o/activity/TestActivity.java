package com.lihai.myo2o.activity;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.lihai.common.base.BaseActivity;
import com.lihai.myo2o.R;
import com.lihai.myo2o.utils.MyFlowLayout;

/**
 * Created by LiHai on 2017/3/16.
 */
public class TestActivity extends BaseActivity {

    private String[] data = new String[]
            { "Hello", "Android", "Welcome to ", "Button",  "Hello","TextView",
                    "Android", "Welcome", "Button ImageView", "TextView", "Hello World",
                    "Android", "Welcome Hello", "Button Text", "TextView" };


    @Override
    protected int setLayout() {
        return R.layout.flow_layout;
    }

    MyFlowLayout myFlowLayout;

    @Override
    protected void initView() {

        myFlowLayout = (MyFlowLayout) findViewById(R.id.flow_layout);

        initData();
    }

    public void initData(){
      /*  for (int i=0;i<data.length ;i++){
            TextView textView = new TextView(this);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            lp.leftMargin = 10;
            lp.rightMargin= 10;
            lp.topMargin = 10;
            lp.bottomMargin = 10;

            textView.setText(data[i]);

           // textView.setLayoutParams(lp);
            myFlowLayout.addView(textView,lp);*/

        LayoutInflater layoutInflater = LayoutInflater.from(this);
            for (int i=0 ;i<data.length; i++){

                TextView textView = (TextView) layoutInflater.inflate(R.layout.text_view_flow_layout,myFlowLayout,false);

                textView.setText(data[i]);
                myFlowLayout.addView(textView);
            }


    }

}
