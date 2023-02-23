package org.cube.plugin.sensitive.test.custom;

import org.cube.plugin.sensitive.ISensitiveCustom;

public class PhoneSensitive implements ISensitiveCustom {
    @Override
    public String sensitive(String value) {
        return value + "-Test";
    }
}
