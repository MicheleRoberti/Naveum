package naveum.naveum;

import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.Eddystone;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WebView webview;
    private BeaconManager beaconManager;
    private com.estimote.coresdk.observation.region.RegionUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.loadUrl("http://cmanfredi.github.io/naveum");
        setContentView(webview);
        webview.setWebViewClient(new WebViewClient());

        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);


        beaconManager =  new BeaconManager(getApplicationContext());
        utils = new com.estimote.coresdk.observation.region.RegionUtils();
        beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> eddystones) {
               //Cerchiamo il più vicino)
                String url = "";
                double minDistance, distance;
                for ( Eddystone eddystone : eddystones) {
                    url = "";
                    minDistance = 10.;
                    distance = utils.computeAccuracy(eddystone);
                    //Log.d(TAG, ""+distance);
                    if(distance < minDistance){
                        minDistance = distance;
                        url = eddystone.url;
                    }

                }
                Log.d(TAG, url);
                if (eddystones.get(0).url == "https://www.twitter.com")
                    webview.loadUrl("mostra");
                if (eddystones.get(0).url == "https://www.facebook.com")
                    webview.loadUrl("https://naveum.link/rooms/1");
                if (eddystones.get(0).url == "https://www.google.it")
                webview.loadUrl("https://naveum.link/exhibitions/1");
                //beaconManager.stopEddystoneDiscovery();
            }
        });

    }

    protected  void onStart() {
        super.onStart();

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                System.out.println("onStart");
                beaconManager.startEddystoneDiscovery();
            }
        });
    }


    protected void onStop(){
        super.onStop();
        beaconManager.stopEddystoneDiscovery();
    }

    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

}
