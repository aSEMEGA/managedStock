package com.example.managedStock.security.config;

public class SecurityConstants {
    // 1 day expressed in milliseconds 24*60*60*1000
    public static final long EXPIRATION_TIME = 60*60*1000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;
    public static final String JWT_SECRET_KEY = "504E635266556A596E3272357538792F413F4428492C7B6250645367566B5970";


}

