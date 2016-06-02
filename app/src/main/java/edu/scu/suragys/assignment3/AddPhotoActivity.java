package edu.scu.suragys.assignment3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPhotoActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String AUDIO_FILE_KEY = "AUDIOFILE";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    GoogleApiClient mGoogleApiClient = null;
    Location mLastLocation;
    private boolean flag;
    private Uri file;
    private Uri audioFile;
    private Uri thumbFile;
    private ImageView iv;
    private EditText captionText;
    private Button takePhoto;
    private TextView latlangText;
    private TextView latlang;
    private Button savePhoto;
    private String path;
    private String thumbNailPath;
    private String caption;
    private double lattitude;
    private double longitude;

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Assignment3");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Assignment3", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    private static File getOutputAudioFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), "Assignment3");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Assignment3", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "AUD_" + timeStamp + ".3gp");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        iv = (ImageView) findViewById(R.id.imageView);
        captionText = (EditText) findViewById(R.id.editTextCaption);
        takePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        savePhoto = (Button) findViewById(R.id.buttonSavePhoto);
        findViewById(R.id.latlang).setVisibility(View.INVISIBLE);
        findViewById(R.id.latLangtextView).setVisibility(View.INVISIBLE);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }



    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        toast("onConnected() is called");
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
//        toast("onConnectionSuspended() is called : i=" + i);
    }

    @Override
    public void onLocationChanged(Location location) {
        lattitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        toast("onConnectionFailed() is called");
    }

    private void startTrackLocation() {
//        toast("Start tracking");
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, AddPhotoActivity.this);
    }

    private void stopTrackLocation() {
//        toast("Stop tracking");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, AddPhotoActivity.this);
    }

    public void takePhoto(View view) {

        flag = false;
        Intent i = new Intent(this, PhotoCanvasActivity.class);
        startTrackLocation();
        startActivityForResult(i, 25);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 25) {
            if (resultCode == RESULT_OK) {
                file = Uri.parse(data.getStringExtra("PicAddress"));
                flag = true;
                iv.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, this.getResources().getDisplayMetrics()));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;      // 1/8 of original image
                Bitmap b = BitmapFactory.decodeFile(file.getPath());
                path = file.getPath();
                thumbFile = Uri.fromFile(new File(path.substring(0, path.indexOf(".")) + "_thumbNail.jpg"));
                thumbNailPath = thumbFile.getPath();
                iv.setImageBitmap(b);

                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lattitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                    latlang = (TextView) findViewById(R.id.latlang);

                    findViewById(R.id.latlang).setVisibility(View.VISIBLE);
                    findViewById(R.id.latLangtextView).setVisibility(View.VISIBLE);
                    latlang.setText(lattitude + "," + longitude);


            }
        }

    }

    public void savePhoto(View view) {

        stopTrackLocation();
        caption = captionText.getText().toString();
        if (caption.isEmpty()) {
            captionText.setError("Please enter the caption!");
        } else if (path == null || !flag) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Capture an Image!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            MyObject o = new MyObject();
            o.setPath(path);
            o.setCaption(caption);
            o.setThumbNailPath(thumbNailPath);
            o.setLattitude(String.valueOf(lattitude));
            o.setLongitude(String.valueOf(longitude));
            o.setAudioPath(audioFile == null ? "No Audio" : audioFile.getPath().toString());

            Thumbify.generateThumbnail(path, thumbNailPath);

            Intent intent = new Intent();
            intent.putExtra("object", o);
            setResult(AddPhotoActivity.RESULT_OK, intent);
            finish();
        }


    }

    public void addAudio(View view) {

        Intent i = new Intent(getApplicationContext(), AddAudioActivity.class);
        audioFile = Uri.fromFile(getOutputAudioFile());
        i.putExtra(AUDIO_FILE_KEY, audioFile.getPath().toString());
        startActivity(i);

    }

    public static class Thumbify {
        public static void generateThumbnail(String imgFile, String thumbFile) {
            try {
                Bitmap picture = BitmapFactory.decodeFile(imgFile);
                Bitmap resized = ThumbnailUtils.extractThumbnail(picture, 120, 120);
                FileOutputStream fos = new FileOutputStream(thumbFile);
                resized.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }



}
