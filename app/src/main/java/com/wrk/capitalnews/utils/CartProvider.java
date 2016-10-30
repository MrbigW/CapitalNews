package com.wrk.capitalnews.utils;


import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wrk.capitalnews.bean.ShoppingCart;
import com.wrk.capitalnews.bean.ShoppingMallBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/27.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 购物车存储器类，存取购物车的内容，取内容
 * -------------------=.=------------------------
 */

public class CartProvider {

    public static final String JSON_CART = "json_cart";
    private Context mContext;

    // 千量级性能优于haspMap;
    private SparseArray<ShoppingCart> mSparseArray;

    public CartProvider(Context context) {
        this.mContext = context;
        mSparseArray = new SparseArray<>(10);
        listToSparse();
    }

    /**
     * 将列表中的数据转换成SparseArray();
     */
    private void listToSparse() {
        List<ShoppingCart> shoppingCarts = getAllData();

        if (shoppingCarts != null && shoppingCarts.size() > 0) {
            for (int i = 0; i < shoppingCarts.size(); i++) {
                ShoppingCart shoppingCart = shoppingCarts.get(i);
                mSparseArray.put(shoppingCart.getId(), shoppingCart);
            }
        }

    }

    /**
     * 得到所有数据
     *
     * @return
     */
    public List<ShoppingCart> getAllData() {
        return getLocalData();
    }

    /**
     * 得到缓存的数据
     *
     * @return
     */
    private List<ShoppingCart> getLocalData() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        String saveJson = CacheUtils.getString(mContext, JSON_CART); // json字符串
        if (!TextUtils.isEmpty(saveJson)) {
            // 把json数据解析成集合
            shoppingCarts = new Gson().fromJson(saveJson, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return shoppingCarts;
    }

    /**
     * 添加购物车数据
     *
     * @param shoppingCart
     */
    public void addData(ShoppingCart shoppingCart) {
        ShoppingCart tempCart = mSparseArray.get(shoppingCart.getId()); // 判断是否在列表存在
        if (tempCart != null) {
            // 列表中已存在
            tempCart.setCount(tempCart.getCount() + 1);
        } else {
            tempCart = shoppingCart;
            tempCart.setCount(1);
        }

        // 保存到sparseArray
        mSparseArray.put(tempCart.getId(), tempCart);

        // 保存
        commit();
    }

    /**
     * 删除购物车数据
     *
     * @param shoppingCart
     */
    public void deleteData(ShoppingCart shoppingCart) {
        mSparseArray.delete(shoppingCart.getId());

        commit();
    }

    /**
     * 修改购物车数据
     *
     * @param shoppingCart
     */
    public void updateData(ShoppingCart shoppingCart) {

        mSparseArray.put(shoppingCart.getId(), shoppingCart);

        commit();
    }

    /**
     * 提交数据的更改
     */
    private void commit() {
        List<ShoppingCart> shoppingCarts = sparseToList();
        String json = new Gson().toJson(shoppingCarts);
        CacheUtils.putString(mContext, JSON_CART, json);
    }

    /**
     * 把List列表数据->json文本数据-->保存到本地
     *
     * @return
     */
    private List<ShoppingCart> sparseToList() {

        List<ShoppingCart> shoppingCarts = new ArrayList<>();

        for (int i = 0; i < mSparseArray.size(); i++) {
            ShoppingCart shoppingCart = mSparseArray.valueAt(i);
            shoppingCarts.add(shoppingCart);
        }

        return shoppingCarts;
    }


    /**
     * 将Wares类对象转换为shoppingCart
     *
     * @param wares
     * @return
     */
    public ShoppingCart conversion(ShoppingMallBean.Wares wares) {

        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.setImgUrl(wares.getImgUrl());
        shoppingCart.setName(wares.getName());
        shoppingCart.setId(wares.getId());
        shoppingCart.setCount(1);
        shoppingCart.setCheck(true);
        shoppingCart.setDescription(wares.getDescription());
        shoppingCart.setPrice(wares.getPrice());
        shoppingCart.setSale(wares.getSale());

        return shoppingCart;
    }
}































