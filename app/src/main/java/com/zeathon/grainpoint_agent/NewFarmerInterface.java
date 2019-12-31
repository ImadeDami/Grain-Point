package com.zeathon.grainpoint_agent;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NewFarmerInterface {
    @FormUrlEncoded
    @POST("grainpoint/newfarmer.php")
    Call<ResponseBody> submitResponse(
            @Field("userID") String userID,
            @Field("fullName") String fullName,
            @Field("phone_number") String phone_number,
            @Field("idType") String idType,
            @Field("idNumber") String idNumber,
            @Field("bankName") String bankName,
            @Field("bankAccount") String bankAccount,
            @Field("bankVNum") String bankVNum,
            @Field("farmSize") String farmSize,
            //@Field("picture") String picture,
            @Field("image") String image,
            @Field("gpsLocation") String gpsLocation,
            @Field("gender") String gender,
            @Field("state") String state,
            @Field("lga") String lga,
            @Field("coopGroup") String coopGroup,
            @Field("roleCGroup") String roleCGroup,
            @Field("cropFarmed") String cropFarmed);

}
