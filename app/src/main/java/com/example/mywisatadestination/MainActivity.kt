package com.example.mywisatadestination

import android.Manifest
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.patloew.rxlocation.RxLocation
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), MainView {
    private lateinit var rxLocation: RxLocation
    private lateinit var presenter: LocationPresenter
    private var isTracking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Wisata Destination"
        init(savedInstanceState)
        rxLocation = RxLocation(this)
        rxLocation.setDefaultTimeout(15, TimeUnit.SECONDS);
        presenter = LocationPresenter(rxLocation)
    }


    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    private fun init(savedInstanceState: Bundle?) {
        loadWisataFragment(savedInstanceState)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        loadWisataFragment(savedInstanceState)
                    }
                    1 -> {
                        loadFavWisataFragment(savedInstanceState)
                    }
                }
            }

        })

        btnMyLoc.setOnClickListener {
            if (!isTracking) {
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) { /* ... */
                            presenter.attachView(this@MainActivity)
                            isTracking = true
                            btnMyLoc.text = "Stop"
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                            Toast.makeText(
                                applicationContext,
                                "Tidak Mendapatkan Izin untuk mengakses lokasi anda",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                            dialogIzin(token)
                        }
                    }).check()
            } else {
                btnMyLoc.text = "Periksa"
                isTracking = false
                txtMyLoc.text = "Alamat Ku"
                presenter.detachView()
            }

        }
    }

    private fun dialogIzin(token: PermissionToken?) {
        AlertDialog.Builder(this, R.style.MyDialogTheme)
            .setTitle("Izin mengakses lokasi")
            .setMessage("Izin untuk mengakses lokasi di butuhkan oleh aplikasi ini!")
            .setNegativeButton(
                "Batal"
            ) { dialog, _ ->
                dialog.dismiss()
                token!!.cancelPermissionRequest()
            }
            .setPositiveButton(
                "Mengerti"
            ) { dialog, _ ->
                dialog.dismiss()
                token!!.continuePermissionRequest()
            }
            .setOnDismissListener { token!!.cancelPermissionRequest() }
            .show()

    }

    private fun loadWisataFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    WisataFragment(), WisataFragment::class.java.simpleName
                )
                .commit()
        }
    }

    private fun loadFavWisataFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    FavWisataFragment(), FavWisataFragment::class.java.simpleName
                )
                .commit()
        }
    }

    override fun onLocationUpdate(location: Location?) {

    }

    override fun onAddressUpdate(address: Address?) {
        txtMyLoc.text = address?.let { getAddressText(it) }
    }

    override fun onLocationSettingsUnsuccessful() {
        Toast.makeText(
            applicationContext,
            "Location settings requirements not satisfied. Showing last known location if available.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getAddressText(address: Address): String? {
        var addressText: String? = ""
        val maxAddressLineIndex = address.maxAddressLineIndex
        for (i in 0..maxAddressLineIndex) {
            addressText += address.getAddressLine(i)
            if (i != maxAddressLineIndex) {
                addressText += "\n"
            }
        }
        return addressText
    }

}
