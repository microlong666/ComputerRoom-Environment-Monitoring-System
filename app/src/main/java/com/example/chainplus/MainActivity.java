package com.example.chainplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chainplus.connect.BodyConnect;
import com.example.chainplus.connect.FanConnect;
import com.example.chainplus.connect.Pm25Connect;
import com.example.chainplus.connect.TempHumConnect;
import com.example.chainplus.databinding.ActivityMainBinding;
import com.example.chainplus.fragment.ConnectSettingFragment;
import com.example.chainplus.fragment.DataFragment;
import com.example.chainplus.fragment.MineFragment;
import com.example.chainplus.util.Const;
import com.example.chainplus.viewModel.DataViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private DataViewModel dataViewModel;
    private TempHumConnect tempHumConnect;
    private BodyConnect bodyConnect;
    private FanConnect fanConnect;
    private Pm25Connect pm25Connect;
    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 创建ViewModel
        dataViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DataViewModel.class);
        // 使用代码的设置
        dataViewModel.initData();
        // 使用已经保存的设置进行替换
        useLocalSetting();

        // 创建试图
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        dialogBuilder = new AlertDialog.Builder(this);

        // 启动时默认创建连接
        bodyConnect = new BodyConnect(this, dataViewModel);
        bodyConnect.start();
        tempHumConnect = new TempHumConnect(this, dataViewModel);
        tempHumConnect.start();
        pm25Connect = new Pm25Connect(this, dataViewModel);
        pm25Connect.start();
        fanConnect = new FanConnect(this, dataViewModel);
        fanConnect.start();

        // 开启定时任务
        startTimer();

        // 设置点击事件
        BottomNavigationView bar = findViewById(R.id.bottomNavigation);
        bar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.index:
                    switchToFragment("index");
                    return true;
                case R.id.mine:
                    switchToFragment("mine");
                    return true;
            }
            return false;
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void oneClickConnect(View view) {
        // WLAN 状态检测
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            dialogBuilder
                    .setTitle("WLAN未开启")
                    .setMessage("请先开启手机WLAN，再重新连接设备。")
                    .setNegativeButton("取消", (dialog, which) -> {
                    })
                    .setPositiveButton("确定", (dialog, which) -> startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)))
                    .create()
                    .show();
        }
        // TODO 一键连接

    }

    public void switchFans(View view){
        Boolean isConnect= dataViewModel.getFanIsConnect().getValue();
        Boolean isOpen = dataViewModel.getFanIsOpen().getValue();
        Const.linkage = false;
        if (isConnect) {
            if (isOpen) {
                fanConnect.fanOff();
                dataViewModel.getFanIsOpen().postValue(false);
            } else {
                fanConnect.fanOn();
                dataViewModel.getFanIsOpen().postValue(true);
            }
        }
    }

    public void switchTempHumConnect(View view) {
        Boolean isConnect = dataViewModel.getTempHumIsConnect().getValue();
        if (isConnect) {
            tempHumConnect.exit = true;
            dataViewModel.getTempHumIsConnect().setValue(false);
        } else {
            tempHumConnect = new TempHumConnect(this, dataViewModel);
            tempHumConnect.start();
        }
    }

    public void switchPm25Connect(View view) {
        Boolean isConnect = dataViewModel.getPm25IsConnect().getValue();
        if (isConnect) {
            pm25Connect.exit = true;
            dataViewModel.getPm25IsConnect().postValue(false);
        } else {
            pm25Connect = new Pm25Connect(this, dataViewModel);
            pm25Connect.start();
        }
    }


    private void startTimer() {
        // 随机数据生成
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                data.getTempHumIsConnect().postValue(!data.getTempHumIsConnect().getValue());
//            }
//        }, 1000, 1000);

        // 判定是否全部连接，如果是那么一键全部连接消失
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Boolean fanIsConnect = dataViewModel.getFanIsConnect().getValue();
                Boolean tempHumIsConnect = dataViewModel.getTempHumIsConnect().getValue();
                Boolean pm25IsConnect = dataViewModel.getPm25IsConnect().getValue();
                if (fanIsConnect && tempHumIsConnect && pm25IsConnect) {
                    dataViewModel.getConnectVisibility().postValue(false);
                } else {
                    dataViewModel.getConnectVisibility().postValue(true);
                }
            }
        }, 0, 50);
    }

    private String getSettingData(String key) {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, "");
    }

    private void setSettingData(String key, String value) {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void useLocalSetting() {
        if ("".equals(getSettingData("tempHumSensorIP"))) {
            Log.d("abc", "useLocalSetting: tempHumSensorIP"+getSettingData("tempHumSensorIP"));
            dataViewModel.getTempHumSensorIp().setValue(getSettingData("tempHumSensorIP"));
        }
        if ("".equals(getSettingData("tempHumSensorPort"))) {
            dataViewModel.getTempHumSensorPort().setValue(getSettingData("tempHumSensorPort"));
        }
        if ("".equals(getSettingData("PM25SensorIP"))) {
            dataViewModel.getPM25SensorIp().setValue(getSettingData("PM25SensorIP"));
        }
        if ("".equals(getSettingData("PM25SensorPort"))) {
            dataViewModel.getPM25SensorPort().setValue(getSettingData("PM25SensorPort"));
        }
        if ("".equals(getSettingData("bodySensorIP"))) {
            dataViewModel.getBodySensorIp().setValue(getSettingData("bodySensorIP"));
        }
        if ("".equals(getSettingData("bodySensorPort"))) {
            dataViewModel.getBodySensorPort().setValue(getSettingData("bodySensorPort"));
        }
        if ("".equals(getSettingData("fanIP"))) {
            dataViewModel.getFanIp().setValue(getSettingData("fanIP"));
        }
        if ("".equals(getSettingData("fanPort"))) {
            dataViewModel.getFanPort().setValue(getSettingData("fanPort"));
        }
    }

    // 切换fragment
    private void switchToFragment(String fragment) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if ("index".equals(fragment)) {
            DataFragment dataFragment = new DataFragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, dataFragment);
            fragmentTransaction.commit();
        } else if ("mine".equals(fragment)) {
            MineFragment mineFragment = new MineFragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, mineFragment);
            fragmentTransaction.commit();
        }
    }

    public void toConnectSetting(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ConnectSettingFragment fragment = new ConnectSettingFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    public void backToMind(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MineFragment fragment = new MineFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    public void setConnect(View view) {
        // 保存设置
        setSettingData("tempHumSensorIP", dataViewModel.getTempHumSensorIp().getValue());
        setSettingData("tempHumSensorPort", dataViewModel.getTempHumSensorPort().getValue());
        setSettingData("PM25SensorIP", dataViewModel.getPM25SensorIp().getValue());
        setSettingData("PM25SensorPort", dataViewModel.getPM25SensorPort().getValue());
        setSettingData("bodySensorIP", dataViewModel.getBodySensorIp().getValue());
        setSettingData("bodySensorPort", dataViewModel.getBodySensorPort().getValue());
        setSettingData("fanIP", dataViewModel.getFanIp().getValue());
        setSettingData("fanPort", dataViewModel.getFanPort().getValue());

        Toast.makeText(this, "设置保存成功", Toast.LENGTH_SHORT).show();

        // 返回
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MineFragment fragment = new MineFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

}