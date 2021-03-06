package com.example.babarmustafa.chatapplication.User_Profile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.babarmustafa.chatapplication.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserProfile extends AppCompatActivity {
    TextView username;
    ImageView imageView;
    Toolbar toolbar;
    TextView name, email, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().hide();getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        gender = (TextView) findViewById(R.id.gender);

        //for User Name
        imageView = (ImageView) findViewById(R.id.mainbackdrop);
        String userName = getIntent().getStringExtra("username");
        String userimage = getIntent().getStringExtra("userimage");
        String useremail = getIntent().getStringExtra("useremail");
        String usergender = getIntent().getStringExtra("usergender");


        //Picasso.with(this).load(userimage).into(imageView);
        Glide.with(UserProfile.this).load(userimage).into(imageView);

        name.setText(" " + userName);
        email.setText(" " + useremail);
        gender.setText(" " + usergender);


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.maincollapsing);
        collapsingToolbarLayout.setTitle(userName);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.cardview_light_background));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


    }
}
