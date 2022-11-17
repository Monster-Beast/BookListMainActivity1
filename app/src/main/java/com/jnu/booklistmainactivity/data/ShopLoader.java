package com.jnu.booklistmainactivity.data;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShopLoader {
    // 获取网页的html源代码
    @NonNull
    public String download(String path){
        try{
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader reader;
                reader = new BufferedReader(inputStreamReader);
                String tempLine = null;
                StringBuffer resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
                Log.i("test data",resultBuffer.toString());
                return resultBuffer.toString();
            }
        }catch(Exception exception)
        {
            Log.e("error",exception.getMessage());
        }
        return "";
    }

    //解析JSON数据的函数
    @NonNull
    public List<SiteLocation> parseJson(String JsonText)
    {
        List<SiteLocation> locations=new ArrayList<>();
        try {
            JSONObject root = new JSONObject(JsonText);
            JSONArray shops = root.getJSONArray("shops");
            for(int i=0;i<shops.length();++i){
                JSONObject shop=shops.getJSONObject(i);
                SiteLocation siteLocation=new SiteLocation();
                siteLocation.setName(shop.getString("name"));
                siteLocation.setLatitude(shop.getDouble("latitude"));
                siteLocation.setLongitude(shop.getDouble("longitude"));
                siteLocation.setMemo(shop.getString("memo"));
                locations.add(siteLocation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locations;
    }

}
