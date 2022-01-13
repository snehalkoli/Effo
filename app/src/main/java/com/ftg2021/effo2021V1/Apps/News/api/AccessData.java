package com.ftg2021.effo2021V1.Apps.News.api;

public class AccessData {
    private final static String baseUrl="https://api.effoapp.com";
    private final static String port="5000";
    private final static String apiUrl="https://api.effoapp.com/news/";

    public static int badgeCount=0;

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getPort() {
        return port;
    }

    public static String getApiUrl() {
        return apiUrl;
    }



}
