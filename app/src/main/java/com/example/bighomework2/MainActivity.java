package com.example.bighomework2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.bighomework2.Connect.BodyConnect;
import com.example.bighomework2.Connect.FanConnect;
import com.example.bighomework2.Connect.Pm25Connect;
import com.example.bighomework2.Connect.TempHumConnect;
import com.example.bighomework2.databinding.ActivityMainBinding;
import com.example.bighomework2.fragment.DataFragment;
import com.example.bighomework2.fragment.MineFragment;
import com.example.bighomework2.util.Const;
import com.example.bighomework2.viewModel.DataViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        dataViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DataViewModel.class);
        dataViewModel.initData();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bodyConnect = new BodyConnect(this, dataViewModel);
        bodyConnect.start();
        tempHumConnect = new TempHumConnect(this, dataViewModel);
        tempHumConnect.start();
        pm25Connect = new Pm25Connect(this, dataViewModel);
        pm25Connect.start();
        fanConnect = new FanConnect(this, dataViewModel);
        fanConnect.start();
        startTimer();

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

    public void switchFans(View view){
        Boolean isConnect= dataViewModel.getFansIsConnect().getValue();
        Boolean isOpen = dataViewModel.getFans().getValue();
        Const.linkage = false;
        if (isConnect) {
            if (isOpen) {
                fanConnect.fanOff();
                dataViewModel.getFans().postValue(false);
            } else {
                fanConnect.fanOn();
                dataViewModel.getFans().postValue(true);
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
                Boolean fanIsConnect = dataViewModel.getFansIsConnect().getValue();
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

}