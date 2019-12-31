package com.zeathon.grainpoint_agent;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NewRecordInterface {
    @FormUrlEncoded
    @POST("grainpoint/newrecord.php")
    Call<ResponseBody> submitResponse(
            @Field("fullName") String fullName,
            @Field("phone_number") String phone_number,
            @Field("cropType") String cropType,
            @Field("weight") String weight,
            @Field("moistureCont") String moistureCont,
            @Field("entryDate") String entryDate);

}
