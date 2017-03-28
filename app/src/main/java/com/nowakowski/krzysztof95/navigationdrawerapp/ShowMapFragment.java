package com.nowakowski.krzysztof95.navigationdrawerapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

import com.nowakowski.krzysztof95.navigationdrawerapp.transform.DateTransform;


public class ShowMapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    private final String url = "http://52.174.235.185";
    private static final float ACHOR_WINDOWS_U = -9999;
    private static final float ACHOR_WINDOWS_V = -9999;


    GoogleMap mGoogleMap;
    MapView mMapView;
    View v;
    View info;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    ActionProcessButton join;
    Animation slideUp;
    Animation slideDown;
    List<ListItem> listItems;
    List<ListItem> listItems1;
    HashMap<String, ListItem> markerData = new HashMap<>();
    String MarkerId;
    SharedPreferences prefs;


    public ShowMapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) v.findViewById(R.id.ShowEventsMap);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_show_map, container, false);
        info = v.findViewById(R.id.infoEventLayout);
        prefs = getApplicationContext().getSharedPreferences("Name", Context.MODE_PRIVATE);

        join = (ActionProcessButton) v.findViewById(R.id.join_event);
        join.setOnClickListener(this);

        if (prefs.getString("user_id", "") == "") {
            join.setVisibility(View.GONE);
            join.setClickable(false);
        }
        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.swipe_up);
        slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.swipe_down);

        listItems = new ArrayList<>();
        listItems1 = new ArrayList<>();
        return v;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap = googleMap;
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.41177549551888, 19.17423415929079), (float) 5.3173866));
        loadEventsMarkers();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.hideInfoWindow();

                join.setClickable(true);
                join.setMode(ActionProcessButton.Mode.PROGRESS);
                join.setProgress(0);

                MarkerId = marker.getTitle();
                ListItem listItem = markerData.get(MarkerId);

                IsJoinedEventRequest(marker.getTitle());

                DateTransform dateTransform = new DateTransform();


                ImageView imageViewAvatar = (ImageView) v.findViewById(R.id.m_imageView);
                TextView title = (TextView) info.findViewById(R.id.m_textViewTitle);
                TextView author = (TextView) info.findViewById(R.id.m_textViewAuthor);
                TextView desc = (TextView) info.findViewById(R.id.m_textViewDesc);
                TextView time = (TextView) info.findViewById(R.id.m_textViewTime);
                TextView start_time = (TextView) info.findViewById(R.id.m_textViewStartTime) ;

                title.setText(listItem.getEvent_title());
                author.setText(listItem.getEvent_author());
                desc.setText(listItem.getEvent_desc());
                time.setText(dateTransform.countDownToDate(listItem.getEvent_time()));
                start_time.setText(dateTransform.countDownToEvent(listItem.getEvent_start_time()));

                Picasso.with(getApplicationContext())
                        .load(listItem.getUser_avatar()).placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder).resize(100, 100).into(imageViewAvatar);


                if (info.getVisibility() == View.INVISIBLE) {
                    info.startAnimation(slideDown);
                    info.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (info.getVisibility() == View.VISIBLE) {

                    info.startAnimation(slideUp);
                    info.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));


        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void loadEventsMarkers() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.Loading_data));
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        Call<List<ListItem>> call = service.getEventDetails();

        call.enqueue(new Callback<List<ListItem>>() {
            @Override
            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
                try {
                    progressDialog.dismiss();
                    listItems.clear();
                    listItems = response.body();

                    for (int i = 0; i < listItems.size(); i++) {

                        MarkerOptions marker = new MarkerOptions().position(
                                new LatLng(listItems.get(i).getEvent_lat(), listItems.get(i).getEvent_lng())).title(listItems.get(i).getEvent_id());
                        Marker marker1 = mGoogleMap.addMarker(marker);
                        markerData.put(marker.getTitle(), listItems.get(i));
                        marker1.setInfoWindowAnchor(ACHOR_WINDOWS_U, ACHOR_WINDOWS_V);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ListItem>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void JoinEventRequest(String user_id, String user_name, String event_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();

        listItem.setUser_id(user_id);
        listItem.setUser_name(user_name);
        listItem.setEvent_id(event_id);

        Call<ListItem> call = service.joinEvent(listItem);

        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                join.setMode(ActionProcessButton.Mode.PROGRESS);
                join.setProgress(100);
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                join.setMode(ActionProcessButton.Mode.PROGRESS);
                join.setProgress(-1);
            }
        });
    }

    public void IsJoinedEventRequest(String event_id){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        ListItem requestItems = new ListItem();
        requestItems.setEvent_id(event_id);

        Call<List<ListItem>> call = service.whoJoinEvent(requestItems);

        call.enqueue(new Callback<List<ListItem>>() {

            @Override
            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {

                try {
                    listItems1.clear();
                    listItems1= response.body();

                    for(int i = 0; i< listItems1.size(); i++){
                        if(listItems1.get(i).getUser_id().equals(prefs.getString("user_id", ""))){
                            join.setProgress(100);
                            join.setText("Dołączono");
                            join.setClickable(false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ListItem>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        join.setMode(ActionProcessButton.Mode.ENDLESS);
        join.setProgress(1);
        JoinEventRequest(prefs.getString("user_id", ""), prefs.getString("user", ""), MarkerId);
    }
}
