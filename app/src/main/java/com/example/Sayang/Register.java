package com.example.Sayang;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Sayang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText editName,editEmail,editPassword,editConfirmasiPassword;
    private ImageView btnsignup;
    private TextView signup;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editName= findViewById(R.id.editName);
        editEmail= findViewById(R.id.editTextEmail);
        editPassword= findViewById(R.id.editTextPassword);
        editConfirmasiPassword = findViewById(R.id.editTextConfirmPassword);
        btnsignup = findViewById(R.id.btnsignup);
        signup = findViewById(R.id.textSign);
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu");
        progressDialog.setCancelable(false);
        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(view -> {
            Intent i = new Intent(Register.this, GetStarted.class);
            Bundle b = ActivityOptions.makeSceneTransitionAnimation(Register.this).toBundle();
            startActivity(i,b);
        });

        btnsignup.setOnClickListener(view -> {
            if (editEmail.getText().length()>0 && editPassword.getText().length()>0 && editConfirmasiPassword.getText().length()>0 ){
                if (editPassword.getText().toString().equals(editConfirmasiPassword.getText().toString())){
                    register(editName.getText().toString(),editEmail.getText().toString(),editPassword.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Silahkan isi data password yang sama",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Silahkan isi data terlebih dahulu",Toast.LENGTH_SHORT).show();
            }
        });

    }

        private void register (String name,String email,String password){
        progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful() && task.getResult() != null){
                        FirebaseUser user = task.getResult().getUser();
                        DocumentReference df = fstore.collection("users").document(user.getUid());
                        Map<String,Object> userInfo = new HashMap<>();
                        userInfo.put("Nama",editName.getText().toString());
                        userInfo.put("Email",editEmail.getText().toString());
                        userInfo.put("Password",editPassword.getText().toString());

                        userInfo.put("isUser","1");
                        df.set(userInfo);

                        if (user != null) {
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                reload();
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),"Register gagal",Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

        private void reload(){
        startActivity(new Intent(getApplicationContext(),Admin.class));

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }






}