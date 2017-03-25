package com.lihai.myo2o.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lihai.common.adapter.MyFragmentPagerAdapter;
import com.lihai.common.base.BaseActivity;

import com.lihai.myo2o.R;
import com.lihai.myo2o.fragment.FindFragment;
import com.lihai.myo2o.fragment.MainFragment;
import com.lihai.myo2o.fragment.MineFragment;
import com.lihai.myo2o.fragment.SaleFragment;
import com.lihai.request.utils.RequestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LiHai on 2017/2/27.
 */
public class IndexActivity extends BaseActivity implements View.OnClickListener {

    //内容区
    FrameLayout contentLayout;

    //上一次选中的Id
    int lastPosition;

    //当前内容的Fragment

    List<Fragment> fragments;

    ViewPager index_viewpager;

    LinearLayout tabRoot;



    @Override
    protected int setLayout() {
        return R.layout.layout_index;
    }

    @Override
    protected void initView() {

        fragments = new ArrayList<Fragment>();
        fragments.add(new MainFragment());
        fragments.add(new SaleFragment());
        fragments.add(new FindFragment());
        fragments.add(new MineFragment());




        //底部菜单的主布局
         tabRoot = (LinearLayout) findViewById(R.id.tab_root);
        int childCount = tabRoot.getChildCount();
        for (int i=0; i< childCount ; i++){

            View tab = tabRoot.getChildAt(i);
            tab.setTag(i);
            tab.setOnClickListener(this);
        }

        index_viewpager = (ViewPager) findViewById(R.id.index_viewpager);
        index_viewpager.setOffscreenPageLimit(4);  //设置缓存页面的个数

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getFragmentManager(),fragments);
        index_viewpager.setAdapter(myPagerAdapter);
        index_viewpager.addOnPageChangeListener(new MyPagerChangeListener());


        LinearLayout firstTabLayout = (LinearLayout) tabRoot.getChildAt(lastPosition);
        selectTab(firstTabLayout,lastPosition);




    }


    /**
     * 选中
     * @param layout
     * @param position
     */
    public void selectTab(LinearLayout layout ,int position){

        ImageView imageView = (ImageView) layout.getChildAt(0);
        switch (position){
            case 0:
                imageView.setImageResource(R.drawable.main_select);
                break;
            case 1:
                imageView.setImageResource(R.drawable.sale_select);
                break;
            case 2:
                imageView.setImageResource(R.drawable.find_select);
                break;
            case 3:
                imageView.setImageResource(R.drawable.my_select);
                break;
        }

    }


    /**
     * 取消选中
     * @param lastPosition
     */
    public void unSelectTab(int lastPosition){

        LinearLayout linearLayout = (LinearLayout) tabRoot.getChildAt(lastPosition);
        ImageView imageView = (ImageView) linearLayout.getChildAt(0);
        switch (lastPosition){
            case 0:
                imageView.setImageResource(R.drawable.main_unselect);
                break;
            case 1:
                imageView.setImageResource(R.drawable.sale_unselect);
                break;
            case 2:
                imageView.setImageResource(R.drawable.find_unselect);
                break;
            case 3:
                imageView.setImageResource(R.drawable.my_unselect);
                break;
        }

    }



    @Override
    public void onClick(View v) {

        int tabId = v.getId();

        int position = Integer.parseInt(v.getTag()+"");
        if (lastPosition == position){
            return;
        }

        //让上一个恢复可以点击 。
        tabRoot.getChildAt(lastPosition).setEnabled(true);
        //跳转到指定位置

        index_viewpager.setCurrentItem(position);
        //当前的不能再点击
        tabRoot.getChildAt(position).setEnabled(false);
        lastPosition = position;

        if (tabId == R.id.tab_mine ){
            //加载个人信息，判断是否登录
            String URL = "http://testwxys.rhy.com/e/theuser";
            RequestUtil.get(URL, null, null, false, new RequestUtil.MyCallBack() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    int status = Integer.parseInt(result.get("status") + "");
                    if (status == -91){   //未登录
                        Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        return;
                    }
                }

                @Override
                public void onFailure(String error) {

                }
            });
        }


    }

    class MyPagerAdapter extends MyFragmentPagerAdapter {

        List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {



            LinearLayout linearLayout = (LinearLayout) tabRoot.getChildAt(position);

            selectTab(linearLayout,position);

            unSelectTab(lastPosition);

            lastPosition = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }



}














