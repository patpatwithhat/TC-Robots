package com.example.tc_robots.uihelpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final String IPv4_REGEX = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b";
    private static final int MAX_PORT_NUMBER = 65535;
    private static final int MIN_PORT_NUMBER = 1;

    public static boolean isEmailValid(String email) {
        if (isTextinputEmpty(email)) return false;
        return getMatcherWithRegex(EMAIL_REGEX, email).matches();
    }

    private static boolean isTextinputEmpty(String editText) {
        return editText.isEmpty();
    }

    public static boolean isTextFieldValid(String editText) {
        return isTextinputEmpty(editText);
    }

    public static boolean isIPValid(String ip) {
        if (isTextinputEmpty(ip)) return false;
        return getMatcherWithRegex(IPv4_REGEX, ip.trim()).matches();
    }

    private static Matcher getMatcherWithRegex(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

    public static boolean isPortValid(String port) {
        try {
            int portNum = Integer.parseInt(port.trim());
            return portNum >= MIN_PORT_NUMBER && portNum <= MAX_PORT_NUMBER;
        } catch (Exception e) {
            return false;
        }
    }
}
