package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewListingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String selectedName = intent.getExtras().getString("VIEWED_NAME");
        String selectedPrice = intent.getExtras().getString("VIEWED_PRICE");
        String selectDescription = intent.getExtras().getString("VIEWED_DESCRIPTION");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dove Shopping");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView viewedName = (TextView)findViewById(R.id.viewedName);
        TextView viewedPrice = (TextView)findViewById(R.id.viewedPrice);
        TextView viewedDescription = (TextView)findViewById(R.id.viewedDescription);

        viewedName.setText(selectedName);
        viewedPrice.setText("$"+selectedPrice);
        viewedDescription.setText(selectDescription);
    }
}