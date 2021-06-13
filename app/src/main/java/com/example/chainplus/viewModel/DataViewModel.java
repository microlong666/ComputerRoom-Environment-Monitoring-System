package com.example.chainplus.viewModel;

import com.example.chainplus.util.Const;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * MainActivity存储数据的对象
 * 初始化后请立刻执行initData方法，再执行其他操作，预防空指针错误
 */
public class DataViewModel extends ViewModel {

    // 传感器的连接状态、读取到的数据
    private MutableLiveData<Double> temperature;
    private MutableLiveData<Double> humidity;
    private MutableLiveData<Boolean> bodyIsConnect;
    private MutableLiveData<Boolean> tempHumIsConnect;
    private MutableLiveData<Boolean> pm25IsConnect;
    private MutableLiveData<Integer> pm25;
    private MutableLiveData<Boolean> fanIsOpen;
    private MutableLiveData<Boolean> fanIsConnect;
    private MutableLiveData<Double> wind;
    private MutableLiveData<Boolean> hasHuman;
    private MutableLiveData<Integer> health;
    private MutableLiveData<Boolean> connectVisibility;

    // 传感器的连接设置
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

    /**
     * 初始化数据
     */
    public void initData() {

        temperature = new MutableLiveData<>(0.0);
        humidity = new MutableLiveData<>(0.0);
        pm25 = new MutableLiveData<>(0);
        fanIsOpen = new MutableLiveData<>(false);
        hasHuman = new MutableLiveData<>(false);
        bodyIsConnect = new MutableLiveData<>(false);
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

    public MutableLiveData<Double> getTemperature() {
        return temperature;
    }

    public void setTemperature(MutableLiveData<Double> temperature) {
        this.temperature = temperature;
    }

    public MutableLiveData<Double> getHumidity() {
        return humidity;
    }

    public void setHumidity(MutableLiveData<Double> humidity) {
        this.humidity = humidity;
    }

    public MutableLiveData<Boolean> getBodyIsConnect() {
        return bodyIsConnect;
    }

    public void setBodyIsConnect(MutableLiveData<Boolean> bodyIsConnect) {
        this.bodyIsConnect = bodyIsConnect;
    }

    public MutableLiveData<Boolean> getTempHumIsConnect() {
        return tempHumIsConnect;
    }

    public void setTempHumIsConnect(MutableLiveData<Boolean> tempHumIsConnect) {
        this.tempHumIsConnect = tempHumIsConnect;
    }

    public MutableLiveData<Boolean> getPm25IsConnect() {
        return pm25IsConnect;
    }

    public void setPm25IsConnect(MutableLiveData<Boolean> pm25IsConnect) {
        this.pm25IsConnect = pm25IsConnect;
    }

    public MutableLiveData<Integer> getPm25() {
        return pm25;
    }

    public void setPm25(MutableLiveData<Integer> pm25) {
        this.pm25 = pm25;
    }

    public MutableLiveData<Boolean> getFanIsOpen() {
        return fanIsOpen;
    }

    public void setFanIsOpen(MutableLiveData<Boolean> fanIsOpen) {
        this.fanIsOpen = fanIsOpen;
    }

    public MutableLiveData<Boolean> getFanIsConnect() {
        return fanIsConnect;
    }

    public void setFanIsConnect(MutableLiveData<Boolean> fanIsConnect) {
        this.fanIsConnect = fanIsConnect;
    }

    public MutableLiveData<Double> getWind() {
        return wind;
    }

    public void setWind(MutableLiveData<Double> wind) {
        this.wind = wind;
    }

    public MutableLiveData<Boolean> getHasHuman() {
        return hasHuman;
    }

    public void setHasHuman(MutableLiveData<Boolean> hasHuman) {
        this.hasHuman = hasHuman;
    }

    public MutableLiveData<Integer> getHealth() {
        return health;
    }

    public void setHealth(MutableLiveData<Integer> health) {
        this.health = health;
    }

    public MutableLiveData<Boolean> getConnectVisibility() {
        return connectVisibility;
    }

    public void setConnectVisibility(MutableLiveData<Boolean> connectVisibility) {
        this.connectVisibility = connectVisibility;
    }

    public MutableLiveData<String> getTempHumSensorIP() {
        return tempHumSensorIP;
    }

    public void setTempHumSensorIP(MutableLiveData<String> tempHumSensorIP) {
        this.tempHumSensorIP = tempHumSensorIP;
    }

    public MutableLiveData<String> getTempHumSensorPort() {
        return tempHumSensorPort;
    }

    public void setTempHumSensorPort(MutableLiveData<String> tempHumSensorPort) {
        this.tempHumSensorPort = tempHumSensorPort;
    }

    public MutableLiveData<String> getBodySensorIP() {
        return bodySensorIP;
    }

    public void setBodySensorIP(MutableLiveData<String> bodySensorIP) {
        this.bodySensorIP = bodySensorIP;
    }

    public MutableLiveData<String> getBodySensorPort() {
        return bodySensorPort;
    }

    public void setBodySensorPort(MutableLiveData<String> bodySensorPort) {
        this.bodySensorPort = bodySensorPort;
    }

    public MutableLiveData<String> getPM25SensorIP() {
        return PM25SensorIP;
    }

    public void setPM25SensorIP(MutableLiveData<String> PM25SensorIP) {
        this.PM25SensorIP = PM25SensorIP;
    }

    public MutableLiveData<String> getPM25SensorPort() {
        return PM25SensorPort;
    }

    public void setPM25SensorPort(MutableLiveData<String> PM25SensorPort) {
        this.PM25SensorPort = PM25SensorPort;
    }

    public MutableLiveData<String> getFanIP() {
        return fanIP;
    }

    public void setFanIP(MutableLiveData<String> fanIP) {
        this.fanIP = fanIP;
    }

    public MutableLiveData<String> getFanPort() {
        return fanPort;
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
