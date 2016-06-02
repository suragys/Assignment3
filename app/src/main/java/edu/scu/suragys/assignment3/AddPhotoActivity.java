package edu.scu.suragys.assignment3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPhotoActivity extends AppCompatActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String AUDIO_FILE_KEY = "AUDIOFILE";
//    public static final int MEDIA_TYPE_IMAGE = 1;
    private GPSTracker gps;
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

    }

    public void takePhoto(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }
        flag =false;
        Intent i = new Intent(this, PhotoCanvasActivity.class);
        startActivityForResult(i, 25);

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = Uri.fromFile(getOutputMediaFile());
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
////        Log.v("File path", "----------------" + file.getPath());
//        path = file.getPath();
//        thumbFile = Uri.fromFile(new File(path.substring(0, path.indexOf(".")) + "_thumbNail.jpg"));
//        thumbNailPath = thumbFile.getPath();
//
//        startActivityForResult(intent, 100);


//        Log.v("Done", "Took picture and coming here ----------------" + file.getPath());
//        Log.v("Done", "Took picture and coming here ----------------" + thumbFile.getPath());
    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode ==  25){
            if (resultCode == RESULT_OK)
            {
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
            }
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            gps = new GPSTracker(this.getApplicationContext());

            if (resultCode == RESULT_OK)
            {
//                previewCapturedImage();
                flag = true;
                iv.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, this.getResources().getDisplayMetrics()));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;      // 1/8 of original image
//                Bitmap b = BitmapFactory.decodeFile(file.getPath(), options);
                Bitmap b = BitmapFactory.decodeFile(path, options);
                iv.setImageBitmap(b);

                if(gps.canGetLocation())
                {
//                    Location l = gps.getLocation();
                    lattitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lattitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                    latlang = (TextView) findViewById(R.id.latlang);

//                    latlang.setVisibility(View.VISIBLE);
                    findViewById(R.id.latlang).setVisibility(View.VISIBLE);
                    findViewById(R.id.latLangtextView).setVisibility(View.VISIBLE);
                    latlang.setText(lattitude+","+longitude);
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    Toast.makeText(getApplicationContext(), "Cannot get location GPS or Network not enabled", Toast.LENGTH_SHORT).show();

                }

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "Cancelled", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Error!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void savePhoto(View view) {

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
            o.setAudioPath(audioFile.getPath().toString());

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


}
