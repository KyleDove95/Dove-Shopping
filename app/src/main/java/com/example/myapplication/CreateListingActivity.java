package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class CreateListingActivity extends AppCompatActivity {

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dove Shopping");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void onSubmitClick(View view) {
        EditText productNameText = findViewById(R.id.productName);
        EditText productPriceText = findViewById(R.id.productPrice);
        productPriceText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        EditText productDescriptionText = findViewById(R.id.productDescription);

        String name = productNameText.getText().toString();
        String priceString = productPriceText.getText().toString();
        String description = productDescriptionText.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        if(isValid(name, priceString, description)) {
            double price = Double.parseDouble(priceString);
            Listing listing = new Listing(name, price, description, email);
            mDb.collection("listings").add(listing).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(CreateListingActivity.this,"Your listing has been created!", Toast.LENGTH_SHORT).show();
                }
            });
            startActivity(new Intent(this, HomeActivity.class));
        }
        else {
            Toast.makeText(CreateListingActivity.this, "Input was invalid", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isValid(String name, String price, String description) {
        if (name.isEmpty() || price.isEmpty() || description.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
}
class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigits;

    /**
     * Constructor.
     *
     * @param decimalDigits maximum decimal digits
     */
    public DecimalDigitsInputFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int start,
                               int end,
                               Spanned dest,
                               int dstart,
                               int dend) {


        int dotPos = -1;
        int len = dest.length();
        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }
        if (dotPos >= 0) {

            // protects against many dots
            if (source.equals(".") || source.equals(","))
            {
                return "";
            }
            // if the text is entered before the dot
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }

        return null;
    }

}