package com.example.bighomework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bighomework2.Connect.BodyConnect;
import com.example.bighomework2.Connect.FanConnect;
import com.example.bighomework2.Connect.TempHumConnect;
import com.example.bighomework2.databinding.ActivityMainBinding;
import com.example.bighomework2.viewModel.DataViewModel;

public class MainActivity extends AppCompatActivity {
    private DataViewModel data;
    private TempHumConnect tempHumConnect;
    private BodyConnect bodyConnect;
    private FanConnect fanConnect;

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
        bodyConnect.start();
        tempHumConnect = new TempHumConnect(this, data);
        tempHumConnect.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        data.getFans().setValue(false);
        closeAllSocket();
    }

    public void switchFans(View view){
        Boolean isOpen = data.getFans().getValue();
        if (isOpen) {
            data.getFans().setValue(false);
            fanConnect.exit = true;
        } else {
            data.getFans().setValue(true);
            fanConnect = new FanConnect(this, data);
            fanConnect.start();
        }
    }

    public void switchTempHumConnect(View view) {
        Boolean isConnect = data.getTempHumIsConnect().getValue();
        if (isConnect) {
            tempHumConnect.exit = true;
            data.getTempHumIsConnect().setValue(false);
        } else {
            tempHumConnect = new TempHumConnect(this, data);
            tempHumConnect.start();
        }
    }

    public void closeAllSocket() {
        tempHumConnect.exit = true;
        bodyConnect.exit = true;
        fanConnect.exit = true;
    }

}