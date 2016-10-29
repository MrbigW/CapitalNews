package com.wrk.capitalnews.bean;

/**
 * Created by MrbigW on 2016/10/27.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class ShoppingCart extends ShoppingMallBean.Wares {

    // 商品的个数
    private int count = 1;

    // 商品是否被选中
    private boolean isCheck = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
