package com.example.crcle;

import com.example.crcle.Notification_model.Fcm_Reqquest;
import com.example.crcle.Notification_model.Myresponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Interface {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAUwPj_9k:APA91bGESt7ZQNh9A_Zb5sbqYxYudB33HO4a0UdTyY12W72hlstoJj8pvj_eLvVvEdrhV8bXClRiPRPpqH3YYVO6z59mgL-kZ-17pFl-7eSgVto9JzR9LzynomXWehPBwMzMr6S4nS0o"
            }
    )
    @POST("fcm/send")
    Call<Myresponse> Fcm_request(@Body Fcm_Reqquest notification);
}
