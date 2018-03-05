package com.mina;

import android.nfc.Tag;
import android.util.Log;

import com.minaclient.MainActivity;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.annotation.Target;
import java.net.InetSocketAddress;

/**
 * Created by RJYF-ZhangBo on 2018/3/5.
 */

public class MinaManager {
    public static String TAG = MinaManager.class.getName();

    public static final int DEFAULT_SEND_BUFFER_SIZE = 10 * 1024;
    public static final int DEFAULT_RECEIVE_BUFFER_SIZE = 10 * 1024;
    public static final int DEFAULT_RECONNECT_TIMEOUT = 5000;
    public static NioSocketConnector connector;
    private InetSocketAddress address;
    private IoSession session;

    public MinaManager(InetSocketAddress address) {
        this.address = address;
    }

    public void initMina() {
        connector = new NioSocketConnector();
        //接受数据大小
        connector.getSessionConfig().setReceiveBufferSize(DEFAULT_RECEIVE_BUFFER_SIZE);
        connector.getSessionConfig().setSendBufferSize(DEFAULT_SEND_BUFFER_SIZE);
    }

    public void send(String str){
        if (null == session) {
            return;
        }
        session.write(str);
    }



    public boolean connect() {
        try {
            connector.setConnectTimeoutMillis(DEFAULT_RECONNECT_TIMEOUT);
            connector.setDefaultRemoteAddress(new InetSocketAddress("192.168.8.105",7006));
            connector.setHandler(new ClientMessageHandler());
            ConnectFuture future = connector.connect();
            //等待连接到server
            future.awaitUninterruptibly();
            session = future.getSession();
            return future.isConnected();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public void close() {
        if (null == connector) {
            return ;
        }
        connector.dispose();
    }

}
