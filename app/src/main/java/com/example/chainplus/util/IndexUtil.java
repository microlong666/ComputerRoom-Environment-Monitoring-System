package com.example.chainplus.util;

public class IndexUtil {

    /**
     * 映射计算函数
     *
     * @param inMin  输入最小值
     * @param inMax  输入最大值
     * @param outMin 输出最小值（映射后）
     * @param out_Max 输出最大值（映射后）
     * @param inVal  输入值
     */
    private static double map(double inMin, double inMax, double outMin, double out_Max, double inVal) {
        if (inVal > inMax) {
            return inMax;
        } else if (inVal < inMin) {
            return inMin;
        } else {
            return outMin + (inVal - inMin) * (out_Max - outMin) / (inMax - inMin);
        }
    }

    /**
     * 温度指数
     */
    private static double tempIndex(double temp, double tempThreshold) {
        double MIN = 20.0;
        double MAX = tempThreshold * 2;
        return map(MIN, MAX, 0, 40, temp);
    }

    /**
     * 湿度指数
     */
    private static double humIndex(double hum) {
        double MIN = 40.0;
        double MAX = 100;
        return map(MIN, MAX, 0, 30, hum);
    }

    /**
     * PM2.5指数
     */
    private static double pm25Index(double pm25, double pm25Threshold) {
        double MIN = 0;
        return map(MIN, pm25Threshold, 0, 30, pm25);
    }

    public static double getHealth(double temp, double hum, double pm25, double tempThreshold, double pm25Threshold) {
        return 100 - (tempIndex(temp, tempThreshold) + humIndex(hum) + pm25Index(pm25, pm25Threshold));
    }

    public static double getWind(double temp) {
        return map(0, 40, 0, 10, temp);
    }
}
