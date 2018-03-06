package com.minaclient;


import android.os.Handler;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by LIANGSE on 2018/3/5.
 */

public class Utils {

    public static final Handler handler = new Handler();

    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 1000, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
    public static void runOnUiThreadView(final TextView textView, final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(str + "\n");
            }
        });
    }

    public static void executor(Runnable runnable) {
        executor.execute(runnable);
    }

}
