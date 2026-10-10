package com.faisal.backend.util;

import java.util.UUID;

public class CustomRandomGenerator {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
