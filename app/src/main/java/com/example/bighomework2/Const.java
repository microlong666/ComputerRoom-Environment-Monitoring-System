package com.example.bighomework2;

public class Const {

    public static String TAG = "CASE";

    // RGB灯
    public static final String RED_CMD = "01 10 00 5a 00 02 04 00 01 00 00 27 2c";
    public static final String GREEN_CMD = "01 10 00 5a 00 02 04 00 00 01 00 77 7c";
    public static final String BLUE_CMD = "01 10 00 5a 00 02 04 00 00 00 01 b7 2c";

    // 联动设置
    public static Double maxTem = 33.6d;
    public static Double maxHum = 60d;
    public static Boolean linkage = true;

    // 温湿度
    public static String TEMHUM_CHK = "01 03 00 14 00 02 84 0f";
    public static int TEMHUM_NUM = 1;
    public static int TEMHUM_LEN = 9;

    // 蜂鸣器
    public static String BUZZER_ON = "01 10 00 5a 00 02 04 01 00 00 00 77 10";
    public static String BUZZER_OFF = "01 10 00 5a 00 02 04 00 00 00 00 76 ec";
    public static boolean isBuzzerOn = false;

    // 风扇
    public static String FAN_ON = "01 10 00 48 00 01 02 00 01 68 18";
    public static String FAN_OFF = "01 10 00 48 00 01 02 00 02 28 19";

    // PM2.5
    public static String PM25_CHK = "01 03 00 58 00 01 05 d9";
    public static int PM25_LEN = 7;
    public static int PM25_NUM = 1;

    // 人体
    public static String BODY_CHK = "01 03 00 3c 00 01 44 06";
    public static int BODY_LEN = 7;
    public static int BODY_NUM = 1;


    // IP端口
    public static String TEMHUM_IP = "192.168.0.100";
    public static int TEMHUM_PORT = 4001;
    public static String RGB_BUZZER_IP = "192.168.0.107";
    public static int RGB_BUZZER_PORT = 4001;
    public static String FAN_IP = "192.168.0.105";
    public static int FAN_PORT = 4001;
    public static String BODY_IP = "192.168.0.104";
    public static int BODY_port = 4001;
    public static String PM25_IP = "192.168.0.101";
    public static int PM25_port = 4001;

    // 配置
    public static Integer time = 500;

}
