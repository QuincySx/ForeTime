package com.smallraw.time.model

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.smallraw.foretime.app.App
import com.smallraw.library.smallpermissions.SmallPermission
import java.util.concurrent.CountDownLatch

public class LocationModel {
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context): String {
        try {
            var location = ""
            val countDownLatch = CountDownLatch(1)
            val systemService = context.getSystemService(Service.LOCATION_SERVICE) as LocationManager

            val providerList = systemService.getProviders(true)
            var provider = ""
            if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                provider = LocationManager.NETWORK_PROVIDER
            } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                provider = LocationManager.GPS_PROVIDER
            }

            SmallPermission.with(this)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .onGranted {
                    if (provider != "") {
                        App.getInstance().getAppExecutors().mainThread().execute {
                            systemService.requestLocationUpdates(provider, 5 * 60 * 1000L, 1f, object :
                                LocationListener {
                                override fun onLocationChanged(local: Location?) {
                                    if (local != null) {
                                        location = "${local.getLongitude()},${local.getLatitude()}"
                                    }
                                    countDownLatch.countDown()
                                }

                                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                                }

                                override fun onProviderEnabled(provider: String?) {

                                }

                                override fun onProviderDisabled(provider: String?) {

                                }

                            })
                        }
                    }
                }
                .onDenied {}
                .start()
            countDownLatch.await()

            return location
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}
