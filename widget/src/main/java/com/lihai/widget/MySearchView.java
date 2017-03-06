package com.lihai.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by LiHai on 2017/2/28.
 */
public class MySearchView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private boolean canInput;
    private EditText editText;
    ImageView cancelView;
    TextView search_TextView;




    public MySearchView(Context context) {
        this(context,null);
    }

    public MySearchView(Context context, AttributeSet attrs) {

        this(context, attrs,0);
    }

    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.widget_search_view,this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MySearchView);
        canInput = typedArray.getBoolean(R.styleable.MySearchView_canInput,false);
        typedArray.recycle();




        editText = (EditText) findViewById(R.id.edit_text);
        editText.addTextChangedListener(this);



        //TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,12,getResources().getDisplayMetrics());  sp 转 dp
       // TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,12,getResources().getDisplayMetrics()); // sp 转 px


        editText.setTextSize(14);

        cancelView = (ImageView) findViewById(R.id.cancel_view);
        cancelView.setOnClickListener(this);

        search_TextView = (TextView) findViewById(R.id.search_textView);
        search_TextView.setOnClickListener(this);


        if (!canInput){
            editText.setEnabled(false);
        }


    }

    /**
     * 获取内容
     * @return
     */
    public String getContent(){

        return editText.getText().toString();
    }


    /**
     * cancelView的监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.cancel_view) {
            editText.setText("");
        }

    }

    //editText 输入触动

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (TextUtils.isEmpty(editText.getText().toString())){
            cancelView.setVisibility(GONE);
        }else {
            cancelView.setVisibility(VISIBLE);
        }

    }

    ///..................................////
}
