package com.example.mywisatadestination

import android.location.Address
import android.location.Location

interface MainView {
    fun onLocationUpdate(location: Location?)
    fun onAddressUpdate(address: Address?)
    fun onLocationSettingsUnsuccessful()
}