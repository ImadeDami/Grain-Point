package com.zeathon.grainpoint_agent;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zeathon.grainpoint_agent.DataBHelper.TAG;

public class NetworkStateChecker extends BroadcastReceiver {
    private Context context;
    private DataBaseHelper db;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        db = new DataBaseHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        userSignUp(
                                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COL1)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL18)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL2)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL3)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL4)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL5)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL6)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL7)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL8)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL9)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL10)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL11)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL12)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL13)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL14)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL15)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL16)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL17))
                        );
                    } while (cursor.moveToNext());
                }
                Log.d(TAG, "add data: adding ");
            }
        }
    }

    private void userSignUp(final int id, final String userID, final String fName, final String phoneNm, final String idTy, final String idNumb, final String bankNam, final String bankAc, final String bankVN, final String farmSz, final String Image, final String gpsLoc, final String gend, final String sta, final String localG, final String grpCoop, final String grpRol, final String crop){
        /** do user registration using api call **/
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getNaSurvey()
                .submitResponse(userID, fName, phoneNm, idTy, idNumb, bankNam, bankAc, bankVN, farmSz, Image, gpsLoc, gend, sta, localG, grpCoop, grpRol, crop);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    if (!obj.getBoolean("error")) {
                        //updating the status in sqlite
                        db.updateNameStatus(id, NewFarmer.SYNC_STATUS_OK);

                        //sending the broadcast to refresh the list
                        context.sendBroadcast(new Intent(NewFarmer.DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userID);
                params.put("fname", fName);
                params.put("phN", phoneNm);
                params.put("idT", idTy);
                params.put("idNm", idNumb);
                params.put("bkNm", bankNam);
                params.put("bkAc", bankAc);
                params.put("bkVn", bankVN);
                params.put("fmSz", farmSz);
                params.put("pic", Image);
                params.put("gpsLc", gpsLoc);
                params.put("gen", gend);
                params.put("sta", sta);
                params.put("lga", localG);
                params.put("grpC", grpCoop);
                params.put("grpR", grpRol);
                params.put("cp", crop);

                return;

            }
        });

    }
  }
