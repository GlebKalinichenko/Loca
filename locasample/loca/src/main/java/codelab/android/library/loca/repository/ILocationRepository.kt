package codelab.android.locationbuilder.library.repository

import codelab.android.library.loca.delegate.LocationDelegate

interface ILocationRepository {
    fun listenLocation(delegate: LocationDelegate)
    fun permissions()
    fun settings()
    fun registerLocation()
    fun removeListenLocation()
}