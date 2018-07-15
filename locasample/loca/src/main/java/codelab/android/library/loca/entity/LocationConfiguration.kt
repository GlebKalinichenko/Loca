package codelab.android.library.loca.entity

import com.google.android.gms.location.LocationRequest

class LocationConfiguration(var interval: Long = 1000, var fastestInterval: Long = 1000,
    var numUpdates: Int = 1, var priority: Int = LocationRequest.PRIORITY_HIGH_ACCURACY,
    var mode: LocationMode = LocationMode.NEW) {

    fun request(): LocationRequest {
        var locationRequest = LocationRequest()
        locationRequest.interval = interval
        locationRequest.fastestInterval = fastestInterval
        locationRequest.numUpdates = numUpdates
        locationRequest.priority = priority

        return locationRequest

    }

    class LocationConfigurationBuilder(private var interval: Long = 1000, private var fastestInterval: Long = 1000, private var numUpdates: Int = 1,
            private var priority: Int = LocationRequest.PRIORITY_HIGH_ACCURACY, private var mode: LocationMode = LocationMode.NEW) {

        fun build(): LocationConfiguration = LocationConfiguration(interval, fastestInterval, numUpdates, priority, mode)

        fun interval(interval: Long): LocationConfigurationBuilder {
            this.interval = interval
            return this
        }

        fun fastestInteval(fastestInterval: Long): LocationConfigurationBuilder {
            this.fastestInterval = fastestInterval
            return this
        }

        fun numUpdates(count: Int): LocationConfigurationBuilder  {
            this.numUpdates = count
            return this
        }

        fun priority(priority: Int): LocationConfigurationBuilder {
            this.priority = priority
            return this
        }

        fun mode(mode: LocationMode): LocationConfigurationBuilder {
            this.mode = mode
            return this
        }
    }
}