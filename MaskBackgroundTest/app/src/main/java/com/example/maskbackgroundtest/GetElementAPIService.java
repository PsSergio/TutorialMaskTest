package com.example.maskbackgroundtest;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;

public class GetElementAPIService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String TAG = "APIServiceGetElement";

    private String dataResponse;

    private final Context context;

    public GetElementAPIService(Context context) {
        this.context = context;
    }


    public void saveInFile(String json) {
        var baseName = "jsonOutput";
        var extension = "json";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Modern way for Android 10+ using MediaStore
            var values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, baseName + "." + extension);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/json");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                    if (outputStream != null) {
                        outputStream.write(json.getBytes());
                        Log.d("DEBUG_FILE", "Arquivo salvo no Downloads via MediaStore: " + uri.toString());
                    }
                } catch (IOException e) {
                    Log.e("DEBUG_FILE", "Erro ao salvar via MediaStore", e);
                }
            }
        } else {
            // Legacy way for Android 9 and below
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e("DEBUG_FILE", "Não foi possível criar o diretório Downloads");
            }
            
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

}
