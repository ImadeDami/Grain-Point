package com.zeathon.grainpoint_agent;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient3 {

    private static final String BASE_URL = "http://namarkets.com/";
    private static RetrofitClient3 mInstance;
    private Retrofit retrofit;

    private RetrofitClient3(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient3 getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient3();
        }
        return mInstance;
    }

    public NewFarmerInterface getNaSurvey() {
        return  retrofit.create(NewFarmerInterface.class);
    }
}