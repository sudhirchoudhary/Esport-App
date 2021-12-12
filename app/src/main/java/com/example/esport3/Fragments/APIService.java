package com.example.esport3.Fragments;

import com.example.esport3.Notifications.MyResponse;
import com.example.esport3.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAHp5gg6I:APA91bFUeR7MW9ajnHrmpxU3Z7jNF1e48j-NIZJZxWg3yxDMCyfJ20t1e9pN__plLU8Rrx2Nuxa3F1QLZVcMBLRcmfo3XGpjtI-QfH6viHEZCWSBe2xrVLT374OH4CwGloJmUBCghgw4"
            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
