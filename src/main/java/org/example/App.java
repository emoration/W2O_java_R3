package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class App {
    public static void main(String[] args){
        Demo test = new Demo();
        test.initial();
        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            System.out.println("input: ['0': quit, 'q': query weather, 'a': add city, 'u' to update]");
            String input = scanner.next();
            switch (input) {
                case "0":
                    break label;
                case "q": {
                    System.out.println("input the cityName to query, or input \"all\" to query all cities");
                    String cityName = scanner.next().toLowerCase();
                    System.out.println("input the day to query, ['0': today, '1': tomorrow, '2': three days]");
                    String days = scanner.next();
                    if (!cityName.equals("all")) {
                        switch (days) {
                            case "0" -> test.queryCityToday(cityName);
                            case "1" -> test.queryCityTomorrow(cityName);
                            case "2" -> test.queryCityDays(cityName);
                        }
                    } else {
                        switch (days) {
                            case "0" -> test.queryTodayCities();
                            case "1" -> test.queryTomorrowCities();
                            case "2" -> test.queryDaysCities();
                        }
                    }
                    break;
                }
                case "a": {
                    System.out.println("input the cityName to add");
                    String cityName = scanner.next().toLowerCase();
                    test.addCity(cityName);
                    break;
                }
                case "u": {
                    System.out.println("input the cityName to query, or input \"all\" to query all cities");
                    String cityName = scanner.next().toLowerCase();
                    if (!cityName.equals("all")) {
                        test.update(cityName);
                    } else {
                        test.updateAll();
                    }
                    break;
                }
            }
        }
        test.close();
    }
}

final class Weather {
    LocalDate fxDate = null;
    Float tempMax = null;
    Float tempMin = null;
    String textDay = null;
    public Weather(LocalDate fxDate, Float tempMax, Float tempMin, String textDay) {
        this.fxDate = fxDate;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.textDay = textDay;
    }
    public Weather() {

    }
    @Override
    public String toString() {
        return "date: " + fxDate + " " +
                "tempMax: " + tempMax + " " +
                "tempMin: " + tempMin + " " +
                "textDay: " + textDay;
    }
}
final class CityWeather {
    String cityName = null;
    ArrayList<Weather> weathers = new ArrayList<>();
    public CityWeather() {

    }
    public CityWeather(String cityName) {
        this.cityName = cityName;
    }
}
final class Demo {
    public void queryCityToday(String cityName) {
        printCityWeathers(query((new ArrayList<>(Collections.singletonList(cityName))),new ArrayList<>(List.of(0))));
    }
    public void queryCityTomorrow(String cityName) {
        printCityWeathers(query((new ArrayList<>(Collections.singletonList(cityName))),new ArrayList<>(List.of(1))));
    }
    public void queryCityDays(String cityName) {
        printCityWeathers(query((new ArrayList<>(Collections.singletonList(cityName))),new ArrayList<>(List.of(0,1,2))));
    }
    public void queryTodayCities() {
        cityNames.forEach(this::queryCityToday);
    }
    public void queryTomorrowCities() {
        cityNames.forEach(this::queryCityTomorrow);
    }
    public void queryDaysCities() {
        cityNames.forEach(this::queryCityDays);
    }
    public void update(String cityName) {
        if (queryIsCityExist(cityName)) {
            ArrayList<Integer> weatherIds = queryCityToIds(cityName);
            CityWeather cityWeather = APIGets(cityName);
            WeatherReplace(weatherIds.get(0), cityWeather.weathers.get(0));
            WeatherReplace(weatherIds.get(1), cityWeather.weathers.get(1));
            WeatherReplace(weatherIds.get(2), cityWeather.weathers.get(2));
        } else {
            CityWeather cityWeather = APIGets(cityName);
            CityInsert(cityName, WeatherInsert(cityWeather.weathers.get(0)), WeatherInsert(cityWeather.weathers.get(1)), WeatherInsert(cityWeather.weathers.get(2)));
        }
    }
    public void updateAll() {
        cityNames.forEach(this::update);
    }
    public void addCity(String cityName) {
        if (cityNames.contains(cityName)) {
            System.out.println(cityName + " is already existed");
            return ;
        }
        cityNames.add(cityName);
        update(cityName);
    }

