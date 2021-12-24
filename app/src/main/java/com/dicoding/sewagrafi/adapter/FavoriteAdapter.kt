package com.dicoding.sewagrafi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.sewagrafi.R
import com.dicoding.sewagrafi.database.Favorite
import com.dicoding.sewagrafi.databinding.ItemRowKameraBinding
import com.dicoding.sewagrafi.helper.FavoriteDiffCallback
import com.dicoding.sewagrafi.ui.beranda.DetailActivity
import com.dicoding.sewagrafi.ui.favorite.DetailFavoriteActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorites = ArrayList<Favorite>()

    fun setListFavorites(listFavorites: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorites, listFavorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(listFavorites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRowKameraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position])

    }

    override fun getItemCount(): Int {
        return listFavorites.size
    }

    inner class FavoriteViewHolder(private val binding: ItemRowKameraBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            with(binding) {
                tvNamaKamera.text = favorite.merk
                tvJenisKamera.text = favorite.jenis
                tvHargaSewa.text = favorite.hargaSewa
                Glide.with(itemView)
                    .load(favorite.gambar)
                    .placeholder(R.drawable.loading_icon)
                    .error(R.drawable.ic_baseline_person_24)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgKamera)
                itemView.setOnClickListener{
                    val intent = Intent(it.context, DetailFavoriteActivity::class.java)
                    intent.putExtra(DetailFavoriteActivity.EXTRA_FAVORITE, favorite)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}