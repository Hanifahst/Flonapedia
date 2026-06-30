package com.example.endemikdb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.endemikdb.databinding.FragmentEndemikBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class EndemikFragment extends Fragment {

    private static final String ARG_KATEGORI = "kategori";
    private String kategori;
    private FragmentEndemikBinding binding;
    private EndemikAdapter adapter;
    private final List<Endemik> list = new ArrayList<>();

    public static EndemikFragment newInstance(String kategori) {
        EndemikFragment f = new EndemikFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KATEGORI, kategori);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kategori = getArguments().getString(ARG_KATEGORI);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEndemikBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new EndemikAdapter(requireContext(), list);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerView.setAdapter(adapter);

        loadData();
    }

    public void loadData() {
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Endemik> result = db.endemikDAO().getByKategori(kategori);
            requireActivity().runOnUiThread(() -> {
                list.clear();
                list.addAll(result);
                adapter.notifyDataSetChanged();

                if (list.isEmpty()) {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        int total = db.endemikDAO().getCount();
                        requireActivity().runOnUiThread(() ->
                                binding.tvEmpty.setText("Data kosong untuk kategori: " + kategori +
                                        "\nTotal di ROOM: " + total +
                                        "\n(Periksa nilai field 'kategori' di JSON API)")
                        );
                    });
                } else {
                    binding.tvEmpty.setVisibility(View.GONE);
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}