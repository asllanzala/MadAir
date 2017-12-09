package com.honeywell.hch.airtouch.plateform.http.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.honeywell.hch.airtouch.library.util.FileUtils;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by wuyuan on 9/10/15.
 */
public class DownloadBackgroundTask extends AsyncTask<Object, Object, Object> {

    private static final String CHINA_CITYCODE_PRE = "CH";


    private static final int ALL_CITY_IS_CHINA = 0;

    private Context mContext;

    private String localRootPath;
    private String mLocalVersionJsonPath;

    private Set<String> mLocalCityCodeList = new HashSet<>();

    private Map<String, String> serverVersionMap = new HashMap<>();
    private JSONArray mLocalVersionJsonArray = new JSONArray();
//    private JSONArray mLocalVersionWillWritedJsonArray = new JSONArray();

    private static final String CHINA_CITY_SERVER_URL = "https://hch.blob.core.chinacloudapi.cn/airtouchscitybackground/";

    private static final String VERSION_URL = "city.txt";

    private static final String IAMGE_NAME_KEY = "image";

    private static final String IMAGE_VERSION = "version";

    private static final String DEFAULT_CITY_KEY = "Default";

    private JSONArray bitmapJsonArray = null;

    private static boolean isRunning = false;

    private boolean isDefaultCity = false;

//    private String mBlurBackgroundPath = "";

    private int mCountryCount = 1;

    public DownloadBackgroundTask() {
        mContext = AppManager.getInstance().getApplication().getApplicationContext().getApplicationContext();
    }


    @Override
    protected Object doInBackground(Object... params) {

        try {
            isRunning = true;

            //get local city list after login success
            getLocalCityCodeList();

            getCountryCount();

            //consider use chinese account in other country.
            resetData();
            if(!constructLocationPath()){
                return null;
            }


            mLocalVersionJsonPath = localRootPath + File.separator + VERSION_URL;

            //判断是否有文件。
            File versionJsonFile = new File(mLocalVersionJsonPath);
            //get local version.txt content
            if (versionJsonFile.exists()) {
                readLocalCityFile(mLocalVersionJsonPath);
            }

            //get the version data from server
            if (!getMapForJson(getServerJsonFile(getServerPath() + VERSION_URL), serverVersionMap)) {
                //get failed
                setBackgroundListWhenNoServerList();
                return null;
            }

            downLoadFileAndWriteJsonToLocal();

            setBackgroundListWhenHasServerList();

        }catch (Exception e){
            LogUtil.log(LogUtil.LogLevel.ERROR,"Download","doInBackground exception = " + e.toString());
        }

        return null;
    }

    private void resetData(){
        serverVersionMap.clear();
        mLocalVersionJsonArray = null;
    }

    private boolean constructLocationPath(){

        File bgFile = createDirFile(Environment.getExternalStorageDirectory().getPath(), FileUtils.BACKGROUND_FILE_PATH);
        if(bgFile == null){
            return false;
        }

        String path = getLocationLastPathName();

        File bgFile3 = createDirFile(bgFile.getAbsolutePath(), path);
        if (bgFile3 == null) {
            return false;
        }

        localRootPath = bgFile3.getAbsolutePath();
        return true;
    }

    private void getCountryCount(){
        mCountryCount = 1;
    }

    private String getServerPath(){
        return CHINA_CITY_SERVER_URL;
    }

    private String getLocationLastPathName(){
        return FileUtils.BACKGROUND_FILE_PATH;
    }


    private int getLocalCityList(){
        return ALL_CITY_IS_CHINA;

    }

    @Override
    protected void onPostExecute(Object responseResult) {
        isRunning = false;
        Intent boradIntent = new Intent(HPlusConstants.DOWN_LOAD_BG_END);
        AppManager.getInstance().getApplication().getApplicationContext().sendOrderedBroadcast(boradIntent,null);

        super.onPostExecute(responseResult);
    }

    private File createDirFile(String rootPath,String fileName){
        File bgFile = new File(rootPath, fileName);
        if (!bgFile.exists() && !bgFile.mkdir()) {
            return null;
        }
        return bgFile;
    }


    public boolean isRunning() {
        return isRunning;
    }

