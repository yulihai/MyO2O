package com.lihai.myo2o.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lihai.common.adapter.MyAdapter;
import com.lihai.common.base.BaseActivity;
import com.lihai.myo2o.R;
import com.lihai.myo2o.app.MyApplication;
import com.lihai.myo2o.damain.Product;
import com.lihai.myo2o.utils.MyTypeEvaluator;
import com.lihai.request.utils.RequestUtil;
import com.lihai.widget.MyDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

/**
 * Created by LiHai on 2017/3/11.
 */
public class ShopInfoActivity extends BaseActivity {

    //String shopId="8a2a0f6458bf29fb0158bf38c4f60000";
    String shopId = "8a2a0f645a8a9a16015a8cce3b8e0007";

    ListView productTypeListView, productListView;

    MyAdapter productTypeAdapter, productAdapter;

    List<Map<String, Object>> productTypeDatas = new ArrayList<Map<String, Object>>();   //商品类型数据
    List<Map<String, Object>> productDatas = new ArrayList<Map<String, Object>>();       //商品数据

    List<Map<String, Object>> datas;    //购物车popupWindow 里的数据

    ImageView shopPicView, shopLogoView;
    TextView collectView, shopName;

    int lastSelectType = 0;
    int lastUserFav, userFav;

    BGABadgeImageView shoppingCar;  //购物车

    RelativeLayout root;   //根布局

    TextView shopTotalPrice, total;  // shopTotalPrice  购物总价格
    Button operation;      //

    int position;



    @Override
    protected int setLayout() {
        return R.layout.layout_shop_info;
    }

