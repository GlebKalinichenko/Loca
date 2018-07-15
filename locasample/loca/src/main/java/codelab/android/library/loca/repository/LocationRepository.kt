package codelab.android.locationbuilder.library.repository

import  android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.support.v4.content.ContextCompat
import codelab.android.library.loca.delegate.LocationDelegate
import codelab.android.library.loca.entity.GeoEntity
import codelab.android.library.loca.entity.LocationMode
import com.google.android.gms.location.*

class LocationRepository(var context: Context, var request: LocationRequest, var mode: LocationMode = LocationMode.NEW) : ILocationRepository {

    var client: FusedLocationProviderClient? = null
    var delegate: LocationDelegate? = null

    /**
     * Method for validation location's permission
     * */
    override fun permissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            delegate?.let { it.permissionFailed() }
            return
        }

        settings()
    }

    /**
     * Method for validation location's settings
     * */
    override fun settings() {
        var builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(request)
        builder.setAlwaysShow(true);

        LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())
                .addOnSuccessListener {
                    registerLocation()
                }
                .addOnFailureListener {
                    delegate?.let { i -> i.settingsFailed(it) }
                }
    }

    // Callback for listen location's changes
    var locationCallback = object: com.google.android.gms.location.LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            p0?.let {
                var point = GeoEntity(p0.lastLocation.latitude, p0.lastLocation.longitude)
                delegate?.let { i -> i.successLocation(point) }
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
            super.onLocationAvailability(p0)
        }
    }

    /**
     * Method for subscribe for update locations
     * */
    @SuppressLint("MissingPermission")
    override fun registerLocation() {
        client = LocationServices.getFusedLocationProviderClient(context)

        // Need to fetch only fresh location position
        if (mode == LocationMode.NEW)
            client!!.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

        // Need to fetch only last known location position
        if (mode == LocationMode.LAST_KNOWN) {
            client!!.lastLocation.addOnSuccessListener {
                var point = GeoEntity(it.latitude, it.longitude)
                delegate?.let { it.successLocation(point) }
            }
            .addOnFailureListener { delegate?.let { i -> i.failedLocation(it) } }
        }

        // Need fetch both last known location and fresh location
        if (mode == LocationMode.COMMON) {
            client!!.lastLocation.addOnSuccessListener {
                var point = GeoEntity(it.latitude, it.longitude)
                delegate?.let { it.successLocation(point) }
            }.addOnFailureListener { delegate?.let { i -> i.failedLocation(it) } }

            client!!.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
        }
    }

    /**
     * Prepare for location listener by using special delegate
     * @param delegate          Delegate for callback
     * */
    override fun listenLocation(delegate: LocationDelegate) {
        this.delegate = delegate
        permissions()
    }

    /**
     * Remove lication listener
     * */
    override fun removeListenLocation() {
        delegate = null
        client?.let {
            client!!.removeLocationUpdates(locationCallback)
        }
    }
}