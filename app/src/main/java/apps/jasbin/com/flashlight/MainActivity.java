package apps.jasbin.com.flashlight;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {


    private Camera camera;
    private boolean isFlashOn;

    Parameters params;

    private Context mContext = MainActivity.this;

    private static final int REQUEST = 112;

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
            }
            else {
               getCamera();
            }
        }
       else {
            getCamera();
            }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                    Log.e("Permission","Granted");
                    getCamera();
                    Toast.makeText(mContext, "Permission Granted ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "The app was not allowed to access camera", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }
        return true;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
       final ImageButton imgbutton = findViewById(R.id.imageButton);



                getCamera();


             imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                   turnOffFlash();
                   imgbutton.setImageResource(R.drawable.light_off);
                    Toast.makeText(getApplicationContext(), "Flash Off!", Toast.LENGTH_SHORT).show();
                    }else{
                    turnOnFlash();
                    imgbutton.setImageResource(R.drawable.light_on);
                    Toast.makeText(getApplicationContext(), "Flash On!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void getCamera() {

        if (camera == null) {
            try {
                camera = Camera.open(0);
                params = camera.getParameters();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void turnOnFlash() {

        if(!isFlashOn) {
            if(camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }

    }

    private void turnOffFlash() {

        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;


        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(camera!=null){
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
           turnOnFlash();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }



}