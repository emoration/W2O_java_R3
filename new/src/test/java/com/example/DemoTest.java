package com.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DemoTest {
    Demo demo;
    @Before
    public void start() {
        System.out.println("begin");
        demo = new Demo();
    }
    @After
    public void close() {
        System.out.println("end");
        demo.close();
    }
    @Test
    public void queryCityToday() {
        System.out.println(demo.queryCityToday("fuzhou"));
    }

    @Test
    public void queryCityTomorrow() {
        System.out.println(demo.queryCityTomorrow("fuzhou"));
    }

    @Test
    public void queryCityDays() {
        System.out.println(demo.queryCityDays("fuzhou"));
    }

    @Test
    public void updateCity() {
        Demo demo = new Demo();
        if (demo.updateCity("xiamen")) {
            System.out.println("success");
        } else {
            System.out.println("fail, existed");
        }
        demo.close();
    }

    @Test
    public void addCity() {
        if (demo.addCity("xiamen")) {
            System.out.println("success");
        } else {
            System.out.println("fail, existed");
        }
    }

    @Test
    public void deleteCity() {
        demo.deleteCity("xiamen");
    }

    @Test
    public void isCityExist() {
        System.out.println(demo.isCityExist("shanghai"));
    }
}