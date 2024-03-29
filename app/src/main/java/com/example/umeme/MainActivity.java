package com.example.umeme;

import static com.example.umeme.Constants.BUSINESS_SHORT_CODE;
import static com.example.umeme.Constants.CALLBACKURL;
import static com.example.umeme.Constants.PARTYB;
import static com.example.umeme.Constants.PASSKEY;
import static com.example.umeme.Constants.TRANSACTION_TYPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.umeme.Models.AccessToken;
import com.example.umeme.Models.STKPush;
import com.example.umeme.Services.DarajaApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DarajaApiClient mApiClient;

    @BindView(R.id.amount) EditText mAmount;
    @BindView(R.id.phone)EditText mPhone;
    @BindView(R.id.token) Button token;
    String[] items ={"1","2","3","4","5","6","7"};
    FirebaseFirestore fStore;

    ArrayAdapter<String> adapterItems;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("myCh","My Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,"myCh")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentTitle("UMEME")
                .setContentText("Token Paid .Thank you For Using Umeme");

        notification = builder.build();

        notificationManagerCompat = NotificationManagerCompat.from(this);



        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.
        fStore = FirebaseFirestore.getInstance();
        adapterItems =new ArrayAdapter<String>(this,R.layout.list_item,items);


        token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),LandingActivity.class);
                startActivity(intent);
                finish();
                String phone = mPhone.getText().toString().trim();
                String amount = mAmount.getText().toString().trim();


                uploadData(phone,amount);
                if (phone.isEmpty()){
                    mPhone.setError("Value Required.");
                }
                if (amount.length()<3){
                    mAmount.setError("Cannot buy token of less amount");
                    return;

                }

                notificationManagerCompat.notify(1,notification);
            }

            private void uploadData(String phone, String amount) {
                String id = UUID.randomUUID().toString();

                Map<String,Object> lighting = new HashMap<>();
                lighting.put("id",id);
                lighting.put("Phone",phone);
                lighting.put("Amount",amount);


                fStore.collection("lighting").document(id).set(lighting)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //called when data is added successfully
                                Toast.makeText(MainActivity.this," Token Paid Successfully",Toast.LENGTH_SHORT).show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //called when there is an error
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();


                            }
                        });

            }
        });

        getAccessToken();

    }

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view== token){
            String phone = mPhone.getText().toString();
            String amount = mAmount.getText().toString();
            performSTKPush(phone,amount);
        }
    }


    public void performSTKPush(String phone,String amount) {

        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone),
                PARTYB,
                Utils.sanitizePhoneNumber(phone),
                CALLBACKURL,
                "Umeme", //Account reference
                "Umeme by Mugane"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {

                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {

                Timber.e(t);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}