package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Listing;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    ArrayList<Listing> listingList = new ArrayList<>();
    private ArrayAdapter<Listing> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listingListView = findViewById(R.id.listingListView);

        mDb.collection("listings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document: queryDocumentSnapshots) {
                    Listing listing = document.toObject(Listing.class);
                    listingList.add(listing);
                }
                adapter.addAll(listingList);
            }
        });

//        adapter = new ArrayAdapter<Listing>(this, android.R.layout.simple_list_item_1, new ArrayList<Listing>());
        adapter = new ListingAdapter(this, new ArrayList<Listing>());
        listingListView.setAdapter(adapter);

        listingListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(HomeActivity.this, ViewListingActivity.class));
                Listing selectedListing = listingList.get(position);
                Intent intent = new Intent(HomeActivity.this, ViewListingActivity.class);
                intent.putExtra("VIEWED_NAME", selectedListing.getItemName());
                intent.putExtra("VIEWED_PRICE", Double.toString(selectedListing.getPrice()));
                intent.putExtra("VIEWED_DESCRIPTION", selectedListing.getItemDescription());
                startActivity(intent);
            }

        });

    }

    class ListingAdapter extends  ArrayAdapter<Listing> {

        ArrayList<Listing> listing;
        ListingAdapter(Context context, ArrayList<Listing> listing) {
            super(context, 0, listing);
            this.listing = listing;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_layout, parent, false);
            }
            TextView productName = convertView.findViewById(R.id.editName);
            TextView productPrice = convertView.findViewById(R.id.editPrice);

            Listing list = listing.get(position);
            productName.setText(list.getItemName());
            productPrice.setText("$" + String.format("%.2f", list.getPrice()));

            return convertView;

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logoutMenu:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.addListing:
                startActivity(new Intent(this, CreateListingActivity.class));
        }
        return true;
    }
}