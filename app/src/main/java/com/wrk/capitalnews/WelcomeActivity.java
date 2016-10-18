package com.wrk.capitalnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.wrk.capitalnews.activity.GuideActivity;
import com.wrk.capitalnews.activity.MainActivity;
import com.wrk.capitalnews.utils.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {


    @BindView(R.id.rl_welcome_root)
    RelativeLayout rlWelcomeRoot;

    public static final String START_MAIN = "start_main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //渐变动画，缩放动画，旋转动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setDuration(2000);

        animationSet.setAnimationListener(new MyAnimationListener());
        rlWelcomeRoot.startAnimation(animationSet);

    }

    class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // 判断是否进入过主页面
            boolean isStartMain = CacheUtils.getBoolean(WelcomeActivity.this, START_MAIN);
            if (isStartMain) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
            }

            // 结束当前页面
            finish();

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}


















