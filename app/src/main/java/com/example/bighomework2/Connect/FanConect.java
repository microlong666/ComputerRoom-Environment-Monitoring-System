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

public class FanConect extends AsyncTask<Void, Void, Void> {

    private final DataViewModel dataViewModel;
    private Context context;
    private Socket fanConnect;

    public FanConect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Looper looper = Looper.myLooper();
        fanConnect = GetSocket.get(Const.BODY_IP, Const.BODY_port);
        Log.d("abc", "doInBackground: fan connect");
        while (!isCancelled()) {
            try {
                // 如果连接成功
                if (fanConnect != null) {
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
                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    looper.quit();
                    return null;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void closeSocket() {
        try {
            if (fanConnect != null) {
                fanConnect.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
