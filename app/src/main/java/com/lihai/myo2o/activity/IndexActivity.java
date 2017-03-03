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

/**
 * Created by LiHai on 2017/2/27.
 */
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

    /**
     * 监听底部tab的点击事件
     * @param v
     */
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
