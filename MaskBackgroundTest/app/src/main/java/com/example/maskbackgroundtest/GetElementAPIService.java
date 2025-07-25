package com.example.maskbackgroundtest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
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

    private String dataResponse;

    private final Context context;

    public GetElementAPIService(Context context) {
        this.context = context;
    }


    public void saveinFile(String json){
        var baseName = "jsonOutput";
        var extension = "json";
        File dir = context.getExternalFilesDir(null);
        int index = 0;
        File file;

        do {
            String fileName = (index == 0) ? baseName + "." + extension
                    : baseName + index + "." + extension;
            file = new File(dir, fileName);
            index++;
        } while (file.exists());

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
            Log.d("DEBUG_FILE", "Arquivo salvo: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("DEBUG_FILE", "Erro ao salvar o arquivo", e);
        }
    }

    public void getIdentifidor(List<ComponentModel> componentModelList){

        Gson gson = new Gson();

        OkHttpClient client = new OkHttpClient();

        String json = gson.toJson(componentModelList);

        saveinFile(json);

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("http://192.168.0.110:8080/component/getBounds")
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
//                    Log.e(TAG, "Response "+ responseBody);
                    dataResponse = responseBody;
                    Log.e(TAG, "onResponse: " + dataResponse );

                    Intent intentIdenfidor = new Intent("com.example.maskbackgroundtest.GET_IDENTIFOR_ELEMENT");
                    intentIdenfidor.putExtra("identifidor", dataResponse);
                    context.sendBroadcast(intentIdenfidor);

                }else{
//                    Log.e(TAG, response.message().toString() );
                    Log.e(TAG, "Erro: " +response.code());
                }

            }
        });
//

    }

}
