package org.cube.plugin.easyexcel.constraintvalidators;

import org.cube.plugin.easyexcel.ConstraintValidator;

import java.util.regex.Pattern;

/**
 * IdCard
 *
 * @author gjw
 * @version 1.0
 * @description
 * @date 2022/11/11 13:15
 */
public class IdCard implements ConstraintValidator {
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    @Override
    public boolean isValid(String str) {
        return Pattern.matches(REGEX_ID_CARD, str);
    }
}
