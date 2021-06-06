package com.example.bighomework2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.bighomework2.util.FROBody;
import com.example.bighomework2.util.FROTemHum;
import com.example.bighomework2.util.StreamUtil;
import com.example.bighomework2.viewModel.DataViewModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "wyatex";
    private Context context;
    private DataViewModel dataViewModel;

    private Float tem;
    private Float hum;
    private byte[] read_buff;

    private Socket temHumSocket;
    private Socket bodySocket;
    private Socket fansSocket;

    private boolean CIRCLE = false;

    public static InputStream fansInputStream;
    public static OutputStream fansOutputStream;

    public ConnectTask(Context context, DataViewModel dataViewModel) {
        this.context = context;
        this.dataViewModel = dataViewModel;
    }

    /**
     * 建立连接并返回socket，若连接失败返回null
     *
     * @param ip
     * @param port
     * @return
     */
    private Socket getSocket(String ip, int port) {
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

    public void setCIRCLE(boolean cIRCLE) {
        CIRCLE = cIRCLE;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // 连接
        temHumSocket = getSocket(Const.TEMHUM_IP, Const.TEMHUM_PORT);
        bodySocket = getSocket(Const.BODY_IP, Const.BODY_port);
        fansSocket = getSocket(Const.FAN_IP, Const.FAN_PORT);
        try {
            assert fansSocket != null;
            fansInputStream = fansSocket.getInputStream();// 得到输入流
            fansOutputStream = fansSocket.getOutputStream();// 得到输出流
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 风扇启动时默认关闭
        StreamUtil.writeCommand(fansOutputStream, Const.FAN_OFF);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (CIRCLE) {
            try {
                // 如果全部连接成功
                if (temHumSocket != null && bodySocket != null && fansSocket != null) {
                    // 查询光照度
                    StreamUtil.writeCommand(temHumSocket.getOutputStream(), Const.TEMHUM_CHK);
                    Thread.sleep(Const.time);
                    read_buff = StreamUtil.readData(temHumSocket.getInputStream());
                    tem = FROTemHum.getTemData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    hum = FROTemHum.getHumData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    dataViewModel.getTemperature().postValue(((int)(tem * 100)/ 100.0));
                    dataViewModel.getHumidity().postValue(((int)(hum * 100)/ 100.0));

                    // 查询人体
                    StreamUtil.writeCommand(bodySocket.getOutputStream(), Const.BODY_CHK);
                    Thread.sleep(Const.time);
                    read_buff = StreamUtil.readData(bodySocket.getInputStream());
                    Boolean body = FROBody.getData(Const.BODY_LEN, Const.BODY_NUM, read_buff);
                    Log.d(TAG, "doInBackground: 人体：" + body);
                    if (body != null) {
                        dataViewModel.getHasHuman().postValue(body);
                    }

                    // 发开关风扇命令
                    Boolean fanIsOpen = dataViewModel.getFans().getValue();
                    if (fanIsOpen) {
                        StreamUtil.writeCommand(fansOutputStream, Const.FAN_ON);
                    } else {
                        StreamUtil.writeCommand(fansOutputStream, Const.FAN_OFF);
                    }

                } else {
                    Looper.prepare();
                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    void closeSocket() {
        try {
            if (temHumSocket != null) {
                temHumSocket.close();
            }
            if (bodySocket != null) {
                bodySocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
