package com.example.umeme;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText tokenEt,emailEt,phoneEt, passwordEt;
    Button btn_sign_up;
    TextView loginText;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tokenEt = findViewById(R.id.token);
        emailEt = findViewById(R.id.profileemail);
        phoneEt = findViewById(R.id.phone);
        passwordEt = findViewById(R.id.password);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        loginText =findViewById(R.id.loginText);
        fStore =FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        //User already logged in
        if(fAuth.getCurrentUser()  !=null){
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            finish();
        }

        loginText .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String token = tokenEt.getText().toString().trim();
                String phone = phoneEt.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    emailEt.setError("Email is Required");
                    return;

                }
                if(token.length()<11){
                    tokenEt.setError("Token Must be >=11 Characters");
                }

                if(TextUtils.isEmpty(password)){
                    passwordEt.setError("Password is Required");
                    return;
                }

                if (password.length()<6){
                    passwordEt.setError("Password Must be >= 6 Characters");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email,password) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegisterActivity.this,"Verification Email Sent.",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d (TAG,"onFailure:Email not sent "+e.getMessage());
                                }
                            });

                            Toast.makeText(RegisterActivity.this,"User is Created.",Toast.LENGTH_SHORT).show();
                            customerID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference= fStore.collection("customer").document(customerID);
                            Map<String,Object> customer = new HashMap<>();
                            customer.put("Token",token);
                            customer.put("Email",email);
                            customer.put("Password",password);
                            customer.put("phoneNumber",phone);


                            documentReference.set(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onSuccess:user profile is created for "+customerID);

                                }
                            });

                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this,"Error !" +task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });




    }
}
