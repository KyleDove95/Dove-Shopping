package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Listing;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    ArrayList<Listing> listingList = new ArrayList<>();
    private ArrayAdapter<Listing> adapter;
    private RecyclerAdapter mAdapter;

    private static final String LISTINGS = "listings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        Query query = mDb.collection(LISTINGS);
        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class)
                .build();

        mAdapter = new RecyclerAdapter(options, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Listing selectedListing = mAdapter.getSnapshots().getSnapshot(position).toObject(Listing.class);
                String email = user.getEmail();
                String selectedEmail = selectedListing.getEmail();
                if(email.equals(selectedEmail)){
                    String id = mAdapter.getSnapshots().getSnapshot(position).getId();

                    Intent intent = new Intent(HomeActivity.this, EditActivity.class);
                    intent.putExtra("LISTING_ID", id);
                    startActivity(intent);

                }
                else {

                    Intent intent = new Intent(HomeActivity.this, ViewListingActivity.class);
                    intent.putExtra("VIEWED_NAME", selectedListing.getItemName());
                    intent.putExtra("VIEWED_PRICE", Double.toString(selectedListing.getPrice()));
                    intent.putExtra("VIEWED_DESCRIPTION", selectedListing.getItemDescription());
                    intent.putExtra("VIEWED_EMAIL", selectedListing.getEmail());
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        EditText searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Query query;
                if(s.toString().isEmpty()){
                    query = mDb.collection(LISTINGS);
                }
                else {
                    query = mDb.collection(LISTINGS).whereEqualTo("itemName", s.toString());
                }
                FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                        .setQuery(query, Listing.class)
                        .build();
                mAdapter.updateOptions(options);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
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