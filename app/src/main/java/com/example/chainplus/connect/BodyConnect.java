package com.example.chainplus.connect;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.chainplus.util.Const;
import com.example.chainplus.util.FROBody;
import com.example.chainplus.util.GetSocket;
import com.example.chainplus.util.StreamUtil;
import com.example.chainplus.viewModel.DataViewModel;

import java.io.IOException;
import java.net.Socket;

/**
 * 用于连接并自动获取检测到人体数据
 */
public class BodyConnect extends Thread {

    public volatile boolean exit = false;

    private final DataViewModel dataViewModel;
    private Context context;
    private Socket bodySocket;

    public BodyConnect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context = context;
    }

    @Override
    public void run() {
        Looper looper = Looper.myLooper();
        bodySocket = GetSocket.get(dataViewModel.getBodySensorIP().getValue(), Integer.parseInt(dataViewModel.getBodySensorPort().getValue()));
        while (!exit) {
            try {
                // 如果连接成功
                if (bodySocket != null) {
                    dataViewModel.getBodyIsConnect().postValue(true);
                    // 查询是否有人
                    StreamUtil.writeCommand(bodySocket.getOutputStream(), Const.BODY_CHK);
                    Thread.sleep(Const.time);
                    byte[] read_buff = StreamUtil.readData(bodySocket.getInputStream());
                    Boolean body = FROBody.getData(Const.BODY_LEN, Const.BODY_NUM, read_buff);
                    if (body != null) {
                        dataViewModel.getHasHuman().postValue(body);
                    }
                } else {
                    Log.d("abc", "doInBackground: 人体传感器连接失败");
                    Looper.prepare();
                    Toast.makeText(context, "人体传感器连接失败", Toast.LENGTH_SHORT).show();
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
            if (bodySocket != null) {
                bodySocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
