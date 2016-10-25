package com.wrk.capitalnews.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.utils.DensityUtil;
import com.wrk.capitalnews.view.NewsDetialPopUpWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.icon_textsize)
    ImageButton iconTextsize;
    @BindView(R.id.icon_share)
    ImageButton iconShare;
    @BindView(R.id.pb_load)
    ProgressBar pbLoad;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.fab_newsdetail_more)
    FloatingActionButton fabNewsdetailMore;

    private WebSettings mWebSettings;
    private NewsDetialPopUpWindow mMPopupWindow;
    private int screenWidth;
    private int screenHeight;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        getScreenWidthAndHeight();

        getData();

    }

    private void getData() {
        mUri = getIntent().getData();
        if (mUri != null) {
            mWebSettings = webview.getSettings();
            mWebSettings.setJavaScriptEnabled(true);
            mWebSettings.setBuiltInZoomControls(true);

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    pbLoad.setVisibility(View.GONE);
                }
            });
            webview.loadUrl(String.valueOf(mUri));
        }
    }

    @OnClick({R.id.ib_back, R.id.icon_textsize, R.id.icon_share, R.id.fab_newsdetail_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.icon_textsize:
                showChangeTextDialog();
                break;
            case R.id.icon_share:
                showShare();
                break;
            case R.id.fab_newsdetail_more:

                final float x = fabNewsdetailMore.getX() - screenWidth / 2 + fabNewsdetailMore.getWidth() / 2;
                final float y = DensityUtil.dip2px(this, 180);

                mMPopupWindow = new NewsDetialPopUpWindow(NewsDetailActivity.this);

                mMPopupWindow.setClippingEnabled(true);
                mMPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationX", -x, 0);
                        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationY", -y, 0);
                        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("rotation", 720 - 45, 0);
                        ObjectAnimator.ofPropertyValuesHolder(fabNewsdetailMore, p1, p2, p3).setDuration(700).start();

                    }
                });

                PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationX", 0, -x);
                PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationY", 0, -y);
                PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("rotation", 0, 720 - 45);

                final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fabNewsdetailMore, p1, p2, p3);
                animator.setDuration(700);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mMPopupWindow.showAtLocation(webview, Gravity.BOTTOM, 0, 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();


                break;
        }
    }


    public void getScreenWidthAndHeight() {
        // 得到屏幕的宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
    }


    private int tempSuze = 2; //正常
    private int realSize = tempSuze;

    private void showChangeTextDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        String[] items = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};

        dialog.setTitle("设置文字大小")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realSize = tempSuze;
                        changeTextSize(realSize);
                    }
                }).setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSuze = which;
            }
        });

        dialog.show();

    }

    private void changeTextSize(int realSize) {
        switch (realSize) {
            case 0: // 超大
                mWebSettings.setTextZoom(200);
                break;
            case 1: // 大号
                mWebSettings.setTextZoom(150);
                break;
            case 2: // 正常
                mWebSettings.setTextZoom(100);
                break;
            case 3: // 小号
                mWebSettings.setTextZoom(75);
                break;
            case 4: // 超小
                mWebSettings.setTextZoom(50);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMPopupWindow != null && mMPopupWindow.isShowing()) {
            mMPopupWindow.dismiss();
            mMPopupWindow = null;
        }

    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("CapitalNews");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("哎呦不错哦");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f.hiphotos.baidu.com/image/h%3D360/sign=131b9d890d2442a7b10efba3e143ad95/4d086e061d950a7b8f42e9ce08d162d9f2d3c941.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(String.valueOf(mUri));
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("哎呦，不错哦");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("CapitalNews");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(String.valueOf(mUri));

        // 启动分享GUI
        oks.show(this);
    }
}
















