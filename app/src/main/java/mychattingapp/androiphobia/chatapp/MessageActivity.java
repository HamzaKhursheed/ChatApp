package mychattingapp.androiphobia.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import mychattingapp.androiphobia.chatapp.Model.User;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView messagesRV;
    private EditText enterMessageET;
    private ImageButton sendMessageBtn;

    private CircleImageView profile_img;
    private TextView username;

    private FirebaseUser fUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolBar = findViewById(R.id.toolBar_msgAct);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        messagesRV = findViewById(R.id.messages_recycler_view);
        enterMessageET = findViewById(R.id.message_edit_text);
        sendMessageBtn = findViewById(R.id.send_message_btn);

        profile_img = findViewById(R.id.profile_image_view_msgAct);
        username = findViewById(R.id.username_textview_msgAct);

        final String userid = getIntent().getStringExtra("userid");

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = enterMessageET.getText().toString().trim();
                if (!message.equals("")) {
                    sendMessage(fUser.getUid(), userid, message);
                } else {
                    Toast.makeText(MessageActivity.this, "Can't Send Empty Message!", Toast
                            .LENGTH_SHORT).show();
                }
                enterMessageET.setText("");
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_img.setImageResource(R.drawable.ic_person);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, String> msgInfo = new HashMap<>();
        msgInfo.put("sender", sender);
        msgInfo.put("receiver", receiver);
        msgInfo.put("message", message);

        dbReference.child("Chats").push().setValue(msgInfo);
    }
}
