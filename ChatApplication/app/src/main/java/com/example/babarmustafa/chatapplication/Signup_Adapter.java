package com.example.babarmustafa.chatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BabarMustafa on 10/20/2016.
 */

public class Signup_Adapter extends BaseAdapter {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Signup_Adapter listapater;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private ArrayList<User> dataList;
    private Context context;

    public Signup_Adapter(ArrayList<User> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public User getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // final View inflater = LayoutInflater.from(MainView.this).inflate(R.layout.for_messages,null);
        View view = inflater.inflate(R.layout.signle_user_show, null);


        TextView forname = (TextView) view.findViewById(R.id.v_username);
      CircularImageView pcircularImageView = (CircularImageView) view.findViewById(R.id.profile_view);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_user);


        final User data = dataList.get(position);

        String username = data.getName();
//        String iml = data.getProfile_image();
//        Toast.makeText(context, ""+iml, Toast.LENGTH_SHORT).show();



//mAuth.getCurrentUser().toString();

        //to still the condition after changes
        final User todoChekd = (User) getItem(position);
        forname.setText(dataList.get(position).getName());
//        System.out.print(""+data.getProfile_image());
//        Picasso.with(context).load(data.getProfile_image()).into(pcircularImageView);
        Glide.with(context)
                .load(data.getProfile_image())
                .error(R.drawable.user1)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
//                        pcircularImageView.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/chatapplication-f99c2.appspot.com/o/grou_ico.png?alt=media&token=b664fb78-2b74-49b5-8297-6bc0e9e3c6c9"));

                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(pcircularImageView);


        return view;
    }
}
