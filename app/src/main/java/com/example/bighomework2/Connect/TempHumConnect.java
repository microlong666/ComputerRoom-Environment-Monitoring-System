package com.example.bighomework2.Connect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.bighomework2.Const;
import com.example.bighomework2.util.FROTemHum;
import com.example.bighomework2.util.GetSocket;
import com.example.bighomework2.util.StreamUtil;
import com.example.bighomework2.viewModel.DataViewModel;

import java.io.IOException;
import java.net.Socket;

/**
 * 用于连接并自动获取温湿度数据
 */
public class TempHumConnect extends AsyncTask<Void, Void, Void> {

    private final DataViewModel dataViewModel;
    private Context context;
    private Socket temHumSocket;

    public TempHumConnect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Looper looper = Looper.myLooper();
        temHumSocket = GetSocket.get(Const.TEMHUM_IP, Const.TEMHUM_PORT);
        Log.d("abc", "doInBackground: 温湿度传感器isCancelled：" + isCancelled());
        while (!isCancelled()) {
            try {
                // 如果连接成功
                if (temHumSocket != null) {
                    Log.d("abc", "doInBackground: 温湿度传感器连接成功");
                    dataViewModel.getTempHumIsConnect().postValue(true);
                    // 查询温湿度
                    StreamUtil.writeCommand(temHumSocket.getOutputStream(), Const.TEMHUM_CHK);
                    Thread.sleep(Const.time);
                    byte[] read_buff = StreamUtil.readData(temHumSocket.getInputStream());
                    float tem = FROTemHum.getTemData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    float hum = FROTemHum.getHumData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    dataViewModel.getTemperature().postValue(((int)(tem * 100)/ 100.0));
                    dataViewModel.getHumidity().postValue(((int)(hum * 100)/ 100.0));
                } else {
                    Log.d("abc", "doInBackground: 温湿度传感器连接失败");
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
            if (temHumSocket != null) {
                temHumSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
