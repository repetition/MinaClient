package com.minaclient;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mina.MinaManager;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,View.OnLongClickListener {
    public static String TAG = MainActivity.class.getName();

    private Button mBt_Connect;
    private Button mBt_Close;
    private Button mBt_Send;
    public static ScrollView mScrollView;
    private EditText mEd_Addr;
    private EditText mEd_Send;
    public static TextView mTv_Received = null;
    private MinaManager minaManager;
    private InetSocketAddress address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mScrollView = findViewById(R.id.scrollView);
        mBt_Connect = findViewById(R.id.bt_connect);
        mBt_Close = findViewById(R.id.bt_close);
        mBt_Send = findViewById(R.id.bt_send);
        mTv_Received = findViewById(R.id.tv_received);
        mEd_Addr = findViewById(R.id.ed_ip);
        mEd_Send = findViewById(R.id.ed_send);
        mBt_Connect.setOnClickListener(this);
        mBt_Close.setOnClickListener(this);
        mBt_Send.setOnClickListener(this);
        mBt_Send.setOnLongClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_connect:
                //执行连接
                connect();
                break;
            case R.id.bt_close:
                minaManager.close();
                mBt_Connect.setEnabled(true);
                mBt_Close.setEnabled(false);
                break;
            case R.id.bt_send:
                String str_Send = mEd_Send.getText().toString();
                Log.i(TAG, "send:" + str_Send);
                minaManager.send(str_Send);

                break;
        }

    }

    @Override
    public boolean onLongClick(View v) {




        return false;
    }


    private void connect() {
        Utils.executor(new Runnable() {
            @Override
            public void run() {
                String[] strings = null;
                String ip_Str = mEd_Addr.getText().toString();
                if (null == ip_Str || ip_Str.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                strings = ip_Str.split(":");
                address = new InetSocketAddress(strings[0], Integer.valueOf(strings[1]));
                minaManager = new MinaManager(address);
                minaManager.initMina();
                Log.i(TAG, "执行连接...地址：" + Arrays.toString(strings));
                Snackbar.make(mBt_Connect, "正在连接！+地址：" + Arrays.toString(strings), Snackbar.LENGTH_SHORT).show();
                Utils.runOnUiThreadView(mTv_Received, "connect to server " + Arrays.toString(strings));
                boolean connect = minaManager.connect();
                if (!connect) {
                    Log.i(TAG, "连接失败");
                    Snackbar.make(mBt_Connect, "连接：" + Arrays.toString(strings) + "失败!", Snackbar.LENGTH_SHORT).show();
                    mTv_Received.append("connect to server " + Arrays.toString(strings) + "fail!");
                    Utils.runOnUiThreadView(mTv_Received, "connect to server " + Arrays.toString(strings) + "fail!");
                    return;
                }
                Utils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBt_Connect.setEnabled(false);
                        mBt_Close.setEnabled(true);
                    }
                });
                Log.i(TAG, "连接成功!");
                Snackbar.make(mBt_Connect, "连接成功！", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
