package com.example.safeout.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safeout.R;
import com.example.safeout.activities.LoginActivity;
import com.example.safeout.models.SearchResult;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private static final String TAG = "SearchResultAdapter";
    Context context;
    List<ParseUser> results;

    public SearchResultAdapter(List<ParseUser> results, Context context) {
        this.context = context;
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder holder, int position) {
        ParseUser result = results.get(position);
        holder.bind(result);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvSUsername;
        ImageView ivSProfilePic;
        Button btnSAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSUsername = itemView.findViewById(R.id.tvSUsername);
            ivSProfilePic = itemView.findViewById(R.id.ivSProfilePic);
            btnSAdd = itemView.findViewById(R.id.btnSAdd);

            btnSAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        addFriend();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void addFriend() throws ParseException {
            Log.d(TAG, String.valueOf(results.size()));
            List<String> searchNames = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                searchNames.add(results.get(i).getUsername());
            }

            int index = searchNames.indexOf(tvSUsername.getText().toString());
            Log.d(TAG, "index = " + index);
            if (index < 0 || index > results.size())
                return;
            Log.d(TAG, "Object id: " + results.get(index).getObjectId() + " for user: " + results.get(index).getUsername());

            String userId = results.get(index).getObjectId();

            List<String> friendsOfUser = ParseUser.getCurrentUser().getList("friendsList");

            if (friendsOfUser != null) {
                for (String user: friendsOfUser) {
                    Log.d(TAG, user + " is friend of " + ParseUser.getCurrentUser().getUsername());
                }
                if (friendsOfUser.contains(userId)) {
                    Toast.makeText(context, "This user is already your friend", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");

            List<String> requestsOfOtherUser = query.get(userId).getList("friendRequests");

            if (requestsOfOtherUser != null) {
                for (String user: requestsOfOtherUser) {
                    Log.d(TAG, user + " is friend of " + query.get(userId).get("username"));
                }
                if (requestsOfOtherUser.contains(ParseUser.getCurrentUser().getObjectId())) {
                    Toast.makeText(context, "Friend request has already been sent", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                requestsOfOtherUser = new ArrayList<>();
            }

            // Log.d(TAG, "User: " + ParseUser.getCurrentUser().getUsername() + " Other user: " + query.get(userId).get("username"));
            requestsOfOtherUser.add(ParseUser.getCurrentUser().getObjectId());
            JSONArray jsonArray = new JSONArray(requestsOfOtherUser);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Log.d(TAG, "List to upload: " + jsonArray.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            query.getInBackground(userId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    object.add("friendrequests", ParseUser.getCurrentUser().getObjectId());
                    object.saveInBackground();
                }
            });

            //query.get(userId).add("friendRequests", ParseUser.getCurrentUser().getObjectId().toString());
            //query.get(userId).saveInBackground();
            Toast.makeText(context, "Friend request has been sent to " + query.get(userId).get("username"), Toast.LENGTH_SHORT).show();
        }

        public void bind(ParseUser result) {
            tvSUsername.setText(result.getUsername());
            ParseFile image = result.getParseFile("profilePicture");
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivSProfilePic);
            }
        }
    }
}
