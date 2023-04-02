package com.molcom.nms.general.utils;

import java.util.Random;
import java.util.UUID;


public class GetUniqueId {

    public static String getId() {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        return uuidAsString;
    }

    public static String generateRef(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // first not 0 digit
        sb.append(random.nextInt(9) + 1);

        // rest of 11 digits
        for (int i = 0; i < length - 1; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

}
