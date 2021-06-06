package com.example.bighomework2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bighomework2.Connect.BodyConnect;
import com.example.bighomework2.Connect.FanConect;
import com.example.bighomework2.Connect.TempHumConnect;
import com.example.bighomework2.databinding.ActivityMainBinding;
import com.example.bighomework2.viewModel.DataViewModel;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private DataViewModel data;
    private TempHumConnect tempHumConnect;
    private BodyConnect bodyConnect;
    private FanConect fanConnect;

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
        bodyConnect = new BodyConnect(this, data);
        bodyConnect.execute();
        fanConnect = new FanConect(this, data);
        fanConnect.execute();
        tempHumConnect = new TempHumConnect(this, data);
        tempHumConnect.execute();
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
            data.getFans().setValue(false);
        } else {
            data.getFans().setValue(true);
        }
    }

    public void switchTempHumConnect(View view) {
        Boolean isConnect = data.getTempHumIsConnect().getValue();
        if (isConnect) {
            Log.d("abc", "doInBackground: 温湿度传感器打开");
            tempHumConnect.cancel(true);
            tempHumConnect.closeSocket();
            data.getTempHumIsConnect().setValue(false);
        } else {
            Log.d("abc", "doInBackground: 温湿度传感器关闭");
            tempHumConnect = new TempHumConnect(this, data);
            tempHumConnect.execute();
        }
    }

    public void closeAllSocket() throws IOException {
        tempHumConnect.cancel(true);
        tempHumConnect.closeSocket();
        bodyConnect.cancel(true);
        bodyConnect.closeSocket();
        fanConnect.cancel(true);
        fanConnect.closeSocket();
    }

}