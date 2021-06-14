package com.example.chainplus.connect;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.chainplus.util.Const;
import com.example.chainplus.util.FROTemHum;
import com.example.chainplus.util.GetSocket;
import com.example.chainplus.util.StreamUtil;
import com.example.chainplus.viewModel.DataViewModel;

import java.io.IOException;
import java.net.Socket;

/**
 * 用于连接并自动获取温湿度数据
 */
public class TempHumConnect extends Thread {
    public volatile boolean exit = false;

    private final DataViewModel dataViewModel;
    private Context context;
    private Socket temHumSocket;
    private Socket fanSocket;
    private Socket buzzerSocket;

    public TempHumConnect(Context context, DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
        this.context = context;
    }

    @Override
    public void run() {
        Looper looper = Looper.myLooper();
        temHumSocket = GetSocket.get(dataViewModel.getTempHumSensorIP().getValue(), Integer.parseInt(dataViewModel.getTempHumSensorPort().getValue()));
        fanSocket = GetSocket.get(dataViewModel.getFanIP().getValue(), Integer.parseInt(dataViewModel.getFanPort().getValue()));
        buzzerSocket = GetSocket.get(dataViewModel.getBuzzerIP().getValue(), Integer.parseInt(dataViewModel.getBuzzerPort().getValue()));
        while (!exit) {
            try {
                // 如果连接成功
                if (temHumSocket != null) {
                    dataViewModel.getTempHumIsConnect().postValue(true);
                    // 查询温湿度
                    StreamUtil.writeCommand(temHumSocket.getOutputStream(), Const.TEMHUM_CHK);
                    Thread.sleep(Const.time);
                    byte[] read_buff = StreamUtil.readData(temHumSocket.getInputStream());
                    Float tem = FROTemHum.getTemData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    Float hum = FROTemHum.getHumData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    if (tem != null && hum != null) {
                        dataViewModel.getTemperature().postValue((int) (tem * 100) / 100.0);
                        dataViewModel.getHumidity().postValue((int) (hum * 100) / 100.0);
                    }

                    double temperatureThreshold = Double.parseDouble("".equals(dataViewModel.getTemperatureThreshold().getValue()) ? "1000" : dataViewModel.getTemperatureThreshold().getValue());
                    double humidityThreshold = Double.parseDouble("".equals(dataViewModel.getHumidityThreshold().getValue()) ? "1000" : dataViewModel.getHumidityThreshold().getValue());
                    // 空调联动
                    if (Boolean.parseBoolean(dataViewModel.getIsLinkage().getValue())) {
                        if (dataViewModel.getTemperature().getValue() >= temperatureThreshold || dataViewModel.getHumidity().getValue() >= humidityThreshold) {
                            // 风扇
                            StreamUtil.writeCommand(fanSocket.getOutputStream(), Const.FAN_ON);
                            dataViewModel.getFanIsOpen().postValue(true);
                        } else {
                            StreamUtil.writeCommand(fanSocket.getOutputStream(), Const.FAN_OFF);
                            dataViewModel.getFanIsOpen().postValue(false);
                        }
                    }
                    // 警报联动
                    if (Boolean.parseBoolean(dataViewModel.getIsOpenAlert().getValue())) {
                        if (dataViewModel.getTemperature().getValue() >= temperatureThreshold || dataViewModel.getHumidity().getValue() >= humidityThreshold) {
                            StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.RED_CMD);
                            Thread.sleep(200);
                            StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_ON);
                            Thread.sleep(200);
                            StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
                        }
                    }

                } else {
                    Log.d("abc", "doInBackground: 温湿度传感器连接失败");
                    Looper.prepare();
                    Toast.makeText(context, "温湿度传感器连接失败", Toast.LENGTH_SHORT).show();
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
            if (temHumSocket != null) {
                temHumSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
