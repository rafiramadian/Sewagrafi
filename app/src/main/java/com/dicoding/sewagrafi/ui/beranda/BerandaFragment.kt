package com.dicoding.sewagrafi.ui.beranda

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sewagrafi.R
import com.dicoding.sewagrafi.adapter.KameraAdapter
import com.dicoding.sewagrafi.database.Kamera
import com.dicoding.sewagrafi.databinding.FragmentBerandaBinding
import com.dicoding.sewagrafi.helper.ViewModelFactory
import com.dicoding.sewagrafi.model.KameraViewModel

class BerandaFragment : Fragment(R.layout.fragment_beranda) {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding as FragmentBerandaBinding

    private lateinit var kameraViewModel: KameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerList()

        kameraViewModel = obtainViewModel(requireActivity() as AppCompatActivity)

        initDataKamera()
        kameraViewModel.getAllKamera().observe(viewLifecycleOwner, {
            showRecyclerView(it)
        })


    }

    private fun initDataKamera() {
        val nama = resources.getStringArray(R.array.nama)
        val jenis = resources.getStringArray(R.array.jenis)
        val hargaSewa = resources.getStringArray(R.array.harga_sewa)
        val spesifikasi = resources.getStringArray(R.array.spesifikasi)
        val status = 1
        val gambar = resources.obtainTypedArray(R.array.gambar)
        for (i in nama.indices) {
            val kamera = Kamera(
                nama[i],
                jenis[i],
                hargaSewa[i],
                spesifikasi[i],
                status,
                gambar.getResourceId(i,0)
            )
            kameraViewModel.insert(kamera)
        }
        gambar.recycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): KameraViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(KameraViewModel::class.java)
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
    }

    private fun showRecyclerView(listKamera: List<Kamera>) {
        val listKameraAdapter = KameraAdapter(listKamera)
        binding.rvSewaKamera.adapter = listKameraAdapter
    }
}