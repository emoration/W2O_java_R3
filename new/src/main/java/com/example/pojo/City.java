package com.example.pojo;

public class City {
    private int id;
    private String cityName;
    private String cityId;
    private int weather0_id;
    private int weather1_id;
    private int weather2_id;

    public City() {
    }

    public City(int id, String cityName, String cityId, int weather0_id, int weather1_id, int weather2_id) {
        this.id = id;
        this.cityName = cityName;
        this.cityId = cityId;
        this.weather0_id = weather0_id;
        this.weather1_id = weather1_id;
        this.weather2_id = weather2_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public int getWeather0_id() {
        return weather0_id;
    }

    public void setWeather0_id(int weather0_id) {
        this.weather0_id = weather0_id;
    }

    public int getWeather1_id() {
        return weather1_id;
    }

    public void setWeather1_id(int weather1_id) {
        this.weather1_id = weather1_id;
    }

    public int getWeather2_id() {
        return weather2_id;
    }

    public void setWeather2_id(int weather2_id) {
        this.weather2_id = weather2_id;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", weather0_id=" + weather0_id +
                ", weather1_id=" + weather1_id +
                ", weather2_id=" + weather2_id +
                '}';
    }
}
