package com.kostya_zinoviev.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://jsonplaceholder.typicode.com/posts";
    public static final String USER_ID = "userId";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String BODY = "body";

    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        resultText = findViewById(R.id.result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResultFromServer();
            }
        });

    }

    private void getResultFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();

                Request request = new Builder()
                        .url(BASE_URL)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("Result","Error " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String result  = response.body().string();
                        Log.i("res",result);
                        try {
                            //Мы засовываем единственный массив из строки result (это наши json данные)
                            JSONArray jsonArray = new JSONArray(result);
                            //После мы у нашего jsonArray (который у нас один),тоесть 1 массив,
                            // но у этого массива 100 обЪектов,мы в цикле получеам первые 5 обЪектов данного массива
                            for (int i = 0; i <5 ; i++) {

                                //Здесь мы будем получать первые 5 обЪектов массива jsonArray
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String userId = jsonObject.getString(USER_ID);
                                String id = jsonObject.getString(ID);
                                String title = jsonObject.getString(TITLE);
                                String body = jsonObject.getString(BODY);

                                final String data = "User id " + userId + " id " + id + " title " + title + " body " + body + "\n";

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        resultText.append(data);
                                    }
                                });

                            }
//                            final String finalData = data;
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    resultText.append(finalData);
//                                }
//                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }).start();

    }
}
