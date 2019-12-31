package com.zeathon.grainpoint_agent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView newFmr, newRec, myFarmers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newFmr = findViewById(R.id.newFmr);
        newRec = findViewById(R.id.newRec);
        myFarmers = findViewById(R.id.myFarmers);

        newFmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewFarmer.class);
                startActivity(intent);
            }
        });

        newRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewRecord.class);
                startActivity(intent);
            }
        });

        myFarmers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewFarmer.class);
                startActivity(intent);
            }
        });
    }
}
