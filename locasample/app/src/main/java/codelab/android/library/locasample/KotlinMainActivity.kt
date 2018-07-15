package codelab.android.locationbuilder.locationsample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import codelab.android.library.loca.delegate.LocationDelegate
import codelab.android.library.loca.entity.GeoEntity
import codelab.android.library.loca.entity.LocationConfiguration
import codelab.android.library.loca.entity.LocationMode
import codelab.android.library.loca.subscriber.LocationSubscriber
import codelab.android.library.locasample.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsStatusCodes

class KotlinMainActivity : AppCompatActivity() {

    lateinit var repository: LocationSubscriber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val request = LocationConfiguration.LocationConfigurationBuilder().interval(1000)
                .fastestInteval(1000).numUpdates(10).mode(LocationMode.COMMON).build()
        repository = LocationSubscriber.instance(applicationContext, request)
    }

    override fun onStart() {
        super.onStart()

        repository.registerLocation(object: LocationDelegate {

            override fun permissionFailed() {
                ActivityCompat.requestPermissions(this@KotlinMainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        PermissionConstants.LOCATIONPERMISSION)
            }

            override fun settingsFailed(exception: Exception) {
                var statusCode: Int = (exception as ApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    var resolvableApiException = exception as ResolvableApiException;
                    resolvableApiException.startResolutionForResult(this@KotlinMainActivity, PermissionConstants.LOCATIONSETTINGS);
                }
            }

            override fun successLocation(point: GeoEntity) {
                Log.d("", "")
            }

            override fun failedLocation(exception: Exception) {
                Log.d("", exception.message)
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionConstants.LOCATIONPERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    repository.settings()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PermissionConstants.LOCATIONSETTINGS && resultCode == Activity.RESULT_OK)
            repository.registerLocation()
    }

    override fun onStop() {
        repository.unregisterLocation()
        super.onStop()
    }
}
