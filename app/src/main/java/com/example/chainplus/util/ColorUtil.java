package com.example.chainplus.util;

import android.graphics.Color;

public class ColorUtil {

    // 根据健康获取颜色
    public static Integer getColorFromHealth(Integer health) {
        if (health > 70) {
            return Color.rgb(52, 199, 89);
        } else if (health > 50) {
            return Color.rgb(255, 149, 0);
        } else if (health > 0) {
            return Color.rgb(255, 59, 48);
        } else {
            return Color.rgb(170, 170, 170);
        }
    }

    public static Integer setTextColor(double value, double threshold) {
        if (value >= threshold) {
            return Color.rgb(255,59,48);
        } else {
            return Color.rgb(51, 51, 51);
        }
    }

    public static Integer setTextColor(int value, int threshold) {
        if (value >= threshold) {
            return Color.rgb(255,59,48);
        } else {
            return Color.rgb(51, 51, 51);
        }
    }
}
