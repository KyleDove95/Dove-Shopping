package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.ShareActionProvider;
import android.widget.TextView;

import static android.content.Intent.*;

public class ViewListingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String selectedName = intent.getExtras().getString("VIEWED_NAME");
        String selectedPrice = intent.getExtras().getString("VIEWED_PRICE");
        String selectedDescription = intent.getExtras().getString("VIEWED_DESCRIPTION");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dove Shopping");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        TextView viewedName = (TextView)findViewById(R.id.viewedName);
        TextView viewedPrice = (TextView)findViewById(R.id.viewedPrice);
        TextView viewedDescription = (TextView)findViewById(R.id.viewedDescription);

        viewedName.setText(selectedName);
        viewedPrice.setText("$"+String.format("%.2f",Double.parseDouble(selectedPrice)));
        viewedDescription.setText(selectedDescription);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        String selectedName = intent.getExtras().getString("VIEWED_NAME");
        String selectedEmail = intent.getExtras().getString("VIEWED_EMAIL");
        getMenuInflater().inflate(R.menu.view_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        String[] address = {selectedEmail};
        setShareActionIntent(address, selectedName);
        return super.onCreateOptionsMenu(menu);
    }

    public void setShareActionIntent(String[] address, String subject) {
        Intent intent = new Intent(ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(intent);

    }
}