package com.lihai.myo2o.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lihai.common.adapter.MyFragmentPagerAdapter;
import com.lihai.common.base.BaseFragment;
import com.lihai.myo2o.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiHai on 2017/2/28.
 */
public class MainFragment extends BaseFragment {

    ViewPager advPager ,typePager;


    MyAdvAdapter myAdvAdapter ;


    List<ImageView> advImages = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();


    CirclePageIndicator advIndicator ,typeIndicator;



    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init(View view) {

        //advPager
        int [] imageResId = new int[]{R.drawable.pet01,R.drawable.pet02,R.drawable.pet03,R.drawable.pet04,R.drawable.pet05};
        int length = imageResId.length;
        for (int i=0;i<length ;i++){
            ImageView imageView = new ImageView(activity);
            imageView.setBackgroundResource(imageResId[i]);
            advImages.add(imageView);
        }

        myAdvAdapter = new MyAdvAdapter();
        advPager = (ViewPager) view.findViewById(R.id.adv_pager);
        advPager.setAdapter(myAdvAdapter);

        //指示器
        advIndicator = (CirclePageIndicator) view.findViewById(R.id.advIndicator);
       // advIndicator.set
        advIndicator.setViewPager(advPager);




        //Fragment

        for (int i=0; i<3 ;i++){
            TypeFragment typeFragment = new TypeFragment();

            fragments.add(typeFragment);
        }

        typePager = (ViewPager) view.findViewById(R.id.type_pager);
        //在Fragment里面添加子Fragment，需要。。
        FragmentManager fragmentManager = getFragmentManager();

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyTypeAdapter(fragmentManager,fragments);
        typePager.setAdapter(myFragmentPagerAdapter);

        //指示器
        typeIndicator = (CirclePageIndicator) view.findViewById(R.id.typeIndicator);
        typeIndicator.setViewPager(typePager);


    }


   class MyAdvAdapter extends PagerAdapter{

       @Override
       public int getCount() {
           return advImages.size();
       }

       @Override
       public boolean isViewFromObject(View view, Object object) {
           return view == object;


       }

       @Override
       public Object instantiateItem(ViewGroup container, int position) {
           ImageView imageView = advImages.get(position);
           container.addView(imageView);
           return imageView;


       }

       @Override
       public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((ImageView)object);

       }
   }

    class MyTypeAdapter extends MyFragmentPagerAdapter{

        List<Fragment> fragments;

        public MyTypeAdapter(FragmentManager fm ,List<Fragment> fragments) {
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



}
