package mychattingapp.androiphobia.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private MaterialEditText usernameET,emailET, passwordET;
    private Button register;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.tool_bar_layout_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usernameET = findViewById(R.id.usernameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        register = findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString().trim();
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty
                        (password)) {
                    Toast.makeText(RegisterActivity.this, "Must Fill All Fields", Toast
                            .LENGTH_SHORT).show();
                } else if (password.length() < 6 ) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters",
                            Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(username, password, email);
                }
            }
        });
    }


    public void registerUser(final String userN, final String pass, final String mail) {
        mProgress.setMessage("Creating User...");
        mProgress.show();

        auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String userId = auth.getCurrentUser().getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(userId);

                    HashMap<String, String> userData = new HashMap<>();
                    userData.put("id", userId);
                    userData.put("username", userN);
                    userData.put("imageURL", "default");

                    databaseReference.setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mProgress.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                                    .FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

                } else {
                    mProgress.dismiss();
                    Toast.makeText(RegisterActivity.this, "Can't create user with this " +
                            "email/password", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
