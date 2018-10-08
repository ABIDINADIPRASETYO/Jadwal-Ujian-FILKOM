package poros.filkom.ub.jadwalujianfilkom;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SplashScreen extends Activity {
    private static int LamaTampilSplash = 1000;


    int progress = 0;
    private ProgressBar simpleProgressBar;
    int p = 0;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static int SPLASH_TIME_OUT = 2000;
    private String TAG = "tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //checkAndRequestPermissions();///mayboooo

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        // KODING
        setContentView(R.layout.splashscreen);
        //permissionHelper = new PermissionHelper(this);//EXPERIMENT
        p = 0;
        simpleProgressBar = (ProgressBar) findViewById(R.id.progressBarSplash);
        setProgressValue(progress);


    }





    private void setProgressValue(final int progress) {

        // set the progress
        simpleProgressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progress + 25);
                p += 25;
                if (p == 100) {



                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);

                    //MAYBOOO
                }
            }
        });

        thread.start();

    }
}