    @Override
    protected void initView() {

        shopPicView = (ImageView) findViewById(R.id.shop_pic);  //店铺背景图
        shopLogoView = (ImageView) findViewById(R.id.shop_logo);//店铺标志图
        collectView = (TextView) findViewById(R.id.collect);  //收藏
        shopName = (TextView) findViewById(R.id.shop_name);   //店铺名

        shoppingCar = (BGABadgeImageView) findViewById(R.id.shopping_car);    //购物车图片

        root = (RelativeLayout) findViewById(R.id.root);     //根布局

        shopTotalPrice = (TextView) findViewById(R.id.shopping_info);  //  购物总价格
        total = (TextView) findViewById(R.id.total);
        operation = (Button) findViewById(R.id.operation);   //联系商家

        //点击按钮，联系商家 或者 购买
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String buttonText = button.getText().toString();
                if (buttonText.equals("联系商家")) {
                    openDialog();  //对话框
                } else if (buttonText.equals("选好了")) {
                    //TODO
                }
            }
        });

        //点击购物车，打开popupWindow
        shoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                MyPopupWindow();
                backgroundAlpha(0.5f);
            }
        });


        collectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastUserFav == 0) {
                    collectView.setText("已收藏");
                    lastUserFav = 1;
                    userFav = lastUserFav;
                    Toast.makeText(ShopInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                } else if (lastUserFav == 1) {
                    collectView.setText("收藏");
                    lastUserFav = 0;
                    userFav = lastUserFav;
                    Toast.makeText(ShopInfoActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //加载当前店铺下的商品类别
        productTypeListView = (ListView) findViewById(R.id.product_type_list_view);
        productTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastSelectType = position;
                productTypeAdapter.notifyDataSetChanged();
                refreshProduct(productTypeDatas.get(position).get("categoryId") + "", false);//加载商品
            }
        });


        productTypeAdapter = new MyAdapter(this,productTypeDatas,R.layout.item_product_type,new String[]{"name"},new int[] {R.id.type_name});
        productTypeAdapter.setItemListener(new MyAdapter.ItemListener() {
            @Override
            public void itemInit(View view, Map<String, Object> itemData) {

            }

            @Override
            public void itemSetData(View view, Map<String, Object> itemData, int position) {

                TextView textView = (TextView) view;

                //每一个都会触发
                if (lastSelectType ==position){
                    view.setBackgroundResource(R.drawable.background);
                    textView.setTextColor(getResources().getColor(R.color.orange));

                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    textView.setTextColor(getResources().getColor(R.color.grey));
                }

            }
        });

        productTypeListView.setAdapter(productTypeAdapter);

        //网络加载
        String URL = "http://testwxys.rhy.com/goodspage";
        final Map<String,String> param = new HashMap<>();
        param.put("shopId",shopId);

        RequestUtil.get(URL, param, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                int status = Integer.parseInt(result.get("status") + "");

                if (status == 1) {   //等于1 代表成功

                    Map<String, Object> data = (Map<String, Object>) result.get("data");
                    //设置收藏
                    int userFav = Integer.parseInt(data.get("userFav") + "");    //TODO
                    if (userFav == 0) {
                        collectView.setText("收藏");
                        lastUserFav = userFav;
                    } else if (userFav == 1) {
                        collectView.setText("已收藏");
                        lastUserFav = userFav;
                    }

                    Map<String, String> shopData = (Map<String, String>) data.get("shop");  //店铺头数据
                    setShopInfo(shopData);

                    List<Map<String, Object>> typeDatas = (List<Map<String, Object>>) data.get("category");//产品类别数据
                    productTypeDatas.addAll(typeDatas);
                    productTypeAdapter.notifyDataSetChanged();
                    refreshProduct(productTypeDatas.get(0).get("categoryId") + "", false); //默认加载第一项数据
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });




        //加载商品

        productListView = (ListView) findViewById(R.id.product_list_view);
        productAdapter = new MyAdapter(this, productDatas, R.layout.item_product, new String[]{"picS", "name", "salesNum", "price"},
                new int[]{R.id.product_pic, R.id.product_name, R.id.sale_count, R.id.price});  //
        productAdapter.setItemListener(new MyAdapter.ItemListener() {
            @Override
            public void itemInit(View view, Map<String, Object> itemData) {

            /* int position = productDatas.indexOf(itemData);
              //  int position = productListView.getPositionForView(view);
                //不重用时调用
               minusBtn.setOnClickListener(new BuyClickListener(position));
                plusBtn.setOnClickListener(new BuyClickListener(position));*/

             /*   int position = productDatas.indexOf(itemData);
                //不重用时调用
                ImageButton minusBtn = (ImageButton) view.findViewById(R.id.minus);
               minusBtn.setOnClickListener(new BuyClickListener(position));

                ImageButton plusBtn = (ImageButton) view.findViewById(R.id.plus);
              plusBtn.setOnClickListener(new BuyClickListener(position));*/
            }

            @Override
            public void itemSetData(View view, Map<String, Object> itemData, int position) {

                //总是调用

                ImageButton plusBtn = (ImageButton) view.findViewById(R.id.plus);  //加按钮
                plusBtn.setOnClickListener(new BuyClickListener());
                plusBtn.setTag(position);

                ImageButton minusBtn = (ImageButton) view.findViewById(R.id.minus); //减按钮
                minusBtn.setOnClickListener(new BuyClickListener());
                minusBtn.setTag(position);


                //设置购物车信息
                shoppingCar.showTextBadge(MyApplication.shoppingCar.totalCount + "");      //显示购物车物品数量
                if (MyApplication.shoppingCar.totalCount == 0) {
                    shopTotalPrice.setText("购物车是空的");
                    operation.setText("联系商家");
                    shopTotalPrice.setTextColor(getResources().getColor(R.color.black));
                    shopTotalPrice.setTextSize(16);
                    total.setVisibility(View.GONE);
                } else {
                    operation.setText("选好了");
                    shopTotalPrice.setText(MyApplication.shoppingCar.totalPrice + "");   //显示总价格
                    shopTotalPrice.setTextColor(getResources().getColor(R.color.orange));
                    shopTotalPrice.setTextSize(26);
                    total.setVisibility(View.VISIBLE);
                }

                TextView selectedCount = (TextView) view.findViewById(R.id.select_count);  //选择的数量

                //总是调用
                String goodsId = itemData.get("goodsId") + "";
                int count = MyApplication.shoppingCar.getCountByProductId(goodsId);
                if (count == 0) {
                    minusBtn.setVisibility(View.GONE);//减按钮隐藏
                    selectedCount.setVisibility(View.GONE); //选择数量为0 ，隐藏
                } else {
                    minusBtn.setVisibility(View.VISIBLE);  //减按钮可见

                    selectedCount.setText(count + "");    //可见，显示数量
                    //  shoppingCar.showTextBadge(MyApplication.shoppingCar.totalCount + "");    //显示总数量
                    selectedCount.setVisibility(View.VISIBLE);
                }
            }
        });
        productListView.setAdapter(productAdapter);
    }

    /**
     * 设置店铺信息
     * @param info
     */
    private void setShopInfo(Map<String, String> info) {
        Picasso.with(this).load(info.get("titpic")).into(shopPicView);
        Picasso.with(this).load(info.get("pic")).into(shopLogoView);
        shopName.setText(info.get("name"));
        // collectView.setText("收藏");
    }

    /**
     * 加载与刷新商品信息
     * @param categoryId
     * @param isLoadMore
     */
    private void refreshProduct(String categoryId, final boolean isLoadMore) {

        String URL = "http://testwxys.rhy.com/getgoodslistbycategory";
        final Map<String, String> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("categoryId", categoryId);

        RequestUtil.get(URL, param, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                int status = Integer.parseInt(result.get("status") + "");
                if (status == 1) {
                    Map<String, Object> data = (Map<String, Object>) result.get("data");

                    List<Map<String, Object>> goods = (List<Map<String, Object>>) data.get("goods");

                    if (!isLoadMore) {
                        productDatas.clear();//清空之前数据，重新加载
                    }
                    productDatas.addAll(goods);//加载数据
                    productAdapter.notifyDataSetChanged();//刷新
                } else {

                    //TODO
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }


    /**
     * 点击 加减按钮 购买监听器
     */
    private class BuyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            position = (int) v.getTag();
            int id = v.getId();
            switch (id) {
                case R.id.plus:
                    addFlyView(v);
                    plus(position);
                    productAdapter.notifyDataSetChanged();
                    break;
                case R.id.minus:
                    addFlyView(v);
                    minus(position);
                    productAdapter.notifyDataSetChanged();
                    break;
            }
        }

        /**
         * 减
         * @param position
         */
        private void minus(int position) {
            //获取商品ID
            String productId = productDatas.get(position).get("goodsId") + "";
            //传到购物车
            MyApplication.shoppingCar.minus(productId);
        }

        /**
         * 加
         * @param position
         */
        private void plus(int position) {

            String productId = productDatas.get(position).get("goodsId") + "";  //获取商品id
            String name = productDatas.get(position).get("name") + "";        //获取商品名
            float price = Float.parseFloat(productDatas.get(position).get("price") + "");    //获取商品单价
            MyApplication.shoppingCar.plus(shopId, productId, name, price);
        }
    }


    /**
     * 打开对话框
     */
    public void openDialog() {

        //弹出对话框
        MyDialog myDialog = MyDialog.getInstance(this);
        myDialog.setMsg("您将联系该店铺，确认拨打吗？");
        myDialog.setTitle(null);
        myDialog.setBtn("取消", "确定", new MyDialog.BtnClickerListener() {
            @Override
            public void onClicker(Dialog dialog, int which) {

                if (which == 0) {
                    dialog.dismiss();
                } else {
                    //拨打电话
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "10000"));
                    // 再次检查是否有权限
                    //GRANTED  授权
                    if (ActivityCompat.checkSelfPermission(ShopInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        //  第一步   ActivityCompat#requestPermissions
                        ActivityCompat.requestPermissions(ShopInfoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        // 第二步 (重写） public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
                        return;
                    }
                    startActivity(callIntent);  //因权限问题报错
                    dialog.dismiss();
                }
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        myDialog.show(fragmentManager, "dialog");
    }

    /**
     * 打电话的权限判断   第二步
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //没有权限
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * popupWindow
     */
    public void MyPopupWindow(){

        View view = getLayoutInflater().inflate(R.layout.shopping_car_info_popup_layout, null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        final TextView cleanTextView = (TextView) view.findViewById(R.id.clean_all_shopInfo);  //清空购物车
        cleanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.shoppingCar.shoppingCarMap.clear();
                MyApplication.shoppingCar.totalCount = 0;
                MyApplication.shoppingCar.totalPrice = 0;
                productAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });


        ListView shopCarInfo = (ListView) view.findViewById(R.id.shop_car_info_listView);
        datas = new ArrayList<>();

        Set<Map.Entry<String, Product>> entrys = MyApplication.shoppingCar.shoppingCarMap.entrySet();
        for (Map.Entry<String, Product> entry : entrys) {
            String productId = entry.getKey();
            Product product = MyApplication.shoppingCar.shoppingCarMap.get(productId);
            int count = MyApplication.shoppingCar.getCountByProductId(productId);

            Map<String, Object> data = new HashMap<>();
            data.put("name", product.name);
            data.put("price", product.price);
            data.put("selectCount", count);
            datas.add(data);

        }

        final MyAdapter myAdapter = new MyAdapter(this, datas, R.layout.item_shop_car_info, new String[]{"name", "price", "selectCount"},
                new int[]{R.id.product_name, R.id.price, R.id.select_count});

        myAdapter.setItemListener(new MyAdapter.ItemListener() {
            @Override
            public void itemInit(View view, Map<String, Object> itemData) {

            }

            @Override
            public void itemSetData(View view, Map<String, Object> itemData, int position) {

                ImageButton plusBtn = (ImageButton) view.findViewById(R.id.plus);  //加按钮
                plusBtn.setOnClickListener(new BuyClickListener());
                plusBtn.setTag(position);

                ImageButton minusBtn = (ImageButton) view.findViewById(R.id.minus); //减按钮
                minusBtn.setOnClickListener(new BuyClickListener());
                minusBtn.setTag(position);
                //TODO

            }
        });

        shopCarInfo.setAdapter(myAdapter);


        //获取屏幕的宽度 高度
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (MyApplication.shoppingCar.shoppingCarMap.size() > 4) {
            popupWindow.setHeight(height / 2);   //设置popupWindow的大小
        }


        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.tongcheng_all_bg02, null);
        } else {
            drawable = getResources().getDrawable(R.drawable.tongcheng_all_bg02);
        }
        popupWindow.setBackgroundDrawable(drawable);
        popupWindow.showAtLocation(root, Gravity.BOTTOM, 90, 90);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


    }





    /**
     * 添加属性动画
     * @param view
     */
    private void addFlyView(View view){

        int id = view.getId();

        //设置开始点(坐标)
        int[] startLocation = new int[2];
        view.getLocationOnScreen(startLocation);

        //设置结束点
        int[] endLocation = new int[2];
        shoppingCar.getLocationOnScreen(endLocation);

        int[] parentLocation = new int[2];
        root.getLocationInWindow(parentLocation);
        startLocation[1] = startLocation[1] - parentLocation[1];
        endLocation[0] = endLocation[0] + parentLocation[1];

        //设置控制点
        int[] controlPoint = new int[2];
        controlPoint[0] = endLocation[0];
        controlPoint[1] = startLocation[1];

        //设置动的图片
        final ImageView flyImageView = new ImageView(this);
        flyImageView.setImageResource(R.drawable.icon_fly);

        //给图片坐标
        flyImageView.setX(startLocation[0]);
        flyImageView.setY(startLocation[1]);
        //与布局绑定
        root.addView(flyImageView);

        //属性动画
        ObjectAnimator objectAnimator = new ObjectAnimator();
        //绑定图片
        objectAnimator.setTarget(flyImageView);
        //设置时间
        objectAnimator.setDuration(2000);


        //设置动画的路径
        switch (id) {
            case R.id.plus:
                objectAnimator.setObjectValues(startLocation, endLocation);
                break;
            case R.id.minus:
                objectAnimator.setObjectValues(endLocation, startLocation);
                break;
        }


        //设置插值器
        objectAnimator.setEvaluator(new MyTypeEvaluator(controlPoint));

        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int[] result = (int[]) animation.getAnimatedValue();
                flyImageView.setX(result[0]);
                flyImageView.setY(result[1]);
            }
        });

        //开始
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            /**
             * 动画结束时调用
             *
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animator animation) {

                root.removeView(flyImageView);  //结束时移除
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    /**
     * 设置屏幕透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

}
