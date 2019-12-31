package com.zeathon.grainpoint_agent;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewRecord extends AppCompatActivity {
    AutoCompleteTextView fullname, phoneNum;
    Spinner cropTyp;
    EditText weit, moistCnt, dt;
    Button submit;
    DataBHelper dataBHelper;
    DataBaseHelper dataBaseHelper;
    DatePickerDialog datePickerDialog;

    private ArrayList<SpinnerModel> goodModelArrayList;
    private ArrayList<String> playerNames = new ArrayList<String>();
    AutoCompleteTextView spinner;

    BroadcastReceiver broadcastReceiver;
    private static final int IMAGE_REQUEST = 777;
    private Bitmap bitmap;
    private static final String TAG = "ListDataActivity3";
    public static final int SYNC_STATUS_OK = 1;
    public static final int SYNC_STATUS_FAILED = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    public static final String UI_UPDATE_BROADCAST = "com.nasurvey.icaptech.uiupdatebroadcast";
    public static final String URL_SAVE_NAME = "http://namarkets.com/nasurvey/newrecord.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrecord);

        dataBHelper = new DataBHelper(this);
        dataBaseHelper = new DataBaseHelper(this);
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        dataBHelper.getData();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dataBHelper.getData();
            }
        };

        weit = findViewById(R.id.weit);
        moistCnt = findViewById(R.id.moistCnt);
        dt = findViewById(R.id.dt);
        fullname = findViewById(R.id.fullname);
        phoneNum = findViewById(R.id.phoneNum);
        cropTyp = findViewById(R.id.cropTyp);

        //spinner = findViewById(R.id.phonNum);
        fetchJSON();


        submit = findViewById(R.id.submit);

        //date = findViewById(R.id.date);
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(NewRecord.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        dt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        dataSubmit();
        populateFullname();
        populatePhone();

    }

    private void populateFullname() {
        //Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getFN();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fullname.setAdapter(dataAdapter);

    }

    private void populatePhone() {
        //Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getPN();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneNum.setAdapter(dataAdapter);

    }


    private void dataSubmit() {
        final String fName = fullname.getText().toString();
        final String phoneNm = phoneNum.getText().toString();
        final String crpTyp = cropTyp.getSelectedItem().toString();
        final String weig = weit.getText().toString();
        final String moistCn = moistCnt.getText().toString();
        final String dat = dt.getText().toString();

        if (fName.isEmpty()) {
            fullname.setError("some fields are empty");
            fullname.requestFocus();
            return;
        }
        if (phoneNm.isEmpty()) {
            phoneNum.setError("some fields are empty");
            phoneNum.requestFocus();
            return;
        }
        if (crpTyp.isEmpty()) {
            Toast.makeText(NewRecord.this, "some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (weig.isEmpty()) {
            weit.setError("some fields are empty");
            weit.requestFocus();
            return;
        }
        if (moistCn.isEmpty()) {
            moistCnt.setError("some fields are empty");
            moistCnt.requestFocus();
            return;
        }
        if (dat.isEmpty()) {
            dt.setError("some fields are empty");
            dt.requestFocus();
        }

        /** do user registration using api call **/
        Call<ResponseBody> call = RetrofitClient2
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
    }

    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                dataSubmit();

                break;
        }

    }

    private void fetchJSON() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SpinnerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        SpinnerInterface api = retrofit.create(SpinnerInterface.class);

        Call<String> call = api.getJSONString();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                    Log.i("Responsestring", response.body().toString());
                    //Toast.makeText()
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

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(NewRecord.this, android.R.layout.simple_spinner_dropdown_item , (List<String>) phoneNum);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

