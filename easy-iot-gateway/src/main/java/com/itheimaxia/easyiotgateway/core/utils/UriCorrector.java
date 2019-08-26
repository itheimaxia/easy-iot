package com.itheimaxia.easyiotgateway.core.utils;

import static org.apache.commons.lang3.StringUtils.*;

public class UriCorrector {
    public static String correctUri(String uri) {
        if (isBlank(uri)) {
            return EMPTY;
        }
        return removeEnd(prependIfMissing(uri, "/"), "/");
    }
}
