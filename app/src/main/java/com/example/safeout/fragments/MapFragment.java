package com.example.safeout.fragments;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.safeout.R;
import com.example.safeout.activities.MainActivity;
import com.example.safeout.services.LocationService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "MapFragment";
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int LOCATION_UPDATE_INTERVAL = 28000; // 28 seconds

    private MapView mapView;
    private GoogleMap map;
    private LatLngBounds mapBoundary;
    private ParseGeoPoint userLocation;

    private FusedLocationProviderClient fLocationClient;
    // Friends/Contacts List
    private ArrayList<String> userIds = new ArrayList<>();
    private ArrayList<String> userNames = new ArrayList<>();
    // Friends/Contacts locations
    private ArrayList<ParseGeoPoint> coordinates = new ArrayList<>();
    // Friends Phone numbers
    private ArrayList<String> phoneNumbers = new ArrayList<>();
    // Friends Profile Pictures
    protected ArrayList<ParseFile> profilePics = new ArrayList<>();
    protected ArrayList<String> status = new ArrayList<>();
    // Map markers
    private ArrayList<Marker> markers = new ArrayList<>();


    private Handler handler = new Handler();
    private Runnable runnable;

    private Context context = getContext();

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

        getLastLocation();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        try {
            getFriends();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!profilePics.isEmpty()) {
            for (int i = 0; i < profilePics.size(); i++) {
                Log.d(TAG, "url is: " + profilePics.get(i) + " " + userNames.get(i));
            }
        } else {
            Log.d(TAG, "Images are empty");

        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        startLocationService();
    }

    // Backend and Location Functions --------------------------------------------------------------------------------------------------

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
                if (location != null) {
                    Log.d(TAG, String.valueOf(location.getLatitude()));
                    ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                    // Upload location to DB
                    userLocation = geoPoint;
                    sendLocationToDB(geoPoint);
                }

            }
        });
    }

    private void sendLocationToDB(ParseGeoPoint location) {
        // Database Class goes in getQuery
        userLocation = location;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // objectId -> can be retrieved from ParseUser.getCurrentUser().getObjectId()
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                object.put("currentLocation", location);
                //All other fields will remain the same
                try {
                    object.save();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
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
        userIds = (ArrayList<String>) query.get(ParseUser.getCurrentUser().getObjectId()).get("friendsList");
        getFriendsInformation();

    }

    private void getFriendsInformation() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereContainedIn("objectId", userIds);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d(TAG, "FriendsList"+String.valueOf(objects.size()));
                for (int i = 0; i<objects.size(); i++) {
                    coordinates.add((ParseGeoPoint) objects.get(i).get("currentLocation"));
                    userNames.add((String) objects.get(i).get("username"));
                    phoneNumbers.add((String) objects.get(i).get("phoneNumber"));
                    profilePics.add(objects.get(i).getParseFile("profilePicture"));
                    status.add((String) objects.get(i).get("currentStatus"));
                    Log.d(TAG, "Status for users " + status.get(i));
                }
            }
        });

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
        map = googleMap;

        // OnMalLoadedCallback is necessary for animating the camera since
        // it means that mapBoundaries can be set
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                try {
                    setCameraView();
                    addMapMarkers();
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String phoneNumber = marker.getSnippet();
                phoneNumber = phoneNumber.substring(3);
                Log.d(TAG, "Phone number is" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+52"+phoneNumber));
                startActivity(intent);
            }
        });
    }

    private void setCameraView() throws ParseException {
        mapBoundary = new LatLngBounds(
                new LatLng(userLocation.getLatitude()-.01,userLocation.getLongitude()-.01),
                new LatLng(userLocation.getLatitude()+.01, userLocation.getLongitude()+.01)
        );
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(mapBoundary, 2));
    }

    private void addMapMarkers() throws IOException {
        if (coordinates != null) {
            for (int i = 0; i < userNames.size(); i++) {
                LatLng location = new LatLng(coordinates.get(i).getLatitude(), coordinates.get(i).getLongitude());

                markers.add(map.addMarker(new MarkerOptions()
                                .position(location)
                                .title(userNames.get(i))
                                .snippet("📞 " + phoneNumbers.get(i))
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(profilePics.get(i), status.get(i))))
                        )
                );
            }
        }
    }

    private void removeMarkers() {
        markers.clear();
    }

    // Location Services and Refreshing Functions ------------------------------------------------------------------------------------

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(getContext(), LocationService.class);
            // this.startService(serviceIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                getContext().startForegroundService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(".services.LocationService".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }

    private void startUserLocationsRunnable() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                retrieveUserLocations();
                handler.postDelayed(runnable, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates() {
        handler.removeCallbacks(runnable);
    }

    private void retrieveUserLocations() {

        try {
            getFriends();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        removeMarkers();
            markers.clear();
        try {
            addMapMarkers();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    // Life Cycle Functions ----------------------------------------------------------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        getLastLocation();
        startLocationService();
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
        startUserLocationsRunnable();
        getLastLocation();
        startLocationService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private Bitmap getMarkerBitmapFromView(ParseFile image, String userStatus) {
        Log.d(TAG, "user status where it matters is " + userStatus);
        if (getActivity() != null) {
            //HERE YOU CAN ADD YOUR CUSTOM VIEW
            View customMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
            ImageView ivImageOnMap = customMarkerView.findViewById(R.id.ivImageOnMap);
            TextView tvTopPin = customMarkerView.findViewById(R.id.tvTopPin);
            TextView tvLeftPin = customMarkerView.findViewById(R.id.tvLeftPin);
            TextView tvRightPin = customMarkerView.findViewById(R.id.tvRightPin);
            TextView tvBottomPin = customMarkerView.findViewById(R.id.tvBottomPin);
            // LinearLayout layout = customMarkerView.findViewById(R.id.flMapMarker);
            Glide.with(getContext()).load(image.getUrl()).into(ivImageOnMap);
            if (userStatus.equals("Safe")) {
                tvTopPin.setBackgroundColor(getResources().getColor(R.color.safeGreen));
                tvLeftPin.setBackgroundColor(getResources().getColor(R.color.safeGreen));
                tvRightPin.setBackgroundColor(getResources().getColor(R.color.safeGreen));
                tvBottomPin.setBackgroundColor(getResources().getColor(R.color.safeGreen));
            } else if (userStatus.equals("Alert")){
                tvTopPin.setBackgroundColor(getResources().getColor(R.color.alertOrange));
                tvLeftPin.setBackgroundColor(getResources().getColor(R.color.alertOrange));
                tvRightPin.setBackgroundColor(getResources().getColor(R.color.alertOrange));
                tvBottomPin.setBackgroundColor(getResources().getColor(R.color.alertOrange));
            } else if (userStatus.equals("Danger")){
                tvTopPin.setBackgroundColor(getResources().getColor(R.color.dangerRed));
                tvLeftPin.setBackgroundColor(getResources().getColor(R.color.dangerRed));
                tvRightPin.setBackgroundColor(getResources().getColor(R.color.dangerRed));
                tvBottomPin.setBackgroundColor(getResources().getColor(R.color.dangerRed));
            } else {
                tvTopPin.setBackgroundColor(getResources().getColor(R.color.white));
                tvLeftPin.setBackgroundColor(getResources().getColor(R.color.white));
                tvRightPin.setBackgroundColor(getResources().getColor(R.color.white));
                tvBottomPin.setBackgroundColor(getResources().getColor(R.color.white));
            }
            customMarkerView.measure(150, 150);
            customMarkerView.layout(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
            customMarkerView.buildDrawingCache();

            customMarkerView.setBackgroundColor(getResources().getColor(R.color.alertOrange));

            Bitmap returnedBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
            Drawable drawable = customMarkerView.getBackground();
            if (drawable != null)
                drawable.draw(canvas);
            customMarkerView.draw(canvas);
            return returnedBitmap;
        }
        else {
            return null;
        }
    }
}
