package com.example.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.Map;

public interface WeatherAPI {
    static String getCityId(String cityName) {
        String urlQueryCity = "https://geoapi.qweather.com/v2/city/lookup?key=2bb2aea597d94511b6d62c1f98ded765&location=";
        Map<String, Object> response = (Map<String, Object>) doGet(urlQueryCity, cityName);
        assert response != null;
        JSONObject map_Location_0 = (JSONObject) (((JSONArray) response.get("location")).get(0));
        return (String) (map_Location_0.get("id"));
    }

    static JSONObject getWeather(String cityId) {
        String urlQueryWeather = "https://devapi.qweather.com/v7/weather/3d?key=2bb2aea597d94511b6d62c1f98ded765&location=";
        JSONObject response = doGet(urlQueryWeather, cityId);
        assert response != null;
        JSONObject map = new JSONObject();
        map.put("daily", new JSONArray());
        JSONArray map_daily = (JSONArray) map.get("daily");
        for (int i = 0; i < 3; i++) {
            JSONObject response_daily = (JSONObject) (((JSONArray) response.get("daily")).get(i));
            map_daily.add(new JSONObject());
            JSONObject map_daily_i = (JSONObject) map_daily.get(i);
            map_daily_i.put("fxDate", (String) (response_daily.get("fxDate")));
            map_daily_i.put("tempMax", (String) (response_daily.get("tempMax")));
            map_daily_i.put("tempMin", (String) (response_daily.get("tempMin")));
            map_daily_i.put("textDay", (String) (response_daily.get("textDay")));
        }
        return map;
    }

    private static JSONObject doGet(String urlPart1, String urlPart2) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(urlPart1 + urlPart2);
        CloseableHttpResponse response = null;
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(5000))
                    .setConnectionRequestTimeout(Timeout.ofMilliseconds(5000))
                    .setRedirectsEnabled(true).build();
            httpGet.setConfig(requestConfig);
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
//                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                String str = EntityUtils.toString(responseEntity);
//                System.out.println("响应内容为:" + str);
                return JSONObject.parseObject(str);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
