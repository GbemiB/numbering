package com.molcom.nms.general.utils;

import java.util.Random;

public class RefGenerator {

    public static String getRefNo(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        sb.append(random.nextInt(9) + 1);

        for (int i = 0; i < length - 1; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

}
