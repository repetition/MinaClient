package com.minaclient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by LIANGSE on 2018/3/12.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEDToIp;
    private Button mBTok;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        mEDToIp.findViewById(R.id.ed_to_ip);
        mBTok.findViewById(R.id.bt_ok);
        mBTok.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        String str = mEDToIp.getText().toString();
        if (null != str || !str.isEmpty() || str.equals("")) {
            SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("ip", str);
            edit.commit();
            Snackbar.make(mEDToIp,"保存成功",Snackbar.LENGTH_SHORT).show();
        }


    }
}
