package com.ftg2021.effo2021V1.Authentication.DataValidation;

import java.util.regex.Pattern;

public class DataValidator {
    public static boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }


    public static boolean isValidEmail(String email)
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern))
            return true;

        return false;
    }
}
