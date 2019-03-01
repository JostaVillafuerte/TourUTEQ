package com.jonathanvillafuerte.touruteq;


import com.jonathanvillafuerte.touruteq.Remote.APIService;
import com.jonathanvillafuerte.touruteq.Remote.RetrofitClient;

public class Common {
    public static String currentToken = "AAAAncZP1LA:APA91bFT99wDQrRzfB0Za-Ipcx8d1xe3B0Y29GlEFtlTU5soSunaSKftAJDXEhWKnTnT-4k-NPJBKOFOOV-rJQVCK8B5z34r6wx7IWw127XynKAEvhYzw4w0QEletKopJr2TgdWH-0Lg";

    private static String baseUrl="https://fcm.googleapis.com/";

    public static APIService GETFCMClient()
    {
        return RetrofitClient.getClient(baseUrl).create(APIService.class);
    }
}