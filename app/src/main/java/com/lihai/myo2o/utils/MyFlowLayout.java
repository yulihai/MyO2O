package com.lihai.myo2o.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiHai on 2017/3/16. 流式布局
 */

public class MyFlowLayout extends ViewGroup
{

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        //
    }

    public MyFlowLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyFlowLayout(Context context)
    {
        this(context, null);
    }

    /**
     * 测量控件所需的空间
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        //3种模式
        //MeasureSpec.EXACTLY        用于    match-parent   /  100dp
        // MeasureSpec.AT_MOST               warp_content
        //MeasureSpec.UNSPECIFIED             adapterView 控件要多大就给多大
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);   //屏幕宽度
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);   //屏幕高度
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;

        // 记录每一行的宽度与高度
        int lineWidth = 0;
        int lineHeight = 0;

        // 得到内部元素的个数
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++)
        {
            View child = getChildAt(i);
            // 测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();

            // 子View占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            // 子View占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            // 换行
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight())   // - getPaddingLeft() - getPaddingRight()  flowLayout布局如果要用到padding ，必需用
            {
                // 对比得到最大的宽度
                width = Math.max(width, lineWidth);
                // 重置lineWidth
                lineWidth = childWidth;
                // 记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else
            // 未换行
            {
                // 叠加行宽
                lineWidth += childWidth;
                // 得到当前行最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 最后一个控件
            if (i == cCount - 1)
            {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

       /* Log.e("TAG", "sizeWidth = " + sizeWidth);
        Log.e("TAG", "sizeHeight = " + sizeHeight);*/

        setMeasuredDimension(
                //
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop()+ getPaddingBottom()//
        );

    }

    /**
     * 存储所有的View
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        mAllViews.clear();
        mLineHeight.clear();

        // 当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<View>();

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++)
        {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight())
            {
                // 记录LineHeight
                mLineHeight.add(lineHeight);
                // 记录当前行的Views
                mAllViews.add(lineViews);

                // 重置我们的行宽和行高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                // 重置我们的View集合
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);

        }// for end
        // 处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        // 设置子View的位置

        int left = getPaddingLeft();   //布局用到padding
        int top = getPaddingTop();

        // 行数
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++)
        {
            // 当前行的所有的View
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++)
            {
                View child = lineViews.get(j);
                // 判断child的状态
                if (child.getVisibility() == View.GONE)
                {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                // 为子View进行布局
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin
                        + lp.rightMargin;
            }
            left = getPaddingLeft() ;
            top += lineHeight ;
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

}

















/*
package com.lihai.myo2o.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by LiHai on 2017/3/16. 流式布局
 *//*


public class MyFlowLayout extends ViewGroup {
    public MyFlowLayout(Context context) {
        this(context,null);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    */
/**
     * 测量控件所需的空间  width  height
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     *//*

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //3种模式
        //MeasureSpec.EXACTLY        用于    match-parent   /  100dp
        // MeasureSpec.AT_MOST               warp_content
        //MeasureSpec.UNSPECIFIED             adapterView

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);   //屏幕宽度

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);   //屏幕的高度

        int lineWidth = 0;//目前最长宽度
        int lineHeight = 0;   //目前行最高的

        int totalHeight = 0;
        int tempWidth = 0;    //目前行的宽度

        if (widthMode == MeasureSpec.AT_MOST){  //wrap 计算子控件的宽度
            //测量子控件的宽度
            int childCount = getChildCount(); //获取子控件的数量

            for (int i=0; i<childCount ;i++){
                View childView  = getChildAt(i);

                measureChild(childView,widthMeasureSpec,heightMeasureSpec);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                int childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                int childHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

                if (childWidth + tempWidth >width){     //换行

                    lineWidth = Math.max(lineWidth,tempWidth);
                    tempWidth = childWidth;

                    totalHeight = lineHeight + totalHeight ;
                    lineHeight = childHeight;

                }else {      //不换行
                    tempWidth = tempWidth +childWidth;
                    //totalHeight = totalHeight + lineHeight;
                    lineHeight = Math.max(lineHeight,childHeight);
                }

                if (i == childCount-1){
                    lineWidth = Math.max(lineWidth,tempWidth);
                    totalHeight = totalHeight + lineHeight;
                }
            }
        }
        setMeasuredDimension(

                widthMode == MeasureSpec.EXACTLY? width: lineWidth,
                heightMode == MeasureSpec.EXACTLY?height:totalHeight);

    }

    */
/**
     * 控制子控件的位置
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     *//*

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int width = getWidth();


        //多行  --》  第一行的控件集合
        List<List<View>> lineListView = new ArrayList<List<View>>();
        List<Integer> lineHeights = new ArrayList<Integer>();

        //获取控件摆放的位置信息
        int childCount = getChildCount();

        int lineWidth = 0; //目前最宽的宽度
       // int tempWidth =0;  //目前行宽度

        int lineHeight=0;

        List<View> lineView = new ArrayList<View>();
        for (int i=0 ; i<childCount; i++){
            View childView = getChildAt(i);

            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (lineWidth + childWidth >width) {
               // lineWidth = Math.max(lineWidth, tempWidth);
                lineListView.add(lineView);
                lineHeights.add(lineHeight);
                //重置

                lineView = new ArrayList<View>();
                lineWidth = 0;
                lineHeight = childHeight;

               // tempWidth = childWidth;
            }else {
               // tempWidth = tempWidth + childWidth ;
                lineWidth = childWidth +lineWidth;
                lineHeight = Math.max(lineHeight,childHeight);
                lineView.add(childView);
            }

            if (i== childCount-1){
                lineListView.add(lineView);
                lineHeights.add(lineHeight);
            }
        }

        //摆放  位置

        int lineCount = lineListView.size();

       // int baseHeight =0;
        int preLineWidth = 0;
        int baseHeight = 0;


        for (int i=0 ;i<lineCount ;i++){

            List<View> tempLineView = lineListView.get(i);
            int tempLineViewCount = tempLineView.size();
           // int preLineWidth = 0;

            for (int j=0 ;j<tempLineViewCount; j++){
                View tempChildView = tempLineView.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) tempChildView.getLayoutParams();

                int leftPosition = preLineWidth + layoutParams.leftMargin;
                int topPosition = layoutParams.topMargin + baseHeight;
                int rightPosition = leftPosition + tempChildView.getMeasuredWidth();
                int bottomPosition = topPosition + tempChildView.getMeasuredHeight();

                tempChildView.layout(leftPosition,topPosition,rightPosition,bottomPosition);
                preLineWidth = rightPosition + layoutParams.rightMargin;
            }
            preLineWidth = 0;
            baseHeight = baseHeight + lineHeights.get(i);
        }

    }


    */
/**
     *  设置子控件的layoutParams
     * @param attrs
     * @return
     *//*

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs){

        return  new MarginLayoutParams(getContext(),attrs);

    }


}
*/
