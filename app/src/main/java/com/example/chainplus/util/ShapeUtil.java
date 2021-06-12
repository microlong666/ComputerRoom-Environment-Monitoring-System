package com.example.chainplus.util;

import com.example.chainplus.R;

public class ShapeUtil {
    public static int getConnectShape(Boolean isConnect) {
        if (isConnect) {
            return R.drawable.connect_dot_shape;
        } else {
            return R.drawable.unconnect_dot_shape;
        }
    }
}
