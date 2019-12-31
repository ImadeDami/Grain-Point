package com.zeathon.grainpoint_agent;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SpinnerInterface {
    String JSONURL = "http://www.namarkets.com/";
    @GET("grainpoint/viewphone.php")
    Call<String> getJSONString();
}
