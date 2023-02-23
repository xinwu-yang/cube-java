package org.cube.modules.system.oss.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StrAttackFilter {

    /**
     * 文件上传字符串过滤特殊字符
     */
    public static String filter(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx = "[`_《》~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
