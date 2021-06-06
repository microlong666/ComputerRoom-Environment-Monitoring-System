package com.example.bighomework2.Connect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.bighomework2.Const;
import com.example.bighomework2.util.FROBody;
import com.example.bighomework2.util.GetSocket;
import com.example.bighomework2.util.StreamUtil;
import com.example.bighomework2.viewModel.DataViewModel;

import java.io.IOException;
import java.net.Socket;

/**
 * 用于连接并自动获取检测到人体数据
 */
public class BodyConnect extends AsyncTask<Void, Void, Void> {

    private final DataViewModel dataViewModel;
    private Context context;
    private Socket bodySocket;

    public BodyConnect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Looper looper = Looper.myLooper();
        bodySocket = GetSocket.get(Const.BODY_IP, Const.BODY_port);
        while (!isCancelled()) {
            try {
                // 如果连接成功
                if (bodySocket != null) {
                    // 查询是否有人
                    StreamUtil.writeCommand(bodySocket.getOutputStream(), Const.BODY_CHK);
                    Thread.sleep(Const.time);
                    byte[] read_buff = StreamUtil.readData(bodySocket.getInputStream());
                    Boolean body = FROBody.getData(Const.BODY_LEN, Const.BODY_NUM, read_buff);
                    if (body != null) {
                        dataViewModel.getHasHuman().postValue(body);
                    }
                } else {
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
}
