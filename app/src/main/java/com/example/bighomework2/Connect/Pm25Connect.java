package com.example.bighomework2.Connect;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.bighomework2.Const;
import com.example.bighomework2.util.FROPm25;
import com.example.bighomework2.util.GetSocket;
import com.example.bighomework2.util.StreamUtil;
import com.example.bighomework2.viewModel.DataViewModel;

import java.io.IOException;
import java.net.Socket;

public class Pm25Connect extends Thread {
    private final DataViewModel dataViewModel;
    public volatile boolean exit = false;
    private Context context;
    private Socket pm25Socket;

    public Pm25Connect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context = context;
    }

    public void run() {
        Looper looper = Looper.myLooper();
        pm25Socket = GetSocket.get(Const.PM25_IP, Const.PM25_port);
        while (!exit) {
            try {
                // 如果连接成功
                if (pm25Socket != null) {
                    // 查询是否连接
                    StreamUtil.writeCommand(pm25Socket.getOutputStream(), Const.PM25_CHK);
                    Thread.sleep(Const.time);
                    byte[] read_buff = StreamUtil.readData(pm25Socket.getInputStream());
                    Float pm25 = FROPm25.getData(Const.PM25_LEN, Const.PM25_NUM, read_buff);
                    if (pm25 != null) {
                        dataViewModel.getPm25().postValue((int)(float)pm25);
                    }
                } else {
                    Log.d("abc", "doInBackground: PM2.5传感器连接失败");
                    Looper.prepare();
                    Toast.makeText(context, "PM2.5传感器连接失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    looper.quit();
                    return;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        closeSocket();
    }

    public void closeSocket() {
        try {
            if (pm25Socket != null) {
                pm25Socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
