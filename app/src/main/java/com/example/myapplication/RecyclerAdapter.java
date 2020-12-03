package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RecyclerAdapter extends FirestoreRecyclerAdapter<Listing, RecyclerAdapter.ListingViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener listener;

    RecyclerAdapter(FirestoreRecyclerOptions<Listing> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    RecyclerAdapter(FirestoreRecyclerOptions<Listing> options) {
        super(options);
        this.listener = null;
    }

    class ListingViewHolder extends  RecyclerView.ViewHolder {
        final CardView view;
        final TextView listingName;
        final TextView listingPrice;
        final TextView listingUser;

        ListingViewHolder(CardView v) {
            super(v);
            view = v;
            listingName = v.findViewById(R.id.listing_name);
            listingPrice = v.findViewById(R.id.listing_price);
            listingUser = v.findViewById(R.id.listing_user);

        }
    }
    @Override
    public void onBindViewHolder(final ListingViewHolder holder, @NonNull int position, @NonNull final Listing listing) {
        holder.listingName.setText(listing.getItemName());
        holder.listingPrice.setText("$" + String.format("%.2f", listing.getPrice()));
        holder.listingUser.setText(listing.getEmail());
        if(listener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_card_view, parent, false);

        return new ListingViewHolder(v);
    }
}
