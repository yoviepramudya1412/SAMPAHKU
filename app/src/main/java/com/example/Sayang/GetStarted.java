package com.example.Sayang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Sayang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetStarted extends AppCompatActivity {
    private EditText editemail, editpassword;
    private ImageView login;
    private TextView register;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editemail = findViewById(R.id.editTextEmail);
        editpassword = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.login);
        register = findViewById(R.id.textRegister);
        progressDialog = new ProgressDialog(GetStarted.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu");
        progressDialog.setCancelable(false);

        fstore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
            Intent i = new Intent(GetStarted.this, Register.class);
            Bundle b = ActivityOptions.makeSceneTransitionAnimation(GetStarted.this).toBundle();
            startActivity(i,b);
        });
        login.setOnClickListener(view -> {
            Log.d("TAG", "onCreate: "+ editemail.getText().toString());
            if (editemail.getText().length()>0 && editpassword.getText().length()>0){
                login(editemail.getText().toString(), editpassword.getText().toString());

            }else{
                Toast.makeText(getApplicationContext(), "Silahkan isi data terlebih dahulu",Toast.LENGTH_SHORT).show();
            }
        });


        }

    private void login(String email,String password){
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!= null){

                    if (task.getResult().getUser()!=null){
                        reload();
                    }else {
                        Toast.makeText(getApplicationContext(), "Login gagal",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Login gagal",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });


    }
    private void checkUserAccesLevel(String uid){
        DocumentReference df = fstore.collection("Users").document(uid) ;
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: "+ documentSnapshot.getData());
                if (documentSnapshot.getString("isAdmin" )!= null){
                    startActivity(new Intent(getApplicationContext(),Admin.class));
                    finish();

                }
                if (documentSnapshot.getString("isUser")!= null){
                    startActivity(new Intent(getApplicationContext(),User.class));
                    finish();
                }
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