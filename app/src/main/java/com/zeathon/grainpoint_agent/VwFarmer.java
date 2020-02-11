package com.zeathon.grainpoint_agent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VwFarmer extends AppCompatActivity {
    TextView viewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_vw);

        viewResult = findViewById(R.id.viewResult);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://namarkets.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Farmer>> call = api.getFarmers();

        call.enqueue(new Callback<List<Farmer>>() {
            @Override
            public void onResponse(Call<List<Farmer>> call, Response<List<Farmer>> response) {
                if(!response.isSuccessful()){
                    viewResult.setText("Code: " + response.code());
                    return;
                }
                List<Farmer> farmers = response.body();
                for (Farmer farmer: farmers){
                    String content = "";
                    content += "Name: " + farmer.getFullName() + "\n";
                    content += "Phone: " + farmer.getPhone_number() + "\n";
                    content += "State: " + farmer.getState() + "\n\n";

                    viewResult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Farmer>> call, Throwable t) {
                viewResult.setText(t.getMessage());
            }
        });
    }
}
