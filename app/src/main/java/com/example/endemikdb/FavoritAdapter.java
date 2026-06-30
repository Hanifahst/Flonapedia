package com.example.endemikdb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.endemikdb.databinding.ItemEndemikBinding;

import java.util.List;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.ViewHolder> {

    private final List<Favorit> list;
    private final Context context;

    public FavoritAdapter(Context context, List<Favorit> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEndemikBinding binding = ItemEndemikBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorit item = list.get(position);
        holder.binding.tvNama.setText(item.getNama());

        Glide.with(context)
                .load(item.getGambar())
                .centerCrop()
                .placeholder(R.drawable.baseline_broken_image_24)
                .into(holder.binding.ivGambar);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("extra_id", item.getIdEndemik());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return list != null ? list.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemEndemikBinding binding;
        public ViewHolder(ItemEndemikBinding b) {
            super(b.getRoot());
            this.binding = b;
        }
    }
}