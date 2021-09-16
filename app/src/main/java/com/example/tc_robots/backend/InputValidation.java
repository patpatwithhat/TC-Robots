package com.example.tc_robots.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";

    public static boolean isEmailValid(String email) {
        if(isTextinputEmpty(email)) return false;
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isTextinputEmpty(String editText) {
        return editText.isEmpty();
    }

    public static boolean isTextFieldValid(String editText) {
        return isTextinputEmpty(editText);
    }
}
