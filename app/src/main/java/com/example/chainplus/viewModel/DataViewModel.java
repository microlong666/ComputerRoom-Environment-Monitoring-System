package com.example.chainplus.viewModel;

import com.example.chainplus.util.Const;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {

    private MutableLiveData<Double> temperature;
    private MutableLiveData<Double> humidity;
    private MutableLiveData<Boolean> tempHumIsConnect;
    private MutableLiveData<Boolean> pm25IsConnect;
    private MutableLiveData<Integer> pm25;
    private MutableLiveData<Boolean> fanIsOpen;
    private MutableLiveData<Boolean> fanIsConnect;
    private MutableLiveData<Double> wind;
    private MutableLiveData<Boolean> hasHuman;
    private MutableLiveData<Integer> health;
    private MutableLiveData<Boolean> connectVisibility;


    private MutableLiveData<String> tempHumSensorIp;
    private MutableLiveData<String> tempHumSensorPort;
    private MutableLiveData<String> bodySensorIp;
    private MutableLiveData<String> bodySensorPort;
    private MutableLiveData<String> PM25SensorIp;
    private MutableLiveData<String> PM25SensorPort;
    private MutableLiveData<String> fanIp;
    private MutableLiveData<String> fanPort;


    public void initData() {
        temperature = new MutableLiveData<>(0.0);
        humidity = new MutableLiveData<>(0.0);
        pm25 = new MutableLiveData<>(0);
        fanIsOpen = new MutableLiveData<>(false);
        hasHuman = new MutableLiveData<>(false);
        tempHumIsConnect = new MutableLiveData<>(false);
        pm25IsConnect = new MutableLiveData<>(false);
        fanIsConnect = new MutableLiveData<>(false);
        wind = new MutableLiveData<>(0.0);
        health = new MutableLiveData<>(0);
        connectVisibility = new MutableLiveData<>(true);
        tempHumSensorIp = new MutableLiveData<>(Const.TEMHUM_IP);
        tempHumSensorPort = new MutableLiveData<String>(String.valueOf(Const.TEMHUM_PORT));
        bodySensorIp = new MutableLiveData<>(Const.BODY_IP);
        bodySensorPort = new MutableLiveData<String>(String.valueOf(Const.BODY_port));
        PM25SensorIp = new MutableLiveData<>(Const.PM25_IP);
        PM25SensorPort = new MutableLiveData<String>(String.valueOf(Const.PM25_port));
        fanIp = new MutableLiveData<>(Const.FAN_IP);
        fanPort = new MutableLiveData<String>(String.valueOf(Const.FAN_PORT));
    }

    public MutableLiveData<Boolean> getConnectVisibility() {
        if (connectVisibility == null) {
            connectVisibility = new MutableLiveData<>();
            connectVisibility.setValue(true);
        }
        return connectVisibility;
    }

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
            tempHumIsConnect.postValue(false);
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
            pm25IsConnect.postValue(false);
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


    public MutableLiveData<Boolean> getFanIsOpen() {
        if (fanIsOpen == null) {
            fanIsOpen = new MutableLiveData<>();
            fanIsOpen.setValue(false);
        }
        return fanIsOpen;
    }

    public MutableLiveData<Boolean> getFanIsConnect() {
        if (fanIsConnect == null) {
            fanIsConnect = new MutableLiveData<>();
            fanIsConnect.setValue(false);
        }
        return fanIsConnect;
    }

    public MutableLiveData<Double> getWind() {
        return wind;
    }

    public MutableLiveData<String> getTempHumSensorIp() {
        return tempHumSensorIp;
    }

    public MutableLiveData<String> getTempHumSensorPort() {
        return tempHumSensorPort;
    }

    public MutableLiveData<String> getBodySensorIp() {
        return bodySensorIp;
    }

    public MutableLiveData<String> getBodySensorPort() {
        return bodySensorPort;
    }

    public MutableLiveData<String> getPM25SensorIp() {
        return PM25SensorIp;
    }

    public MutableLiveData<String> getPM25SensorPort() {
        return PM25SensorPort;
    }

    public MutableLiveData<String> getFanIp() {
        return fanIp;
    }

    public MutableLiveData<String> getFanPort() {
        return fanPort;
    }

    public void setTempHumSensorIp(MutableLiveData<String> tempHumSensorIp) {
        this.tempHumSensorIp = tempHumSensorIp;
    }

    public void setTempHumSensorPort(MutableLiveData<String> tempHumSensorPort) {
        this.tempHumSensorPort = tempHumSensorPort;
    }

    public void setBodySensorIp(MutableLiveData<String> bodySensorIp) {
        this.bodySensorIp = bodySensorIp;
    }

    public void setBodySensorPort(MutableLiveData<String> bodySensorPort) {
        this.bodySensorPort = bodySensorPort;
    }

    public void setPM25SensorIp(MutableLiveData<String> PM25SensorIp) {
        this.PM25SensorIp = PM25SensorIp;
    }

    public void setPM25SensorPort(MutableLiveData<String> PM25SensorPort) {
        this.PM25SensorPort = PM25SensorPort;
    }

    public void setFanIp(MutableLiveData<String> fanIp) {
        this.fanIp = fanIp;
    }

    public void setFanPort(MutableLiveData<String> fanPort) {
        this.fanPort = fanPort;
    }
}
