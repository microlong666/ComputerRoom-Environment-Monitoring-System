package com.example.bighomework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bighomework2.Connect.BodyConnect;
import com.example.bighomework2.Connect.TempHumConnect;
import com.example.bighomework2.databinding.ActivityMainBinding;
import com.example.bighomework2.util.FROIOControl;
import com.example.bighomework2.util.GetSocket;
import com.example.bighomework2.util.StreamUtil;
import com.example.bighomework2.viewModel.DataViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private DataViewModel data;
    private TempHumConnect tempHumConnect;
    private Socket fanConnect;
    private BodyConnect bodyConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        data = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DataViewModel.class);
        data.getTempHumIsConnect();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setData(data);
        binding.setLifecycleOwner(this);
//        connectTask = new ConnectTask(this, data);
//        connectTask.setCIRCLE(true);
//        connectTask.execute();
//        if (data.getTempHumConnect() == null) {
//            TempHumConnect tempHumConnect = new TempHumConnect(this, data);
//            data.getTempHumConnect().setValue(tempHumConnect);
//            tempHumConnect.execute();
//        }
//        tempHumConnect = new TempHumConnect(this, data);
//        tempHumConnect.execute();
        if (bodyConnect == null) {
            bodyConnect = new BodyConnect(this, data);
        }
        if (fanConnect == null) {
            fanConnect = GetSocket.get(Const.FAN_IP, Const.FAN_PORT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        data.getFans().setValue(false);
        try {
            closeAllSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchFans(View view) throws InterruptedException, IOException {
        Boolean isOpen = data.getFans().getValue();
        if (isOpen) {
            StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_OFF);
            data.getFans().setValue(false);
        } else {
            StreamUtil.writeCommand(fanConnect.getOutputStream(), Const.FAN_ON);
            data.getFans().setValue(true);
        }
    }

    public void switchTempHumConnect(View view) {
        Boolean isConnect = data.getTempHumIsConnect().getValue();
        if (isConnect) {
            tempHumConnect.cancel(true);
            tempHumConnect.closeSocket();
            data.getTempHumIsConnect().setValue(false);
        } else {
            tempHumConnect = new TempHumConnect(this, data);
            tempHumConnect.execute();
        }
    }

    public void closeAllSocket() throws IOException {
        fanConnect.close();
        tempHumConnect.cancel(true);
        tempHumConnect.closeSocket();
    }

}