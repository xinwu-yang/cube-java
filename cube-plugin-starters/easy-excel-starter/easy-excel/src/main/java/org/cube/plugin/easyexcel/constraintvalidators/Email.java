package org.cube.plugin.easyexcel.constraintvalidators;

import org.cube.plugin.easyexcel.ConstraintValidator;

import java.util.regex.Pattern;

/**
 * Email
 *
 * @author gjw
 * @version 1.0
 * @description
 * @date 2022/11/11 13:07
 */
public class Email implements ConstraintValidator {
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    @Override
    public boolean isValid(String str) {
        return Pattern.matches(REGEX_EMAIL, str);
    }
}
