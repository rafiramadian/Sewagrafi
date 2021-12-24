package com.dicoding.sewagrafi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.sewagrafi.database.History
import com.dicoding.sewagrafi.databinding.ItemRowHistoryBinding

class HistoryAdapter(private val listHistory: List<History>) : RecyclerView.Adapter<HistoryAdapter.ItemViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRowHistoryBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        with(viewHolder){
            with(listHistory[position]) {
                binding.apply {
                    tvNamaKamera.text = merk_kamera
                    tvTanggalPinjam.text = tanggal_pinjam
                    tvTanggalKembali.text = tanggal_kembali
                }
            }
        }
    }

    override fun getItemCount() = listHistory.size

    class ItemViewHolder(val binding: ItemRowHistoryBinding)
        : RecyclerView.ViewHolder(binding.root)
}