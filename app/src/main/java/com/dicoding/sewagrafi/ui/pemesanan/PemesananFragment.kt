package com.dicoding.sewagrafi.ui.pemesanan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sewagrafi.adapter.HistoryAdapter
import com.dicoding.sewagrafi.database.History
import com.dicoding.sewagrafi.databinding.FragmentPemesananBinding
import com.dicoding.sewagrafi.helper.ViewModelFactory
import com.dicoding.sewagrafi.model.BookViewModel
import com.dicoding.sewagrafi.model.HistoryViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PemesananFragment : Fragment() {
    private var _binding: FragmentPemesananBinding? = null
    private val binding get() = _binding as FragmentPemesananBinding

    private lateinit var bookViewModel: BookViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var auth: FirebaseAuth

    private var uid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPemesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid

        bookViewModel = obtainBookViewModel(requireActivity() as AppCompatActivity)
        historyViewModel = obtainHistoryViewModel(requireActivity() as AppCompatActivity)

        initRecyclerList()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateNow = dateFormat.format(calendar.time)
        val timeNow = timeFormat.format(calendar.time)
        val tanggalNow = dateNow.split("-").toTypedArray()
        val waktuNow = timeNow.split(":").toTypedArray()

        val tglNow = Integer.parseInt(tanggalNow[2])
        val blnNow = Integer.parseInt(tanggalNow[1]) - 1
        val jamNow = Integer.parseInt(waktuNow[0])
        val menitNow = Integer.parseInt(waktuNow[1])

        bookViewModel.getAllBooks(uid).observe(viewLifecycleOwner, {
            if (it != null) {
                for (i in it.indices) {

                    val dateKembali = it[i].tanggal_kembali?.split("-")!!.toTypedArray()
                    val timeKembali = it[i].waktu_kembali?.split(":")!!.toTypedArray()

                    val tglKembali = Integer.parseInt(dateKembali[2])
                    val blnKembali = Integer.parseInt(dateKembali[1]) - 1
                    val jamKembali = Integer.parseInt(timeKembali[0])
                    val menitKembali = Integer.parseInt(timeKembali[1])

                    val history = History()
                    history.merk_kamera = it[i].merk_kamera
                    history.uid = it[i].uid
                    history.tanggal_pinjam = it[i].tanggal_pinjam
                    history.tanggal_kembali = it[i].tanggal_kembali
                    history.waktu_pinjam = it[i].waktu_pinjam
                    history.waktu_kembali = it[i].waktu_kembali

                    CoroutineScope(Dispatchers.IO).launch {
                        val count = historyViewModel.checkHistory(
                            it[i].merk_kamera,
                            it[i].uid,
                            it[i].tanggal_pinjam,
                            it[i].tanggal_kembali,
                            it[i].waktu_pinjam,
                            it[i].waktu_kembali
                        )
                        withContext(Dispatchers.Main) {
                            if (count == 0) {
                                if (blnNow > blnKembali) {
                                    historyViewModel.insert(history)
                                } else if (blnNow == blnKembali) {
                                    if (tglNow > tglKembali) {
                                        historyViewModel.insert(history)
                                    } else if (tglNow == tglKembali) {
                                        if (jamNow > jamKembali) {
                                            historyViewModel.insert(history)
                                        } else if (jamNow == jamKembali){
                                            if (menitNow >= menitKembali) {
                                                historyViewModel.insert(history)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })

        historyViewModel.getAllHistory(uid).observe(viewLifecycleOwner, {
            if (it != null) {
                showRecyclerView(it)
            }
        })

    }

    private fun obtainBookViewModel(activity: AppCompatActivity): BookViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(BookViewModel::class.java)
    }
    private fun obtainHistoryViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    private fun initRecyclerList() {
        binding.rvSewaKamera.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvSewaKamera.setHasFixedSize(true)
        binding.rvSewaKamera.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun showRecyclerView(listHistory: List<History>) {
        val listHistoryAdapter = HistoryAdapter(listHistory)
        binding.rvSewaKamera.adapter = listHistoryAdapter
    }

    companion object {

    }
}