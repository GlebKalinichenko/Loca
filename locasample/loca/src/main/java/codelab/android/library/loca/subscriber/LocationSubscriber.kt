package codelab.android.library.loca.subscriber

import android.content.Context
import codelab.android.library.loca.delegate.LocationDelegate
import codelab.android.library.loca.entity.LocationConfiguration
import codelab.android.locationbuilder.library.repository.ILocationRepository
import codelab.android.locationbuilder.library.repository.LocationRepository

object LocationSubscriber {

    lateinit var repository: ILocationRepository

    fun instance(context: Context, configuration: LocationConfiguration): LocationSubscriber {
        val request = configuration.request()
        repository = LocationRepository(context, request, configuration.mode)
        return this
    }

    /**
     * Register callback for fetch locations
     * @param delegate      Callback with information about update location
     * */
    fun registerLocation(delegate: LocationDelegate) {
        repository.listenLocation(delegate)
    }

    /**
     * Checking location settings
     * */
    fun settings() {
        repository.settings()
    }

    /**
     * Prepare listening of location updates
     * */
    fun registerLocation() = repository.registerLocation()

    /**
     * Disabled callback for listen locations
     * */
    fun unregisterLocation() = repository.removeListenLocation()
}