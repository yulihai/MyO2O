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
import com.lihai.myo2o.damain.ShopType;
import com.lihai.request.utils.JsonUtil;
import com.lihai.request.utils.RequestUtil;
import com.lihai.widget.ScrollTextView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LiHai on 2017/2/28.
 */
public class MainFragment extends BaseFragment {

    ViewPager advPager ,typePager;

    MyAdvAdapter myAdvAdapter ;


    List<ImageView> advImages = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();


    CirclePageIndicator advIndicator ,typeIndicator;   //指示器


    MyTypeAdapter myFragmentPagerAdapter;

    ScrollTextView scrollTextView; //公告Text

    List<ImageView> imageLists;

    ImageView image_1, image_2;


    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init(View view) {


        image_1 = (ImageView) view.findViewById(R.id.image_1);
        image_2 = (ImageView) view.findViewById(R.id.image_2);


        //advPager   本地加载 广告
       /* int [] imageResId = new int[]{R.drawable.pet01,R.drawable.pet02,R.drawable.pet03,R.drawable.pet04,R.drawable.pet05};
        int length = imageResId.length;
        for (int i=0;i<length ;i++){
            ImageView imageView = new ImageView(activity);
            imageView.setBackgroundResource(imageResId[i]);
            advImages.add(imageView);
        }*/

        //广告。。。。。。。。。。。。。。。
        myAdvAdapter = new MyAdvAdapter();
        advPager = (ViewPager) view.findViewById(R.id.adv_pager);
        advPager.setAdapter(myAdvAdapter);

        //指示器
        advIndicator = (CirclePageIndicator) view.findViewById(R.id.advIndicator);
        // advIndicator.set
        advIndicator.setViewPager(advPager);


        scrollTextView = (ScrollTextView) view.findViewById(R.id.notice);//公告



        //网络请求
        RequestUtil.post("http://testwxys.rhy.com/index?longitude=123.93&latitude=41.88&_=1488543327000",null,null,false, new RequestUtil.MyCallBack() {
            @Override
            public void onFailure(String error) {

            }

            @Override
            public void onSuccess(Map<String ,Object> result) {

                String dataStr = result.get("data") +"";
                Map<String ,Object> dataMap = JsonUtil.jsonToOject(dataStr);



                //广告
                List<Map<String ,String>> adList = (List<Map<String, String>>) dataMap.get("ad");
                 //imageLists = new ArrayList<ImageView>();
                //imageLists.add(image_1);
               // imageLists.add(image_2);

                for (Map<String ,String> temp : adList){
                    ImageView imageView = new ImageView(activity);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);//图片填充
                    Picasso.with(activity).load(temp.get("picpath")).into(imageView);
                    advImages.add(imageView);
                    myAdvAdapter.notifyDataSetChanged();
                }


                //公告。。。。。。。


                List<Map<String,String>> noticeList = (List<Map<String, String>>) dataMap.get("notice");
                List<String>  notices = new ArrayList<String>();
                for (Map<String ,String> item :noticeList){
                    notices.add(item.get("title"));
                }
                scrollTextView.setDate(notices);
                //............


                //  子Fragment
                String shopClassStr = dataMap.get("shopClass") +"";
                List<ShopType> dates = JsonUtil.jsonToList(shopClassStr,ShopType.class);
                int count = dates.size();
                int itemCount = count%8 ==0 ? count/8 : count/8 + 1;

                for (int i=0; i<itemCount; i++){
                    TypeFragment typeFragment = new TypeFragment();
                    if (i == itemCount-1){
                        typeFragment.setDate(dates.subList(i*8 , count-1));
                    }else {
                        typeFragment.setDate(dates.subList(i*8 , (i+1)*8));
                    }
                    fragments.add(typeFragment);
                    myFragmentPagerAdapter.notifyDataSetChanged();
                }



                //两张图片.........




            }
        });

        // 子Fragment
        typePager = (ViewPager) view.findViewById(R.id.type_pager);
        //在Fragment里面添加子Fragment，需要。。
        FragmentManager fragmentManager = getFragmentManager();

         myFragmentPagerAdapter = new MyTypeAdapter(fragmentManager,fragments);
        typePager.setAdapter(myFragmentPagerAdapter);

        //指示器
        typeIndicator = (CirclePageIndicator) view.findViewById(R.id.typeIndicator);
        typeIndicator.setViewPager(typePager);
        //........................




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