/*
package com.lihai.myo2o.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lihai.common.base.BaseActivity;
import com.lihai.common.utils.FragmentUtil;

import com.lihai.myo2o.R;
import com.lihai.myo2o.fragment.FindFragment;
import com.lihai.myo2o.fragment.MainFragment;
import com.lihai.myo2o.fragment.MineFragment;
import com.lihai.myo2o.fragment.SaleFragment;

*/
/**
 * Created by LiHai on 2017/2/27.
 *//*

public class IndexActivity extends BaseActivity implements View.OnClickListener {

    //内容区
    FrameLayout contentLayout;

    //上一次选中的Id
    int lastSelectId;

    //当前内容的Fragment
    Fragment currentContentFragment;

    Fragment mainFragment,saleFragment,mineFragment,findFragment;



    @Override
    protected int setLayout() {
        return R.layout.layout_index;
    }

    @Override
    protected void initView() {

        contentLayout = (FrameLayout) findViewById(R.id.fragment_content);

        //底部菜单的主布局
        LinearLayout tabRoot = (LinearLayout) findViewById(R.id.tab_root);
        int childCount = tabRoot.getChildCount();
        for (int i=0; i< childCount ; i++){
            tabRoot.getChildAt(i).setOnClickListener(this);
        }

        select(R.id.tab_main);//默认选中第一个
    }

    */
/**
     * 监听底部tab的点击事件
     * @param v
     *//*

    @Override
    public void onClick(View v) {
        select(v.getId());

    }

    private void select(int id){

        //上一个选中的tab和现在选的tab一样，就不用再重选。
        if (lastSelectId == id){
            return;
        }
        LinearLayout tab;
        ImageView imageView;

        //取消上一个选中的tab
        if (lastSelectId != 0){
            tab = (LinearLayout) findViewById(lastSelectId);
            imageView = (ImageView) tab.getChildAt(0);

            switch (lastSelectId){
                case R.id.tab_main:
                    imageView.setImageResource(R.drawable.main_unselect);
                    break;
                case R.id.tab_sale:
                    imageView.setImageResource(R.drawable.sale_unselect);
                    break;
                case R.id.tab_find:
                    imageView.setImageResource(R.drawable.find_unselect);
                    break;
                case R.id.tab_mine:
                    imageView.setImageResource(R.drawable.my_unselect);
                    break;
            }
        }

        tab = (LinearLayout) findViewById(id);
        imageView = (ImageView) tab.getChildAt(0);
        //选择
        switch (id){
            case R.id.tab_main:
                imageView.setImageResource(R.drawable.main_select);
                if (mainFragment == null){
                    mainFragment = new MainFragment();
                }
                //显示fragment
                showFragment(mainFragment,"main");
                break;
            case R.id.tab_sale:
                imageView.setImageResource(R.drawable.sale_select);
                if (saleFragment == null){
                    saleFragment = new SaleFragment();
                }
                //显示fragment
                showFragment(saleFragment,"sale");
                break;
            case R.id.tab_find:
                imageView.setImageResource(R.drawable.find_select);
                if (findFragment == null){
                    findFragment = new FindFragment();
                }
                //显示fragment
                showFragment(findFragment,"find");
                break;
            case R.id.tab_mine:
                imageView.setImageResource(R.drawable.my_select);
                if (mineFragment == null){
                    mineFragment = new MineFragment();
                }
                //显示fragment
                showFragment(mineFragment,"mine");
                break;

        }

        lastSelectId =id;

    }

    private Fragment lastFragment;

    private void showFragment(Fragment content, String tab){

        FragmentManager fragmentManager = getFragmentManager();

        //显示fragment
        FragmentUtil.showFragment(fragmentManager,R.id.fragment_content,content,tab);

        //隐藏fragment
        FragmentUtil.hideFragment(fragmentManager,lastFragment);
        lastFragment = content;

    }


}
*/
