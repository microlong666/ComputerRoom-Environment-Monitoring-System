package com.example.chainplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chainplus.connect.BodyConnect;
import com.example.chainplus.connect.FanConnect;
import com.example.chainplus.connect.Pm25Connect;
import com.example.chainplus.connect.TempHumConnect;
import com.example.chainplus.fragment.DataFragment;
import com.example.chainplus.fragment.MineFragment;
import com.example.chainplus.util.Const;
import com.example.chainplus.util.IndexUtil;
import com.example.chainplus.util.Page;
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
    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private DataViewModel dataViewModel;
    private TempHumConnect tempHumConnect;
    private BodyConnect bodyConnect;
    private FanConnect fanConnect;
    private Pm25Connect pm25Connect;
    private AlertDialog.Builder dialogBuilder;
    private long exitTime = 0;
    private boolean fanManualOpen = false;

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
        DataBindingUtil.setContentView(this, R.layout.activity_main);

        // 创建对话框构造器
        dialogBuilder = new AlertDialog.Builder(this);

        // 启动时默认创建连接
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (dataViewModel.getPosition().getValue() == Page.LINK_SETTING || dataViewModel.getPosition().getValue() == Page.CONNECT_SETTING) {
            dataViewModel.getPosition().setValue(Page.MINE);
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再次返回退出链+", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public void switchFans(View view) {
        Boolean isConnect = dataViewModel.getFanIsConnect().getValue();
        Boolean isOpen = dataViewModel.getFanIsOpen().getValue();
        if (isConnect) {
            if (Boolean.parseBoolean(dataViewModel.getIsLinkage().getValue())) {
                dataViewModel.getIsLinkage().postValue("false");
                Toast.makeText(this, "联动将自动关闭", Toast.LENGTH_LONG).show();
            }
            if (isOpen) {
                // 人工开启状态
                fanManualOpen = false;
                fanConnect.fanOff();
            } else {
                fanManualOpen = true;
                fanConnect.fanOn();
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
            dataViewModel.getTempHumIsConnect().postValue(false);
            TextView textView = findViewById(R.id.textView);
            textView.setTextColor(Color.rgb(51, 51, 51));
            textView = findViewById(R.id.textView6);
            textView.setTextColor(Color.rgb(51, 51, 51));
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
            TextView textView = findViewById(R.id.textView12);
            textView.setTextColor(Color.rgb(51, 51, 51));
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

                // 温度指数更新
                if (!dataViewModel.getTempHumIsConnect().getValue() || !dataViewModel.getPm25IsConnect().getValue()) {
                    dataViewModel.getHealth().postValue(0);
                } else {
                    dataViewModel.getHealth().postValue((int) IndexUtil.getHealth(dataViewModel.getTemperature().getValue(),
                            dataViewModel.getHumidity().getValue(), dataViewModel.getPm25().getValue(),
                            Double.parseDouble("".equals(dataViewModel.getTemperatureThreshold().getValue()) ? "0" : dataViewModel.getTemperatureThreshold().getValue()),
                            Double.parseDouble("".equals(dataViewModel.getPm25Threshold().getValue()) ? "0" : dataViewModel.getPm25Threshold().getValue())));
                }

                // 风量更新
                dataViewModel.getWind().postValue(IndexUtil.getWind(dataViewModel.getTemperature().getValue()));

                double temperatureThreshold = Double.parseDouble("".equals(dataViewModel.getTemperatureThreshold().getValue()) ? "1000" : dataViewModel.getTemperatureThreshold().getValue());
                double humidityThreshold = Double.parseDouble("".equals(dataViewModel.getHumidityThreshold().getValue()) ? "1000" : dataViewModel.getHumidityThreshold().getValue());

                // 人工介入且掉出阈值时自动关闭联动设备
                if (!fanManualOpen && dataViewModel.getFanIsOpen().getValue() && dataViewModel.getTemperature().getValue() < temperatureThreshold && dataViewModel.getHumidity().getValue() < humidityThreshold) {
                    fanConnect.fanOff();
                }
            }
        }, 1000, 1000);
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
        for (String s : setting) {
            if (!"".equals(getSettingData(s))) {
                try {
                    Field f = DataViewModel.class.getDeclaredField(s);
                    f.setAccessible(true);
                    MutableLiveData<String> data = new MutableLiveData<>(getSettingData(s));
                    f.set(dataViewModel, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 切换fragment
     */
    private void switchToFragment(String fragment) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack();
        if ("index".equals(fragment) && dataViewModel.getPosition().getValue() != Page.INDEX) {
            dataViewModel.getPosition().setValue(Page.INDEX);
            fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_right).replace(R.id.fragmentContainerView, new DataFragment()).commit();
        } else if ("mine".equals(fragment) && dataViewModel.getPosition().getValue() == Page.INDEX) {
            dataViewModel.getPosition().setValue(Page.MINE);
            fragmentTransaction.setCustomAnimations(R.anim.from_right, R.anim.out_left).replace(R.id.fragmentContainerView, new MineFragment()).commit();
        } else if ("index".equals(fragment)) {

        }
    }

    /**
     * 返回“我的”界面
     */
    public void backToMind(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.popBackStack();
        dataViewModel.getPosition().setValue(Page.MINE);
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
        Switch switch1 = findViewById(R.id.switch1);
        Switch switch2 = findViewById(R.id.switch2);
        setSettingData("isLinkage", String.valueOf(switch1.isChecked()));
        setSettingData("temperatureThreshold", dataViewModel.getTemperatureThreshold().getValue());
        setSettingData("humidityThreshold", dataViewModel.getHumidityThreshold().getValue());
        setSettingData("pm25Threshold", dataViewModel.getPm25Threshold().getValue());
        setSettingData("isOpenAlert", String.valueOf(switch2.isChecked()));

        dataViewModel.getIsLinkage().setValue(String.valueOf(switch1.isChecked()));
        dataViewModel.getIsOpenAlert().setValue(String.valueOf(switch2.isChecked()));

        // 收起键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Toast.makeText(this, "设置保存成功", Toast.LENGTH_SHORT).show();
    }

}