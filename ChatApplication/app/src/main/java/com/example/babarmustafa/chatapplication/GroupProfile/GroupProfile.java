package com.example.babarmustafa.chatapplication.GroupProfile;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.babarmustafa.chatapplication.Chat_Work.Chat_Main_View;
import com.example.babarmustafa.chatapplication.Groups.Group_coversation;
import com.example.babarmustafa.chatapplication.Groups.ItemClickSupport;
import com.example.babarmustafa.chatapplication.Groups.MyRecyclerViewAdapter;
import com.example.babarmustafa.chatapplication.Groups.groups_create_info;
import com.example.babarmustafa.chatapplication.R;
import com.example.babarmustafa.chatapplication.Signup_Adapter;
import com.example.babarmustafa.chatapplication.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class GroupProfile extends AppCompatActivity {
    ImageView imageView;
    Toolbar toolbar;
    TextView name, email, gender;
    RecyclerView group_members;
    MyRecyclerViewAdapter adapter;
    ArrayList<User> users_list;
    User data;
    String groupName;
    ArrayList<String> list;
    DatabaseReference database;
    String m_pos;
    String group_real_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        users_list = new ArrayList<>();
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference();
        imageView = (ImageView) findViewById(R.id.mainbackdropforgroup);
        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        group_members = (RecyclerView) findViewById(R.id.rvOrderList);
        adapter = new MyRecyclerViewAdapter(this, users_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        group_members.setLayoutManager(mLayoutManager);
        group_members.setItemAnimator(new DefaultItemAnimator());
        group_members.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        gender = (TextView) findViewById(R.id.gender);
        groupName = getIntent().getStringExtra("groupname");
        String AdminName = getIntent().getStringExtra("AdminName");
        String groupImage = getIntent().getStringExtra("groupImage");
        Glide.with(GroupProfile.this).load(groupImage).into(imageView);

        FirebaseDatabase.getInstance().getReference().child("Groups_info").child(groupName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map)dataSnapshot.getValue();

                group_real_admin =map.get("admin_name");
                Toast.makeText(GroupProfile.this, "get from forebasse"+group_real_admin, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("My_Groups")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d("Testing", " Key " + dataSnapshot.getKey() + " values " + dataSnapshot.getValue().toString());
                                String s = snapshot.getKey().toString();

                                if (snapshot.hasChild(groupName)) {

                                    FirebaseDatabase
                                            .getInstance()
                                            .getReference()
                                            .child("User Info")
                                            .child(s)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot !=null) {
                                                        data = dataSnapshot.getValue(User.class);
                                                        list.add(data.getUID());
                                                        User email = new User(data.getUID(),data.getName(), data.getEmail(), data.getPassword(), data.getGEnder(), data.getProfile_image());
                                                        users_list.add(email);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
//
                                } else {

                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//
        ItemClickSupport.addTo(group_members).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {

            m_pos =  list.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(GroupProfile.this);
                builder.setTitle("Do you want to remove this person ");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Chat_Main_View.name.equals(group_real_admin)){


                            database
                                    .child("My_Groups")
                                    .child(m_pos)
                                    .child(groupName)
                                    .removeValue();
                            startActivity(getIntent());

                        }
                        else {

                            Toast.makeText(GroupProfile.this, "You donot have rights to remove members", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
//                database

            }
        });
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.maincollapsing);
        collapsingToolbarLayout.setTitle(groupName);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.cardview_light_background));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }
}
