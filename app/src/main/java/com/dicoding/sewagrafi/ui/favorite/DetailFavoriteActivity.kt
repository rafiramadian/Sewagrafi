package com.dicoding.sewagrafi.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.sewagrafi.R
import com.dicoding.sewagrafi.database.Favorite
import com.dicoding.sewagrafi.databinding.ActivityDetailFavoriteBinding
import com.dicoding.sewagrafi.helper.ViewModelFactory
import com.dicoding.sewagrafi.model.FavoriteViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFavoriteActivity : AppCompatActivity() {
    private var _binding: ActivityDetailFavoriteBinding? = null
    private val binding get() = _binding as ActivityDetailFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var auth: FirebaseAuth

    private var favorite: Favorite? = null
    private var isChecked = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<Favorite>(EXTRA_FAVORITE) as Favorite
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        binding.apply {
            val tersedia = "Tersedia"
            tvNamaKamera.text = item.merk
            tvJenisKamera.text = item.jenis
            tvSpesifikasi.text = item.spesifikasi
            tvHargaSewa.text = item.hargaSewa
            tvStatus.text = tersedia
            Glide.with(this@DetailFavoriteActivity)
                .load(item.gambar)
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgKamera)

            favorite = Favorite(
                item.merk,
                item.jenis,
                item.hargaSewa,
                item.spesifikasi,
                item.status,
                item.gambar,
                uid
            )

            favoriteViewModel = obtainViewModel(this@DetailFavoriteActivity)

            CoroutineScope(Dispatchers.IO).launch {
                val count = favoriteViewModel.checkKamera(item.merk)
                withContext(Dispatchers.Main) {
                    if (count > 0) {
                        btnFavorite.isSelected = true
                        isChecked = false
                    } else {
                        btnFavorite.isSelected = false
                        isChecked = true
                    }
                }
            }

            btnFavorite.setOnClickListener {
                if (isChecked) {
                    favoriteViewModel.insert(favorite as Favorite)
                    showToast("Berhasil ditambahkan!")
                } else {
                    favoriteViewModel.delete(favorite as Favorite)
                    showToast("Berhasil dihapus!")
                }
                btnFavorite.isSelected = isChecked
                isChecked = !isChecked
            }
        }

        supportActionBar?.title = produk
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
        const val produk = "Produk"
    }
}