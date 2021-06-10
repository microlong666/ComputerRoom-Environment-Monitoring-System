package com.example.bighomework2.Connect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.bighomework2.Const;
import com.example.bighomework2.util.GetSocket;
import com.example.bighomework2.util.StreamUtil;
import com.example.bighomework2.viewModel.DataViewModel;

import java.io.IOException;
import java.net.Socket;

public class FanConnect extends Thread {
    public volatile boolean exit = false;

    private final DataViewModel dataViewModel;
    private Context context;
    private Socket fanConnect;

    public FanConnect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context = context;
    }

    public void run() {
        super.run();
        Looper looper = Looper.myLooper();
        fanConnect = GetSocket.get(Const.FAN_IP, Const.FAN_PORT);
        Log.d("abc", "doInBackground: fan connect");
        while (!exit) {
            try {
                // 如果连接成功
                if (fanConnect != null) {
                    dataViewModel.getFansIsConnect().postValue(true);
                    // 判断应该开风扇还是关风扇
                    if (dataViewModel.getFans().getValue()) {
                        Log.d("abc", "doInBackground: fan open");
                        StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_ON);
                    } else {
                        Log.d("abc", "doInBackground: fan close");
                        StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_OFF);
                    }
                    Thread.sleep(Const.time);
                } else {
                    Log.d("abc", "doInBackground: 风扇连接失败");
                    Looper.prepare();
                    Toast.makeText(context, "风扇连接失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    looper.quit();
                    return;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Log.d("abc", "doInBackground: fan close");
            StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_OFF);
            closeSocket();
            Thread.sleep(Const.time);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket() {
        dataViewModel.getFansIsConnect().postValue(false);
        try {
            if (fanConnect != null) {
                fanConnect.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fanOn() {
        class MyThread extends Thread {
            public void run() {
                if (fanConnect != null) {
                    try {
                        StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_ON);
                        dataViewModel.getFans().postValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        (new MyThread()).start();
    }

    public void fanOff() {
        class MyThread extends Thread {
            public void run() {
                if (fanConnect != null) {
                    try {
                        StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_OFF);
                        dataViewModel.getFans().postValue(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        (new MyThread()).start();
    }
}
