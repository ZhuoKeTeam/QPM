package com.jm.android.gt.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DecimalFormatUtil {

    private static final String SCALE_ZERO = "0";
    private static final String SCALE_ONE = "0.#";
    private static final String SCALE_TWO = "0.##";
    private static final String SCALE_THREE = "0.###";

    public static String setScale(double d, int scale) {
        if (scale < 0) {
            return String.valueOf(d);
        }
        String scaleStr = null;
        if (scale == 0) {
            scaleStr = SCALE_ZERO;
        } else if (scale == 1) {
            scaleStr = SCALE_ONE;
        } else if (scale == 2) {
            scaleStr = SCALE_TWO;
        } else if (scale == 3) {
            scaleStr = SCALE_THREE;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("0.");
            for (int i = 0; i < scale; i++) {
                sb.append("#");
            }
            scaleStr = sb.toString();
        }

        DecimalFormat df = new DecimalFormat(scaleStr);
        return df.format(d);
    }

    public static String divider(long one, long two, int scale) {
        if (one == -1) {
            return "-1";
        }
        return new BigDecimal(one).divide(new BigDecimal(two), scale, RoundingMode.HALF_UP).toPlainString();
    }
}
