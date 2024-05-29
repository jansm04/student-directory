package com.jan.studentdirectory;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;

public class MarkerCallback implements Callback {

    private final Marker marker;

    public MarkerCallback(Marker marker) {
        this.marker = marker;
    }
    @Override
    public void onSuccess() {
        marker.showInfoWindow();
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }
}
