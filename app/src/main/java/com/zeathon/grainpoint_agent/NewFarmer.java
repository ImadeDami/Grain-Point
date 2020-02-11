package com.zeathon.grainpoint_agent;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFarmer extends AppCompatActivity implements View.OnClickListener, LocationListener {
    AutoCompleteTextView stat, lg, grpCop, grpRole, bank;
    EditText fullnam, phoneNum, idTyp, idNum, account, bvn, fmSz, pic;
    Spinner gendr, crps;
    Button submit, btnChoose;
    TextView textViewId, myLocation, gpsAddr;
    ImageView Img;
    LocationManager locationManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    BroadcastReceiver broadcastReceiver;
    DataBaseHelper dataBaseHelper;
    private static final int IMAGE_REQUEST = 777;
    private Bitmap bitmap;
    private static final String TAG = "ListDataActivity3";
    public static final int SYNC_STATUS_OK = 1;
    public static final int SYNC_STATUS_FAILED = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    public static final String UI_UPDATE_BROADCAST = "com.nasurvey.icaptech.uiupdatebroadcast";
    public static final String URL_SAVE_NAME = "http://namarkets.com/nasurvey/newfarmer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newfarmer);

        checkLocationPermission();

        Img = findViewById(R.id.imageView);

        // call user id
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        textViewId = findViewById(R.id.textViewId);
        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();
        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));

        dataBaseHelper = new DataBaseHelper(this);
        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        dataBaseHelper.getData();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dataBaseHelper.getData();
            }
        };

        fullnam = findViewById(R.id.fullnam);
        phoneNum = findViewById(R.id.phoneNum);
        idTyp = findViewById(R.id.idTyp);
        idNum = findViewById(R.id.idNum);
        bank = findViewById(R.id.bank);
        account = findViewById(R.id.account);
        bvn = findViewById(R.id.bvn);
        fmSz = findViewById(R.id.fmSz);
        //pic = findViewById(R.id.pic);
        myLocation = findViewById(R.id.gps);
        gpsAddr = findViewById(R.id.gpsAddr);

        gendr = findViewById(R.id.gendr);

        stat = findViewById(R.id.stat);
        lg = findViewById(R.id.lg);
        grpCop = findViewById(R.id.grpCop);
        grpRole = findViewById(R.id.grpRole);
        crps = findViewById(R.id.crps);

        //submit = findViewById(R.id.submit);
        findViewById(R.id.submitBtn).setOnClickListener(this);

      //  locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        //function for longitude and latitude coordinates:
        //onLocationChanged(location);



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        //function for longitude and latitude coordinates:
        onLocationChanged(location);


        btnChoose = findViewById(R.id.choseImg);
        if(null == Img) {
            Log.e("Error", "Ouh! there is no there is no child view with R.id.imageView ID within my parent view View.");
        }
        btnChoose.setOnClickListener(this);

        populateState();
        populateLG();
        populateGrpCop();
        populateGrpRole();
       // populateCrps();
        populateBank();
        dataSubmit();

    }
    private void populateState() {
        //Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getAllState();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stat.setAdapter(dataAdapter);

    }
    private void populateLG() {
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getLG();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lg.setAdapter(dataAdapter);

    }
    private void populateGrpCop() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getGrpCop();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grpCop.setAdapter(dataAdapter);

    }

    private void populateGrpRole() {
       // Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getGrpRole();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grpRole.setAdapter(dataAdapter);

    }

    private void populateBank() {
        //Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getBank();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank.setAdapter(dataAdapter);

    }

   /** private void populateCrps() {
       // Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getCrps();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crps.setAdapter(dataAdapter);

    } **/

    private void dataSubmit() {
        final String userID = textViewId.getText().toString();
        final String fName = fullnam.getText().toString();
        final String phoneNm = phoneNum.getText().toString();
        final String idTy = idTyp.getText().toString();
        final String idNumb = idNum.getText().toString();
        final String bankNam = bank.getText().toString();
        final String bankAc = account.getText().toString();
        final String bankVN = bvn.getText().toString();
        final String farmSz = fmSz.getText().toString();
        //final String pict = pic.getText().toString();
        final String gpsLoc = myLocation.getText().toString();
        final String gend = gendr.getSelectedItem().toString();
        final String sta = stat.getText().toString();
        final String localG = lg.getText().toString();
        final String grpCoop = grpCop.getText().toString();
        final String grpRol = grpRole.getText().toString();
        final String crop = crps.getSelectedItem().toString();

        final String Image = imageToString();

        if (fName.isEmpty()) {
            fullnam.setError("some fields are empty");
            fullnam.requestFocus();
            return;
        }
        if (phoneNm.isEmpty()) {
            phoneNum.setError("some fields are empty");
            phoneNum.requestFocus();
            return;
        }
        if (idTy.isEmpty()) {
            idTyp.setError("some fields are empty");
            idTyp.requestFocus();
            return;
        }
        if (idNumb.isEmpty()) {
            idNum.setError("some fields are empty");
            idNum.requestFocus();
            return;
        }
        if (bankNam.isEmpty()) {
            bank.setError("some fields are empty");
            bank.requestFocus();
            return;
        }
        if (bankAc.isEmpty()) {
            account.setError("some fields are empty");
            account.requestFocus();
            return;
        }
        if (bankVN.isEmpty()) {
            bvn.setError("some fields are empty");
            bvn.requestFocus();
            return;
        }
        if (farmSz.isEmpty()) {
            fmSz.setError("some fields are empty");
            fmSz.requestFocus();
            return;
        }
        /**if(pict.isEmpty()){
         pic.setError("some fields are empty");
         pic.requestFocus();
         return;
         }
        if (gpsLoc.isEmpty()) {
            myLocation.setError("some fields are empty");
            myLocation.requestFocus();
            return;
        }**/
        if (gend.isEmpty()) {
            Toast.makeText(NewFarmer.this, "some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sta.isEmpty()) {
            stat.setError("some fields are empty");
            stat.requestFocus();
            return;
        }
        if (localG.isEmpty()) {
            lg.setError("some fields are empty");
            lg.requestFocus();
            return;
        }
        if (grpCoop.isEmpty()) {
            grpCop.setError("some fields are empty");
            grpCop.requestFocus();
            return;
        }
        if (grpRol.isEmpty()) {
            grpRole.setError("some fields are empty");
            grpRole.requestFocus();
            return;
        }
       /** if (crop.isEmpty()) {
            crps.setError("some fields are empty");
            crps.requestFocus();
            return;
        }**


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
                        //if there is a success
                        //storing the name to sqlite with status synced
                        dataBaseHelper.addData(userID, fName, phoneNm, idTy, idNumb, bankNam, bankAc, bankVN, farmSz, Image, gpsLoc, gend, sta, localG, grpCoop, grpRol, crop, SYNC_STATUS_OK);
                    } else {
                        //if there is some error
                        //saving the name to sqlite with status unsynced
                        dataBaseHelper.addData(userID, fName, phoneNm, idTy, idNumb, bankNam, bankAc, bankVN, farmSz, Image, gpsLoc, gend, sta, localG, grpCoop, grpRol, crop, SYNC_STATUS_FAILED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    // String s = response.body().toString();
                    Toast.makeText(NewFarmer.this, "Submitted...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewFarmer.this, ProfileActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dataBaseHelper.addData(userID, fName, phoneNm, idTy, idNumb, bankNam, bankAc, bankVN, farmSz, Image, gpsLoc, gend, sta, localG, grpCoop, grpRol, crop, SYNC_STATUS_FAILED);
                //Toast.makeText(SurveyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(NewFarmer.this, "data has been saved on phone and will submitted once there is internet connection", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewFarmer.this, NewFarmer.class);
                startActivity(intent);
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri picture = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picture);
                Img.setImageBitmap(bitmap);
                // Img.setVisibility(View.VISIBLE);
                //btnChoose.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString() {
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.id.imageView);
        //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable) Img.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imgByte, Base64.DEFAULT);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(NewFarmer.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(this);
        }
    }



    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
                dataSubmit();
                break;

            case R.id.choseImg:
                selectImage();
                break;
        }

    }
        public void onStart(){
            super.onStart();
            registerReceiver(broadcastReceiver, new IntentFilter(UI_UPDATE_BROADCAST));
        }

    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        myLocation.setText(String.format("%s\n%s", longitude, latitude));
        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            //gpAddress.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
            // gpAddress.setText(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName());
            gpsAddr.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude","status");

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude","enable");

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude","disable");
    }
}
