package com.example.projectpab_distribusimbg_teori;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<List<JadwalPengirimanModel>> jadwalList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<JadwalPengirimanModel>> getJadwalList() {
        return jadwalList;
    }

    public MutableLiveData<List<JadwalPengirimanModel>> getMutableJadwalList() {
        return jadwalList;
    }

    public void addJadwal(JadwalPengirimanModel jadwal) {
        List<JadwalPengirimanModel> currentList = jadwalList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(jadwal);
        jadwalList.setValue(currentList);
    }

    public void removeJadwal(JadwalPengirimanModel jadwal) {
        List<JadwalPengirimanModel> currentList = jadwalList.getValue();
        if (currentList == null) {
            return; // Tidak ada yang perlu dihapus jika daftar null
        }
        currentList.remove(jadwal);
        jadwalList.setValue(currentList);
    }
}
