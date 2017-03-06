package com.lihai.myo2o.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lihai.common.base.BaseFragment;
import com.lihai.myo2o.R;
import com.lihai.myo2o.damain.ShopType;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by LiHai on 2017/3/2.
 */
public class TypeFragment extends BaseFragment {

    List<ShopType> shopTypes;

    GridView typeGridView;

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
        MyAdapter myAdapter = new MyAdapter(shopTypes);
        typeGridView.setAdapter(myAdapter);

    }


    private class MyAdapter extends BaseAdapter implements View.OnClickListener {

        List<ShopType> shopTypes;

        public MyAdapter(List<ShopType> shopTypes){
            this.shopTypes = shopTypes;
        }

        @Override
        public int getCount() {
            return shopTypes.size();
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

            Toast.makeText(activity,"我被点击了" ,Toast.LENGTH_SHORT).show();

        }
    }


}
