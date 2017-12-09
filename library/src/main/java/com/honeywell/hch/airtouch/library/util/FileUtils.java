package com.honeywell.hch.airtouch.library.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.honeywell.hch.airtouch.library.LibApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FileUtils {

    public static final String BACKGROUND_FILE_PATH = ".airtouchscitybackground";

    public static final String BACKGROUND_BLUR_FILE_PATH = "blurbackground";

    public static final String INDIA_BACKGROUND_FILE_PATH = "indian";

    public static final String INDIA_BACKGROUND_BLUR_FILE_PATH = "indian/blurbackground";


    private static final String LOG_TAG = FileUtils.class.getSimpleName();

    /**
     * Write text to filer.
     *
     * @param content
     * @param dir
     * @param fileName
     * @return
     */
    public static boolean write2File(String content, String dir, String fileName) {
        return write2File(content, dir, fileName, false);
    }

    /**
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {
        File targetFile = new File(fileName);
        StringBuilder readedStr = new StringBuilder();

        InputStream in = null;
        BufferedReader br = null;
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
                return readedStr.toString();
            } else {
                in = new BufferedInputStream(new FileInputStream(targetFile));
                br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String tmp;

                while ((tmp = br.readLine()) != null) {
                    readedStr.append(tmp);
                }
                br.close();
                in.close();

                return readedStr.toString();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "readFile", e);
            return readedStr.toString();
        }
    }

    /**
     * Retrieves last modified time.
     *
     * @param path
     * @return
     */
    public static long getLastModifiedTime(String path) {
        File file = new File(path);
        return file.lastModified();
    }

    /**
     * Check whether file exists.
     *
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * Check whether file exists.
     *
     * @return
     */
    public static boolean fileCpy(String srcPath, String dstPath, boolean rewrite) {
        File srcFile = null;
        File dstFile = null;
        FileInputStream fosfrom = null;
        FileOutputStream fosto = null;
        try {
            srcFile = new File(srcPath);
            dstFile = new File(dstPath);
            if (!srcFile.exists()) {
                return false;
            }
            if (!srcFile.isFile()) {
                return false;
            }
            if (!srcFile.canRead()) {
                return false;
            }
            if (!dstFile.getParentFile().exists()) {
                dstFile.getParentFile().mkdirs();
            }
            if (dstFile.exists() && rewrite) {
                dstFile.delete();
            }
            fosfrom = new FileInputStream(srcFile);
            fosto = new FileOutputStream(dstFile, false);
            byte bt[] = new byte[1024];
            int c;

            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "fileCpy", e);
            return false;
        } finally {
            if (fosfrom != null) {
                try {
                    fosfrom.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "fileCpy.IOException.1", e);
                }
            }
            if (fosto != null) {
                try {
                    fosto.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "fileCpy.IOException.2", e);
                }
            }
        }
    }

    /**
     * Rename file.
     *
     * @param srcPath
     * @param dstPath
     * @return
     */
    public static boolean renameToFile(String srcPath, String dstPath) {
        File srcFile = null;
        File dstFile = null;
        try {
            srcFile = new File(srcPath);
            dstFile = new File(dstPath);
            return srcFile.renameTo(dstFile);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception", e);
            return false;
        }

    }

    /**
     * Create file.
     *
     * @param dir      directory
     * @param fileName file name
     */
    public static boolean createFile(String dir, String fileName) {
        if (StringUtil.isEmpty(dir) || StringUtil.isEmpty(fileName)) {
            return false;
        }
        try {
            // create directory
            createDir(dir);
            return createFile(StringUtil.trimRight(dir, "/") + "/"
                    + StringUtil.trimLeft(fileName, "/"));
        } catch (Exception e) {
            Log.e(LOG_TAG, "createFile", e);
            return false;
        }
    }

    /**
     * Create directory.
     *
     * @param dir
     */
    public static void createDir(String dir) {
        if (StringUtil.isEmpty(dir)) {
            return;
        }
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * Create file.
     *
     * @param path the whole file.
     */
    public static boolean createFile(String path) {
        if (StringUtil.isEmpty(path)) {
            return false;
        }
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "createFile", e);
            return false;
        }
    }

    /**
     * Create file.
     *
     * @param path the whole file.
     */
    public static void clearFileInDir(String path) {
        if (StringUtil.isEmpty(path)) {
            return;
        }
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {

            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    /**
     * Delete file.
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    /**
     * Uncompress the zip file to folder
     *
     * @param zipFileString
     * @param outPathString
     * @throws Exception
     */
    public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
        Log.v(LOG_TAG, "UnZipFolder(zipFileString, String)");
        unZipFolder(zipFileString, outPathString);
    }

    /**
     * Uncompress the zip file to folder
     *
     * @param inputStream   input stream
     * @param outPathString folder uncompressed
     * @throws Exception
     */
    public static void unZipFolder(InputStream inputStream, String outPathString) throws Exception {
        if (inputStream == null || StringUtil.isEmpty(outPathString)) {
            return;
        }
        Log.v(LOG_TAG, "UnZipFolder(InputStream, String)");
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(inputStream);
        java.util.zip.ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {

                // get the folder name of the widget  
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();

            } else {

                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file  
                FileOutputStream out = new FileOutputStream(file, false);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer  
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0  
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }//end of while  

        inZip.close();

    }

    /**
     * Write text to filer.
     *
     * @param content
     * @param dir
     * @param fileName
     * @return
     */
    public static boolean write2File(String content, String dir, String fileName, boolean appendMode) {
        if (StringUtil.isEmpty(dir) || StringUtil.isEmpty(fileName)) {
            return false;
        }
        String path = StringUtil.trimRight(dir, "/") + "/"
                + StringUtil.trimLeft(fileName, "/");
        if (!createFile(dir, fileName)) {
            return false;
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(path, appendMode);
            bw = new BufferedWriter(fw);

            if (appendMode) {
                bw.append(StringUtil.notNullString(content));
            } else {
                bw.write(StringUtil.notNullString(content));
            }

            bw.flush();
            bw.close();
            fw.close();
            return true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "createFile.1", e);
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "createFile.2", e);
            }
        }
    }

    /**
     * Check whether SDCard is available to access.
     *
     * @return true is SDCard is available or false
     */
    public static boolean isSDCardAvailableToAccess() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }

    public static boolean close(Closeable c, String name) {

        name = name == null ? "Untyped" : name;

        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Issue when closing " + name, e);
            return false;
        }

        return true;
    }


    /**
     * Get data from stream
     *
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


    public static boolean saveFile(Bitmap bm, String saveFileName) {
        File dirFile = new File(saveFileName);
        BufferedOutputStream bos = null;
        try {
            if (!dirFile.exists()) {
                dirFile.createNewFile();
            }
            File myCaptureFile = new File(saveFileName);
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();

        } catch (Exception e) {
            return false;
        } finally {
            try {
                bos.close();
            } catch (Exception e) {

            }
        }
        return true;
    }

    public static File getBlurFile() {
        String appDataPath = Environment.getExternalStorageDirectory().getPath();

        File bgFile = new File(appDataPath, BACKGROUND_BLUR_FILE_PATH);
        if (bgFile != null && bgFile.exists()) {
            return bgFile;
        }
        return null;
    }


    /**
     * @param
     * @return
     */
    public static String readAssesFile() {

        StringBuilder readedStr = new StringBuilder();

        InputStream in = null;
        BufferedReader br = null;
        try {

            in = LibApplication.getContext().getResources().getAssets().open("android_Hplus_update_info.txt");
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String tmp;

            while ((tmp = br.readLine()) != null) {
                readedStr.append(tmp);
            }
            br.close();
            in.close();

            return readedStr.toString();

        } catch (Exception e) {
            Log.e(LOG_TAG, "readFile", e);
            return readedStr.toString();
        }
    }

    //从resources中的raw 文件夹中获取文件并读取数据
    public static String readFromRaw(int rawData) {
        StringBuilder readedStr = new StringBuilder();
        try {
            InputStream in = LibApplication.getContext().getResources().openRawResource(rawData);
            //获取文件的字节数
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String tmp;

            while ((tmp = br.readLine()) != null) {
                readedStr.append(tmp);
                readedStr.append("\n");
            }
            br.close();
            in.close();

            return readedStr.toString();
        } catch (Exception e) {
            return readedStr.toString();
        }
    }


}
