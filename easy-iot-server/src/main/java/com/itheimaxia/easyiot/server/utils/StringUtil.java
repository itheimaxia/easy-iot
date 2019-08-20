package com.itheimaxia.easyiot.server.utils;

import org.apache.commons.lang3.RandomStringUtils;

public final class StringUtil {
    public static String createDeviceName(){
        return RandomStringUtils.randomAlphanumeric(6);
    }
}
