package com.lihai.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * Created by LiHai on 2017/3/5.
 */
public class ScrollTextView extends TextSwitcher implements ViewSwitcher.ViewFactory, View.OnClickListener {

    Context context;

    int speed; //速度

    List<String> notices;
    int count;

    Handler handler;



    public ScrollTextView(Context context) {
        this(context,null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;


        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ScrollTextView);
        speed = typedArray.getInteger(R.styleable.ScrollTextView_speed,2000);
        typedArray.recycle(); //回收

        setFactory(this);


        setInAnimation(context,R.anim.notice_in);
        setOutAnimation(context,R.anim.notice_out);

    }


    /**
     * 设置数据
     * @param notices
     */
    public void setDate(List<String> notices){

        if (notices == null || notices.isEmpty()){
            return;
        }

        this.notices = notices;
        this.count = notices.size();
        handler = new Handler();
        handler.postDelayed(runnable,0);
    }


    int lastPosition;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,speed);
            setText(notices.get(lastPosition));  //给TextView 设值
            lastPosition = lastPosition+1;
            if (lastPosition == count){     //lastPosition == count 时，设lastPosition = 0 ，继续循环。
                lastPosition = 0;
            }
        }
    };



    /**
     * 构造要显示的控件
     * @return
     */
    @Override
    public View makeView() {

        TextView textView = new TextView(context);
        textView.setTextColor(getResources().getColor(R.color.red)); //给TextView设颜色
        textView.setOnClickListener(this);
        return textView;
    }

    /**
     * 点击TextView时触发
     * @param v
     */
    @Override
    public void onClick(View v) {

    }
}