    private void downLoadFileAndWriteJsonToLocal() {
        try {
            for (String cityCode : mLocalCityCodeList) {
                isDefaultCity = false;
                //if the version of the city is not same as the server.
                if (!isHasSameCityVersion(cityCode)) {
                    boolean isDownSuccess = false;
                    cityCode = isDefaultCity ? "Default" : cityCode;
                    //down load the citycode.txt,get the json,and check the different
                    if (getCityImageJsonFile(cityCode)) {
                        JSONArray localJsonArrayList = getJsonArrayOfTheFile(localRootPath + File.separator + cityCode + ".txt");

                        for (int i = 0; i < bitmapJsonArray.length(); i++) {
                            JSONObject serverBgOb = bitmapJsonArray.getJSONObject(i);
                            String imageName = serverBgOb.getString("image");
                            if (!isLocalJsonArrayContain(imageName, localJsonArrayList)) {
                                //down load the file and wirte to the local citycode.txt
                                LogUtil.log(LogUtil.LogLevel.INFO, "DownLoad", "begin to down file =" + imageName);
                                downloadBackground(imageName);
                                LogUtil.log(LogUtil.LogLevel.INFO, "DownLoad", "end to down file =" + imageName);
                                writeFile(localRootPath + File.separator + cityCode + ".txt", serverBgOb.toString(), true);

                                isDownSuccess = true;
                            }
                        }
                    }

                    if (isDownSuccess){
                        //if all file down load success,write the version to the city.txt
                        String cityVersion = serverVersionMap.get(cityCode);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(cityCode, cityVersion);
                        if (mLocalVersionJsonArray == null){
                            mLocalVersionJsonArray = new JSONArray();
                        }
                        mLocalVersionJsonArray.put(jsonObject);
                    }

                }

            }
        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.ERROR, "DownLoadBackground", e.toString());
        }

