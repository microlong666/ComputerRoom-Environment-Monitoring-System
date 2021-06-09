package com.example.bighomework2.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {

    private MutableLiveData<Double> temperature;
    private MutableLiveData<Double> humidity;
    private MutableLiveData<Boolean> tempHumIsConnect;
    private MutableLiveData<Boolean> pm25IsConnect;
    private MutableLiveData<Integer> pm25;
    private MutableLiveData<Boolean> fans;
    private MutableLiveData<Boolean> hasHuman;
    private MutableLiveData<Integer> health;

    public MutableLiveData<Integer> getHealth() {
        if (health == null) {
            health = new MutableLiveData<>();
            health.setValue(0);
        }
        return health;
    }

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

    public MutableLiveData<Boolean> getPm25IsConnect() {
        if (pm25IsConnect == null) {
            pm25IsConnect = new MutableLiveData<>();
            pm25IsConnect.setValue(false);
        }
        return pm25IsConnect;
    }

    public MutableLiveData<Integer> getPm25() {
        if (pm25 == null) {
            pm25 = new MutableLiveData<>();
            pm25.setValue(0);
        }
        return pm25;
    }


    public MutableLiveData<Boolean> getFans() {
        if (fans == null) {
            fans = new MutableLiveData<>();
            fans.setValue(false);
        }
        return fans;
    }

}
