package com.example.safeout.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.safeout.R;
import com.example.safeout.activities.FriendsActivity;
import com.example.safeout.activities.LoginActivity;
import com.example.safeout.activities.MainActivity;
import com.example.safeout.activities.SearchActivity;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class UserFragment extends Fragment {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final String TAG = "UserFragment";

    private File photo;

    private Button btnLogout;
    private Button btnAddFriend;
    private Button btnChangePic;
    private ImageView ivProfilePicture;
    private TextView tvUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogOut);
        btnAddFriend = view.findViewById(R.id.svSearchUser);
        btnChangePic = view.findViewById(R.id.btnChangePic);
        ivProfilePicture = view.findViewById(R.id.ivUserProfilePicture);
        tvUsername = view.findViewById(R.id.tvUserName);
        try {
            setFragment();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        btnChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfilePicture();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStatus();
            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFriendsActivity();
            }
        });
    }

    private void updateProfilePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photo = getPhotoFileUri("newPicture.jpeg");

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileProvider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
                if (e == null) {
                    ParseFile newPicture = new ParseFile(photo);
                    object.put("profilePicture", newPicture);
                    try {
                        object.save();
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            try {
                Glide.with(getContext()).load(query.get(ParseUser.getCurrentUser().getObjectId()).getParseFile("profilePicture").getUrl()).into(ivProfilePicture);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getContext(), "Couldn't get picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String fileName) {

        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create dir");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }


    private void goToFriendsActivity() {
        Intent i = new Intent(getContext(), SearchActivity.class);
        startActivity(i);
    }

    private void logOut() {
        ParseUser.logOut();
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        ((MainActivity)getActivity()).finish();
    }

    private void resetStatus() {
        // Database Class goes in getQuery
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // objectId -> can be retrieved from ParseUser.getCurrentUser().getObjectId()
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                ParseGeoPoint emptyLocation = new ParseGeoPoint();
                emptyLocation.setLatitude(0.0);
                emptyLocation.setLongitude(0.0);
                object.put("currentStatus", "(undefined)");
                // TODO: Uncomment location reset when logging out
                // object.put("currentLocation", emptyLocation);
                //All other fields will remain the same
                try {
                    object.save();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                Log.d("StatusFragment", object.get("currentStatus").toString());
            } else {
                // something went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (object.get("currentStatus") == "(undefined)") {
                logOut();
            }
        });
    }

    private void setFragment() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        String user = ParseUser.getCurrentUser().getObjectId();
        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
        Glide.with(getContext()).load(query.get(user).getParseFile("profilePicture").getUrl()).into(ivProfilePicture);
    }

}
