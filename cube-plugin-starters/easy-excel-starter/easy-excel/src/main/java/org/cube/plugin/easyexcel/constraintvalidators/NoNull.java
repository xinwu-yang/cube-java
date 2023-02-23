package org.cube.plugin.easyexcel.constraintvalidators;

import org.cube.plugin.easyexcel.ConstraintValidator;

/**
 * NoNull
 *
 * @author gjw
 * @version 1.0
 * @description
 * @date 2022/11/9 16:46
 */
public class NoNull implements ConstraintValidator {
    @Override
    public boolean isValid(String value) {
        if (value.trim().equals("") || value ==null){
            return false;
        }
        return true;
    }
}
