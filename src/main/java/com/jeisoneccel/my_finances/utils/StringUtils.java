package com.jeisoneccel.my_finances.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    private static final String TRIM_REGEX = "\\s{2,}";

    public String trimAll(String value) {
        if (value == null) return null;
        return value.trim().replaceAll(TRIM_REGEX, " ");
    }

}
