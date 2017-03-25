package com.lihai.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiHai on 2017/3/7.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String , Object>> data;
    private int itemLayoutId;
    private String[] from;
    private int[] to;


    ItemListener itemListener;



    public MyAdapter(Context context,List<Map<String , Object>> data ,int itemLayoutId,String[] from,int[] to){

        this.context = context;
        this.data = data;
        this.itemLayoutId = itemLayoutId;
        this.from = from;
        this.to = to;

    }


     public void setItemListener(ItemListener itemListener){
         this.itemListener = itemListener;
     }



    @Override
    public int getCount() {
        return data == null? 0 :data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Map<String , Object> itemData = data.get(position);   //数据

        Map<Integer ,View> holder = null;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(itemLayoutId,null);
            holder = new HashMap<Integer,View>();

            for (int i=0 ;i<to.length ; i++){
              holder.put(to[i] , convertView.findViewById(to[i]));
            }
            convertView.setTag(holder); //tag所有的子控件对象

            if (itemListener != null){
                itemListener.itemInit(convertView,itemData);
            }
        }
        if (itemListener != null){
            itemListener.itemSetData(convertView,itemData,position);
        }

        if (holder == null){
            holder = (Map<Integer, View>) convertView.getTag();   // get
        }
        for (int i=0 ; i< to.length ;i++){
            View view = holder.get(to[i]);
            setData(view , itemData ,from[i]);
        }

        return convertView;
    }


    public void setData (View view , Map<String ,Object> itemData , String key){

        Object viewData = itemData.get(key);
        if (view instanceof TextView){
            ((TextView) view).setText(viewData + "");
        }else if(view instanceof ImageView){
            if (viewData instanceof String){
               Picasso.with(context).load(viewData + "").into((ImageView) view);
            }else {
                ((ImageView) view).setImageResource(Integer.parseInt(viewData + ""));
            }

        }else if (view instanceof RatingBar){
            ((RatingBar) view).setRating(Integer.parseInt(viewData + ""));
        }

    }


    public interface ItemListener{
        void itemInit(View view,Map<String ,Object> itemData);
        void itemSetData(View view ,Map<String ,Object> itemData,int position);
    }


}
