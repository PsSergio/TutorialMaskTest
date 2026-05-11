package com.example.maskbackgroundtest;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.MediaType;

public class GetElementAPIService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String TAG = "APIServiceGetElement";

    private String dataResponse;

    private final Context context;

    public GetElementAPIService(Context context) {
        this.context = context;
    }


    public void saveInFile(String json){
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

}
