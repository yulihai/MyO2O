package com.lihai.myo2o.utils;

/**
 * Created by LiHai on 2017/3/12.
 */

import com.lihai.myo2o.damain.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 购物车
 */
public class ShoppingCar {

    public int totalCount ;  //总共有多少个商品
    public float totalPrice; //总共多少钱，总价格
    public Map<String,Product> shoppingCarMap = new HashMap<String,Product>();//装产品的集合



    /**
     * 添加商品
     * @param shopId           店铺id
     * @param productId        商品id
     * @param name             商品名
     * @param price             单价格
     */
    public void plus(String shopId,String productId ,String name,float price){

        if (shoppingCarMap.containsKey(productId)){       //如果商品id一样，就此商品数量加1
            Product tempProduct = shoppingCarMap.get(productId);
            tempProduct.selectCount = tempProduct.selectCount+1 ;
        }else {
            Product product = new Product();
            product.shopId = shopId;
            product.productId = productId;
            product.name = name;
            product.price = price;
            product.selectCount = 1;
            shoppingCarMap.put(productId,product);
        }
        totalCount = totalCount +1 ;   //总数量加1
        //totalPrice = totalPrice+ price;  //总价格添加
        BigDecimal money = new BigDecimal(totalPrice+ price);
        totalPrice = money.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }


    /**
     * 减少商品
     * @param productId
     */
    public void minus (String productId){

        if (shoppingCarMap.containsKey(productId)){
            Product product = shoppingCarMap.get(productId);
            if (product.selectCount == 1){
                shoppingCarMap.remove(productId);
            }else {
                product.selectCount --;
            }
            //修改价格
            totalCount--;   //数量
           // totalPrice = totalPrice - product.price;
            BigDecimal money = new BigDecimal(totalPrice - product.price);
            totalPrice = money.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
    }


    /**
     * 获取指定商品数量
     * @param productId
     * @return
     */
    public int getCountByProductId(String productId){

        if (shoppingCarMap.containsKey(productId)){
            return  shoppingCarMap.get(productId).selectCount;
        }else {
            return  0;
        }
    }

}
