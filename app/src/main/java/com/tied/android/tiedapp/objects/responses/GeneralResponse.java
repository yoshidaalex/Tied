package com.tied.android.tiedapp.objects.responses;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Femi on 7/29/2016.
 */
public class GeneralResponse  {
    JSONObject response=null;
    ResponseBody responseBody;

    public GeneralResponse(ResponseBody rb) {
        this.responseBody=rb;
        try{
           this.response=new JSONObject(responseBody.string());
        }catch (Exception je) {
            Logger.write(je);
        }
    }


    public  <T>  List<T> getDataAsList(String key, Class<T> classOfT)  throws Exception {
        if(response==null) response=new JSONObject(responseBody.string());
        JSONArray ja=response.getJSONArray(key);
        Gson gson=new Gson();
        int len=ja.length();
        List<T> list = new ArrayList<T>(len);
        // List<Object> list=(List<Object>)l;
        for(int i=0; i<len; i++) {
            list.add(gson.fromJson(ja.getString(i), classOfT));
        }
        return list;
    }
    public <T> T getData(String key, Class<T> classOfT) throws Exception {
        if(response==null) response=new JSONObject(responseBody.string());
        Gson gson=new Gson();
        return  gson.fromJson(response.getString(key), (Type)classOfT);

    }
    public JSONObject getKeyObjects() throws Exception {
        if(response==null) response=new JSONObject(responseBody.string());
       return new JSONObject(response.getString("result")).getJSONObject("data");


    }
    public List<Object> getKeys() throws Exception {
       //JSONArray ja= ;
        Gson gson=new Gson();
        return gson.fromJson(new JSONObject(response.getString("result")).getString("keys"),  new TypeToken<List<Object>>(){}.getType());
    }
    public _Meta getMeta() throws Exception {
        if(response==null) response=new JSONObject(responseBody.string());
        return MyUtils.getMeta(response);
    }

    public boolean isAuthFailed() throws Exception  {
        if(response==null) response=new JSONObject(responseBody.string());
        return MyUtils.isAuthFailed(response);
       // return getMeta().getStatus_code()==401;
    }
    public String toString() {
        return response.toString();
    }
}
