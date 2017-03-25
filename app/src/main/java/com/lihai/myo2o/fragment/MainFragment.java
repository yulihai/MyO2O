package com.lihai.myo2o.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lihai.common.adapter.MyFragmentPagerAdapter;
import com.lihai.common.base.BaseFragment;
import com.lihai.myo2o.R;
import com.lihai.myo2o.activity.SearchPageActivity;
import com.lihai.myo2o.damain.ShopType;
import com.lihai.request.utils.JsonUtil;
import com.lihai.request.utils.RequestUtil;
import com.lihai.widget.ScrollTextView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.math.BigDecimal;
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

    List<String> notices;//公告

    Map<String ,Object> dataMap;
    String dataStr;


    CirclePageIndicator advIndicator ,typeIndicator;   //指示器


    MyTypeAdapter myFragmentPagerAdapter;

    ScrollTextView scrollTextView; //公告Text


    ImageView image_1, image_2,activity_zone;

    int count;
    int status; // 网络请求  1 成功   0 失败


    public SwipeRefreshLayout swipeRefreshLayout;  //刷新控件

    EditText editText;



    int lastPosition;
    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }


    /**
     * 广告条自动转换
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //得到当前ViewPager和用户交互的条目
                    lastPosition = advPager.getCurrentItem();
                    //设置ViewPager当前显示的界面

                    lastPosition = lastPosition + 1;
                    if (lastPosition == count) {     //lastPosition == count 时，设lastPosition = 0 ，继续循环。
                        lastPosition = 0;
                    }
                    advPager.setCurrentItem(lastPosition);
                    //继续延时发送，永远执行下去
                    sendEmptyMessageDelayed(0, 2000);
                    break;
                default:
                    break;
            }
        }
    };



    @Override
    protected void init(final View view) {

        editText = (EditText) view.findViewById(R.id.edit_text);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchPageActivity.class);
                startActivity(intent);
            }
        });

        //  Log.i("MainFragment","onCreateView");


        //刷新
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange);
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               //TODO
                advImages.clear();
                fragments.clear();
                dataMap.clear();
                notices.clear();
                request();
                myAdvAdapter.notifyDataSetChanged();
                myFragmentPagerAdapter.notifyDataSetChanged();
            }
        });


        //两张图片
        image_1 = (ImageView) view.findViewById(R.id.image_1);
        image_2 = (ImageView) view.findViewById(R.id.image_2);

        //特色活动图片
        activity_zone = (ImageView) view.findViewById(R.id.activity_zone);
       //公告
        scrollTextView = (ScrollTextView) view.findViewById(R.id.notice);


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
        handler.sendEmptyMessageDelayed(0,2000);



       //网络请求
        request();


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


    /**
     * 网络请求数据
     */
    public void request(){

        //网络请求
        String URL ="http://testwxys.rhy.com/index?longitude=123.93&latitude=41.88&_=1488543327000" ;
        RequestUtil.post(URL,null,null,false, new RequestUtil.MyCallBack() {
            @Override
            public void onFailure(String error) {

               activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,"获取数据失败",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onSuccess(Map<String ,Object> result) {
                 status = Integer.parseInt(result.get("status")+ "");
                 dataStr = result.get("data") +"";
                dataMap = JsonUtil.jsonToOject(dataStr);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (status == 1) {

                            //广告    //TODO
                            List<Map<String, String>> adList = (List<Map<String, String>>) dataMap.get("ad");    //slide
                            for (Map<String, String> temp : adList) {
                                ImageView imageView = new ImageView(activity);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);//图片填充
                                Picasso.with(activity).load(temp.get("picpath")).into(imageView);
                                advImages.add(imageView);
                                myAdvAdapter.notifyDataSetChanged();
                            }

                            //两张图片
                            int n = 0;
                            List<Map<String, String>> imageLists = (List<Map<String, String>>) dataMap.get("ad");
                            {
                                for (Map<String, String> imageList : imageLists) {
                                    if (n == 0) {
                                        image_1.setScaleType(ImageView.ScaleType.FIT_XY);//图片填充
                                        Picasso.with(activity).load(imageList.get("picpath")).into(image_1);
                                        n++;
                                    } else {
                                        image_2.setScaleType(ImageView.ScaleType.FIT_XY);//图片填充
                                        Picasso.with(activity).load(imageList.get("picpath")).into(image_2);
                                    }
                                }
                            }

                            //公告。。。。。。。

                            scrollTextView.clear();
                            List<Map<String, String>> noticeList = (List<Map<String, String>>) dataMap.get("notice");
                             notices = new ArrayList<String>();
                            for (Map<String, String> item : noticeList) {
                                notices.add(item.get("title"));
                            }
                            scrollTextView.setDate(notices);
                            //............


                            //  子Fragment
                            String shopClassStr = dataMap.get("shopClass") + "";
                            List<ShopType> dates = JsonUtil.jsonToList(shopClassStr, ShopType.class);
                            int count = dates.size();
                            int itemCount = count % 8 == 0 ? count / 8 : count / 8 + 1;

                            for (int i = 0; i < itemCount; i++) {
                                TypeFragment typeFragment = new TypeFragment();
                                if (i == itemCount - 1) {
                                    typeFragment.setDate(dates.subList(i * 8, count - 1));
                                } else {
                                    typeFragment.setDate(dates.subList(i * 8, (i + 1) * 8));
                                }
                                fragments.add(typeFragment);
                                myFragmentPagerAdapter.notifyDataSetChanged();
                            }


                            //活动.........
                            String activityObject = dataMap.get("activity") + "";
                            Map<String, String> activityList = JsonUtil.jsonToOject(activityObject);
                            activity_zone.setScaleType(ImageView.ScaleType.FIT_XY);//图片填充
                            Picasso.with(activity).load(activityList.get("picB")).into(activity_zone);

                        } else {
                            Toast.makeText(activity, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }



    class MyAdvAdapter extends PagerAdapter{



        @Override
        public int getCount() {
            count = advImages.size();
            return count;
           // return advImages.size();
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
