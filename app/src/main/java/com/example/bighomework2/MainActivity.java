package com.example.bighomework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bighomework2.Connect.TempHumConnect;
import com.example.bighomework2.databinding.ActivityMainBinding;
import com.example.bighomework2.util.FROIOControl;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        data.getFans().setValue(false);
        tempHumConnect.cancel(true);
        tempHumConnect.closeSocket();
    }

    public void switchFans(View view) throws InterruptedException {
        Boolean isOpen = data.getFans().getValue();
        if (isOpen) {
            data.getFans().setValue(false);
            return;

        } else {
            data.getFans().setValue(true);
            return;
        }
    }

    public void switchTempHumConnect(View view) {
        Boolean isConnect = data.getTempHumIsConnect().getValue();
        if (isConnect) {
            data.getTempHumIsConnect().setValue(false);
            tempHumConnect.cancel(true);
            tempHumConnect.closeSocket();
        } else {
            tempHumConnect = new TempHumConnect(this, data);
            tempHumConnect.execute();
            data.getTempHumIsConnect().setValue(true);
        }
    }

}