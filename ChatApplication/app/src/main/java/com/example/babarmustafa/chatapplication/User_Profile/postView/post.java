package com.example.babarmustafa.chatapplication.User_Profile.postView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.babarmustafa.chatapplication.Chat_Work.Chat_Main_View;
import com.example.babarmustafa.chatapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;


public class post extends AppCompatActivity {
    ImageButton imageButton;
    Button post_button;
    EditText post, description;
    private Uri mImageUri = null;
    public static final int Gallery_Request = 1;
    private ProgressDialog mprogress;
    private StorageReference mStoarge;
    private DatabaseReference mData;
    DatabaseReference newPost;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mStoarge = FirebaseStorage.getInstance().getReference();
        mData = FirebaseDatabase.getInstance().getReference().child("posts");
        mprogress = new ProgressDialog(this);


        imageButton = (ImageButton) findViewById(R.id.select_image_Button);
        post = (EditText) findViewById(R.id.Edit_Title_field);
        description = (EditText) findViewById(R.id.Edit_description_field);
        post_button = (Button) findViewById(R.id.Submit_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(post.this);
                builder.setTitle("Choose Action");
                builder.setMessage("choose Camera Or Gallery ");
                builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, Gallery_Request);
                    }
                });
                builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getBaseContext().getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                });
                builder.create().show();

            }
        });

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });


    }

    private void startPosting() {

        mprogress.setMessage("Posting to blog..");
        mprogress.show();

        final String title_value = post.getText().toString().trim();
        final String desc_value = description.getText().toString().trim();

        if (!TextUtils.isEmpty(title_value) && !TextUtils.isEmpty(desc_value) && mImageUri != null) {

            StorageReference filepath = mStoarge.child("post-images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.d("TAG", "url: " + downloadUrl);
                    newPost = mData.push();
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    newPost.child("userName").setValue(Chat_Main_View.name);
                    newPost.child("UserImage").setValue(Chat_Main_View.photoUrlForUserImage);
                    newPost.child("postTime").setValue(currentDateTimeString);
                    newPost.child("title").setValue(title_value);
                    newPost.child("Desc").setValue(desc_value);
                    newPost.child("images").setValue(downloadUrl.toString());


                    mprogress.dismiss();

                    finish();

                }
            });

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Request && resultCode == RESULT_OK) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                imageButton.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            //saves the pic locally
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();

//            assert imageBitmap != null;
            Glide.with(post.this).load(dataBAOS).into(imageButton);
//
//            StorageReference filepath = mStoarge.child("post-images").child(imageBitmap.toString());
//            UploadTask uploadTask = filepath.putBytes(dataBAOS);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Toast.makeText(post.this, "Failed to upload camera", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                    Uri downrl = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(post.this, "upload camera", Toast.LENGTH_SHORT).show();
//
//
////                    newPost.child("images").setValue(downloadUrl.toString());
////
////                    DatabaseReference database_reference = databaseReference.child(mAuth.getCurrentUser().getUid());
//
//                    //database_reference.child("Profile_image").setValue(downrl.toString());
//                    // for_message.setText(downrl.toString());
//                    imageButton.setImageURI(downrl);
//                }
//            });
        }


    }
}
