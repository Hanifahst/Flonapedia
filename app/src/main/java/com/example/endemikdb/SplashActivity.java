package com.example.endemikdb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.endemikdb.databinding.ActivitySplashBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    private static final int MAX_RETRY = 3;

    private ActivitySplashBinding binding;
    private AppDatabase db;
    private RequestQueue requestQueue;
    private ExecutorService dbExecutor;
    private Handler retryHandler;
    private int retryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getDatabase(this);
        requestQueue = Volley.newRequestQueue(this);
        dbExecutor = Executors.newSingleThreadExecutor();
        retryHandler = new Handler(Looper.getMainLooper());

        dbExecutor.execute(() -> {
            int hewan = db.endemikDAO().getCountHewan();
            int tumbuhan = db.endemikDAO().getCountTumbuhan();
            boolean dataValid = (hewan > 0 && tumbuhan > 0);

            runOnUiThreadSafely(() -> {
                binding.progressFetch.setVisibility(View.VISIBLE);

                if (dataValid) {
                    retryHandler.postDelayed(this::goToHome, 1500);
                } else {
                    fetchDataFromApi();
                }
            });
        });
    }

    private void fetchDataFromApi() {
        binding.progressFetch.setVisibility(View.VISIBLE);

        String url = "https://hendroprwk08.github.io/data_endemik/endemik.json";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    List<Endemik> list = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject o = response.getJSONObject(i);

                            int id          = i + 1;
                            String nama     = o.optString("nama", "");
                            String gambar   = o.optString("foto", "");
                            String desk     = o.optString("deskripsi", "");
                            String kat      = o.optString("tipe", "");
                            String wilayah  = "";
                            String namaLat  = o.optString("nama_latin", "");
                            String famili   = o.optString("famili", "");
                            String genus    = o.optString("genus", "");
                            String asal     = o.optString("asal", "");
                            String sebaran  = o.optString("sebaran", "");
                            String status   = o.optString("status", "");

                            list.add(new Endemik(
                                    id, nama, gambar, desk, kat, wilayah,
                                    namaLat, famili, genus, asal, sebaran, status
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showError("Gagal membaca data JSON");
                        return;
                    }

                    dbExecutor.execute(() -> {
                        db.endemikDAO().insertAll(list);
                        runOnUiThreadSafely(this::goToHome);
                    });
                },
                error -> showError("Gagal terhubung ke server. Periksa koneksi!")
        );
        requestQueue.add(request);
    }

    private void showError(String msg) {
        if (isFinishing() || isDestroyed()) return;

        binding.progressFetch.setVisibility(View.GONE);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        retryCount++;
        if (retryCount < MAX_RETRY) {
            retryHandler.postDelayed(this::fetchDataFromApi, 2000);
        } else {
            Toast.makeText(this,
                    "Gagal memuat data setelah beberapa percobaan. Periksa koneksi internet kamu, lalu buka ulang aplikasi.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void runOnUiThreadSafely(Runnable action) {
        runOnUiThread(() -> {
            if (!isFinishing() && !isDestroyed()) {
                action.run();
            }
        });
    }

    private void goToHome() {
        if (isFinishing() || isDestroyed()) return;
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (retryHandler != null) {
            retryHandler.removeCallbacksAndMessages(null);
        }
        if (requestQueue != null) {
            requestQueue.cancelAll(req -> true);
        }
        if (dbExecutor != null && !dbExecutor.isShutdown()) {
            dbExecutor.shutdown();
        }
        binding = null;
    }
}