package com.example.dao;

import com.example.pojo.City;

import java.util.List;
import java.util.Map;

public interface CityMapper {
    List<City> getCityList();

    City getCity(String cityName);
    int addCityByMap(Map<String,Object> map);
    int addCity(City city);
    int deleteCity(String cityName);
}
