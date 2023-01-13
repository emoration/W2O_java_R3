package com.example.dao;

import com.example.pojo.Weather;

import java.util.List;
import java.util.Map;

public interface WeatherMapper {
    List<Weather> getWeatherList();
    Weather getWeather(int id);
    int addWeather(Weather weather);
    int addWeatherByMap(Map<String,Object> map);
    int updateWeather(Weather weather);
    int updateWeatherByMap(Map<String,Object> map);
    int deleteWeather(int id);
}
