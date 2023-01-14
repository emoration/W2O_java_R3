package com.example.dao;

import com.example.pojo.City;
import com.example.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityMapperTest {
    @Test
    public void getCityList() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        CityMapper cityMapper = sqlSession.getMapper(CityMapper.class);
        List<City> cityList = cityMapper.getCityList();
        for (City city : cityList) {
            System.out.println(city);
        }
        sqlSession.close();
    }

    @Test
    public void getCity() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        CityMapper cityMapper = sqlSession.getMapper(CityMapper.class);
        City city = cityMapper.getCity("fuzhou");
        System.out.println(city);
        sqlSession.close();
    }

    @Test
    public void addCity() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        CityMapper cityMapper = sqlSession.getMapper(CityMapper.class);
        Map<String, Object> map = new HashMap<>();
//        cityName, cityId, weather0_id, weather1_id, weather2_id
        map.put("cityName", "fuzhou");
        map.put("cityId", "1");
        map.put("weather0_id", "2");
        map.put("weather1_id", "3");
        map.put("weather2_id", "4");
        int res = cityMapper.addCityByMap(map);
        System.out.println(res);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void deleteCity() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        CityMapper cityMapper = sqlSession.getMapper(CityMapper.class);
        int res = cityMapper.deleteCity("fuzhou");
        System.out.println(res);
        sqlSession.commit();
        sqlSession.close();
    }
}