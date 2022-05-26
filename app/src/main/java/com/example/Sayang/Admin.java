package com.example.Sayang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Admin extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private TextView namauser;
    private ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        namauser = findViewById(R.id.namaUser);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Play.class));
            finish();
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            namauser.setText(firebaseUser.getDisplayName());
        }else {
            namauser.setText("Login Gagal !!");
        }
    }{
}}
