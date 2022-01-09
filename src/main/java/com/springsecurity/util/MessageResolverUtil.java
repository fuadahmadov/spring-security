package com.springsecurity.util;

import java.util.ResourceBundle;

import com.springsecurity.constant.Common;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageResolverUtil {
    public static String resolve(String message) {
        return ResourceBundle
                .getBundle(Common.I18N, LocaleContextHolder.getLocale())
                .getString(message);
    }
}
