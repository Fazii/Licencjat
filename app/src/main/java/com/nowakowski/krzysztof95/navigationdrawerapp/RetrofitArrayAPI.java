package com.nowakowski.krzysztof95.navigationdrawerapp;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

interface RetrofitArrayAPI {

    @GET("/api/v1/events")
    Call<List<ListItem>> getEventDetails();

    @POST("/api/v1/events")
    Call<ListItem> sendComment(@Body ListItem listItem);

    @HTTP(method = "DELETE", path = "/api/v1/events", hasBody = true)
    Call<ListItem> DeleteComment(@Body ListItem listItem);
}