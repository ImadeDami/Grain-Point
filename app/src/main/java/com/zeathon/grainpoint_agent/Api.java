package com.zeathon.grainpoint_agent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "http://namarkets.com/";

    @GET("grainpoint/viewfarmer.php")
    Call<List<Farmer>> getFarmers();
}
