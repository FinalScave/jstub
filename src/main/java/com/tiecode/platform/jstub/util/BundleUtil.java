package com.tiecode.platform.jstub.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ResourceBundle util
 * @author Scave
 */
public class BundleUtil {

    private static final ResourceBundle jstubBundle;

    static {
        jstubBundle = ResourceBundle.getBundle("com.tiecode.platform.jstub.jstub-command", Locale.getDefault());
    }

    public static String getCommandText(String key) {
        return jstubBundle.getString(key);
    }

    public static String getFormatCommandText(String key, Object... params) {
        MessageFormat format = new MessageFormat(jstubBundle.getString(key));
        return format.format(params);
    }
}
