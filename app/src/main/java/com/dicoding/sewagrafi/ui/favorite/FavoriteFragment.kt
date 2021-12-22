package com.dicoding.sewagrafi.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sewagrafi.R
import com.dicoding.sewagrafi.adapter.FavoriteAdapter
import com.dicoding.sewagrafi.adapter.KameraAdapter
import com.dicoding.sewagrafi.database.Favorite
import com.dicoding.sewagrafi.database.Kamera
import com.dicoding.sewagrafi.databinding.FragmentFavoriteBinding
import com.dicoding.sewagrafi.helper.ViewModelFactory
import com.dicoding.sewagrafi.model.FavoriteViewModel
import com.dicoding.sewagrafi.model.KameraViewModel
import com.google.firebase.auth.FirebaseAuth

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding as FragmentFavoriteBinding

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser?.uid

        favoriteViewModel = obtainViewModel(requireActivity() as AppCompatActivity)

        favoriteViewModel.getAllFavorite(uid).observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setListFavorites(it)
            }
        })

        adapter = FavoriteAdapter()
        initRecyclerList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun initRecyclerList() {
        binding.rvSewaKamera.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        binding.rvSewaKamera.setHasFixedSize(true)
        binding.rvSewaKamera.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvSewaKamera.adapter = adapter
    }
}