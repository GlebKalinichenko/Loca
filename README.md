# Android location listener library

## What is this library?
This is library that helps developer subscribe for update location's position. Main cause of creating library was implemented comfortable programming interface for listen location's position.

## Installation

**Step 1.** Add the JitPack repository to your build file. \
Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency

```gradle
dependencies {
    implementation 'com.github.GlebKalinichenko:loca:0.1.1'
}
```

## Settings

Also need to update you **AndroidManifest.xml**. Need add geo **API KEY** from  [link to Google Cloud.](https://console.cloud.google.com/google/maps-apis) Also need to add permissions for get locations.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="codelab.android.locationbuilder.locationsample">

    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <application
        ...

        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="@string/google_maps_key" />
    </application>

</manifest>
```

## Integration 

**Kotlin**
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    val request = LocationConfiguration.LocationConfigurationBuilder().interval(1000).fastestInteval(1000).numUpdates(10).build()
    repository = LocationSubscriber.instance(applicationContext, request)
}

override fun onStart() {
    super.onStart()

    repository.listenLocation( object: LocationDelegate {
        override fun permissionFailed() {
            ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        PermissionConstants.LOCATIONPERMISSION)
        }

        override fun settingsFailed(exception: Exception) {
            var statusCode: Int = (exception as ApiException).statusCode
            if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                var resolvableApiException = exception as ResolvableApiException;
                resolvableApiException.startResolutionForResult(this@MainActivity, PermissionConstants.LOCATIONSETTINGS);
            }
        }

        override fun successLocation(point: GeoEntity) {
            Log.d("", "")
        }
    })
}


```

**Java**

```Java
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subscriber = LocationSubscriberInstance.getLocationSubscriber().instance(getApplicationContext(), request);
        subscriber.registerLocation(new LocationDelegate() {
            @Override
            public void permissionFailed() {
                Log.d("", "");
            }

            @Override
            public void settingsFailed(@NotNull Exception exception) {
                Log.d("", "");
            }

            @Override
            public void successLocation(@NotNull GeoEntity point) {
                Log.d("", "");
            }
        });
    }

    @Override
    protected void onStop() {
        subscriber.unregisterLocation();
        super.onStop();
    }

```

[![](https://jitpack.io/v/GlebKalinichenko/loca.svg)](https://jitpack.io/#GlebKalinichenko/loca)


