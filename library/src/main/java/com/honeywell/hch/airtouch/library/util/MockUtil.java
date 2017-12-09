package com.honeywell.hch.airtouch.library.util;

import com.honeywell.hch.airtouch.library.LibApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vincent on 25/2/16.
 */
public class MockUtil {


    public static String readFileFromWebTestsAsString(int id) {
        InputStream input = LibApplication.getContext().getResources().openRawResource(id);
        BufferedReader reader;
        try {
            byte[] fileData = new byte[input.available()];
            input.read(fileData);
            String dataFromFile = new String(fileData, "UTF-8");
            input.close();
            return dataFromFile;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
