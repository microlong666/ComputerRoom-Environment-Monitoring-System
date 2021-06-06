package com.example.bighomework2.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bighomework2.Connect.TempHumConnect;

public class DataViewModel extends ViewModel {

    private MutableLiveData<Double> temperature;
    private MutableLiveData<Double> humidity;
    private MutableLiveData<Boolean> tempHumIsConnect;
    private MutableLiveData<Integer> PM;
    private MutableLiveData<Boolean> fans;
    private MutableLiveData<Boolean> hasHuman;

    public MutableLiveData<Boolean> getTempHumIsConnect() {
        if (tempHumIsConnect == null) {
            tempHumIsConnect = new MutableLiveData<>();
            tempHumIsConnect.setValue(false);
        }
        return tempHumIsConnect;
    }

    public MutableLiveData<Boolean> getHasHuman() {
        if (hasHuman == null) {
            hasHuman = new MutableLiveData<>();
            hasHuman.setValue(false);
        }
        return hasHuman;
    }

    public MutableLiveData<Double> getTemperature() {
        if (temperature == null) {
            temperature = new MutableLiveData<>();
            temperature.setValue(0.0);
        }
        return temperature;
    }

    public MutableLiveData<Double> getHumidity() {
        if (humidity == null) {
            humidity = new MutableLiveData<>();
            humidity.setValue(0.0);
        }
        return humidity;
    }


    public MutableLiveData<Integer> getPM() {
        if (PM == null) {
            PM = new MutableLiveData<>();
            PM.setValue(0);
        }
        return PM;
    }


    public MutableLiveData<Boolean> getFans() {
        if (fans == null) {
            fans = new MutableLiveData<>();
            fans.setValue(false);
        }
        return fans;
    }

}
