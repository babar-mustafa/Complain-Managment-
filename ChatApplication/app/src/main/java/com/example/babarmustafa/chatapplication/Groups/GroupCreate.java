package com.example.babarmustafa.chatapplication.Groups;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.babarmustafa.chatapplication.Chat_Work.Chat_Main_View;
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

public class GroupCreate extends AppCompatActivity {
    private ListView emailList;
    private ArrayList<User> messages;
    private Signup_Adapter listAdapter;
    Button to_proce_gr_cr;
    Intent intent_of_gallery;
    private static int Gallery_Request = 1;
    private Uri ImageUri = null;
    ImageView group_image;
    private StorageReference mStoarge;
    String downloadUrl;
    String to_get_g_name;
    DatabaseReference databse;
    groups_create_info group ;
    EditText group_Name;
    User data;
    ArrayList<String> list;
    String friend_uid_on_clicked;
    View view1;
    public HashMap<String, String> hashObj = new HashMap<>();
    String g_by_default;
    FirebaseAuth mauth;
    User email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        databse = FirebaseDatabase.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();

        mStoarge = FirebaseStorage.getInstance().getReference();
        group_image = (ImageView)findViewById(R.id.group_image);
        group_Name = (EditText)findViewById(R.id.g_name);
        to_proce_gr_cr = (Button)findViewById(R.id.create_g);
        emailList = (ListView)findViewById(R.id.add_to_g);

        messages = new ArrayList<>();
        list = new ArrayList<>();
        listAdapter = new Signup_Adapter(messages, this);
        emailList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();


        FirebaseDatabase.getInstance().getReference().child("User Info").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // This method is called once with the initial value and again
                // whenever Data at this location is updated.
                data = dataSnapshot.getValue(User.class);
                list.add(data.getUID());
                // Log.v("DATA", "" + data.getId() + data.getName() + data.getCity());
                email = new User(data.getUID(), data.getName(), data.getEmail(),  data.getPassword(),data.getGEnder(), data.getProfile_image());
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
                        friend_uid_on_clicked = list.get(position);
                        Toast.makeText(GroupCreate.this, ""+position +">" +friend_uid_on_clicked, Toast.LENGTH_SHORT).show();
                        group_Name.getText().toString();
                        if (group_Name.getText().toString().length() == 0 ) {
                            group_Name.setError("GROUP Name Can't be Blank");

                        }
                        else {
                            if (downloadUrl != null) {
                                String a_name = Chat_Main_View.name;
                                group = new groups_create_info(a_name, group_Name.getText().toString(), downloadUrl);
                                hashObj.put("admin_name", group.getAdmin_name());
                                hashObj.put("group_name", group.getGroup_name());
                                hashObj.put("g_i_url", group.getG_i_url());
                                databse
                                        .child("My_Groups")
                                        .child(friend_uid_on_clicked)
                                        .child(group_Name.getText().toString())
                                        .setValue(hashObj);
//                                databse
//                                        .child("Group_Members")
//                                        .child(group_Name.getText().toString())
//                                        .child(friend_uid_on_clicked)
//                                        .setValue(email);

                                messages.remove(position);
                                listAdapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(GroupCreate.this, "image cant be null", Toast.LENGTH_SHORT).show();

                            }
                        }
//                       list.remove(position);





                    }
                });
        to_proce_gr_cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_get_g_name = group_Name.getText().toString();
                if (group_Name.getText().toString().length() == 0) {
                    group_Name.setError("GROUP Name Can't be Blank");
                    Toast.makeText(GroupCreate.this, "Name cannot be Blank", Toast.LENGTH_LONG).show();

                    return ;
                }

                else{
                    if (downloadUrl != null) {
                        String a_name = Chat_Main_View.name;
                        group = new groups_create_info(a_name,to_get_g_name,downloadUrl);
                        hashObj.put("admin_name", group.getAdmin_name());
                        hashObj.put("group_name", group.getGroup_name());
                        hashObj.put("g_i_url", group.getG_i_url());
                        databse
                                .child("Groups_info")
                                .child(to_get_g_name)
                                .setValue(hashObj);
                        String current_login = mauth.getCurrentUser().getUid();
                        databse
                                .child("My_Groups")
                                .child(current_login)
                                .child(group_Name.getText().toString())
                                .setValue(hashObj);
                        finish();

                    }
                    else{

                        Toast.makeText(GroupCreate.this, "You MUst SElect the Group Image", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
                group_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //group_image_work
                        AlertDialog.Builder builder = new AlertDialog.Builder(GroupCreate.this);
                        builder.setTitle("Profile pic Menu.....");
                        builder.setMessage("Choose your image from the folloeing");
                        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(GroupCreate.this.getBaseContext().getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, 111);
                                }
                            }
                        });

                        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intent_of_gallery = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent_of_gallery, Gallery_Request);
                            }
                        });
                        builder.create().show();
                    }

                });









    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Request && resultCode == RESULT_OK) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                ImageUri = result.getUri();

                StorageReference filepath = mStoarge.child("Batteery").child(ImageUri.getLastPathSegment());

                filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        downloadUrl = String.valueOf(taskSnapshot.getDownloadUrl());

                        Glide.with(GroupCreate.this)
                                .load(downloadUrl)
                                .into(group_image);

                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        }
        if (requestCode == 111 && resultCode == RESULT_OK) {
            //saves the pic locally
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();

            StorageReference imagesRef = mStoarge.child("caa.jpg");

            UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(GroupCreate.this, "Failed to upload camera", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    downloadUrl = String.valueOf(taskSnapshot.getDownloadUrl());
                    Toast.makeText(GroupCreate.this, "upload camera", Toast.LENGTH_SHORT).show();
                    // DatabaseReference database_reference = databaseReference.child(mAuth.getCurrentUser().getUid());

                    //database_reference.child("Profile_image").setValue(downrl.toString());
                    // for_message.setText(downrl.toString());
                    Glide.with(GroupCreate.this)
                            .load(downloadUrl)
                            .into(group_image);
//                    group_image.setImageURI(Uri.parse(downloadUrl));
                }
            });
        }

    }
}
