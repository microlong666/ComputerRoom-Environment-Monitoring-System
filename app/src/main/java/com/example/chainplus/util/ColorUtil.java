package com.example.chainplus.util;

import android.graphics.Color;

import com.example.chainplus.R;

public class ColorUtil {

    // 根据健康获取颜色
    public static Integer getColorFromHealth(Integer health) {
        if (health > 50) {
            return Color.rgb(52, 199, 89);
        } else if (health > 20) {
            return Color.rgb(255, 149, 0);
        } else if (health > 0) {
            return Color.rgb(255, 59, 48);
        } else {
            return Color.rgb(170, 170, 170);
        }
    }

}
