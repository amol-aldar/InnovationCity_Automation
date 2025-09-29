package org.rakdao.utils;

import java.util.Random;

public class PhoneUtil {

    public static String generateRandomPhone(String prefix) {
        Random random = new Random();
        StringBuilder phone = new StringBuilder(prefix);

        // Generate 7 random digits after prefix
        for (int i = 0; i < 7; i++) {
            phone.append(random.nextInt(10)); // digits 0-9
        }

        return phone.toString();
    }
}
