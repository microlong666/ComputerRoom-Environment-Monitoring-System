package com.example.bighomework2.util;

import com.example.bighomework2.R;

public class ShapeUtil {
    public static int getConnectShape(Boolean isConnect) {
        if (isConnect) {
            return R.drawable.connect_dot_shape;
        } else {
            return R.drawable.unconnect_dot_shape;
        }
    }
}
