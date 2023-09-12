package com.example.retrofit2;



import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;

import android.content.DialogInterface;

import android.content.Intent;

import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;

import retrofit2.Callback;

import retrofit2.Response;

import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    TextView tu, tp, ti;

    Button b1;

    SharedPreferences sharedPreferences;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tu = findViewById(R.id.edituserid);

        tp = findViewById(R.id.editpassword);

        b1 = findViewById(R.id.button);



        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://vserveq.voltasworld.com/safetyapi/api/")
                .addConverterFactory(GsonConverterFactory
                .create())
                .build();

        Retrofitinterface retroApi = retrofit.create(Retrofitinterface.class);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                String username =tu.getText().toString();

                String password = tp.getText().toString();

                DataAPI dataapi = new DataAPI();

                dataapi.setLoginName(username);

                dataapi.setPassword(password);

                dataapi.setImei("");


                Call<LoginResponse> call = retroApi.SignResponse(dataapi);


                call.enqueue(new Callback<LoginResponse>() {

                    @Override

                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.isSuccessful()){


                            LoginResponse loginRespnse = response.body();


                            if (loginRespnse != null && loginRespnse.isSuccess()) {

                                saveData((LoginResult) loginRespnse.getResult());

//                                saveData((LoginResult) loginRespnse.getResult());

                                 loginRespnse.getResult();

                                Intent i = new Intent (MainActivity.this,Dashboard.class);

                                startActivity(i);

                                finish();

                            } else {


                                String errorMessage = loginRespnse.getErrors()[0];

                                showErrorDialog("Login Failed", errorMessage);

                            }

                        } else {

                            showErrorDialog("API Error", "API call failed.");

                        }

                    }

                    @Override

                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_LONG).show();

                        showErrorDialog("Network Error", "Network error. Please try again.");

                    }

                });


            }

        });


    }

    private void saveData(LoginResult result){

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId",result.getUserId());

        editor.putString("userName",result.getUserName());

        editor.putString("token", result.getToken());

        editor.apply();

    }

    private void showDialog(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)

                .setMessage(message)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {


                    }

                }).show();

    }

    private void showErrorDialog(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)

                .setMessage(message)

                .setPositiveButton("OK", null)

                .show();

    }

}