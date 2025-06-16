package com.example.projectpab_distribusimbg_teori;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DalamProsesAdapter extends RecyclerView.Adapter<DalamProsesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemMarkedAsComplete(JadwalItem item);
    }

    private List<JadwalItem> jadwalList;
    private OnItemClickListener listener;

    public DalamProsesAdapter(List<JadwalItem> jadwalList, OnItemClickListener listener) {
        this.jadwalList = jadwalList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_dalam_proses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalItem item = jadwalList.get(position);
        holder.bind(item, listener);

        // Set up the button click listener
        holder.btnMarkComplete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemMarkedAsComplete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;
        private TextView time;
        private TextView packageCount;
        private Button btnMarkComplete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            packageCount = itemView.findViewById(R.id.packageCount);
            btnMarkComplete = itemView.findViewById(R.id.btnMarkComplete);
        }

        public void bind(JadwalItem item, OnItemClickListener listener) {
            title.setText(item.getTitle());
            date.setText(item.getDate());
            time.setText(item.getTime());
            packageCount.setText(item.getPackageCount());
        }
    }
}
