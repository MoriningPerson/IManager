package com.greyka.imgr.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.greyka.imgr.R;

public class MapDisplayActivity extends AppCompatActivity implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {
    private MapView mMapView = null;
    private AMap aMap = null;

    private String name;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        double latitude = getIntent().getDoubleExtra("latitude", 1000);
        double longitude = getIntent().getDoubleExtra("longitude", 1000);
        if (latitude == 1000 || longitude == 1000) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        name = getIntent().getStringExtra("name");
        nickname = getIntent().getStringExtra("nickname");
        LatLng latLng = new LatLng(latitude, longitude);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(name);
        Marker marker = aMap.addMarker(markerOptions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(name);
        TextView snippet = view.findViewById(R.id.snippet);
        snippet.setText(nickname);
        Button button = view.findViewById(R.id.set_poi_word);
        button.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}