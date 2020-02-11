package com.zeathon.grainpoint_agent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewFarmer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_farmer);

        ListView listView = findViewById(R.id.listView);
        AutoCompleteTextView autoVw = findViewById(R.id.autoVw);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Farmer>> call = api.getFarmers();

        call.enqueue(new Callback<List<Farmer>>() {
            @Override
            public void onResponse(Call<List<Farmer>> call, Response<List<Farmer>> response) {

                List<Farmer> farmers = response.body();

                String[] farmerNames = new String[farmers.size()];
                for(int i=0; i<farmers.size(); i++){

                    farmerNames[i] = farmers.get(i).getFullName();
                    //farmerNames[i] = farmers.get(i).getPhone_number();
                }
                autoVw.setAdapter(
                        new ArrayAdapter<String>(
                                getApplicationContext(),
                                android.R.layout.simple_list_item_1,
                                farmerNames
                        )
                );

            }

            @Override
            public void onFailure(Call<List<Farmer>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);

            }
        });

    }
}
