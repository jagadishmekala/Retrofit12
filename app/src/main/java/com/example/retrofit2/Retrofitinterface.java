package com.example.retrofit2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Retrofitinterface {

        //https://vserveq.voltasworld.com/safetyapi/api/User/SignIn
        @POST("User/SignIn/")
//        Call<DataAPI> SignResponse (@Body DataAPI dataaapi);
//        @GET()
        Call<LoginResponse> SignResponse(@Body DataAPI dataapi);
}
