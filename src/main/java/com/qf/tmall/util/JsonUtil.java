package com.qf.tmall.util;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

public class JsonUtil {
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }
}
