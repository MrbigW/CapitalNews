package com.wrk.capitalnews.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.fragment.ContentFragment;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化Fragment
        initFragment();

    }

    private void initFragment() {
        // 1. 得到Fragment
        FragmentManager mFragManager = getSupportFragmentManager();
        // 2. 开启事务
        FragmentTransaction transaction = mFragManager.beginTransaction();
        // 3. 替换
        transaction.replace(R.id.main_content, new ContentFragment(), MAIN_CONTENT_TAG); // 主页

        // 4.提交
        transaction.commit();

    }
}






















