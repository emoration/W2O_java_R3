package com.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.pojo.Weather;
import com.example.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherMapperTest {
    @Test
    public void getWeatherList() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        // way 1
        WeatherMapper weatherMapper = sqlSession.getMapper(WeatherMapper.class);
        List<Weather> weatherList = weatherMapper.getWeatherList();
        for (Weather weather : weatherList) {
            System.out.println(weather);
        }

        sqlSession.close();
    }
    @Test
    public void getWeather() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        WeatherMapper weatherMapper = sqlSession.getMapper(WeatherMapper.class);
        Weather weather = weatherMapper.getWeather(4);
        System.out.println(weather);

        sqlSession.close();
    }
    @Test
    public void addWeather() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        WeatherMapper weatherMapper = sqlSession.getMapper(WeatherMapper.class);
        Weather weather = new Weather(-1, "123", 2, 3, "sunny");
        int res = weatherMapper.addWeather(weather);
        System.out.println(weather);
        System.out.println(weather.getId());
        System.out.println(res);
        //重要
        sqlSession.commit();

        sqlSession.close();
    }
    @Test
    public void addWeatherByMap() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        WeatherMapper weatherMapper = sqlSession.getMapper(WeatherMapper.class);
        Map<String, Object> weatherMap = new HashMap<>();
        weatherMap.put("fxDate", "2022-01-03");
        weatherMap.put("tempMax", 38.5);
        weatherMap.put("tempMin", 20.1);
        weatherMap.put("textDay", "sunny");
        int res = weatherMapper.addWeatherByMap(weatherMap);
        System.out.println(res);
        //重要
        sqlSession.commit();

        sqlSession.close();
    }
    @Test
    public void updateWeather() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        WeatherMapper weatherMapper = sqlSession.getMapper(WeatherMapper.class);
        int res = weatherMapper.updateWeather(new Weather(4, "123", 2, 3, "sunny"));
        System.out.println(res);
        //重要
        sqlSession.commit();

        sqlSession.close();
    }
    @Test
    public void updateWeatherByMap() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        WeatherMapper weatherMapper = sqlSession.getMapper(WeatherMapper.class);
        int res = weatherMapper.updateWeatherByMap((new Weather(4, "123", 2, 3, "ooo")).toJSONObject());
        System.out.println((new Weather(4, "123", 2, 3, "ooo")).toJSONObject());
        JSONObject jsonObject = JSONObject.parseObject("{\"tempMax\":\"11\",\"textDay\":\"多云\",\"fxDate\":\"2023-01-16\",\"id\":4,\"tempMin\":\"7\"}");
        res = weatherMapper.updateWeatherByMap(jsonObject);
        System.out.println(jsonObject);
//        {"tempMax":2.0,"textDay":"ooo","fxDate":"123","id":4,"tempMin":3.0}
//        {"tempMax":"11","textDay":"多云","fxDate":"2023-01-16","id":4,"tempMin":"7"}
        System.out.println(res);
        //重要
        sqlSession.commit();

        sqlSession.close();
    }
    @Test
    public void deleteWeather() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        WeatherMapper weatherMapper = sqlSession.getMapper(WeatherMapper.class);
        int res = weatherMapper.deleteWeather(7);
        System.out.println(res);
        //重要
        sqlSession.commit();

        sqlSession.close();
    }
}