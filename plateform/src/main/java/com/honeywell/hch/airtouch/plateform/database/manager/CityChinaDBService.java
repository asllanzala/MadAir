package com.honeywell.hch.airtouch.plateform.database.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.honeywell.hch.airtouch.plateform.database.model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nan.liu on 2/3/15.
 */
public class CityChinaDBService extends DBService {

    //table info
    public static final String TABLE_NAME = "city";
    public static final String NAME_ZH = "nameZh";
    public static final String NAME_EN = "nameEn";
    public static final String CODE = "code";
    public static final String IS_CURRENT = "isCurrent";

    public static String[] DBKey = {NAME_ZH, NAME_EN, CODE, IS_CURRENT};

    public CityChinaDBService(Context context) {
        super(context);
    }

    public  void insertAllCity(List<City> list) {
        List<HashMap<String, Object>> cityList = new ArrayList<>();
        for (City cityInfo : list) {
            cityList.add(cityInfo.getHashMap());
        }
        insertOrUpdate(TABLE_NAME, DBKey, cityList);
    }

    public  ArrayList<City> findAllCities() {
        ArrayList<HashMap<String, String>> cityDBList = findAll(TABLE_NAME, DBKey);
        ArrayList<City> cityList = new ArrayList<>();
        for (HashMap<String, String> cityMap : cityDBList) {
            cityList.add(new City(cityMap));
        }
        return cityList;
    }

    public  ArrayList<City> getCitiesByKey(String key) {
        ArrayList<City> cityList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getSqliteDatabase();
        if (sqLiteDatabase != null) {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT *"
                    + " FROM " + TABLE_NAME
                    + " WHERE " + NAME_ZH + " LIKE '" + "%" + key + "%'"
                    + " OR " + NAME_EN + " LIKE '" + "%" + key + "%'"
                    + " COLLATE NOCASE", null);
            while (cursor.moveToNext()) {
                HashMap<String, String> cityMap = new HashMap<>();
                for (int i = 0; i < DBKey.length; i++) {
                    cityMap.put(DBKey[i], cursor.getString(i));
                }
                cityList.add(new City(cityMap));
            }
            cursor.close();
            sqLiteDatabase.close();
        }

        return cityList;
    }

    public  City getCityByName(String name) {
        SQLiteDatabase sqLiteDatabase = getSqliteDatabase();
        City city = new City();

        if (sqLiteDatabase != null) {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT *"
                    + " FROM " + TABLE_NAME
                    + " WHERE " + NAME_ZH + " = '" + name + "'"
                    + " OR " + NAME_EN + " = '" + name + "'"
                    + " COLLATE NOCASE", null);

            if (cursor.getCount() != 0) {
                cursor.moveToNext();
                HashMap<String, String> cityMap = new HashMap<>();
                for (int i = 0; i < DBKey.length; i++) {
                    cityMap.put(DBKey[i], cursor.getString(i));
                }
                city = new City(cityMap);
            }
            cursor.close();
            sqLiteDatabase.close();
        }


        return city;
    }

    public  City getCityByCode(String cityCode) {
        City city = new City();
        SQLiteDatabase sqLiteDatabase = getSqliteDatabase();
        if (sqLiteDatabase != null) {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT *"
                    + " FROM " + TABLE_NAME
                    + " WHERE " + CODE + " = '" + cityCode + "'", null);

            if (cursor.getCount() != 0) {
                cursor.moveToNext();
                HashMap<String, String> cityMap = new HashMap<>();
                for (int i = 0; i < DBKey.length; i++) {
                    cityMap.put(DBKey[i], cursor.getString(i));
                }
                city = new City(cityMap);
            }

            cursor.close();
            sqLiteDatabase.close();
        }


        return city;
    }
}
