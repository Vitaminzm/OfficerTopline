package com.education.officertopline.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson 解析工具 simple introduction
 * <p/>
 * <p/>
 * detailed comment
 *
 * @author CWI-APST Email:lei.zhang@symboltech.com 2015年11月5日
 * @see
 * @since 1.0
 */
public class GsonUtil {
    private static Gson gson;

    public static synchronized Gson getGsonInstance(boolean createNew) {
        if (createNew) {
            return new GsonBuilder().serializeNulls().create();
        } else if (gson == null) {
            gson = new GsonBuilder().serializeNulls().create();
        }
        return gson;
    }

    /**
     * beanToJson
     *
     * @param obj
     * @return
     * @throws Exception
     * @author CWI-APST email:26873204@qq.com
     * @Description: TODO beanToJson
     */
    public static String beanToJson(Object obj) throws Exception {
        try {
            Gson gson = getGsonInstance(false);
            String json = gson.toJson(obj);
            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @param list
     * @return
     * @throws Exception
     */
    public static String beanToJson(List list) throws Exception {
        try {
            Gson gson = getGsonInstance(false);
            String json = gson.toJson(list);
            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @param map
     * @return
     * @throws Exception
     */
    public static String beanToJson(Map<String, String> map){
        try {
            Gson gson = getGsonInstance(false);
            String json = gson.toJson(map);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
    **
    * @param json
    * @param clazz
    * @return
     */
    public static <T> List<T> jsonToArrayList(String json, Class<T> clazz)
    {
        Type type = new TypeToken<ArrayList<JsonObject>>()
        {}.getType();
        List<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        List<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * jsonToBean
     *
     * @return
     * @throws Exception
     * @author CWI-APST email:26873204@qq.com
     * @Description: TODO jsonToBean
     */
    public static <T> T jsonToBean(String json, Class<T> cls) throws Exception {
        try {
            Gson gson = getGsonInstance(false);
            T vo = gson.fromJson(json, cls);
            return vo;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}