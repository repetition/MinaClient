package com.minaclient;


import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.minaclient.MainActivity.mScrollView;

/**
 * Created by LIANGSE on 2018/3/5.
 */

public class Utils {
    private static String TAG = Utils.class.getName();

    private static final Handler handler = new Handler();

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 1000, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    private static SharedPreferences sp = null;

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static void runOnUiThreadView(final TextView textView, final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(str + "\n");
                scrollToBottom(mScrollView, textView);
            }
        });
    }

    // TODO: 2018/3/12 添加设置发送给指定的ip
    public static SharedPreferences getSp() {
        sp = MyApplication.getContext().getSharedPreferences("setting", MODE_PRIVATE);
        return sp;
    }

    public static void executor(Runnable runnable) {
        executor.execute(runnable);
    }


    /**
     * 根据scrolview 和子view去测量滑动的位置
     *
     * @param scrollView
     * @param view
     */
    public static void scrollToBottom(final ScrollView scrollView, final View view) {
        Utils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (scrollView == null || view == null) {
                    return;
                }
                // offset偏移量。是指当textview中内容超出 scrollview的高度，那么超出部分就是偏移量
                int offset = view.getMeasuredHeight()
                        - scrollView.getMeasuredHeight();
                Log.i(TAG, "run: view.getMeasuredHeight()" + view.getMeasuredHeight() + " scrollView.getMeasuredHeight()" + scrollView.getMeasuredHeight());
                if (offset < 0) {
                    offset = 0;
                }
                //scrollview开始滚动
                scrollView.scrollTo(0, offset);
            }
        });


    }

}
