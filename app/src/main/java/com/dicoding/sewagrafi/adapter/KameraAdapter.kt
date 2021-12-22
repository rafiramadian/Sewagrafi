package com.dicoding.sewagrafi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.sewagrafi.R
import com.dicoding.sewagrafi.database.Kamera
import com.dicoding.sewagrafi.databinding.ItemRowKameraBinding
import com.dicoding.sewagrafi.ui.beranda.DetailActivity

class KameraAdapter(private val listKamera: List<Kamera>) : RecyclerView.Adapter<KameraAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRowKameraBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        with(viewHolder){
            with(listKamera[position]) {
                binding.apply {
                    tvNamaKamera.text = merk
                    tvJenisKamera.text = jenis
                    tvHargaSewa.text = hargaSewa
                    Glide.with(itemView)
                        .load(gambar)
                        .placeholder(R.drawable.loading_icon)
                        .error(R.drawable.ic_baseline_person_24)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imgKamera)
                    itemView.setOnClickListener{
                        val intent = Intent(it.context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_KAMERA, listKamera[position])
                        it.context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount() = listKamera.size

    class ItemViewHolder(val binding: ItemRowKameraBinding)
        : RecyclerView.ViewHolder(binding.root)
}