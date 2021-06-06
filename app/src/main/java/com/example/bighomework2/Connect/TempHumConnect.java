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

public class TempHumConnect extends AsyncTask<Void, Void, Void> {

    private final DataViewModel dataViewModel;
    private Context context;
    private Socket temHumSocket;

    public TempHumConnect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context =context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        temHumSocket = GetSocket.get(Const.TEMHUM_IP, Const.TEMHUM_PORT);
        while (!isCancelled()) {
            Log.d("ABC", "doInBackground: running");
            try {
                // 如果全部连接成功
                if (temHumSocket != null) {
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
                    Looper.prepare();
                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
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
