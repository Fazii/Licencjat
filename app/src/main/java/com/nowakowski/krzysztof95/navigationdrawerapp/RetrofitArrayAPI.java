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

    @POST("/api/v1/joined_events")
    Call<List<ListItem>> getJoinedEvents(@Body ListItem listItem);

    @POST("/api/v1/my_events")
    Call<List<ListItem>> getMyEvents(@Body ListItem listItem);

    @POST("/api/v1/events_tag")
    Call<List<ListItem>> getEventsDetailsTags(@Body ListItem listItem);

    @POST("/api/v1/events/join")
    Call<ListItem> joinEvent(@Body ListItem listItem);

    @POST("/api/v1/events/who_join")
    Call<List<ListItem>> whoJoinEvent(@Body ListItem listItem);

    @HTTP(method = "DELETE", path = "/api/v1/events", hasBody = true)
    Call<ListItem> DeleteEvent(@Body ListItem listItem);

    @HTTP(method = "DELETE", path = "/api/v1/my_events", hasBody = true)
    Call<ListItem> UnsubscribeEvent(@Body ListItem listItem);


}