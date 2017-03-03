package com.lihai.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by LiHai on 2017/2/28.
 */
public class MySearchView extends LinearLayout implements View.OnClickListener {

    private boolean canInput;
    private EditText editText;
    ImageView cancelView;
    TextView searchTextView;
    private Context mContext;
    InputMethodManager imm;

    public MySearchView(Context context) {
        this(context,null);
    }

    public MySearchView(Context context, AttributeSet attrs) {

        this(context, attrs,0);
    }

    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_search_view,this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MySearchView);
        canInput = typedArray.getBoolean(R.styleable.MySearchView_canInput,false);
        typedArray.recycle();



        editText = (EditText) findViewById(R.id.edit_text);
        cancelView = (ImageView) findViewById(R.id.cancel_view);
        searchTextView = (TextView) findViewById(R.id.search_textView);

         imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!canInput){
            editText.setEnabled(false);
        }


        editText.setOnClickListener(this);
        cancelView.setOnClickListener(this);
        searchTextView.setOnClickListener(this);




    }

    /**
     * 获取内容
     * @return
     */
    public String getContent(){

        return editText.getText().toString();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.edit_text){

           if( editText.getText().toString() != null){
               cancelView.setVisibility(VISIBLE);
           }

        }else if (id == R.id.cancel_view) {

            editText.setText("");
            cancelView.setVisibility(GONE);
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS); //隐藏软键盘
        }


    }
}
