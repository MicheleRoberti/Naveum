package naveum.naveum;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EstimoteSDK.initialize(getApplicationContext(), "naveum-j33", "db67fb3f30ee2997d1e0982769af2d39");

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
        //EstimoteSDK.enableDebugLogging(true);
    }
}
