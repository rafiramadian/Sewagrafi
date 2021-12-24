package com.dicoding.sewagrafi.ui.beranda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.sewagrafi.R
import com.dicoding.sewagrafi.database.Book
import com.dicoding.sewagrafi.database.Favorite
import com.dicoding.sewagrafi.database.Kamera
import com.dicoding.sewagrafi.databinding.ActivityDetailBinding
import com.dicoding.sewagrafi.helper.AlarmReceiver
import com.dicoding.sewagrafi.helper.ViewModelFactory
import com.dicoding.sewagrafi.model.BookViewModel
import com.dicoding.sewagrafi.model.FavoriteViewModel
import com.dicoding.sewagrafi.model.KameraViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener, View.OnClickListener {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding as ActivityDetailBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var bookViewModel: BookViewModel
    private lateinit var kameraViewModel: KameraViewModel
    private lateinit var auth: FirebaseAuth

    private var kameraBooked: Kamera? = null
    private var kameraReady: Kamera? = null
    private var favorite: Favorite? = null
    private var book: Book? = null
    private var merk: String = ""
    private var isChecked = true
    private var isCheckedBook = true
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<Kamera>(EXTRA_KAMERA) as Kamera
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid

        favoriteViewModel = obtainFavoriteViewModel(this@DetailActivity)
        bookViewModel = obtainBookViewModel(this@DetailActivity)
        kameraViewModel = obtainKameraViewModel(this@DetailActivity)

        binding.apply {
            val statusBooked = 0
            val statusReady = 1
            kameraBooked = Kamera(
                item.merk,
                item.jenis,
                item.hargaSewa,
                item.spesifikasi,
                statusBooked,
                item.gambar
            )

            kameraReady = Kamera(
                item.merk,
                item.jenis,
                item.hargaSewa,
                item.spesifikasi,
                statusReady,
                item.gambar
            )

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

            bookViewModel.getBookDetail(item.merk).observe(this@DetailActivity, {
                if (it != null) {
                    for (i in it.indices) {
                        val datePinjam = it[i].tanggal_pinjam?.split("-")!!.toTypedArray()
                        val dateKembali = it[i].tanggal_kembali?.split("-")!!.toTypedArray()
                        val timePinjam = it[i].waktu_pinjam?.split(":")!!.toTypedArray()
                        val timeKembali = it[i].waktu_kembali?.split(":")!!.toTypedArray()

                        val tglPinjam = Integer.parseInt(datePinjam[2])
                        val blnPinjam = Integer.parseInt(datePinjam[1]) - 1
                        val tglKembali = Integer.parseInt(dateKembali[2])
                        val blnKembali = Integer.parseInt(dateKembali[1]) - 1
                        val jamPinjam = Integer.parseInt(timePinjam[0])
                        val menitPinjam = Integer.parseInt(timePinjam[1])
                        val jamKembali = Integer.parseInt(timeKembali[0])
                        val menitKembali = Integer.parseInt(timeKembali[1])

                        if (blnNow > blnPinjam) {
                            kameraViewModel.update(kameraBooked as Kamera)
                        } else if (blnNow == blnPinjam) {
                            if (tglNow > tglPinjam) {
                                kameraViewModel.update(kameraBooked as Kamera)
                            } else if (tglNow == tglPinjam) {
                                if (jamNow > jamPinjam) {
                                    kameraViewModel.update(kameraBooked as Kamera)
                                } else if (jamNow == jamPinjam){
                                    if (menitNow >= menitPinjam) {
                                        kameraViewModel.update(kameraBooked as Kamera)
                                    }
                                }
                            }
                        }

                        if (blnNow > blnKembali) {
                            kameraViewModel.update(kameraReady as Kamera)
                        } else if (blnNow == blnKembali) {
                            if (tglNow > tglKembali) {
                                kameraViewModel.update(kameraReady as Kamera)
                            } else if (tglNow == tglKembali) {
                                if (jamNow > jamKembali) {
                                    kameraViewModel.update(kameraReady as Kamera)
                                } else if (jamNow == jamKembali){
                                    if (menitNow >= menitKembali) {
                                        kameraViewModel.update(kameraReady as Kamera)
                                    }
                                }
                            }
                        }
                    }
                }
            })

            kameraViewModel.getKamera(item.merk).observe(this@DetailActivity,{
                if (it[0].status == 1) {
                    tvStatus.text = tersedia
                    linearBook.visibility = View.VISIBLE
                    btnBook.visibility = View.VISIBLE
                } else {
                    tvStatus.text = no_tersedia
                    linearBook.visibility = View.GONE
                    btnBook.visibility = View.GONE
                }
            })

            tvNamaKamera.text = item.merk
            tvJenisKamera.text = item.jenis
            tvSpesifikasi.text = item.spesifikasi
            tvHargaSewa.text = item.hargaSewa
            Glide.with(this@DetailActivity)
                .load(item.gambar)
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgKamera)

            merk = item.merk

            favorite = Favorite(
                item.merk,
                item.jenis,
                item.hargaSewa,
                item.spesifikasi,
                item.status,
                item.gambar,
                uid
            )

            CoroutineScope(Dispatchers.IO).launch {
                val count = favoriteViewModel.checkKamera(item.merk)
                withContext(Dispatchers.Main) {
                    if (count > 0) {
                        btnFavorite.isChecked = true
                        isChecked = false
                    } else {
                        btnFavorite.isChecked = false
                        isChecked = true
                    }
                }
            }

            btnBook.setOnClickListener(this@DetailActivity)
            btnFavorite.setOnClickListener(this@DetailActivity)
            btnTanggalPinjam.setOnClickListener(this@DetailActivity)
            btnTanggalKembali.setOnClickListener(this@DetailActivity)
            btnJamPinjam.setOnClickListener(this@DetailActivity)
            btnJamKembali.setOnClickListener(this@DetailActivity)
            backToMain.setOnClickListener {
                onBackPressed()
            }

            alarmReceiver = AlarmReceiver()

        }
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_favorite -> {
                if (isChecked) {
                    favoriteViewModel.insert(favorite as Favorite)
                    showToast("Berhasil ditambahkan!")
                } else {
                    favoriteViewModel.delete(favorite as Favorite)
                    showToast("Berhasil dihapus!")
                }
                binding.btnFavorite.isChecked = isChecked
                isChecked = !isChecked
            }
            R.id.btn_tanggal_pinjam -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PINJAM_PICKER_TAG)
            }
            R.id.btn_tanggal_kembali -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_KEMBALI_PICKER_TAG)
            }
            R.id.btn_jam_pinjam -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PINJAM_PICKER_TAG)
            }
            R.id.btn_jam_kembali -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_KEMBALI_PICKER_TAG)
            }
            R.id.btn_book -> {
                binding.apply {
                    val tanggalPinjam = tvTanggalPinjam.text.toString().trim()
                    val tanggalKembali = tvTanggalKembali.text.toString().trim()
                    val waktuPinjam = tvJamPinjam.text.toString().trim()
                    val waktuKembali = tvJamKembali.text.toString().trim()

                    if (tanggalPinjam == "Tanggal Pinjam") {
                        showToast("Tanggal atau Waktu Peminjaman tidak boleh kosong!")
                        return
                    } else if (waktuPinjam == "Jam Pinjam") {
                        showToast("Tanggal atau Waktu Peminjaman tidak boleh kosong!")
                        return
                    } else if (tanggalKembali == "Tanggal Kembali") {
                        showToast("Tanggal atau Waktu Peminjaman tidak boleh kosong!")
                        return
                    } else if (waktuKembali == "Jam Kembali") {
                        showToast("Tanggal atau Waktu Peminjaman tidak boleh kosong!")
                        return
                    }

                    val datePinjam = tanggalPinjam.split("-").toTypedArray()
                    val dateKembali = tanggalKembali.split("-").toTypedArray()
                    val timePinjam = waktuPinjam.split(":").toTypedArray()
                    val timeKembali = waktuKembali.split(":").toTypedArray()

                    val tglPinjam = Integer.parseInt(datePinjam[2])
                    val blnPinjam = Integer.parseInt(datePinjam[1]) - 1
                    val tglKembali = Integer.parseInt(dateKembali[2])
                    val blnKembali = Integer.parseInt(dateKembali[1]) - 1
                    val jamPinjam = Integer.parseInt(timePinjam[0])
                    val menitPinjam = Integer.parseInt(timePinjam[1])
                    val jamKembali = Integer.parseInt(timeKembali[0])
                    val menitKembali = Integer.parseInt(timeKembali[1])

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

                    if (blnPinjam < blnNow) {
                        showToast("Bulan Pinjam tidak bisa kurang dari Bulan Sekarang!")
                        return
                    } else if (tglPinjam < tglNow) {
                        showToast("Tanggal Pinjam tidak bisa kurang dari Tanggal Sekarang!")
                        return
                    } else if (tglPinjam == tglNow) {
                        if (jamPinjam < jamNow) {
                            showToast("Jam Pinjam tidak bisa kurang dari Jam Sekarang!")
                            return
                        } else if (jamPinjam == jamNow) {
                            if (menitPinjam < menitNow) {
                                showToast("Menit Pinjam tidak bisa kurang dari Menit Sekarang!")
                                return
                            }
                        }
                    }

                    if (blnKembali < blnPinjam) {
                        showToast("Bulan Kembali tidak bisa kurang dari Bulan Pinjam!")
                        return
                    } else if (tglKembali < tglPinjam) {
                        showToast("Tanggal Kembali tidak bisa kurang dari Tanggal Pinjam!")
                        return
                    } else if (tglKembali == tglPinjam) {
                        if (jamKembali < jamPinjam) {
                            showToast("Jam Kembali tidak bisa kurang dari Jam Pinjam!")
                            return
                        } else if (jamKembali == jamPinjam) {
                            if (menitKembali <= menitPinjam) {
                                showToast("Menit Pinjam tidak bisa kurang dari Menit Sekarang!")
                                return
                            }
                        }
                    }

                    isChecked = true

                    bookViewModel.getBookDetail(merk).observe(this@DetailActivity, {
                        if (it != null) {
                            for (i in it.indices) {
                                Log.d(DetailActivity::class.java.simpleName, "CEK DULU $isCheckedBook")
                                val datePinjamBook = it[i].tanggal_pinjam?.split("-")!!.toTypedArray()
                                val dateKembaliBook = it[i].tanggal_kembali?.split("-")!!.toTypedArray()
                                val timePinjamBook = it[i].waktu_pinjam?.split(":")!!.toTypedArray()
                                val timeKembaliBook = it[i].waktu_kembali?.split(":")!!.toTypedArray()

                                val tglPinjamBook = Integer.parseInt(datePinjamBook[2])
                                val blnPinjamBook = Integer.parseInt(datePinjamBook[1]) - 1
                                val blnKembaliBook = Integer.parseInt(dateKembaliBook[1]) - 1
                                val jamPinjamBook = Integer.parseInt(timePinjamBook[0])
                                val jamKembaliBook = Integer.parseInt(timeKembaliBook[0])
                                val menitPinjamBook = Integer.parseInt(timePinjamBook[1])
                                val menitKembaliBook = Integer.parseInt(timeKembaliBook[1])

                                if (blnPinjam == blnPinjamBook) {
                                    if (tglPinjam == tglPinjamBook) {
                                        if (jamPinjam == jamPinjamBook) {
                                            if (menitPinjam == menitPinjamBook){
                                                isCheckedBook = false
                                            } else if (jamPinjam == jamKembaliBook) {
                                                if (menitPinjam < menitKembaliBook){
                                                    isCheckedBook = false
                                                }
                                            }
                                        } else if (jamPinjam > jamPinjamBook) {
                                            if (jamPinjam == jamKembaliBook) {
                                                if (menitPinjam < menitKembaliBook) {
                                                    isCheckedBook = false
                                                }
                                            } else if (jamPinjam < jamKembaliBook) {
                                                isCheckedBook = false
                                            }
                                        }
                                    } else if (tglPinjam > tglPinjamBook) {
                                        if (tglPinjam < tglKembali) {
                                            isCheckedBook = false
                                        }
                                    }
                                } else if (blnPinjam in blnPinjamBook..blnKembaliBook) {
                                    isCheckedBook = false
                                }
                            }

                            Log.d(DetailActivity::class.java.simpleName, "CEK DULU $isCheckedBook")

                            if (isCheckedBook) {
                                showToast("Berhasil booking kamera!")

                                alarmReceiver.setOneTimeAlarm(this@DetailActivity, AlarmReceiver.TYPE_ONE_TIME,
                                    tanggalKembali,
                                    waktuKembali,
                                    "Booking telah selesai"
                                )

                                book = Book()
                                book?.merk_kamera = merk
                                book?.uid = uid
                                book?.tanggal_pinjam = tanggalPinjam
                                book?.tanggal_kembali = tanggalKembali
                                book?.waktu_pinjam = waktuPinjam
                                book?.waktu_kembali = waktuKembali
                                bookViewModel.insert(book as Book)
                                finish()
                            } else {
                                showToast("Sudah ada yang booking")
                            }
                        }
                    })
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        // Siapkan date formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Set text dari textview once
        if (tag == DATE_PINJAM_PICKER_TAG) {
            binding.tvTanggalPinjam.text = dateFormat.format(calendar.time)
        } else {
            binding.tvTanggalKembali.text = dateFormat.format(calendar.time)
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        // Siapkan time formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        // Set text dari textview berdasarkan tag
        when (tag) {
            TIME_PINJAM_PICKER_TAG -> binding.tvJamPinjam.text = dateFormat.format(calendar.time)
            TIME_KEMBALI_PICKER_TAG -> binding.tvJamKembali.text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }

    private fun obtainKameraViewModel(activity: AppCompatActivity): KameraViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(KameraViewModel::class.java)
    }

    private fun obtainBookViewModel(activity: AppCompatActivity): BookViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(BookViewModel::class.java)
    }

    private fun obtainFavoriteViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_KAMERA = "extra_kamera"
        private const val tersedia = "Tersedia"
        private const val no_tersedia = "Tidak Tersedia"
        private const val DATE_PINJAM_PICKER_TAG = "DatePinjamPicker"
        private const val DATE_KEMBALI_PICKER_TAG = "DateKembaliPicker"
        private const val TIME_PINJAM_PICKER_TAG = "TimePinjamPicker"
        private const val TIME_KEMBALI_PICKER_TAG = "TimeKembaliPicker"
    }
}