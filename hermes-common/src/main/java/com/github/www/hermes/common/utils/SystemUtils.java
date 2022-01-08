package com.github.hermes.hermes.common.utils;

import com.github.hermes.hermes.common.constant.SystemConstant;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class SystemUtils {


    public static String getCurrentSystemOs() {
        String systemOs = System.getProperty("os.name");
        if (systemOs.toLowerCase().contains("linux")) {
            return SystemConstant.LINUX;
        } else if (systemOs.toLowerCase().contains("mac")) {
            return SystemConstant.MAC_OS;
        } else {
            return SystemConstant.WINDOWS;
        }
    }


}
