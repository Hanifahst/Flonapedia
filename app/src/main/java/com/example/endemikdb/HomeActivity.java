package com.example.endemikdb;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.example.endemikdb.databinding.ActivityHomeBinding;
import com.example.endemikdb.databinding.DialogProfilBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();

            if (id == R.id.nav_hewan) {
                fragment = EndemikFragment.newInstance("Hewan");
            } else {
                fragment = EndemikFragment.newInstance("Tumbuhan");
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_fragment, fragment)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            binding.bottomNav.setSelectedItemId(R.id.nav_hewan);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        tintMenuIcons(menu);
        return true;
    }

    private void tintMenuIcons(Menu menu) {
        TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, tv, true);
        int color = tv.data;

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable icon = item.getIcon();
            if (icon != null) {
                Drawable wrapped = DrawableCompat.wrap(icon.mutate());
                DrawableCompat.setTint(wrapped, color);
                item.setIcon(wrapped);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (id == R.id.action_favorit) {
            startActivity(new Intent(this, FavoritActivity.class));
            return true;
        } else if (id == R.id.action_profil) {
            showProfilDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProfilDialog() {
        DialogProfilBinding dialogBinding = DialogProfilBinding.inflate(getLayoutInflater());

        new MaterialAlertDialogBuilder(this)
                .setView(dialogBinding.getRoot())
                .setBackgroundInsetStart(40)
                .setBackgroundInsetEnd(40)
                .setPositiveButton("Tutup", null)
                .setNeutralButton("Toggle Dark/Light", (d, w) -> {
                    int current = AppCompatDelegate.getDefaultNightMode();
                    if (current == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                })
                .show();
    }
}