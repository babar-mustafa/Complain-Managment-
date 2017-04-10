package com.example.babarmustafa.chatapplication.Chat_Work;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babarmustafa.chatapplication.R;
import com.example.babarmustafa.chatapplication.User_Profile.postView.BlogViewAdapter;
import com.example.babarmustafa.chatapplication.User_Profile.postView.PostObj;
import com.example.babarmustafa.chatapplication.User_Profile.postView.post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Two extends Fragment {
    TextView posttext;
    private RecyclerView recList;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    private BlogViewAdapter adapter;
    private List<PostObj> arrayList = new ArrayList<>();
    private ProgressDialog progressDialog;

    public Two() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        progressDialog = new ProgressDialog(getContext());

        posttext = (TextView) view.findViewById(R.id.post);
        recList = (RecyclerView) view.findViewById(R.id.blog_recylView_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        adapter = new BlogViewAdapter(arrayList, getContext());

        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(adapter);

        posttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClick();
            }
        });
        getData();
        return view;
    }


    public void postClick() {
        Intent intent = new Intent(getContext(), post.class);
        startActivity(intent);
    }

    public void getData() {
        progressDialog.setMessage("Plz wait data Loading!!!");
        progressDialog.show();
        mDatabase.child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // for (DataSnapshot Data : dataSnapshot.getChildren()) {
                if (dataSnapshot == null) {
                    Toast.makeText(getContext(), "sorry there is no data!!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    Log.d("TAg", dataSnapshot.getValue().toString());
                    PostObj psts = dataSnapshot.getValue(PostObj.class);
                    arrayList.add(new PostObj(psts.getUserImage(), psts.getUserName(), psts.getPostTime(), psts.getTitle(), psts.getDesc(), psts.getImages()));
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    //}
                }
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
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        progressDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatabase.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostObj psts = dataSnapshot.getValue(PostObj.class);
                arrayList.add(new PostObj(psts.getUserImage(), psts.getUserName(), psts.getPostTime(), psts.getTitle(), psts.getDesc(), psts.getImages()));
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter.notifyDataSetChanged();
    }
}


