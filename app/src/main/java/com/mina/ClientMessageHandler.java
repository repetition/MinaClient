package com.mina;

import android.util.Log;
import android.widget.TextView;

import com.minaclient.MainActivity;
import com.minaclient.Utils;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * Created by RJYF-ZhangBo on 2018/3/5.
 */

public class ClientMessageHandler extends IoHandlerAdapter {

    public static String TAG = ClientMessageHandler.class.getName();

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        Log.i(TAG,session.getRemoteAddress()+" create connected!");
        Utils.runOnUiThreadView(MainActivity.mTv_Received,"Client: "+session.getRemoteAddress()+" create connected!\n");

    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        Log.i(TAG,session.getRemoteAddress()+" opened!");
        Utils.runOnUiThreadView(MainActivity.mTv_Received,"Client: "+session.getRemoteAddress()+" opened!\n");

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        Log.i(TAG,session.getRemoteAddress()+" closed!");
        Utils.runOnUiThreadView(MainActivity.mTv_Received,"Client: "+session.getRemoteAddress()+" closed!\n");

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        Log.i(TAG,session.getRemoteAddress()+" sessionIdle!");
        Utils.runOnUiThreadView(MainActivity.mTv_Received,"Client: "+session.getRemoteAddress()+" sessionIdle!\n");


    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        Log.i(TAG,session.getRemoteAddress()+" exceptionCaught!\n"+cause.getMessage());
        Utils.runOnUiThreadView(MainActivity.mTv_Received,"Client: "+session.getRemoteAddress()+" exceptionCaught!\n");

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        Log.i(TAG,session.getRemoteAddress()+" received!");
        Utils.runOnUiThreadView(MainActivity.mTv_Received,"Client: "+session.getRemoteAddress()+" received!\n");

        if (message instanceof IoBuffer) {

            IoBuffer buffer = (IoBuffer) message;
            InputStream inputStream = buffer.asInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result +=line;
            }
            Log.i(TAG , "收到服务器消息："+result);
            Utils.runOnUiThreadView(MainActivity.mTv_Received,"Server: "+session.getRemoteAddress()+" - "+result+" \n");

        } else if (message instanceof String) {

        }



    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        Log.i(TAG,session.getRemoteAddress()+" messageSent!");
        Utils.runOnUiThreadView(MainActivity.mTv_Received,"Client: "+session.getRemoteAddress()+" messageSent!\n");
        Utils.runOnUiThreadView(MainActivity.mTv_Received,"Client: "+message.toString()+"\n");

    }
}
