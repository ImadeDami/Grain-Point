package com.zeathon.grainpoint_agent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FetchMSQL extends AppCompatActivity {
    TextView vwAll;
    Spinner spinRec;
    DataBaseHelper dataBaseHelper;
    List<SpinnerModel> recList;
    private static final String URL_PRODUCTS = "http://namarkets.com/grainpoint/viewphone.php";

    ArrayList<SpinnerModel> goodModelArrayList;
    ArrayList<String> playerNames = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetchmsql);

        spinRec = findViewById(R.id.spinRec);
        fetchJSON();

    }

    public void fetchJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SpinnerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        SpinnerInterface api = retrofit.create(SpinnerInterface.class);

        Call<String> call = api.getJSONString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        spinJSON(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void spinJSON(String response){

        try {

            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){

                goodModelArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    SpinnerModel spinnerModel = new SpinnerModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    spinnerModel.setfName(dataobj.getString("fullName"));
                    spinnerModel.setPhoneNm(dataobj.getString("phone_number"));
                    spinnerModel.setCrpTyp(dataobj.getString("cropType"));

                    goodModelArrayList.add(spinnerModel);

                }

                for (int i = 0; i < goodModelArrayList.size(); i++){
                    playerNames.add(goodModelArrayList.get(i).getPhoneNm().toString());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(FetchMSQL.this, android.R.layout.simple_spinner_item, playerNames);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinRec.setAdapter(spinnerArrayAdapter);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    }

    /** vwAll = findViewById(R.id.vwAll);
        spinRec = findViewById(R.id.spinRec);
        recList = new ArrayList<>(URL_PRODUCTS);

        SpinnerModel spinnerModel = new SpinnerModel("fName", "phoneNm", "crpTyp");
        recList.add(spinnerModel);

        ArrayAdapter<SpinnerModel> adapter = new ArrayAdapter<SpinnerModel>(this, android.R.layout.simple_spinner_item, recList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRec.setAdapter(adapter);

        dataBaseHelper = new DataBaseHelper(this);

        spinRec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerModel user = (SpinnerModel) adapterView.getSelectedItem();
                displayUser(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // loadData();
    }

    private void displayUser(SpinnerModel user) {
        String fName = user.getfName();
        String phoneNm = user.getPhoneNm();
        String crpTyp = user.getCrpTyp();

        String userData = "Name: " + fName + "\nPhone: " + phoneNm + "\ncrpTyp: " + crpTyp;
        vwAll.setText(userData); **/

   /* private void loadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject product = array.getJSONObject(i);

                        //adding the product to product list
                        productList.add(new SpinnerModel(
                                product.getString("fullName"),
                                product.getString("phone_number"),
                                product.getString("state")
                                ));
            }
        } catch (JSONException e) {
                    e.printStackTrace();

                });
    }
        }); */


       /* Call<ResponseBody> call = RetrofitClient2
                .getInstance()
                .getNaSurvey()
                .submitResponse(fName, phoneNm, crpTyp, weig, moistCn, dat);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    if (!obj.getBoolean("error")) {
                        //if there is a success
                        //storing the name to sqlite with status synced
                        dataBHelper.addData(fName, phoneNm, crpTyp, weig, moistCn, dat, SYNC_STATUS_OK);
                    } else {
                        //if there is some error
                        //saving the name to sqlite with status unsynced
                        dataBHelper.addData(fName, phoneNm, crpTyp, weig, moistCn, dat, SYNC_STATUS_FAILED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    // String s = response.body().toString();
                    Toast.makeText(NewRecord.this, "Submitted...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewRecord.this, ProfileActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dataBHelper.addData(fName, phoneNm, crpTyp, weig, moistCn, dat, SYNC_STATUS_FAILED);
                //Toast.makeText(SurveyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(NewRecord.this, "data has been saved on phone and will submitted once there is internet connection", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewRecord.this, MainActivity.class);
                startActivity(intent);
            }
        });
    } */




