package com.honeywell.hch.airtouch.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.honeywell.hch.airtouch.library.LibApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jin on 16/4/11.
 */
public class SharePreferenceUtil {

    /**
     * get instance of SharedPreference by file name
     *
     * @param name  name of the SharedPreference storage file. (i.e group control, user login etc)
     * @return  instance of the particular SharedPreference
     */
    public static SharedPreferences getSharedPreferenceInstanceByName(String name) {
        return LibApplication.getContext().
                    getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * get/set methods for particular SharePreference
     *
     * @param name name of the SharedPreference storage file. (i.e group control, user login etc)
     * @param key
     * @param defaultValue
     * @return
     */
    public static void setPrefFloat(final String name, final String key, final float defaultValue) {
        getSharedPreferenceInstanceByName(name).edit().putFloat(key, defaultValue).commit();
    }

    public static float getPrefFloat(final String name, final String key, final float defaultValue) {
        return getSharedPreferenceInstanceByName(name).getFloat(key, defaultValue);
    }

    public static void setPrefInt(final String name, final String key, final int defaultValue) {
        getSharedPreferenceInstanceByName(name).edit().putInt(key, defaultValue).commit();
    }

    public static int getPrefInt(final String name, final String key, final int defaultValue) {
        return getSharedPreferenceInstanceByName(name).getInt(key, defaultValue);
    }

    public static long getPrefLong(final String name, final String key, final long defaultValue) {
        return getSharedPreferenceInstanceByName(name).getLong(key, defaultValue);
    }

    public static void setPrefBoolean(final String name, final String key, final boolean defaultValue) {
        getSharedPreferenceInstanceByName(name).edit().putBoolean(key, defaultValue).commit();
    }

    public static boolean getPrefBoolean(final String name, final String key, final boolean defaultValue) {
        return getSharedPreferenceInstanceByName(name).getBoolean(key, defaultValue);
    }

    public static String getPrefString(final String name, String key, final String defaultValue) {
        return getSharedPreferenceInstanceByName(name).getString(key, defaultValue);
    }

    public static void setPrefString(final String name, final String key, final String defaultValue) {
        getSharedPreferenceInstanceByName(name).edit().putString(key, defaultValue).apply();
    }

    public static void setPrefLong(final String name, final String key, final long defaultValue) {
        getSharedPreferenceInstanceByName(name).edit().putLong(key, defaultValue).commit();
    }

    public static void setPrefStringSet(final String name, final String key, final Set<String> defaultValue) {
        getSharedPreferenceInstanceByName(name).edit().putStringSet(key, defaultValue).commit();
    }

    public static  Set<String> getPrefStringSet(final String name, final String key, final HashSet<String> defaultValue) {
        return getSharedPreferenceInstanceByName(name).getStringSet(key, defaultValue);
    }

    public static void clearPreference(Context context, final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }

    public static void clearPreference(final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }

    public static void setObject(String key, Object object,Context context) {
        SharedPreferences sp = context.getSharedPreferences("test_sp", Context.MODE_PRIVATE);

        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences("test_sp", Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
