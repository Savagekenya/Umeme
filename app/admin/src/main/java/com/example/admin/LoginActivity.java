package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailEt, passwordEt;
    Button submit_button;
    FirebaseAuth fAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEt = findViewById(R.id.profileemail);
        passwordEt = findViewById(R.id.password);
        submit_button = findViewById(R.id.submit_button);
        fAuth =  FirebaseAuth.getInstance();



        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if(TextUtils.isEmpty(email)){
                    emailEt.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    passwordEt.setError("Password is Required");
                    return;
                }

                if (password.length()<6){
                    passwordEt.setError("Password Must be >= 6 Characters");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this," Log in Successful.",Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(),ReportActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this,"Error !" +task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });





    }
}