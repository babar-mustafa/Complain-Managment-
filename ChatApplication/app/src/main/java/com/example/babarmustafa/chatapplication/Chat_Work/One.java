package com.example.babarmustafa.chatapplication.Chat_Work;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.babarmustafa.chatapplication.Groups.GroupCreate;
import com.example.babarmustafa.chatapplication.Groups.Group_coversation;
import com.example.babarmustafa.chatapplication.Groups.Groups_show_Adapter;
import com.example.babarmustafa.chatapplication.Groups.groups_create_info;
import com.example.babarmustafa.chatapplication.R;
import com.example.babarmustafa.chatapplication.Signup_Adapter;
import com.example.babarmustafa.chatapplication.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class One extends Fragment {
    private ListView emailList;
    private ArrayList<groups_create_info> messages;
    private Groups_show_Adapter listAdapter;
    ArrayList<String> list;
    groups_create_info data;
    String group_name;
    String admin_name;
    String group_image_url;
    private FloatingActionButton fabButton;
FirebaseAuth mauth;
    public One() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        list = new ArrayList<>();
mauth =FirebaseAuth.getInstance();
        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);
        emailList = (ListView) view.findViewById(R.id.g_list_view);
        messages = new ArrayList<>();
        listAdapter = new Groups_show_Adapter(messages, getActivity());
        emailList.setAdapter(listAdapter);

        String c_login = mauth.getCurrentUser().getUid();
        //to show user only his groups
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("My_Groups")
                .child(c_login)
                .addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // This method is called once with the initial value and again
                // whenever Data at this location is updated.
                data = dataSnapshot.getValue(groups_create_info.class);
                list.add(data.getGroup_name());
                // Log.v("DATA", "" + data.getId() + data.getName() + data.getCity());
                groups_create_info email = new groups_create_info(data.getAdmin_name(), data.getGroup_name(), data.getG_i_url());
                messages.add(email);
                listAdapter.notifyDataSetChanged();
//

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        emailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent conwindow = new Intent(getActivity(), Group_coversation.class);
//


                group_name = list.get(position);
                admin_name = messages.get(position).getAdmin_name();
                group_image_url = messages.get(position).getG_i_url();


                conwindow.putExtra("group_name", group_name);
                conwindow.putExtra("group_admin_name", admin_name);
                conwindow.putExtra("group_ima_url", group_image_url);


                startActivity(conwindow);
            }
        });

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c_group = new Intent(getActivity(), GroupCreate.class);
                startActivity(c_group);
            }
        });


        return view;
    }


}
