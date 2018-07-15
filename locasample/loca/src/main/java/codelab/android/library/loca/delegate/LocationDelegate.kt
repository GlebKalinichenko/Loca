package codelab.android.library.loca.delegate

import codelab.android.library.loca.entity.GeoEntity

interface LocationDelegate {
    fun permissionFailed()
    fun settingsFailed(exception: Exception)
    fun successLocation(point: GeoEntity)
    fun failedLocation(exception: Exception)
}