package com.example.kisileruygulamas;

public class ApiUtils {

    public static final String BASE_URL = "https://snowpiercer.store/";

    //bu base link ana link olmalı alt kolları interface içerisinde belirtilir.

    public static KisilerDaoInterface getKisilerDaoInterfeace() {
        return RetrofitClient.getClient(BASE_URL).create(KisilerDaoInterface.class);
    }
}


