package com.hc.mvparchitecturewsw.utils;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by hc on 2017/12/8.
 * @author wsw
 * LOG
 */

public class LogUtils {
    /**
     * 在application调用初始化
     */
    public static void logInit(boolean debug) {
        Contants.DEBUG_ENABLE = debug;
        if (Contants.DEBUG_ENABLE) {
            // default PRETTYLOGGER or use just init()
            Logger.init(Contants.DEBUG_TAG)
                    // default 2
                    .methodCount(2)
                    // default LogLevel.FULL
                    .logLevel(LogLevel.FULL)
                    // default 0
                    .methodOffset(0);
        } else {
            // default PRETTYLOGGER or use just init()
            Logger.init()
                    // default 2
                    .methodCount(3)
                    // default shown
                    .hideThreadInfo()
                    // default LogLevel.FULL
                    .logLevel(LogLevel.NONE)
                    .methodOffset(2);
        }
    }

    public static void logd(String tag, String message) {
        if (Contants.DEBUG_ENABLE) {
            Logger.d(tag, message);
        }
    }

    public static void logd(String message) {
        if (Contants.DEBUG_ENABLE) {
            Logger.d(message);
        }
    }

    public static void loge(Throwable throwable, String message, Object... args) {
        if (Contants.DEBUG_ENABLE) {
            Logger.e(throwable, message, args);
        }
    }

    public static void loge(String message, Object... args) {
        if (Contants.DEBUG_ENABLE) {
            Logger.e(message, args);
        }
    }

    public static void logi(String message, Object... args) {
        if (Contants.DEBUG_ENABLE) {
            Logger.i(message, args);
        }
    }

    public static void logv(String message, Object... args) {
        if (Contants.DEBUG_ENABLE) {
            Logger.v(message, args);
        }
    }

    public static void logw(String message, Object... args) {
        if (Contants.DEBUG_ENABLE) {
            Logger.v(message, args);
        }
    }

    public static void logwtf(String message, Object... args) {
        if (Contants.DEBUG_ENABLE) {
            Logger.wtf(message, args);
        }
    }

    public static void logjson(String message) {
        if (Contants.DEBUG_ENABLE) {
            Logger.json(message);
        }
    }

    public static void logxml(String message) {
        if (Contants.DEBUG_ENABLE) {
            Logger.xml(message);
        }
    }
}
