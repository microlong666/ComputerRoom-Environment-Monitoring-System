package com.example.bighomework2.util;

import android.util.Log;

public class StringUtil {

    public static String getHasHumanString(Boolean data){
        if (data) {
            return "有人进入计算间";
        } else {
            return "人已离开计算间";
        }
    }

    public static String getFansString(Boolean data) {
        if (data) {
            return "制冷已开启";
        } else {
            return "制冷已关闭";
        }
    }

    public static String getTempHunIsConnect(Boolean data) {
        if (data) {
            return "温湿度传感器已连接";
        } else {
            return "温湿度传感器未连接";
        }
    }
}
