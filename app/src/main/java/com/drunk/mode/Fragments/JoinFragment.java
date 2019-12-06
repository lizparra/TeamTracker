package com.drunk.mode.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.drunk.mode.Models.CircleJoin;
import com.drunk.mode.Models.CreateUser;
import com.drunk.mode.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JoinFragment extends RootFragment
{
    @BindView(R.id.mypinview) Pinview pinView;
    @BindView(R.id.joinBtn) Button joinButton;

    @BindView(R.id.textView4) TextView textViewCode;
    @BindView(R.id.shareButton) Button shareButton;

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference circleReference,joinedReference;
    String joinUserId,currentUserId;
    String currentUsercode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_join, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);


        getActivity().getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN

        );

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUsercode = dataSnapshot.child("circlecode").getValue(String.class);
                textViewCode.setText(currentUsercode); //
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Could not fetch circle code",Toast.LENGTH_LONG).show();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "Hello, I'm using Locator App. Join my group. My group code is:" + textViewCode.getText().toString());
                startActivity(i.createChooser(i, "Share using:"));
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserId = user.getUid();

                if(currentUsercode.equals(pinView.getValue())) {
                    Toast.makeText(getContext(),"You cannot add yourself",Toast.LENGTH_LONG).show();
                }
                else {
                    join();
                }
            }
        });
    }

    private void join()
    {
        Query query = reference.orderByChild("circlecode").equalTo(pinView.getValue());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    CreateUser createUser = null;
                    for(DataSnapshot childDss : dataSnapshot.getChildren())
                    {
                        createUser = childDss.getValue(CreateUser.class);
                    }
                    joinUserId = createUser.userid;

                    circleReference = FirebaseDatabase.getInstance().getReference().child("Users").child(joinUserId).child("CircleMembers");
                    joinedReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");

                    CircleJoin circleJoin = new CircleJoin(currentUserId);
                    final CircleJoin circleJoin1 = new CircleJoin(joinUserId);

                    circleReference.child(user.getUid()).setValue(circleJoin)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        joinedReference.child(joinUserId).setValue(circleJoin1)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            pinView.setValue("");
                                                            Toast.makeText(getContext(),"You joined this circle successfully",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(),"Could not join, try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                else
                {
                    pinView.setValue("");
                    Toast.makeText(getContext(),"Invalid circle code entered",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
