package com.example.studentscity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentscity.R;
import com.example.studentscity.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private List<Place> places = new ArrayList<>();
    private OnPlaceClickListener listener;

    // Add interface for click handling
    public interface OnPlaceClickListener {
        void onPlaceClick(Place place);
    }

    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = places.get(position);
        holder.bind(place);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    // Update ViewHolder to handle clicks
    class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView descriptionText;
        private final TextView distanceText;

        PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.placeName);
            descriptionText = itemView.findViewById(R.id.placeDescription);
            distanceText = itemView.findViewById(R.id.placeDistance);

            // Add click listener to the entire item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onPlaceClick(places.get(position));
                }
            });
        }

        void bind(Place place) {
            nameText.setText(place.getName());
            descriptionText.setText(place.getDescription());
            
            float distance = place.getDistanceToUser();
            if (distance != Float.MAX_VALUE) {
                String distanceText = distance < 1000 
                    ? String.format(Locale.getDefault(), "%.0f m away", distance)
                    : String.format(Locale.getDefault(), "%.1f km away", distance / 1000);
                this.distanceText.setText(distanceText);
                this.distanceText.setVisibility(View.VISIBLE);
            } else {
                this.distanceText.setVisibility(View.GONE);
            }
        }
    }
} 