package codelab.android.library.locasample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.jetbrains.annotations.NotNull;
import codelab.android.library.loca.delegate.LocationDelegate;
import codelab.android.library.loca.entity.GeoEntity;
import codelab.android.library.loca.entity.LocationConfiguration;
import codelab.android.library.loca.jvmannotation.LocationSubscriberInstance;
import codelab.android.library.loca.subscriber.LocationSubscriber;

public class JavaMainActivity extends AppCompatActivity  {

    private LocationConfiguration configuration = new LocationConfiguration.LocationConfigurationBuilder().build();
    private LocationSubscriber subscriber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subscriber = LocationSubscriberInstance.getLocationSubscriber().instance(getApplicationContext(), configuration);
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

            @Override
            public void failedLocation(@NotNull Exception exception) { Log.d("", exception.getMessage()); }
        });
    }

    @Override
    protected void onStop() {
        subscriber.unregisterLocation();
        super.onStop();
    }
}