        try{

            if (mLocalVersionJsonArray != null && mLocalVersionJsonArray.length() > 0){
                //clear and rewrite
                File file = new File(localRootPath + File.separator + VERSION_URL);

                if (file.exists()) {
                    file.delete();
                }

                writeFile(localRootPath + File.separator + VERSION_URL, mLocalVersionJsonArray.toString(), false);
                mLocalVersionJsonArray = null;
            }
        }
        catch (Exception e){

        }

    }



    private boolean isLocalJsonArrayContain(String imageName, JSONArray localJsonArrayList) {
        try {
            if (localJsonArrayList != null && localJsonArrayList.length() > 0) {
                for (int i = 0; i < localJsonArrayList.length(); i++) {
                    JSONObject localBgOb = localJsonArrayList.getJSONObject(i);
                    String localImageName = localBgOb.getString("image");
                    if (imageName != null && imageName.equalsIgnoreCase(localImageName)) {
                        return true;
                    }
                }

            }
        } catch (Exception e) {

        }

        return false;
    }


    private boolean isDefaultCity(String cityCode){
        String cityFileServerVersion = serverVersionMap.get(cityCode);
        if (StringUtil.isEmpty(cityFileServerVersion)) {
            return true;
        }
        return false;
    }

    private boolean isHasSameCityVersion(String cityCode) {

        String cityFileServerVersion = serverVersionMap.get(cityCode);
        if (StringUtil.isEmpty(cityFileServerVersion)) {
            cityFileServerVersion = serverVersionMap.get(DEFAULT_CITY_KEY);
            isDefaultCity = true;
        }

        if (mLocalVersionJsonArray != null){
            int removeIndex = -1;
            for (int i = 0; i < mLocalVersionJsonArray.length(); i++){
                try{
                    JSONObject jsonObject = mLocalVersionJsonArray.getJSONObject(i);
                    String cityFileLocalVersion = "";
                    if (!isDefaultCity) {
                        cityFileLocalVersion = jsonObject.getString(cityCode);
                    }
                    else{
                        cityFileLocalVersion = jsonObject.getString(DEFAULT_CITY_KEY);
                    }
                    if (cityFileServerVersion != null && cityFileServerVersion.equalsIgnoreCase(cityFileLocalVersion)) {
                        return true;
                    }
                    else if (!StringUtil.isEmpty(cityFileLocalVersion) && !cityFileServerVersion.equalsIgnoreCase(cityFileLocalVersion)){
                        //have the different version between server and local.
                        removeIndex = i;
                    }
                }
                catch (Exception e){
                    LogUtil.log(LogUtil.LogLevel.INFO, "isHasSameCityVersion", e.toString());
                }

            }
            if (removeIndex != -1){
                mLocalVersionJsonArray.remove(removeIndex);
            }
        }

        return false;
    }

    private boolean getCityImageJsonFile(String cityCode) {
        if (bitmapJsonArray != null) {
            bitmapJsonArray = null;
        }

        try {
            String cityImageJsonFile = getServerPath() + cityCode + ".txt";
            String jsonString = getServerJsonFile(cityImageJsonFile);
            bitmapJsonArray = new JSONArray(jsonString);
        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.ERROR, "Download", "Exception e =" + e.toString());
            return false;
        }
        return true;
    }

    private void getLocalCityCodeList() {

        List<UserLocationData> userLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
        for (UserLocationData userLocationData : userLocationDataList) {
            mLocalCityCodeList.add(userLocationData.getCity());
        }
    }

    private String getServerJsonFile(String uri) {
        String resultString = "";
        try {

            URL url = new URL(uri);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(300 * 1000);
            conn.setReadTimeout(300 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder resultBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    resultBuilder.append(line);
                }
                resultString = resultBuilder.toString();
                inStream.close();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.log(LogUtil.LogLevel.ERROR, "DownLoadBackground", "getServerJsonFile error: " + e.toString());
        }
        return resultString;
    }

    private JSONArray getJsonArrayOfTheFile(String localFilePath) {

        JSONArray localImageJsonArray = null;
        try {
            File file = new File(localFilePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            String jsonString = readLocalJsonFile(localFilePath);
            localImageJsonArray = new JSONArray(jsonString);
        } catch (Exception e) {

        }
        return localImageJsonArray;
    }

    private String readLocalJsonFile(String localFilePath) {
        String resultString = "";
        File file = new File(localFilePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);


            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder resultBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }
            resultString = resultBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {

                }

            }
        }
        return resultString;
    }

    public void readLocalCityFile(String filePath) {
       mLocalVersionJsonArray = getJsonArrayOfTheFile(filePath);
    }

    //写文件
    public void writeFile(String fileName, String write_str, boolean isAppend) throws IOException {

        File file = new File(fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file, isAppend);

        byte[] bytes = write_str.getBytes();

        fos.write(bytes);

        fos.close();
    }

    /**
     * {    "CHGD000000":"1444802026",    "Default":"1444802026"} to map
     * @param jsonStr
     * @param map
     * @return
     */
    public boolean getMapForJson(String jsonStr, Map<String, String> map) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            String value;
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = (String) jsonObject.get(key);
                map.put(key, value);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void downloadBackground(String fileName) throws Exception {

        byte[] data = getImage(getServerPath() + fileName);
        if (data != null) {

            Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            FileUtils.saveFile(mBitmap, localRootPath + "/" + fileName);
        }

    }

    /**
     * --------------------------------------------------------
     * <p/>
     * //    download the file
     * <p/>
     * /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setConnectTimeout(300 * 1000);
        conn.setReadTimeout(300 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return FileUtils.readStream(inStream);
        }
        return null;
    }




    public void setBackgroundListWhenHasServerList() {

        File bgFile = new File(localRootPath);
        if (bgFile != null && bgFile.exists() && bgFile.listFiles() != null) {
            File[] fileList = bgFile.listFiles();
            List<UserLocationData> userLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
            for (UserLocationData userLocationData : userLocationDataList) {
                addDifferentTypePath(fileList,userLocationData);
            }
        }

    }

    private void addDifferentTypePath(File[] fileList,UserLocationData userLocationData){

        if (fileList != null &&
                fileList.length > 0 &&
                fileList[0].getName() != null)
        {
            if (StringUtil.isEmpty(serverVersionMap.get(userLocationData.getCity()))) {
                //add default
                addPathListToLocationDataItem(fileList,userLocationData,true);
            } else {
                //add citycode background
                addPathListToLocationDataItem(fileList, userLocationData, false);
            }
        }

    }


    public void setBackgroundListWhenNoServerList(){
        File bgFile = new File(localRootPath);
        if (bgFile != null && bgFile.exists()) {
            File[] fileList = bgFile.listFiles();
            List<UserLocationData> userLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
            for (UserLocationData userLocationData : userLocationDataList) {
                if (!StringUtil.isEmpty(getLocalVersionString(userLocationData.getCity()))) {
                    //add citycode background
                    addPathListToLocationDataItem(fileList,userLocationData,false);
                }
            }
        }
    }

    private String getLocalVersionString(String cityCode){
        String cityVersion = "";
        if (mLocalVersionJsonArray != null){
            for (int i = 0; i < mLocalVersionJsonArray.length(); i++){
                try{
                    JSONObject jsonObject = mLocalVersionJsonArray.getJSONObject(i);
                    cityVersion = jsonObject.getString(cityCode);
                }
                catch (Exception e){

                }
            }
        }
        return cityVersion;
    }

    private void addPathListToLocationDataItem(File[] files, UserLocationData userLocationDataItem, boolean isDefault) {

        if (files != null &&
                files.length > 0 &&
                files[0].getName() != null)
        {
            for (File file : files) {
                if (!isLocationDataItemHasThisFile(file,userLocationDataItem)){
                    if (isDefault && file.getName().contains(DEFAULT_CITY_KEY) && !file.getName().contains(".txt")
                            || !isDefault && file.getName().contains(userLocationDataItem.getCity()) && !file.getName().contains(".txt")) {
                        userLocationDataItem.getCityBackgroundDta().addItemToCityPathList(file.getAbsolutePath());
                    }
                }
            }
        }

    }


    private boolean isLocationDataItemHasThisFile(File files, UserLocationData userLocationDataItem){
        List<String> backgroundList = userLocationDataItem.getCityBackgroundDta().getCityBackgroundPathList();
        if (backgroundList.contains(files.getAbsolutePath())){
            return true;
        }
        return false;
    }



}
