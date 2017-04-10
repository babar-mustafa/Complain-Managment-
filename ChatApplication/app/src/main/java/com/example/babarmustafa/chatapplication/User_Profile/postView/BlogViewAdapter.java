package com.example.babarmustafa.chatapplication.User_Profile.postView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.babarmustafa.chatapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by muhammad imran on 11-Jan-17.
 */

public class BlogViewAdapter extends RecyclerView.Adapter<BlogViewAdapter.PostViewHolder> {
    private List<PostObj> postModelList;
    Context context;

    public BlogViewAdapter(List<PostObj> postModelList, Context context) {
        this.postModelList = postModelList;
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, null);

        return new PostViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        PostObj postObj = postModelList.get(position);
        Picasso.with(context).load(postObj.getUserImage()).into(holder.userImage);
        holder.userName.setText(postObj.getUserName());
        holder.postTime.setText(postObj.getPostTime());
        holder.user_name.setText(postObj.getTitle());
        holder.description.setText(postObj.getDesc());
        //Glide.with(context).load(postObj.getImages()).into(holder.imageview);
        Picasso.with(context).load(postObj.getImages()).into(holder.imageview);

    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView postTime;
        TextView user_name;
        TextView description;
        ImageView imageview;

        public PostViewHolder(View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.UserImage);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            postTime = (TextView) itemView.findViewById(R.id.Post_Time);
            user_name = (TextView) itemView.findViewById(R.id.User_Name);
            description = (TextView) itemView.findViewById(R.id.User_status);
            imageview = (ImageView) itemView.findViewById(R.id.User_post_image);
        }

    }
}
