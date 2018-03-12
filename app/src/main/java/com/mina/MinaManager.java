package com.mina;

import android.nfc.Tag;
import android.util.Log;

import com.minaclient.MainActivity;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.annotation.Target;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

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
/*        TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
        factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
        factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));*/

    }

    public void send(String str) {
        if (null == session) {
            Log.e(TAG,"session is null!");
            return;
        }
        session.write(IoBuffer.wrap(str.getBytes(Charset.forName("utf-8"))));
    }


    public boolean connect() {
        try {
            connector.setConnectTimeoutMillis(DEFAULT_RECONNECT_TIMEOUT);
            connector.setDefaultRemoteAddress(address);
            connector.setHandler(new ClientMessageHandler());
            ConnectFuture future = connector.connect();
            //等待连接到server
            future.awaitUninterruptibly();
            // TODO: 2018/3/6 连接成功时才获取session
            boolean connected = future.isConnected();
            Log.i(TAG,"connected : "+connected);

            if (connected) {
                session = future.getSession();
            }
            return future.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void close() {
        if (null == connector) {
            return;
        }
        connector.dispose();
    }

}
