package com.example.safeout.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safeout.R;
import com.example.safeout.models.SearchResult;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

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

        public ViewHolder(View itemView) {
            super(itemView);
            tvSUsername = itemView.findViewById(R.id.tvSUsername);
            ivSProfilePic = itemView.findViewById(R.id.ivSProfilePic);
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
