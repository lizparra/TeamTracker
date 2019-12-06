package com.drunk.mode.Fragments;

//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ChatsFragment extends RootFragment implements View.OnClickListener
//{
//    @BindView(R.id.rvMessage) RecyclerView rvMessage;
//    @BindView(R.id.etMessage) EditText etMessage;
//    @BindView(R.id.btnSend) ImageButton imgButton;
//
//    FirebaseAuth auth;
//    FirebaseDatabase database;
//    DatabaseReference messagedb;
//    MessageAdapter messageAdapter;
//    CreateUser u;
//    List<Message> messages;
//
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_chats, container, false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        setHasOptionsMenu(true);
//
//        init();
//
//
//    }
//
//    private void init(){
//        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        u = new CreateUser();
//        imgButton.setOnClickListener(this);
//        messages = new ArrayList<>();
//    }
//
//    @Override
//    public void onClick(View view) {
//        if(!TextUtils.isEmpty(etMessage.getText().toString())){
//            Message message = new Message(etMessage.getText().toString(), u.getName());
//            etMessage.setText("");
//            messagedb.push().setValue(message);
//        } else {
//            Toast.makeText(getContext(), "You cannot send blank messages", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        final FirebaseUser currentUser = auth.getCurrentUser();
//
//        u.setUid(currentUser.getUid());
//        u.setEmail(currentUser.getEmail());
//
//        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                u = dataSnapshot.getValue(CreateUser.class);
//                u.setUid(currentUser.getUid());
//                AllMethods.name = u.getName();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        messagedb = database.getReference("Messages");
//        messagedb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Message message = dataSnapshot.getValue(Message.class);
//                message.setKey(dataSnapshot.getKey());
//                messages.add(message);
//                displayMessages(messages);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Message message = dataSnapshot.getValue(Message.class);
//                message.setKey(dataSnapshot.getKey());
//
//                List<Message> newMessages = new ArrayList<>();
//
//                for(Message m : messages){
//                    if(m.getKey().equals(message.getKey())){
//                        newMessages.add(message);
//                    } else {
//                        newMessages.add(m);
//                    }
//                }
//
//                messages = newMessages;
//                displayMessages(messages);
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                Message message = dataSnapshot.getValue(Message.class);
//                message.setKey(dataSnapshot.getKey());
//                List<Message> newMessages = new ArrayList<Message>();
//
//                for(Message m: messages){
//                    if(!m.getKey().equals(message.getKey())){
//                        newMessages.add(m);
//                    }
//                }
//
//                messages = newMessages;
//                displayMessages(messages);
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        messages = new ArrayList<>();
//
//    }
//
//    private void displayMessages(List<Message> messages) {
//        rvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
//        messageAdapter = new MessageAdapter(getActivity(), messages, messagedb);
//        rvMessage .setAdapter(messageAdapter);
//    }
//}

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.drunk.mode.Adapters.MessageAdapter;
import com.drunk.mode.Models.AllMethods;
import com.drunk.mode.Models.CreateUser;
import com.drunk.mode.Models.Message;
import com.drunk.mode.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChatsFragment extends RootFragment implements View.OnClickListener {
    CreateUser user;
    List<Message> messages;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;

    MessageAdapter messageAdapter;
    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = new CreateUser();
        rvMessage = (RecyclerView)view.findViewById(R.id.rvMessage);
        etMessage = (EditText)view.findViewById(R.id.etMessage);
        imgButton = (ImageButton)view.findViewById(R.id.btnSend);
        imgButton.setOnClickListener(this);
        messages = new ArrayList<>();

        return view;
    }

    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(etMessage.getText().toString())){
        Message message = new Message(etMessage.getText().toString(), user.getName());
        etMessage.setText("");
        messagedb.push().setValue(message);

        } else {
            Toast.makeText(getContext(), "You cannot send blank messages", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseUser currentUser = auth.getCurrentUser();
        user.setUid(currentUser.getUid());
        user.setEmail(currentUser.getEmail());

        /*
        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(CreateUser.class);
                user.setUid(currentUser.getUid());
                AllMethods.name = user.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

        messagedb = database.getReference("messages");
        messagedb.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());

                List<Message> newMessages = new ArrayList<>();

                for(Message m : messages){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    } else {
                        newMessages.add(m);
                    }
                }

                messages = newMessages;

                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                List<Message> newMessages = new ArrayList<>();

                for(Message m : messages){
                    if(!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }

                messages = newMessages;

                displayMessages(messages);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        messages = new ArrayList<>();

    }

    private void displayMessages(List<Message> messages) {
        rvMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        messageAdapter = new MessageAdapter(getActivity(), messages, messagedb);
        rvMessage.setAdapter(messageAdapter);

    }
}