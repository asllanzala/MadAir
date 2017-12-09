package com.honeywell.hch.airtouch.library.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Vincent on 24/3/16.
 */
public class LocalUtil {

    private static final String TAG = "LocalUtil";

    public static String getLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    public static void getLanguages(Context context) {
        Locale locale = context.getResources().getConfiguration().locale.getDefault();
        Locale[] locales = Locale.getAvailableLocales();
        Map map = new TreeMap();
        for (int i = 0; i < locales.length; i++) {
            map.put(locales[i].getLanguage(), i);
        }
        Set set = map.keySet();
        int i = 0;
        List<String> languagesList = new ArrayList<>();
        for (Iterator iter = set.iterator(); iter.hasNext(); ) {
            languagesList.add("@" + "\"" + String.valueOf(iter.next()) + "\"");
//                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "languages" + i + "@\" " + String.valueOf(iter.next()));
//            i++;
        }
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "languages: " + languagesList.toString());

    }

}
