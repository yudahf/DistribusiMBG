package com.example.projectpab_distribusimbg_teori;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<Account> accountList;
    private OnItemClickListener listener; // Interface untuk klik item/tombol

    // Interface untuk menangani klik pada item atau tombol di dalamnya
    public interface OnItemClickListener {
        void onEditClick(Account account);
        void onDeleteClick(Account account);
    }

    public AccountAdapter(List<Account> accountList, OnItemClickListener listener) {
        this.accountList = accountList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_akun, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.tvNamaAkun.setText(account.getNama());
        holder.tvRoleAkun.setText(account.getJabatan());

        // Set listener untuk tombol edit
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(account);
            }
        });

        // Set listener untuk tombol delete
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(account);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public void updateData(List<Account> newAccountList) {
        this.accountList.clear();
        this.accountList.addAll(newAccountList);
        notifyDataSetChanged();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaAkun;
        TextView tvRoleAkun;
        ImageView btnEdit;
        ImageView btnDelete;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaAkun = itemView.findViewById(R.id.tvNamaAkun);
            tvRoleAkun = itemView.findViewById(R.id.tvRoleAkun);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
