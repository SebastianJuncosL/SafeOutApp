package com.example.safeout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safeout.R;
import com.example.safeout.models.FriendInformation;
import com.parse.ParseFile;

import java.util.List;

public class FriendInformationAdapter extends RecyclerView.Adapter<FriendInformationAdapter.ViewHolder> {

    private Context context;
    private List<FriendInformation> friendsInformation;

    public FriendInformationAdapter(Context context, List<FriendInformation> friendsInformation) {
        this.context = context;
        this.friendsInformation = friendsInformation;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_information, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendInformationAdapter.ViewHolder holder, int position) {
        FriendInformation info = friendsInformation.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return friendsInformation.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvIUsername;
        private TextView tvIPhone;
        private ImageView ivIProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);

            tvIUsername = itemView.findViewById(R.id.tvIUsername);
            tvIPhone = itemView.findViewById(R.id.tvIPhone);
            ivIProfilePic = itemView.findViewById(R.id.ivIProfilePic);
        }

        public void bind(FriendInformation info) {
            tvIUsername.setText(info.getUserName());
            tvIPhone.setText(info.getPhoneNumber());
            ParseFile image = info.getProfilePic();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivIProfilePic);
            }
        }
    }
}
