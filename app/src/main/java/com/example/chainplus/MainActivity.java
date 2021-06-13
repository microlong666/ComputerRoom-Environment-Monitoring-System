package com.example.chainplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.chainplus.connect.BodyConnect;
import com.example.chainplus.connect.FanConnect;
import com.example.chainplus.connect.Pm25Connect;
import com.example.chainplus.connect.TempHumConnect;
import com.example.chainplus.databinding.ActivityMainBinding;
import com.example.chainplus.fragment.ConnectSettingFragment;
import com.example.chainplus.fragment.DataFragment;
import com.example.chainplus.fragment.LinkageSettingFragment;
import com.example.chainplus.fragment.MineFragment;
import com.example.chainplus.util.Const;
import com.example.chainplus.viewModel.DataViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
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

        // 设置顶部状态栏字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // 创建ViewModel
        dataViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DataViewModel.class);
        // 使用代码的设置
        dataViewModel.initData();
        // 使用已经保存的设置进行替换
        useLocalSetting();

        // 创建视图
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // 创建对话框构造器
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
        startFastTimer();
        startSlowTimer();

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

    /**
     * 连接按钮点击事件
     */
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

        if (!dataViewModel.getTempHumIsConnect().getValue()) {
            tempHumConnect = new TempHumConnect(this, dataViewModel);
            tempHumConnect.start();
        }
        if (!dataViewModel.getFanIsConnect().getValue()) {
            fanConnect = new FanConnect(this, dataViewModel);
            fanConnect.start();
        }
        if (!dataViewModel.getPm25IsConnect().getValue()) {
            pm25Connect = new Pm25Connect(this, dataViewModel);
            pm25Connect.start();
        }
        if (!dataViewModel.getBodyIsConnect().getValue()) {
            bodyConnect = new BodyConnect(this, dataViewModel);
            bodyConnect.start();
        }
    }

    /**
     * 风扇开关和连接与断开切换
     */
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

    /**
     * 温湿度连接与断开切换
     */
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

    /**
     * PM2.5连接与断开切换
     */
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

    /**
     * 定时任务(高速)
     */
    private void startFastTimer() {
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

    /**
     * 定时任务(低速)
     */
    private void startSlowTimer() {
        // 判定是否全部连接，如果是那么一键全部连接消失
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dataViewModel.getHealth().postValue((int)(Math.random()*100));
            }
        }, 2000, 500);
    }

    /**
     * 读取SharedPreference的设置数据
     */
    private String getSettingData(String key) {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, "");
    }

    /**
     * 设置数据写入SharedPreference
     */
    private void setSettingData(String key, String value) {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 使用SharedPreference数据替换ViewModel的数据
     */
    private void useLocalSetting() {
        String[] setting = Const.setting;
        for (int i = 0; i < setting.length; i++) {
            if (!"".equals(getSettingData(setting[i]))) {
                try {
                    Field f = DataViewModel.class.getDeclaredField(setting[i]);
                    f.setAccessible(true);
                    MutableLiveData<String> data = new MutableLiveData<String>(getSettingData(setting[i]));
                    f.set(dataViewModel, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 切换fragment
     * @param fragment
     */
    private void switchToFragment(String fragment) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if ("index".equals(fragment)) {
            fragmentTransaction.replace(R.id.fragmentContainerView, new DataFragment()).commit();
        } else if ("mine".equals(fragment)) {
            fragmentTransaction.replace(R.id.fragmentContainerView, new MineFragment()).commit();
        }
    }

    /**
     * 跳转到连接设置界面
     * @param view
     */
    public void toConnectSetting(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new ConnectSettingFragment()).addToBackStack(null).commit();
    }

    /**
     * 跳转到联动设置界面
     * @param view
     */
    public void toLinkageSetting(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new LinkageSettingFragment()).addToBackStack(null).commit();
    }

    /**
     * 返回“我的”界面
     * @param view
     */
    public void backToMind(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new MineFragment()).commit();
    }

    public void setConnect(View view) {
        // 保存设置
        setSettingData("tempHumSensorIP", dataViewModel.getTempHumSensorIP().getValue());
        setSettingData("tempHumSensorPort", dataViewModel.getTempHumSensorPort().getValue());
        setSettingData("PM25SensorIP", dataViewModel.getPM25SensorIP().getValue());
        setSettingData("PM25SensorPort", dataViewModel.getPM25SensorPort().getValue());
        setSettingData("bodySensorIP", dataViewModel.getBodySensorIP().getValue());
        setSettingData("bodySensorPort", dataViewModel.getBodySensorPort().getValue());
        setSettingData("fanIP", dataViewModel.getFanIP().getValue());
        setSettingData("fanPort", dataViewModel.getFanPort().getValue());
        setSettingData("buzzerIP", dataViewModel.getBuzzerIP().getValue());
        setSettingData("buzzerPort", dataViewModel.getBuzzerPort().getValue());
        setSettingData("collectionCycleTime", dataViewModel.getCollectionCycleTime().getValue());

        // 收起键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Toast.makeText(this, "设置保存成功", Toast.LENGTH_SHORT).show();
    }

    public void setLinkage(View view) {
        // 保存设置
        setSettingData("isLinkage", String.valueOf(dataViewModel.getIsLinkage().getValue()));
        setSettingData("temperatureThreshold", dataViewModel.getTemperatureThreshold().getValue());
        setSettingData("humidityThreshold", dataViewModel.getHumidityThreshold().getValue());
        setSettingData("pm25Threshold", dataViewModel.getPm25Threshold().getValue());
        setSettingData("isOpenAlert", String.valueOf(dataViewModel.getIsOpenAlert().getValue()));

        // 收起键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Toast.makeText(this, "设置保存成功", Toast.LENGTH_SHORT).show();
    }

}