package com.dicoding.sewagrafi.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.sewagrafi.database.Favorite

class FavoriteDiffCallback(private val mOldFavoriteList: List<Favorite>, private val mNewFavoriteList: List<Favorite>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldFavoriteList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavoriteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavoriteList[oldItemPosition].merk == mNewFavoriteList[newItemPosition].merk
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldFavoriteList[oldItemPosition]
        val newFavorite = mNewFavoriteList[newItemPosition]
        return oldFavorite.merk == newFavorite.merk &&
                oldFavorite.jenis == newFavorite.jenis &&
                oldFavorite.hargaSewa == newFavorite.hargaSewa &&
                oldFavorite.spesifikasi == newFavorite.spesifikasi &&
                oldFavorite.status == newFavorite.status &&
                oldFavorite.gambar == newFavorite.gambar &&
                oldFavorite.uid == newFavorite.uid
    }
}