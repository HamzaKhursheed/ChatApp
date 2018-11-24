package mychattingapp.androiphobia.chatapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mychattingapp.androiphobia.chatapp.Adapter.UserApater;
import mychattingapp.androiphobia.chatapp.Model.User;
import mychattingapp.androiphobia.chatapp.R;

public class UsersFragment extends Fragment {

    private RecyclerView userRececlerView;

    private UserApater adapter;
    private ArrayList<User> userArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        userRececlerView = view.findViewById(R.id.users_recycler_view);
        userArrayList = new ArrayList<>();

        userRececlerView.setHasFixedSize(true);
        userRececlerView.setLayoutManager(new LinearLayoutManager(getContext()));

        readUsers();

        return view;
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User mUser = dataSnapshot1.getValue(User.class);

                    if (!mUser.getId().equals(firebaseUser.getUid())) {
                        userArrayList.add(mUser);
                    }
                }

                adapter = new UserApater(userArrayList, getContext());
                userRececlerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
