package com.example.chainplus.connect;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.chainplus.util.Const;
import com.example.chainplus.util.GetSocket;
import com.example.chainplus.util.StreamUtil;
import com.example.chainplus.viewModel.DataViewModel;

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

    @Override
    public void run() {
        super.run();
        Looper looper = Looper.myLooper();
        fanConnect = GetSocket.get(dataViewModel.getFanIP().getValue(), Integer.parseInt(dataViewModel.getFanPort().getValue()));
        Log.d("abc", "doInBackground: fan connect");
        while (!exit) {
            // 如果连接成功
            if (fanConnect != null) {
                dataViewModel.getFanIsConnect().postValue(true);
            } else {
                Log.d("abc", "doInBackground: 空调连接失败");
                Looper.prepare();
                Toast.makeText(context, "空调连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
                looper.quit();
                return;
            }
        }
        closeSocket();
    }

    public void closeSocket() {
        dataViewModel.getFanIsConnect().postValue(false);
        try {
            if (fanConnect != null) {
                fanConnect.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fanOn() {
        new Thread(() -> {
            if (fanConnect != null) {
                try {
                    StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_ON);
                    dataViewModel.getFanIsOpen().postValue(true);
                    Log.d("abc", "doInBackground: fan on");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void fanOff() {
        new Thread(() -> {
            if (fanConnect != null) {
                try {
                    StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_OFF);
                    dataViewModel.getFanIsOpen().postValue(false);
                    Log.d("abc", "doInBackground: fan off");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
