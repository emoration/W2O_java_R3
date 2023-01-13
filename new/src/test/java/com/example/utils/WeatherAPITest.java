package com.example.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeatherAPITest {

    @Test
    public void getCityId() {
        System.out.println(WeatherAPI.getCityId("fuzhou"));
    }

    @Test
    public void getWeather() {
        System.out.println(WeatherAPI.getWeather("101230101"));
    }
}