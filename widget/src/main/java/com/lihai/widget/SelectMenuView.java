package com.lihai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lihai.common.utils.PXUtil;

import java.util.List;

/**
 * Created by LiHai on 2017/3/9.   自定义选择菜单
 */
public class SelectMenuView extends LinearLayout {


    Context context ;

    private LinearLayout tabLayout;
    private FrameLayout contentLayout;


    private int MenuIndex =-1;



    public SelectMenuView(Context context) {
        this(context,null);
    }

    public SelectMenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        //设置总布局方向
        setOrientation(VERTICAL);


        //新建一个布局
        tabLayout = new LinearLayout(context);
        //方向
        tabLayout.setOrientation(HORIZONTAL);
        //设置参数
        LayoutParams tabLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //绑定
        tabLayout.setLayoutParams(tabLayoutParams);
        addView(tabLayout,0);//加进去 第一个


        //分割线
        View divideView = new View(context);
        LayoutParams divideLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PXUtil.DP2PX(0.3f));
        divideView.setLayoutParams(divideLayoutParams);
        addView(divideView,1);


        //新建内容布局
        contentLayout = new FrameLayout(context);
      //  contentLayout.setBackgroundColor(getResources().getColor(R.color.divider));
        contentLayout.setVisibility(GONE);//不可见
        LayoutParams contentLayoutParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentLayout.setLayoutParams(contentLayoutParam);
        addView(contentLayout,2);

    }

    /**
     * 添加tab
     * @param tabs
     * @param index
     * @param size
     */
    public void addTag(String tabs,final int index,int size) {

        //新建textView
        TextView tabView = new TextView(context);
        LayoutParams tabLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);//宽度 ，高度，比重
        tabView.setLayoutParams(tabLayoutParams);
        tabView.setText(tabs);
        //设置居中
        tabView.setGravity(Gravity.CENTER);
        tabView.setPadding(12, 12, 12, 12);  //设置padding值

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                switchMenu(v,index);
            }
        });

        tabLayout.addView(tabView, index * 2);

        if (index != size - 1) {
            //设置分割线
            View divide = new View(context);
            LayoutParams divideLayoutParams = new LayoutParams(PXUtil.DP2PX(1f), ViewGroup.LayoutParams.MATCH_PARENT);
            divide.setLayoutParams(divideLayoutParams);
            divide.setBackgroundResource(R.color.divider);
            tabLayout.addView(divide, index * 2 + 1);
        }
    }

     public void init(String[] titles , List<ListView> listViews){

        int titleSize = titles.length;

        //初始化tab
        for (int i=0 ;i<titleSize ;i++){
            addTag(titles[i],i,titleSize);
        }

        //初始化listView
        for (int i =0; i<titleSize ;i++){
            ListView itemListView = listViews.get(i);
            itemListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //添加到  contentLayout
            contentLayout.addView(itemListView);
        }
    }

    private void switchMenu(View target,int index){

        if (MenuIndex == -1){
            //需要打开
            contentLayout.setVisibility(VISIBLE);//让contentLayout可见
            //再显示对应的listView
            contentLayout.getChildAt(index).setVisibility(VISIBLE);
            MenuIndex = index;
        }else if (MenuIndex == index){

            closeMenu(index);

        }else {

            //隐藏上一个再显示当前listView
            contentLayout.getChildAt(MenuIndex).setVisibility(GONE);

            //再显示当前listView
            contentLayout.getChildAt(index).setVisibility(VISIBLE);
            MenuIndex = index;
        }

    }

    /**
     * 关闭
     * @param index
     */
    private void closeMenu(int index){

        contentLayout.getChildAt(index).setVisibility(GONE);
        contentLayout.setVisibility(GONE);
        MenuIndex = -1;

    }

    public void setTab(int index,String name){

        ((TextView)tabLayout.getChildAt(index*2)).setText(name);
        closeMenu(index);
    }






}



