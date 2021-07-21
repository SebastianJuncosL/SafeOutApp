package com.example.safeout.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.safeout.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "MapFragment";
    private MapView mapView;
    private GoogleMap map;
    private LatLngBounds mapBoundary;
    private ParseGeoPoint userLocation;

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private FusedLocationProviderClient fLocationClient;
    // Friends/Contacts List
    private ArrayList<String> userNames = new ArrayList<>();
    // Friends/Contacts locations
    private ArrayList<ParseGeoPoint> coordinates = new ArrayList<>();

    // Creating Fragments Functions ------------------------------------------------------------------------------------------------------------
    // Doing anything inside this function is useless,
    // since there is nothing loaded in the app yet.
    // Do everything in onViewCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        fLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        getLastLocation();

        try {
            setUserPosition();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // TODO: Fetch location and reposition camera

        try {
            getFriends();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Backend and Location Functions --------------------------------------------------------------------------------------------------

    private void setUserPosition() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        userLocation = (ParseGeoPoint) query.get(ParseUser.getCurrentUser().getObjectId()).get("currentLocation");
    }

    // getting location also uploads it to DB
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(Task<Location> task) {
                Location location = task.getResult();
                ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                // Upload location to DB
                sendLocationToDB(geoPoint);
            }
        });
    }

    private void sendLocationToDB(ParseGeoPoint location) {
        // Database Class goes in getQuery
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // objectId -> can be retrieved from ParseUser.getCurrentUser().getObjectId()
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                object.put("currentLocation", location);
                //All other fields will remain the same
                object.saveInBackground();
                // Log.d(TAG, object.get("currentLocation").toString());
            } else {
                // something went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrieving contacts also gets their location
    private void getFriends() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        if (query.get(ParseUser.getCurrentUser().getObjectId()).get("friendsList") == null) {
            Log.d(TAG, "User has no contacts");
            return;
        }
        userNames = (ArrayList<String>) query.get(ParseUser.getCurrentUser().getObjectId()).get("friendsList");
        getFriendsLocations();

    }

    private void getFriendsLocations() throws ParseException {
        for (int i = 0; i < userNames.size(); i++) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            coordinates.add((ParseGeoPoint) query.get(userNames.get(i)).get("currentLocation"));
        }
        if (coordinates == null) {
            Log.d(TAG, "There are no friends, or they aren't sharing location");
        } else {
            for (int i = 0; i < coordinates.size(); i++) {
                Log.d(TAG, userNames.get(i) + " location is " + coordinates.get(i).toString());
            }
        }
    }

    // Map Only Functions ------------------------------------------------------------------------------------------------------------

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                try {
                    setCameraView();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        map = googleMap;



    }

    private void setCameraView() throws ParseException {
        mapBoundary = new LatLngBounds(
                new LatLng(userLocation.getLatitude()-.1,userLocation.getLongitude()-.1),
                new LatLng(userLocation.getLatitude()+.1, userLocation.getLongitude()+.1)
        );
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBoundary, 2));
    }

    // Life Cycle Functions ----------------------------------------------------------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
