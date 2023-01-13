package com.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class DemoTest {

    @Test
    public void queryCityToday() {
        Demo demo = new Demo();
        System.out.println(demo.queryCityToday("fuzhou"));
        demo.close();
    }

    @Test
    public void queryCityTomorrow() {
        Demo demo = new Demo();
        System.out.println(demo.queryCityTomorrow("fuzhou"));
        demo.close();
    }

    @Test
    public void queryCityDays() {
        Demo demo = new Demo();
        System.out.println(demo.queryCityDays("fuzhou"));
        demo.close();
    }

    @Test
    public void update() {
        Demo demo = new Demo();
        demo.update("fuzhou");
        demo.close();
    }

    @Test
    public void updateAll() {
    }

    @Test
    public void addCity() {
    }

    @Test
    public void deleteCity() {
    }
}