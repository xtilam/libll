package com.dinz.library.common;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    
    public static Long getRandomId() {
        return ThreadLocalRandom.current().nextLong(0, 9000000000000000L);
    }

    public static boolean checkCode(String code) {
        if (null == code) {
            return false;
        } else {
            return code.matches("^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*$");
        }
    }
}
