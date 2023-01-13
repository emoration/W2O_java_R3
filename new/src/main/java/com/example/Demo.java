package com.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.CityMapper;
import com.example.dao.WeatherMapper;
import com.example.pojo.City;
import com.example.pojo.Weather;
import com.example.utils.MybatisUtils;
import com.example.utils.WeatherAPI;
import org.apache.ibatis.session.SqlSession;

public class Demo {
    private final SqlSession sqlSession;
    private final CityMapper cityMapper;
    private final WeatherMapper weatherMapper;

    public Demo() {
        sqlSession = MybatisUtils.getSqlSession();
        cityMapper = sqlSession.getMapper(CityMapper.class);
        weatherMapper = sqlSession.getMapper(WeatherMapper.class);
    }

    public void close() {
        sqlSession.close();
    }

    public JSONObject queryCityToday(String cityName) {
        City city = cityMapper.getCity("fuzhou");
//        System.out.println(city);
        int weather0_id = city.getWeather0_id();
        Weather weather = weatherMapper.getWeather(weather0_id);
//        System.out.println(weather);
        return weather.toJSONObjectWithoutId();
    }

    public JSONObject queryCityTomorrow(String cityName) {
        City city = cityMapper.getCity("fuzhou");
//        System.out.println(city);
        int weather1_id = city.getWeather1_id();
        Weather weather = weatherMapper.getWeather(weather1_id);
//        System.out.println(weather);
        return weather.toJSONObjectWithoutId();
    }

    public JSONArray queryCityDays(String cityName) {
        City city = cityMapper.getCity("fuzhou");
//        System.out.println(city);
        int weather0_id = city.getWeather0_id();
        int weather1_id = city.getWeather1_id();
        int weather2_id = city.getWeather2_id();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(weatherMapper.getWeather(weather0_id).toJSONObjectWithoutId());
        jsonArray.add(weatherMapper.getWeather(weather1_id).toJSONObjectWithoutId());
        jsonArray.add(weatherMapper.getWeather(weather2_id).toJSONObjectWithoutId());
        return jsonArray;
    }

    public void update(String cityName) {
        City city = cityMapper.getCity(cityName);
        String cityId = WeatherAPI.getCityId(cityName);
        JSONObject weatherJSONObject = WeatherAPI.getWeather(cityId);
        JSONArray weatherJSONObject_daily = (JSONArray) weatherJSONObject.get("daily");
        JSONObject weatherJSONObject_daily_0 = (JSONObject)weatherJSONObject_daily.get(0);
        weatherJSONObject_daily_0.put("id", city.getWeather0_id());
        weatherMapper.updateWeatherByMap(weatherJSONObject_daily_0);
        JSONObject weatherJSONObject_daily_1 = (JSONObject)weatherJSONObject_daily.get(1);
        weatherJSONObject_daily_1.put("id", city.getWeather1_id());
        weatherMapper.updateWeatherByMap(weatherJSONObject_daily_1);
        JSONObject weatherJSONObject_daily_2 = (JSONObject)weatherJSONObject_daily.get(2);
        weatherJSONObject_daily_2.put("id", city.getWeather2_id());
        weatherMapper.updateWeatherByMap(weatherJSONObject_daily_2);
        System.out.println(weatherJSONObject_daily_2);
    }

    public void updateAll() {
    }

    public void addCity(String cityName) {
    }

    public void deleteCity(String cityName) {
    }


}
