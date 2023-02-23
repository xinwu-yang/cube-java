package org.cube.plugin.easyexcel.constraintvalidators;

import org.cube.plugin.easyexcel.ConstraintValidator;

import java.util.regex.Pattern;

/**
 * PhoneCV
 *
 * @author gjw
 * @version 1.0
 * @description
 * @date 2022/11/9 16:44
 */
public class Phone implements ConstraintValidator {
    public static final String REGEX_PHONE = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
    @Override
    public boolean isValid(String str) {
        if (str.length() != 11) {
            return false;
        }
        return Pattern.matches(REGEX_PHONE, str);
    }
}
