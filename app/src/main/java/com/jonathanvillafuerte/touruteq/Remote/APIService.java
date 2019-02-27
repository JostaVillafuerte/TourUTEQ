package com.jonathanvillafuerte.touruteq.Remote;






import com.jonathanvillafuerte.touruteq.Model.MyResponse;
import com.jonathanvillafuerte.touruteq.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAncZP1LA:APA91bFT99wDQrRzfB0Za-Ipcx8d1xe3B0Y29GlEFtlTU5soSunaSKftAJDXEhWKnTnT-4k-NPJBKOFOOV-rJQVCK8B5z34r6wx7IWw127XynKAEvhYzw4w0QEletKopJr2TgdWH-0Lg"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
