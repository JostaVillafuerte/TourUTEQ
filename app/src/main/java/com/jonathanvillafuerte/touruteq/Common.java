package com.jonathanvillafuerte.touruteq;


import com.jonathanvillafuerte.touruteq.Remote.APIService;
import com.jonathanvillafuerte.touruteq.Remote.RetrofitClient;

public class Common {
    public static String currentToken = "Your_Token";

    private static String baseUrl="https://fcm.googleapis.com/";

    public static APIService GETFCMClient()
    {
        return RetrofitClient.getClient(baseUrl).create(APIService.class);
    }
}