package com.example.bighomework2.util;

import android.util.Log;

import com.example.bighomework2.Const;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GetSocket {
    public static Socket get(String ip, int port) {
        Socket mSocket = new Socket();
        InetSocketAddress mSocketAddress = new InetSocketAddress(ip, port);
        // socket连接
        try {
            // 设置连接超时时间为3秒
            mSocket.connect(mSocketAddress, 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 检查是否连接成功
        if (mSocket.isConnected()) {
            Log.i(Const.TAG, ip + "连接成功！");
            return mSocket;
        } else {
            Log.i(Const.TAG, ip + "连接失败！");
            return null;
        }
    }
}
