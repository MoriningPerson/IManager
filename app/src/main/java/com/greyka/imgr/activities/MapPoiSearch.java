package com.greyka.imgr.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.greyka.imgr.R;
import com.greyka.imgr.dialogs.LocationInfoPickerDialog;
import com.greyka.imgr.utilities.myUtils;

import java.util.ArrayList;
import java.util.List;

import overlay.PoiOverlay;

public class MapPoiSearch extends AppCompatActivity
        implements View.OnClickListener, Inputtips.InputtipsListener, PoiSearch.OnPoiSearchListener,
        TextWatcher, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter,AMap.OnMapClickListener{

    private String keyWord = "";
    private MapView mapView;
    private AMap aMap;
    private AutoCompleteTextView searchText;
    private PoiSearch.Query query;
    private ProgressDialog progressDialog = null;
    private LatLng latLng;
    private String poiName;
    private String nickName;
    private Marker markerNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_poi_search);
        mapView = findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        init();
        myUtils.myWindowManager.setWindow(this);
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        searchText = findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setInfoWindowAdapter(this);
        SharedPreferences sharedPreferences = getSharedPreferences("LatLng", MODE_PRIVATE);
        LatLng latLng = new LatLng(
                sharedPreferences.getFloat("Latitude", 0),
                sharedPreferences.getFloat("Longitude", 0));
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.strokeColor(0);
        myLocationStyle.radiusFillColor(0);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<String> listString = new ArrayList<>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            throw new IllegalStateException("Error!");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchButton) {
            searchButton();
        } else if (v.getId() == R.id.set_poi_word) {
            setPoiWordButton();
        } else {
            throw new IllegalStateException("Error!");
        }
    }
    @Override
    public void onMapClick(LatLng latLng) {
        if(markerNow != null){
            markerNow.hideInfoWindow();
            markerNow = null;
        }
    }

    private void setPoiWordButton() {
        LocationInfoPickerDialog dialog = new LocationInfoPickerDialog();
        dialog.setCallback(nickname -> {
            nickName = nickname;
            Intent intent = new Intent();
            intent.putExtra("latitude", latLng.latitude);
            intent.putExtra("longitude", latLng.longitude);
            intent.putExtra("name", poiName);
            intent.putExtra("nickname", nickName);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        dialog.show(getSupportFragmentManager(), "locationNicknamePicker");
    }

    private String checkEditText(AutoCompleteTextView searchText) {
        if (searchText != null && searchText.getText() != null
                && !(searchText.getText().toString().trim().equals(""))) {
            return searchText.getText().toString().trim();
        } else {
            return "";
        }
    }

    private void searchButton() {
        keyWord = checkEditText(searchText);
        if (!"".equals(keyWord)) {
            doSearchQuery();
        }
    }

    private void doSearchQuery() {
        showProgressDialog();
        query = new PoiSearch.Query(keyWord, "", null);
        query.setPageSize(10);
        query.setPageNum(0);

        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dismissProgressDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(query)) {
                    List<PoiItem> poiItems = result.getPois();
                    if (poiItems != null) {
                        aMap.clear();
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                        myUtils.myToastHelper.showText(this, "共搜索到 " + poiItems.size() + " 个地点信息。", Toast.LENGTH_LONG);
                    } else {
                        throw new IllegalStateException("Error! No result.");
                    }
                }
            }
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在搜索:\n" + keyWord);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int rCode) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!IsEmptyOrNullString(newText)) {
            InputtipsQuery inputQuery = new InputtipsQuery(newText, null);
            inputQuery.setCityLimit(false);
            Inputtips inputTips = new Inputtips(MapPoiSearch.this, inputQuery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        latLng = marker.getPosition();
        poiName = marker.getTitle();
        markerNow = marker;
        return true;
    }

    @Override
    public View getInfoWindow(final Marker marker) {

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = view.findViewById(R.id.title);
        String Title = marker.getTitle();
        if(Title == null){
            Title = "当前位置无信息";
        }
        title.setText(Title);
        TextView snippet = view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        Button button = view.findViewById(R.id.set_poi_word);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public void onMyLocationChange(android.location.Location location) {
        SharedPreferences sharedPreferences = getSharedPreferences("LatLng", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putFloat("Latitude", (float) location.getLatitude());
        edit.putFloat("Longitude", (float) location.getLongitude());
        edit.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}