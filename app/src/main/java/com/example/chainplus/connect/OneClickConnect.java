package com.example.chainplus.connect;

import android.content.Context;
import android.os.Looper;

import com.example.chainplus.util.Const;
import com.example.chainplus.util.GetSocket;
import com.example.chainplus.viewModel.DataViewModel;

import java.net.Socket;

public class OneClickConnect extends Thread {

    private final DataViewModel dataViewModel;
    private Context context;
    private Socket temHumSocket;
    private Socket fanSocket;
    private Socket buzzerSocket;
    private Socket pm25Socket;

    public OneClickConnect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        Looper looper = Looper.myLooper();
        temHumSocket = GetSocket.get(Const.TEMHUM_IP, Const.TEMHUM_PORT);
        fanSocket = GetSocket.get(Const.FAN_IP, Const.FAN_PORT);
        pm25Socket = GetSocket.get(Const.PM25_IP, Const.PM25_port);

        // TODO 一键连接

        try {

        } catch (Exception e) {

        }
    }
}
