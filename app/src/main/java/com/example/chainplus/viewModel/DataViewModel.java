package com.example.chainplus.viewModel;

import com.example.chainplus.util.Const;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * MainActivity存储数据的对象
 * 初始化后请立刻执行initData方法，再执行其他操作，预防空指针错误
 */
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


    private MutableLiveData<String> tempHumSensorIP;
    private MutableLiveData<String> tempHumSensorPort;
    private MutableLiveData<String> bodySensorIP;
    private MutableLiveData<String> bodySensorPort;
    private MutableLiveData<String> PM25SensorIP;
    private MutableLiveData<String> PM25SensorPort;
    private MutableLiveData<String> fanIP;
    private MutableLiveData<String> fanPort;
    private MutableLiveData<String> buzzerIP;
    private MutableLiveData<String> buzzerPort;

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
        tempHumSensorIP = new MutableLiveData<>(Const.TEMHUM_IP);
        tempHumSensorPort = new MutableLiveData<>(String.valueOf(Const.TEMHUM_PORT));
        bodySensorIP = new MutableLiveData<>(Const.BODY_IP);
        bodySensorPort = new MutableLiveData<>(String.valueOf(Const.BODY_port));
        PM25SensorIP = new MutableLiveData<>(Const.PM25_IP);
        PM25SensorPort = new MutableLiveData<>(String.valueOf(Const.PM25_port));
        fanIP = new MutableLiveData<>(Const.FAN_IP);
        fanPort = new MutableLiveData<>(String.valueOf(Const.FAN_PORT));
        buzzerIP = new MutableLiveData<>(Const.RGB_BUZZER_IP);
        buzzerPort = new MutableLiveData<>(String.valueOf(Const.RGB_BUZZER_PORT));
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

    public MutableLiveData<String> getTempHumSensorIP() {
        return tempHumSensorIP;
    }

    public MutableLiveData<String> getTempHumSensorPort() {
        return tempHumSensorPort;
    }

    public MutableLiveData<String> getBodySensorIP() {
        return bodySensorIP;
    }

    public MutableLiveData<String> getBodySensorPort() {
        return bodySensorPort;
    }

    public MutableLiveData<String> getPM25SensorIP() {
        return PM25SensorIP;
    }

    public MutableLiveData<String> getPM25SensorPort() {
        return PM25SensorPort;
    }

    public MutableLiveData<String> getFanIP() {
        return fanIP;
    }

    public MutableLiveData<String> getFanPort() {
        return fanPort;
    }

    public void setTempHumSensorIP(MutableLiveData<String> tempHumSensorIP) {
        this.tempHumSensorIP = tempHumSensorIP;
    }

    public void setTempHumSensorPort(MutableLiveData<String> tempHumSensorPort) {
        this.tempHumSensorPort = tempHumSensorPort;
    }

    public void setBodySensorIP(MutableLiveData<String> bodySensorIP) {
        this.bodySensorIP = bodySensorIP;
    }

    public void setBodySensorPort(MutableLiveData<String> bodySensorPort) {
        this.bodySensorPort = bodySensorPort;
    }

    public void setPM25SensorIP(MutableLiveData<String> PM25SensorIP) {
        this.PM25SensorIP = PM25SensorIP;
    }

    public void setPM25SensorPort(MutableLiveData<String> PM25SensorPort) {
        this.PM25SensorPort = PM25SensorPort;
    }

    public void setFanIP(MutableLiveData<String> fanIP) {
        this.fanIP = fanIP;
    }

    public void setFanPort(MutableLiveData<String> fanPort) {
        this.fanPort = fanPort;
    }

    public MutableLiveData<String> getBuzzerIP() {
        return buzzerIP;
    }

    public void setBuzzerIP(MutableLiveData<String> buzzerIP) {
        this.buzzerIP = buzzerIP;
    }

    public MutableLiveData<String> getBuzzerPort() {
        return buzzerPort;
    }

    public void setBuzzerPort(MutableLiveData<String> buzzerPort) {
        this.buzzerPort = buzzerPort;
    }
}
