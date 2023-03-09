package com.example.javamiaosha.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {

    private static final Pattern mobile_pattern=Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$");

    public static Boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
