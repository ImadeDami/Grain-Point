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

import static com.zeathon.grainpoint_agent.DataBHelper.SYNC_STATUS;
import static com.zeathon.grainpoint_agent.DataBHelper.TAG;

public class NetworkStateChecker2 extends BroadcastReceiver {
    private Context context;
    private DataBHelper db;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        db = new DataBHelper(context);

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
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL2)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL3)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL4)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL5)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL6)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL7))
                        );
                    } while (cursor.moveToNext());
                }
                Log.d(TAG, "add data: adding ");
            }
        }
    }

    private void userSignUp(final int id, final String fName, final String phoneNm, final String crpTyp, final String weig, final String moistCn, final String dat){
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
                        //updating the status in sqlite
                        db.updateNameStatus(id, NewRecord.SYNC_STATUS_OK);

                        //sending the broadcast to refresh the list
                        context.sendBroadcast(new Intent(NewRecord.DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Map<String, String> params = new HashMap<>();
                params.put("fname", fName);
                params.put("phN", phoneNm);
                params.put("crpTyp", crpTyp);
                params.put("weig", weig);
                params.put("moistCn", moistCn);
                params.put("dat", dat);
                return;

            }
        });

    }
}
