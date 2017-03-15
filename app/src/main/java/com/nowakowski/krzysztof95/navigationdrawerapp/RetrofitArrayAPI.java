package com.nowakowski.krzysztof95.navigationdrawerapp;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

interface RetrofitArrayAPI {

    @GET("/api/v1/comments")
    Call<List<ListItem>> getBookDetails();

    @POST("/api/v1/comments")
    Call<ListItem> sendComment(@Body ListItem listItem);

    @HTTP(method = "DELETE", path = "/api/v1/comments", hasBody = true)
    Call<ListItem> DeleteComment(@Body ListItem listItem);
}