    private void getConnection() {
        try {
            //注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");//MySQL5以后可直接省略
            //获取数据库连接
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/MySql?characterEncoding=UTF-8", "root", "mysql");
            //该类就在 mysql-connector-java-5.0.8-bin.jar中,如果忘记了第一个步骤的导包，就会抛出ClassNotFoundException
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    private void makePreparation() {
        try {
            statement = connection.createStatement();
            //执行sql语句并返回结果
            String sql = """
                    CREATE TABLE IF NOT EXISTS cities (
                      id INT NOT NULL auto_increment,
                      cityName VARCHAR(40) NOT NULL,
                      weather0_id INT NOT NULL,
                      weather1_id INT NOT NULL,
                      weather2_id INT NOT NULL,
                      PRIMARY KEY ( id )
                    )""";
            statement.execute(sql);
            sql = """
                    CREATE TABLE IF NOT EXISTS weathers (
                      id INT NOT NULL AUTO_INCREMENT,
                      fxDate DATE NOT NULL,
                      tempMax REAL NOT NULL,
                      tempMin REAL NOT NULL,
                      textDay VARCHAR(100) NOT NULL,
                      PRIMARY KEY ( id )
                    );""";
            statement.execute(sql);
            // *select * from cities where cityName = ?
            // *select * from weathers where id = ?
            // *SELECT 1 FROM cities WHERE cityName = ? LIMIT 1
            //   IF ……
            //   *INSERT INTO weathers (id, fxDate, tempMax, tempMin, textDay) VALUES (1, 1, '小明', 'F', 99); *3
            //   stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            //   int rValue = -1;
            //   ResultSet results = stmt.getGeneratedKeys();
            //   if (results.next()) {
            //     rValue = results.getInt(1);
            //   }
            //   *INSERT INTO cities (id, cityName, weather0_id, weather1_id, weather2_id) VALUES (1, 1, '小明', 'F', 99);
            // *REPLACE INTO weathers (id, fxDate, tempMax, tempMin, textDay) VALUES (1, 1, '小明', 'F', 99);
            //
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePart();
        }
    }
    public void initial() {
        getConnection();
        makePreparation();
        addCity("beijing");
        addCity("shanghai");
        addCity("fuzhou");
    }
    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void closePart() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private final ArrayList<String> cityNames = new ArrayList<>();
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private static CityWeather APIGets(String cityName) {
        return API.getWeather(API.getCityId(cityName));
    }

    private boolean queryIsCityExist(String cityName) {
        boolean isCityExist = false;
        try {
            preparedStatement = connection.prepareStatement("SELECT 1 FROM cities WHERE cityName = ? LIMIT 1;");
            preparedStatement.setString(1, cityName);
            resultSet = preparedStatement.executeQuery();
            isCityExist = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePart();
        }
        return isCityExist;
    }
    private ArrayList<Integer> queryCityToIds(String cityName) {
        ArrayList<Integer> weatherIds = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM cities WHERE cityName = ?");
            preparedStatement.setString(1, cityName);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                weatherIds.add(resultSet.getInt("weather0_id"));
                weatherIds.add(resultSet.getInt("weather1_id"));
                weatherIds.add(resultSet.getInt("weather2_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePart();
        }
        return weatherIds;
    }
    private Weather queryIdToWeather(Integer weather_id) {
        Weather weather = new Weather();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM weathers WHERE id = ?");
            preparedStatement.setInt(1, weather_id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                weather.fxDate = LocalDate.parse(resultSet.getString("fxDate"));
                weather.tempMax = resultSet.getFloat("tempMax");
                weather.tempMin = resultSet.getFloat("tempMin");
                weather.textDay = resultSet.getString("textDay");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePart();
        }
        return weather;
    }
    private void CityInsert(String cityName, Integer weather0_id, Integer weather1_id, Integer weather2_id) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO cities (cityName, weather0_id, weather1_id, weather2_id) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, cityName);
            preparedStatement.setInt(2, weather0_id);
            preparedStatement.setInt(3, weather1_id);
            preparedStatement.setInt(4, weather2_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePart();
        }
    }
    private Integer WeatherInsert(Weather weather) {
        Integer resultKey = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO weathers (fxDate, tempMax, tempMin, textDay) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, weather.fxDate.toString());
            preparedStatement.setFloat(2, weather.tempMax);
            preparedStatement.setFloat(3, weather.tempMin);
            preparedStatement.setString(4, weather.textDay);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                resultKey = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePart();
        }
        return resultKey;
    }
    private void WeatherReplace(Integer weather_id, Weather weather) {
        try {
            preparedStatement = connection.prepareStatement("REPLACE INTO weathers (id, fxDate, tempMax, tempMin, textDay) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, weather_id);
            preparedStatement.setString(2, weather.fxDate.toString());
            preparedStatement.setFloat(3, weather.tempMax);
            preparedStatement.setFloat(4, weather.tempMin);
            preparedStatement.setString(5, weather.textDay);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePart();
        }
    }

    private ArrayList<CityWeather> query(ArrayList<String> cityNames, ArrayList<Integer> queries) {
        ArrayList<CityWeather> cityWeathers = new ArrayList<>();
        cityNames.forEach(cityName -> cityWeathers.add(new CityWeather(cityName)));
        cityWeathers.forEach(cityWeather -> {
            ArrayList<Integer> weatherIds = queryCityToIds(cityWeather.cityName);
            queries.forEach(q -> cityWeather.weathers.add(queryIdToWeather(weatherIds.get(q))));
        });
        return cityWeathers;
    }
    private void printCityWeathers(ArrayList<CityWeather> cityWeathers) {
        cityWeathers.forEach(cityWeather -> {
            System.out.printf("city: %s\n", cityWeather.cityName);
            cityWeather.weathers.forEach(System.out::println);
        });
    }
}
final class API {
    //    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    public static String getCityId(String cityName) {
        String urlQueryCity = "https://geoapi.qweather.com/v2/city/lookup?key=2bb2aea597d94511b6d62c1f98ded765&location=";
        Map<String, Object> response = (Map<String, Object>)doGet(urlQueryCity, cityName);
        assert response != null;
        JSONObject map_Location_0 = (JSONObject)(((JSONArray)response.get("location")).get(0));
        return (String)(map_Location_0.get("id"));
    }
    public static CityWeather getWeather(String citiId) {
        String urlQueryWeather = "https://devapi.qweather.com/v7/weather/3d?key=2bb2aea597d94511b6d62c1f98ded765&location=";
        Map<String, Object> response = (Map<String, Object>)doGet(urlQueryWeather, citiId);
        assert response != null;
        CityWeather cityWeather = new CityWeather();
        for (int i = 0; i < 3; i++) {
            JSONObject response_daily = (JSONObject)(((JSONArray)response.get("daily")).get(i));
            cityWeather.weathers.add(new Weather(
                    LocalDate.parse((String)(response_daily.get("fxDate"))),
                    Float.parseFloat((String)(response_daily.get("tempMax"))),
                    Float.parseFloat((String)(response_daily.get("tempMin"))),
                    (String)(response_daily.get("textDay"))
                    ));
        }
        return cityWeather;
    }
    public static JSONObject doGet(String urlPart1, String urlPart2) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(urlPart1 + urlPart2);
        CloseableHttpResponse response = null;
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(5000))
                    .setConnectionRequestTimeout(Timeout.ofMilliseconds(5000))
                    .setRedirectsEnabled(true).build();
            httpGet.setConfig(requestConfig);
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
//                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                String str = EntityUtils.toString(responseEntity);
//                System.out.println("响应内容为:" + str);
                return JSONObject.parseObject(str);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}