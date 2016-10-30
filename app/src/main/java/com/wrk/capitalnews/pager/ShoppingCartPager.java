package com.wrk.capitalnews.pager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.adapter.ShoppingCartAdapter;
import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.bean.ShoppingCart;
import com.wrk.capitalnews.pay.PayResult;
import com.wrk.capitalnews.pay.SignUtils;
import com.wrk.capitalnews.utils.CartProvider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 购物车页面
 * -------------------=.=------------------------
 */

public class ShoppingCartPager extends BasePager {

    // 商户PID
    public static final String PARTNER = "2088911876712776";
    // 商户收款账号
    public static final String SELLER = "chenlei@atguigu.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJRNvuqc9B73oT54\n" +
            "GmOPng8qiq1IlhLWItWIs0rZLjSacT+VKjVdxAJBbxbTpF61aMhtWu6RwAd+8qBU\n" +
            "5AeQFNs4zU5CTjFFjdGPpm8ROGBYar0C1WzJdkjkqAvLaXG0rJeWP+EIT+FzyAbA\n" +
            "qAXXzWbdKgLdTXtxZb01MDl8mTQjAgMBAAECgYAz29/n4IyJC6Sp2Iu5xu3JdeHa\n" +
            "aGh6G8FAlDXF9Z3vrRXu2vVQhgJVm5YCEG4I5DzI4VyL0hGpTV4AbM70ShDQc4v+\n" +
            "iZh3lTmzv4rhUDz3BrxbwR96ebtITTqvBKDQgXDpLKiJocwvlrd5EFgAJYpGZqRA\n" +
            "GCIBbHftZ5UvMyySIQJBAMT27wKk8xoW4BhcIdma+NOQqONeWX5VatqW8qH/dZTC\n" +
            "98gWYgvUwADxtKdqlNphoDHlBlVF5JUEVWfVsMTQAlECQQDAwRGJ0toVVQpRcqnD\n" +
            "+70F78zb/cEefidkU5ssszgCF90/zikoXJjA1KXFACzug7cyztvXa0VU2mLWLuGW\n" +
            "n14zAkAr++S113X+LnuOlQxuFqBYRmagl5Iulw6Mj8bRDEYKmVtR0EXG1JSn4VHx\n" +
            "TOi+t6xZWAaJBlmcOWKFFIAsAzNxAkA1e1xqaV6pXJcoUjBYeJjR9N9aiuXyl/5G\n" +
            "EAyWMoPv0L9K3OD+mfKoTlhQeOP+qf1C07Kb6t+p045o70kYic+RAkA12/woH7cC\n" +
            "JyDmHW1/HZR/rk/ilM+verss0mCzTNLFYtGh1pu09HyvN5XwkRf36CoE0QYEsC4c\n" +
            "sHjoR3G47/2T";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUTb7qnPQe96E+eBpjj54PKoqtSJYS1iLViLNK2S40mnE/lSo1XcQCQW8W06RetWjIbVrukcAHfvKgVOQHkBTbOM1OQk4xRY3Rj6ZvEThgWGq9AtVsyXZI5KgLy2lxtKyXlj/hCE/hc8gGwKgF181m3SoC3U17cWW9NTA5fJk0IwIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };



    private CartProvider mCartProvider;
    private RecyclerView shoppingcart_recyclerView;
    private CheckBox checkbox_all;
    private TextView tv_total_price;
    private Button btn_order;
    private Button btn_delete;
    private TextView tv_nodata;

    private ShoppingCartAdapter mAdpater;

    // 编辑状态
    private static final String ACTION_EDIT = "1";
    // 完成状态
    private static final String ACTION_COMPLETE = "2";


    public ShoppingCartPager(Context context, int type) {
        super(context, type);
        mCartProvider = new CartProvider(mContext);
    }

    @Override
    public void initData() {
        super.initData();

        btn_cart.setVisibility(View.VISIBLE);

        tvBasepagerTitle.setText("购物车");
        View view = View.inflate(mContext, R.layout.shopping_cart_pager, null);

        shoppingcart_recyclerView = (RecyclerView) view.findViewById(R.id.shoppingcart_recyclerView);
        checkbox_all = (CheckBox) view.findViewById(R.id.checkbox_all);
        tv_total_price = (TextView) view.findViewById(R.id.tv_total_price);
        btn_order = (Button) view.findViewById(R.id.btn_order);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        flBasepagerContent.removeAllViews();

        flBasepagerContent.addView(view);


        btn_cart.setTag(ACTION_EDIT);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = (String) btn_cart.getTag();
                if (state.equals(ACTION_EDIT)) {
                    showDeleteButton();
                } else if (state.equals(ACTION_COMPLETE)) {
                    hideDeleteButton();
                }
            }

        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdpater.deleteData();
                mAdpater.checkAll_none();
                mAdpater.ShowTotalPrice();

                if (mAdpater != null && mAdpater.getCount() > 0) {
                    tv_nodata.setVisibility(View.GONE);
                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }
            }
        });

        // 去结算-支付
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(v);
            }
        });

        showData();
    }

    private void showDeleteButton() {
        btn_cart.setTag(ACTION_COMPLETE);
        btn_cart.setText("完成");
        btn_delete.setVisibility(View.VISIBLE);
        btn_order.setVisibility(View.GONE);
        mAdpater.checkAll_none(false);
        mAdpater.checkAll_none();
        mAdpater.ShowTotalPrice();
    }

    /**
     * 显示数据
     */
    private void showData() {
        // 购物车所有的数据
        List<ShoppingCart> shoppingCarts = mCartProvider.getAllData();
        tv_nodata.setVisibility(View.GONE);
        if (shoppingCarts != null && shoppingCarts.size() > 0) {
            mAdpater = new ShoppingCartAdapter(mContext, shoppingCarts, checkbox_all, tv_total_price);
            // 设置适配器
            shoppingcart_recyclerView.setAdapter(mAdpater);

            shoppingcart_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        } else {
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(mContext).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
//                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", mAdpater.getTotalPrice() + "");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private void hideDeleteButton() {
        btn_cart.setTag(ACTION_EDIT);
        btn_cart.setText("编辑");
        btn_delete.setVisibility(View.GONE);
        btn_order.setVisibility(View.VISIBLE);
        mAdpater.checkAll_none(true);
        mAdpater.checkAll_none();
        mAdpater.ShowTotalPrice();
    }



}
