package org.cube.plugin.easyexcel;

/**
 * ConstraintValidator
 *
 * @author gjw
 * @version 1.0
 * @description
 * @date 2022/11/9 16:37
 */
public interface ConstraintValidator {

    default String initialize(String str) {
            return str;
    }


    boolean isValid(String str);
}
