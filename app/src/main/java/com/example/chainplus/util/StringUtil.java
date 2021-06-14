package com.example.chainplus.util;

import android.view.View;

public class StringUtil {

    public static String getTempString(Double data, Boolean isConnect) {
        if (isConnect) {
            return data + "°C";
        } else {
            return "--.-°C";
        }
    }

    public static String getHumidityString(Double data, Boolean isConnect) {
        if (isConnect) {
            return data + "%";
        } else {
            return "--.-%";
        }
    }

    public static String getPM25String(int data, Boolean isConnect) {
        if (isConnect) {
            return data + "μg/m³";
        } else {
            return "--μg/m³";
        }
    }

    public static String getWindString(double data, Boolean isConnect, Boolean isOpen) {
        if (isConnect && isOpen) {
            return ((int) (data * 100)) / 100 + "m³/s";
        } else {
            return "--m³/s";
        }
    }


    public static String getHasHumanString(Boolean data, Boolean isConnect) {
        if (isConnect) {
            if (data) {
                return "有人进入计算间";
            } else {
                return "人已离开计算间";
            }
        } else {
            return "人体传感器未连接";
        }
    }

    public static String getTempHunIsConnect(Boolean data) {
        if (data) {
            return "温湿度传感器已连接";
        } else {
            return "温湿度传感器未连接";
        }
    }

    public static String getPm25IsConnect(Boolean data) {
        if (data) {
            return "PM2.5传感器已连接";
        } else {
            return "PM2.5传感器未连接";
        }
    }

    public static String getColorOfHealth(int data) {
        return "";
    }

    public static String getStringOfConnect(Boolean data) {
        if (data) {
            return "已连接";
        } else {
            return "未连接";
        }
    }

    public static String getShapePfConnect(Boolean data) {
        if (data) {
            return "@drawable/unconnect_dot_shape";
        } else {
            return "@drawable/connect_dot_shape";
        }
    }

    public static int getConnectVisible(Boolean data) {
        if (data) {
            return View.VISIBLE;
        } else {
            return View.INVISIBLE;
        }
    }

    public static boolean getChecked(String data) {
        return "true".equals(data);
    }

    public static String getTipFromHealth(Integer health) {
        if (health > 70) {
            return "环境不错，继续保持哦~";
        } else if (health > 50) {
            return "环境有些问题，请多加注意~";
        } else if (health > 0) {
            return "环境不太健康，还请检查哦！";
        } else {
            return "未连接";
        }
    }
}
