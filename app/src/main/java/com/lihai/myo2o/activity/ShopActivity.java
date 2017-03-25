package com.lihai.myo2o.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lihai.common.adapter.MyAdapter;
import com.lihai.common.base.BaseActivity;
import com.lihai.myo2o.R;
import com.lihai.myo2o.fragment.TypeFragment;
import com.lihai.request.utils.JsonUtil;
import com.lihai.request.utils.RequestUtil;
import com.lihai.widget.SelectMenuView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by LiHai on 2017/3/6.
 */
public class ShopActivity extends BaseActivity {

    String shopTypeId;

    ListView shopListView;
    SwipeRefreshLayout swipeRefreshLayout;   //刷新用的


    MyAdapter myAdapter,myListAdapter,capacityListAdapter;

    List<Map<String , Object>> data  = new ArrayList<>();

    //........
    Random random = new Random();


    SelectMenuView selectMenuView;

    int lastIndex = -1;

    @Override
    protected int setLayout() {
        return R.layout.layout_shop;
    }

    @Override
    protected void initView() {

        //选择菜单....


        selectMenuView = (SelectMenuView) findViewById(R.id.select_menu);
        //菜单标题
        String[] titles = new String[]{"全部分类","智能排序"};

        List<ListView> listViews = new ArrayList<>();

        //第一个
        ListView totalListView = new ListView(this);
        totalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //点击了那项
                TextView textView = (TextView) view;
                //点击的那项，选中
                textView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.icon_chekced),null);
                String name = textView.getText().toString();

                //设置tab的标题
                selectMenuView.setTab(0,name);
                lastIndex = position;
            }
        });
        //不可见
        totalListView.setVisibility(View.GONE);
        //设置ListView的数据
        List<Map<String ,Object>> itemDatas = new ArrayList<>();
        for (int i=0 ;i<20 ;i++){

            Map<String, Object> itemData = new HashMap<>();
            itemData.put("name",random.nextInt(100));
            itemDatas.add(itemData);
        }
        //适配器
        myListAdapter = new MyAdapter(this,itemDatas,R.layout.item_filter,new String[]{"name"},new int[] {R.id.item_filter});
       myListAdapter.setItemListener(new MyAdapter.ItemListener() {
            @Override
            public void itemInit(View view, Map<String, Object> itemData) {
                //没有被重用的时候调用
            }

            @Override
            public void itemSetData(View view, Map<String, Object> itemData,int position) {

                TextView textView = (TextView) view;

                //总是会被调用
                if (lastIndex == position){

                    textView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.icon_chekced),null);
                    myListAdapter.notifyDataSetChanged();//刷新
                }else {
                    textView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                }

            }
        });
        totalListView.setAdapter(myListAdapter);

        //第二个
        ListView capacityListView = new ListView(this);
        capacityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //点击了那项
                TextView textView = (TextView) view;
                //点击的那项，选中
                textView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.icon_chekced),null);
                String name = textView.getText().toString();

                //设置tab的标题
                selectMenuView.setTab(1,name);
                lastIndex = position;
            }
        });
        //不可见
        capacityListView.setVisibility(View.GONE);
        //设置ListView的数据
        List<Map<String ,Object>> Datas = new ArrayList<>();
        for (int i=0 ;i<20 ;i++){

            Map<String, Object> itemData = new HashMap<>();
            itemData.put("name","商店"+random.nextInt(100));
            Datas.add(itemData);
        }
        //适配器
        capacityListAdapter = new MyAdapter(this,Datas,R.layout.item_filter,new String[]{"name"},new int[] {R.id.item_filter});
        capacityListAdapter.setItemListener(new MyAdapter.ItemListener() {
            @Override
            public void itemInit(View view, Map<String, Object> itemData) {
                //没有被重用的时候调用
            }

            @Override
            public void itemSetData(View view, Map<String, Object> itemData,int position) {

                TextView textView = (TextView) view;

                //总是会被调用
                if (lastIndex == position){

                    textView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.icon_chekced),null);
                    capacityListAdapter.notifyDataSetChanged();//刷新
                }else {
                    textView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                }

            }
        });
        capacityListView.setAdapter(capacityListAdapter);

        listViews.add(totalListView);
        listViews.add(capacityListView);
        selectMenuView.init(titles,listViews);

















        shopTypeId = getIntent().getStringExtra(TypeFragment.DATA_KEY);

        shopListView = (ListView) findViewById(R.id.shop_list_view);

        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShopActivity.this,ShopInfoActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) shopListView.getParent();
        swipeRefreshLayout.setDistanceToTriggerSync(200);  //向下拉距离200dp，刷新更新数据
        swipeRefreshLayout.setColorSchemeResources(R.color.orange);
        //刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                getData();

                //..........
                refresh();
            }
        });




       /* myAdapter = new MyAdapter(this,data,R.layout.item_shop,new String[]{
                "pic","name","score","commentCount","typeName","money", "distance"
        },new int[]{R.id.shop_icon, R.id.shop_name, R.id.score, R.id.commentCount,
                R.id.typeName, R.id.money, R.id.distance});*/

        //............
        getData();
        myAdapter = new MyAdapter(this,data,R.layout.item_shop,new String[]{
                "pic","name","commentCount","typeName","money", "distance"
        },new int[]{R.id.shop_icon, R.id.shop_name,  R.id.commentCount,
                R.id.typeName, R.id.money, R.id.distance});
        //..................

        myAdapter.setItemListener(new MyAdapter.ItemListener() {
            @Override
            public void itemInit(View view, Map<String, Object> itemData) {

            }

            @Override
            public void itemSetData(View view, Map<String, Object> itemData,int position) {

                TextView textView = (TextView) view.findViewById(R.id.shop_name);
               // int grade = Integer.parseInt(itemData.get("grade") + "");   //等级

                //........
                int grade = random.nextInt(5);
                //.............

                setGrade(grade , textView);
            }
        });

        shopListView.setAdapter(myAdapter);
        //getData();
    }

   /* private void getData(){
        Map<String ,String> params = new HashMap<>();
        params.put("shopTypeId" ,shopTypeId);

        RequestUtil.post("http://192.168.1.103/apiWeb/shops", params, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                String JSONresult = result.get("data") + "";
                List<Map<String ,Object>> resultData = JsonUtil.jsonToList(JSONresult,Map.class);
                data.addAll(resultData);
                myAdapter.notifyDataSetChanged();// 刷新
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(String error) {
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }*/


    //.................................
   private void getData(){

       Map<String ,Object> shopMap;

       for (int i=0 ;i<35 ;i++){
           shopMap = new HashMap<>();
           shopMap.put("pic",R.drawable.h001 + random.nextInt(26));
           shopMap.put("name","商店" + random.nextInt(26));
           //shopMap.put("score" , "");
           shopMap.put("commentCount",random.nextInt(26) + "条评论");
           shopMap.put("typeName" ,"美食");
           shopMap.put("money",random.nextInt(26)+ "元起免费送");
           shopMap.put("distance",random.nextInt(26) + "千米");
           data.add(shopMap);
       }

   }
