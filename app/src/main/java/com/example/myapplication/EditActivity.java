package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.DiskLruCache;

public class EditActivity extends AppCompatActivity {

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String selectedID = intent.getExtras().getString("LISTING_ID");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dove Shopping");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        DocumentReference docRef = mDb.collection("listings").document(selectedID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Listing listing = documentSnapshot.toObject(Listing.class);
                String product = listing.getItemName();
                double price = listing.getPrice();
                String description = listing.getItemDescription();

                EditText editName = (EditText)findViewById(R.id.editName);
                EditText editPrice = (EditText)findViewById(R.id.editPrice);
                EditText editDescription = (EditText)findViewById(R.id.editDescription);

                editName.setText(product);
                editPrice.setText(String.format("%.2f", price));
                editDescription.setText(description);
            }
        });


    }
    public void onSaveClick(View view) {
        Intent intent = getIntent();
        String selectedID = intent.getExtras().getString("LISTING_ID");
        DocumentReference docRef = mDb.collection("listings").document(selectedID);
        EditText editName = (EditText)findViewById(R.id.editName);
        EditText editPrice = (EditText)findViewById(R.id.editPrice);
        EditText editDescription = (EditText)findViewById(R.id.editDescription);

        String finalName = editName.getText().toString();
        double finalPrice = Double.parseDouble(editPrice.getText().toString());
        String finalDescription = editDescription.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String finalEmail = user.getEmail();

        docRef.update("email", finalEmail, "itemDescription", finalDescription, "itemName", finalName, "price", finalPrice);
        Toast.makeText(EditActivity.this,"Your listing has been updated!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeActivity.class));

    }

    public void onDeleteClick(View view) {
        Intent intent = getIntent();
        String selectedID = intent.getExtras().getString("LISTING_ID");
        mDb.collection("listings").document(selectedID).delete();
        Toast.makeText(EditActivity.this,"Your listing has been deleted!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeActivity.class));
    }
}