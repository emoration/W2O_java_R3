package com.example.pojo;

import com.alibaba.fastjson.JSONObject;

public class Weather {
    private int id;
    private String fxDate;
    private double tempMax;
    private double tempMin;
    private String textDay;

    public Weather(String fxDate) {
    }

    public Weather(int id, String fxDate, double tempMax, double tempMin, String textDay) {
        this.id = id;
        this.fxDate = fxDate;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.textDay = textDay;
    }
    public Weather(JSONObject jsonObject) {
        this.fxDate  = (String) jsonObject.get("fxDate");
        this.tempMax = (double) jsonObject.get("tempMax");
        this.tempMin = (double) jsonObject.get("tempMin");
        this.textDay = (String) jsonObject.get("textDay");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFxDate() {
        return fxDate;
    }

    public void setFxDate(String fxDate) {
        this.fxDate = fxDate;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public String getTextDay() {
        return textDay;
    }

    public void setTextDay(String textDay) {
        this.textDay = textDay;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "fxDate='" + fxDate + '\'' +
                ", tempMax=" + tempMax +
                ", tempMin=" + tempMin +
                ", textDay='" + textDay + '\'' +
                '}';
    }
    public JSONObject toJSONObjectWithoutId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fxDate", fxDate);
        jsonObject.put("tempMax", tempMax);
        jsonObject.put("tempMin", tempMin);
        jsonObject.put("textDay", textDay);
        return  jsonObject;
    }
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("fxDate", fxDate);
        jsonObject.put("tempMax", tempMax);
        jsonObject.put("tempMin", tempMin);
        jsonObject.put("textDay", textDay);
        return  jsonObject;
    }
}
