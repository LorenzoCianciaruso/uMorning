package com.example.umorning.model;

/**
 * Created by Lorenzo on 10/07/2014.
 */
public enum MetagsEnum {

    CLEAR("clear"),
    RAINY("rainy"),
    STORMY("stormy"),
    SNOWY("snowy"),
    PARTLYCLOUD("partly cloudy"),
    CLOUDY("cloudy"),
    HAILING("hailing"),
    HEAVYSEAS("heavy seas"),
    CALMSEAS("calm seas"),
    FOGGY("foggy"),
    SNOWFLURRIES("snow flurries"),
    WINDY("windy"),
    SUNNY("sunny"),
    CLEAR_MOON("clear_moon"),
    PARTLY_MOON("partly_moon"),
    PARTLY_SUNNY("partly_sunny");

    private String value;

    private MetagsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
