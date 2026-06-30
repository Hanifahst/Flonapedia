package com.example.endemikdb;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.endemikdb.databinding.ActivityDetailBinding;

import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private AppDatabase db;
    private Endemik endemik;
    private MenuItem favoritMenuItem;
    private boolean isFavorit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        db = AppDatabase.getDatabase(this);

        int id = getIntent().getIntExtra("extra_id", -1);
        if (id == -1) {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadDetail(id);
    }

    private void loadDetail(int id) {
        Executors.newSingleThreadExecutor().execute(() -> {
            endemik = db.endemikDAO().getById(id);
            isFavorit = db.favoritDAO().isFavorit(id) > 0;

            runOnUiThread(() -> {
                if (endemik == null) {
                    Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                binding.toolbar.setTitle(endemik.getNama());
                binding.tvNama.setText(endemik.getNama());
                binding.tvDeskripsi.setText(endemik.getDeskripsi());

                setInfo(binding.tvNamaLatin, endemik.getNamaLatin());
                setInfo(binding.tvFamili,    endemik.getFamili());
                setInfo(binding.tvGenus,     endemik.getGenus());
                setInfo(binding.tvAsal,      endemik.getAsal());
                setInfo(binding.tvSebaran,   endemik.getSebaran());
                setInfo(binding.tvStatus,    endemik.getStatus());

                Glide.with(this)
                        .load(endemik.getGambar())
                        .centerCrop()
                        .placeholder(R.drawable.baseline_broken_image_24)
                        .error(R.drawable.baseline_broken_image_24)
                        .into(binding.ivGambar);

                updateFavoritIcon();
            });
        });
    }

    private void setInfo(android.widget.TextView tv, String value) {
        tv.setText((value != null && !value.isEmpty()) ? value : "-");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        favoritMenuItem = menu.findItem(R.id.action_favorit);
        updateFavoritIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_favorit) {
            toggleFavorit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleFavorit() {
        if (endemik == null) return;
        Executors.newSingleThreadExecutor().execute(() -> {
            if (isFavorit) {
                db.favoritDAO().delete(endemik.getId());
                isFavorit = false;
            } else {
                db.favoritDAO().insert(new Favorit(
                        endemik.getId(),
                        endemik.getNama(),
                        endemik.getGambar(),
                        endemik.getKategori()
                ));
                isFavorit = true;
            }
            runOnUiThread(() -> {
                updateFavoritIcon();
                Toast.makeText(this,
                        isFavorit ? "Ditambahkan ke favorit ❤️" : "Dihapus dari favorit",
                        Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void updateFavoritIcon() {
        if (favoritMenuItem == null) return;
        favoritMenuItem.setIcon(isFavorit
                ? R.drawable.baseline_favorite_24
                : R.drawable.baseline_favorite_border_24);
    }
}