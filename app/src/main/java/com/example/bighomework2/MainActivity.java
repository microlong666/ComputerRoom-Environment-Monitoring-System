package com.example.bighomework2;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bighomework2.Connect.BodyConnect;
import com.example.bighomework2.Connect.FanConnect;
import com.example.bighomework2.Connect.Pm25Connect;
import com.example.bighomework2.Connect.TempHumConnect;
import com.example.bighomework2.databinding.ActivityMainBinding;
import com.example.bighomework2.viewModel.DataViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private DataViewModel data;
    private TempHumConnect tempHumConnect;
    private BodyConnect bodyConnect;
    private FanConnect fanConnect;
    private Pm25Connect pm25Connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        data = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DataViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bodyConnect = new BodyConnect(this, data);
        bodyConnect.start();
        tempHumConnect = new TempHumConnect(this, data);
        tempHumConnect.start();
        pm25Connect = new Pm25Connect(this, data);
        pm25Connect.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void switchFans(View view){
        Boolean isOpen = data.getFans().getValue();
        if (isOpen != null) {
            if (isOpen) {
                data.getFans().setValue(false);
                fanConnect.exit = true;
            } else {
                data.getFans().setValue(true);
                fanConnect = new FanConnect(this, data);
                fanConnect.start();
            }
        }
    }

    public void switchTempHumConnect(View view) {
        Boolean isConnect = data.getTempHumIsConnect().getValue();
        if (isConnect != null) {
            if (isConnect) {
                tempHumConnect.exit = true;
                data.getTempHumIsConnect().setValue(false);
            } else {
                tempHumConnect = new TempHumConnect(this, data);
                tempHumConnect.start();
            }
        }
    }

    public void switchPm25Connect(View view) {
        Boolean isConnect = data.getPm25IsConnect().getValue();
        if (isConnect != null) {
            if (isConnect) {
                pm25Connect.exit = true;
                data.getPm25IsConnect().postValue(false);
            } else {
                pm25Connect = new Pm25Connect(this, data);
                pm25Connect.start();
            }
        }
    }

    private void setLiveData() {
        Log.d("abc", "setLiveData: 设置温度连接图标");
//        AppCompatImageView imageView = findViewById(R.id.appCompatImageView2);
//        imageView.setImageResource(R.drawable.connect_dot_shape);
        data.getTempHumIsConnect().observe(this, isConnect -> {
//            GradientDrawable shape = (GradientDrawable) findViewById(R.id.appCompatImageView2).getBackground();
//            GradientDrawable shape2 = (GradientDrawable) findViewById(R.id.appCompatImageView4).getBackground();
//            if (isConnect) {
//                shape.setColor(getResources().getColor(R.color.colorAccent));
//                shape2.setColor(getResources().getColor(R.color.colorAccent));
//            } else {
//                shape.setColor(getResources().getColor(R.color.unConnect));
//                shape2.setColor(getResources().getColor(R.color.unConnect));
//            }
        });

        Log.d("abc", "setLiveData: 设置风扇连接图标");
        data.getFansIsConnect().observe(this, isConnect -> {
//            GradientDrawable shape = (GradientDrawable) findViewById(R.id.appCompatImageView5).getBackground();
//            if (isConnect) {
//                shape.setColor(getResources().getColor(R.color.colorAccent));
//            } else {
//                shape.setColor(getResources().getColor(R.color.unConnect));
//            }
        });

        Log.d("abc", "setLiveData: 设置PM2.5连接图标");
        data.getPm25IsConnect().observe(this, isConnect -> {
//            GradientDrawable shape = (GradientDrawable) findViewById(R.id.appCompatImageView7).getBackground();
//            if (isConnect) {
//                shape.setColor(getResources().getColor(R.color.colorAccent));
//            } else {
//                shape.setColor(getResources().getColor(R.color.unConnect));
//            }
        });
    }

}