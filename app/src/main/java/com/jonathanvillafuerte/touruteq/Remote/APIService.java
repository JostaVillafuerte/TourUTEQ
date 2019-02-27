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
            "Authorization:key=Your Key"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
