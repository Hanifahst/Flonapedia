package com.example.endemikdb;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.endemikdb.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private AppDatabase db;
    private EndemikAdapter adapter;
    private final List<Endemik> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getDatabase(this);

        adapter = new EndemikAdapter(this, list);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);

        binding.tvSearchHint.setVisibility(View.VISIBLE);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (keyword.isEmpty()) {
                    list.clear();
                    adapter.notifyDataSetChanged();
                    binding.tvSearchHint.setVisibility(View.VISIBLE);
                } else {
                    binding.tvSearchHint.setVisibility(View.GONE);
                    doSearch(keyword);
                }
            }
        });
    }

    private void doSearch(String keyword) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Endemik> result = db.endemikDAO().search("%" + keyword + "%");
            runOnUiThread(() -> {
                list.clear();
                list.addAll(result);
                adapter.notifyDataSetChanged();
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