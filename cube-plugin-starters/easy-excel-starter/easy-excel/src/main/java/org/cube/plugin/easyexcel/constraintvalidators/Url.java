package org.cube.plugin.easyexcel.constraintvalidators;

import org.cube.plugin.easyexcel.ConstraintValidator;

import java.util.regex.Pattern;

/**
 * Url
 *
 * @author gjw
 * @version 1.0
 * @description
 * @date 2022/11/11 13:17
 */
public class Url  implements ConstraintValidator {

    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    @Override
    public boolean isValid(String str) {
        return Pattern.matches(REGEX_URL, str);
    }
}