//......................................................


        public void setGrade(int grade ,TextView textView){

            Drawable drawable = null;
        switch (grade){
            case 0:
                drawable = getResources().getDrawable(R.drawable.icon_rank1);
                drawable.setBounds(5,0,drawable.getIntrinsicWidth()+17,drawable.getIntrinsicHeight());
                break;
            case 1:
                drawable = getResources().getDrawable(R.drawable.icon_rank1);
                drawable.setBounds(5,0,drawable.getIntrinsicWidth()+17,drawable.getIntrinsicHeight());
                break;
            case 2:
                drawable = getResources().getDrawable(R.drawable.icon_rank2);
                drawable.setBounds(5,0,drawable.getIntrinsicWidth()+ 17,drawable.getIntrinsicHeight());
                break;
            case 3:
                drawable = getResources().getDrawable(R.drawable.icon_rank3);
                drawable.setBounds(5,0,drawable.getIntrinsicWidth()+ 17,drawable.getIntrinsicHeight());
                break;
            case 4:
                drawable = getResources().getDrawable(R.drawable.icon_rank4);
                drawable.setBounds(5,0,drawable.getIntrinsicWidth()+17,drawable.getIntrinsicHeight());
                break;
            case 5:
                drawable = getResources().getDrawable(R.drawable.icon_rank5);
                drawable.setBounds(5,0,drawable.getIntrinsicWidth()+ 20,drawable.getIntrinsicHeight());
                break;
        }
        if (drawable != null){

            textView.setCompoundDrawables(null,null,drawable,null);

        }

    }


    //............
    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //..........
                        myAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    //.................................

}





