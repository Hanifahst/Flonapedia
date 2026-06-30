package com.example.endemikdb;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.endemikdb.databinding.ActivityFavoritBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FavoritActivity extends AppCompatActivity {

    private ActivityFavoritBinding binding;
    private AppDatabase db;
    private FavoritAdapter adapter;
    private final List<Favorit> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getDatabase(this);

        adapter = new FavoritAdapter(this, list);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorit();
    }

    private void loadFavorit() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Favorit> result = db.favoritDAO().getAll();
            runOnUiThread(() -> {
                list.clear();
                list.addAll(result);
                adapter.notifyDataSetChanged();
                binding.tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}