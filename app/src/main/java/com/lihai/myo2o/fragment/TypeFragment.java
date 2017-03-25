package com.lihai.myo2o.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lihai.common.adapter.MyAdapter;
import com.lihai.common.base.BaseFragment;
import com.lihai.myo2o.R;
import com.lihai.myo2o.activity.ShopActivity;
import com.lihai.myo2o.damain.ShopType;
import com.lihai.request.utils.JsonUtil;
import com.lihai.request.utils.RequestUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiHai on 2017/3/2.
 */
public class TypeFragment extends BaseFragment {

    public static final String DATA_KEY = "shopClassId";

    List<ShopType> shopTypes;

    List<Map<String , Object>> data  = new ArrayList<>();

    GridView typeGridView;

    MyAdapter myAdapter;

    public void setDate(List<ShopType> shopTypes){

        this.shopTypes = shopTypes;

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_type;
    }

    @Override
    protected void init(View view) {

        //数据
        typeGridView = (GridView) view.findViewById(R.id.type_grid);
        // myAdapter = new MyAdapter(activity,data,R.layout.gridview_item,new String[] {"pic","name"},new int[] {R.id.pic,R.id.name});
        myAdapter = new MyAdapter(shopTypes);
        typeGridView.setAdapter(myAdapter);
       // getData();

    }


   /* private void getData(){
       // Map<String ,String> params = new HashMap<>();
       // params.put("shopTypeId" ,shopTypeId);

        RequestUtil.post("http://testwxys.rhy.com/index?longitude=123.93&latitude=41.88&_=1488543327000",null,null,false, new RequestUtil.MyCallBack() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                String dataStr = result.get("data") +"";
                Map<String ,Object> dataMap = JsonUtil.jsonToOject(dataStr);


                List<Map<String ,Object>> resultData = (List<Map<String, Object>>) dataMap.get("shopClass");
                data.addAll(resultData);
                myAdapter.notifyDataSetChanged();// 刷新

            }

            @Override
            public void onFailure(String error) {
            }
        });
    }*/


    private class MyAdapter extends BaseAdapter implements View.OnClickListener {

        List<ShopType> shopTypes;

        public MyAdapter(List<ShopType> shopTypes){
            this.shopTypes = shopTypes;
        }

        @Override
        public int getCount() {
            return shopTypes == null? 0: shopTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return shopTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           if (convertView == null){
               convertView = LayoutInflater.from(activity).inflate(R.layout.gridview_item,null);

               convertView.setOnClickListener(this);

               ShopType temp = shopTypes.get(position);
               convertView.setTag(temp.shopClassId);

               ImageView imageView = (ImageView) convertView.findViewById(R.id.pic);
               Picasso.with(activity).load(temp.pic).into(imageView);

               TextView textView = (TextView) convertView.findViewById(R.id.name);
               textView.setText(temp.name);
           }else{

               //TODO  重用
           }

            return convertView;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(activity, ShopActivity.class);
            intent.putExtra(DATA_KEY,v.getTag()+ "");
            startActivity(intent);

        }
    }


}
