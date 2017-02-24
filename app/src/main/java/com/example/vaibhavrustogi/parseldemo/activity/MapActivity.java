package com.example.vaibhavrustogi.parseldemo.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Property;
import android.view.View;
import android.widget.ImageView;

import com.example.vaibhavrustogi.parseldemo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.PermissionsUtils;

/**
 * Created by vaibhav.rustogi on 2/20/17.
 */

public class MapActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_CHECK_LOC_SETTINGS = REQUEST_LOCATION + 1;
    private GoogleMap mMap;
    private Marker userLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    private boolean isInitialLocationSet;

    @BindView(R.id.my_location_button)
    ImageView myLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createGoogleApiClient();
    }

    @OnClick(R.id.my_location_button)
    public void goToMyLocation() {
        if (canRequestLocation()) {
            checkLocationSettings();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean canRequestLocation() {
        if (PermissionsUtils.mayRequestLocation(this))
            return true;
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showLocationPermissionRationale();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        return false;
    }

    private void showLocationPermissionRationale() {
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.location_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(View v) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    }
                }).show();
    }

    private void createGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (canRequestLocation()) {
            checkLocationSettings();
        }
    }


    @SuppressWarnings("MissingPermission")
    protected void startLocationUpdates() {
        if (PermissionsUtils.mayRequestLocation(this)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, getLocationRequest(), this);
            if (mMap != null) {
                mMap.setMyLocationEnabled(false);
            }
            if (myLocationButton != null) {
                myLocationButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettings();
            } else {
                showLocationPermissionRationale();
            }
        }
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(getLocationRequest()).setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapActivity.this,
                                    REQUEST_CHECK_LOC_SETTINGS);
                        } catch (Exception ignored) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LocationSettingsStates states;

        switch (requestCode) {
            case REQUEST_CHECK_LOC_SETTINGS:
                states = LocationSettingsStates.fromIntent(data);
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        if ((states != null) && states.isLocationUsable()) {
                            startLocationUpdates();
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }


    private LocationRequest getLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void animateMarker(Marker marker, LatLng finalPosition,
                               final LatLngInterpolator latLngInterpolator) {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {

            @Override
            public LatLng evaluate(float fraction, LatLng startValue,
                                   LatLng endValue) {
                return latLngInterpolator.interpolate(fraction, startValue,
                        endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class,
                LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property,
                typeEvaluator, finalPosition);

        animator.setDuration(3000);
        animator.start();
    }


    public interface LatLngInterpolator {

        LatLng interpolate(float fraction, LatLng a, LatLng b);
    }

    public class LinearInterpolatr implements LatLngInterpolator {

        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.latitude - a.latitude) * fraction + a.latitude;
            double lngDelta = b.longitude - a.longitude;

            if (Math.abs(lngDelta) > 180) {
                lngDelta -= Math.signum(lngDelta) * 360;
            }
            double lng = lngDelta * fraction + a.longitude;
            return new LatLng(lat, lng);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!isInitialLocationSet)
            setMapIntialLocation(location);
        if (location != null) {
            updateUserLocationMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void setMapIntialLocation(Location location) {
        if (mMap != null && location != null) {
            isInitialLocationSet = true;
            CameraPosition currentLocationCamPosition = CameraPosition
                    .builder()
                    .target(new LatLng(location.getLatitude(), location
                            .getLongitude())).zoom(16).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(currentLocationCamPosition));
        }
    }

    private void updateUserLocationMarker(LatLng latLng) {
        if (mMap != null && latLng != null) {
            if (userLocationMarker == null) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng);

                userLocationMarker = mMap.addMarker(markerOptions);
            } else {
                animateMarker(userLocationMarker, latLng, new LinearInterpolatr());
            }
        }
    }
}
