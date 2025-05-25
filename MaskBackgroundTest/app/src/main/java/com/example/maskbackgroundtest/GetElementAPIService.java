package com.example.maskbackgroundtest;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetElementAPIService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String TAG = "APIServiceGetElement";

    public void getBounds(List<ComponentModel> componentModelList){
        Log.e(TAG, "inicio" );
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        String json = gson.toJson(componentModelList);

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("http://10.0.0.181:8080/component/getBounds")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage() );

                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();

                if(response.isSuccessful()) {
                    Log.e(TAG, "Response "+ responseBody);

                    BoundsModel bounds = gson.fromJson(responseBody, BoundsModel.class);

                }else{
//                    Log.e(TAG, response.message().toString() );
                    Log.e(TAG, "Erro: " +response.code());
                }

            }
        });
//        return rect;
    }

}
