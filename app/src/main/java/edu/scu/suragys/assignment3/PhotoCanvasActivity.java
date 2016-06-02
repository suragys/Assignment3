package edu.scu.suragys.assignment3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mj on 31-May-16.
 */
public class PhotoCanvasActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private boolean flag;
    private ImageView canvasDrawIV;
    View v;
    public static Context context;
    private String path;
    private String thumbNailPath;
    private String caption;
    public static File file;
    public static Uri fileUri;
    public static Bitmap b;
    private Uri thumbFile;
//    Canvas canvas;
    private CanvasView customCanvas;
    // Shake Sensor.
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_canvas_edit);
//        canvasDrawIV = (ImageView) findViewById(R.id.canvas_drawing);
        context = this;
        v = this.findViewById(R.id.canvas_drawing);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        flag =false;

        // Camera Photo capture intent.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getOutputMediaFile();
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        Log.v("File path", "----------------" + fileUri.getPath());
        path = fileUri.getPath();
        thumbFile = Uri.fromFile(new File(path.substring(0, path.indexOf(".")) + "_thumbNail.jpg"));
        thumbNailPath = thumbFile.getPath();

        startActivityForResult(intent, 100);

    }

    private static File getOutputMediaFile() {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "Assignment3");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Assignment3");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Assignment3", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    // After receiving image from camera app.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
//                iv.setImageURI(fileUri);

                flag = true;
//                canvasDrawIV.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, this.getResources().getDisplayMetrics()));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;      // 1/8 of original image
                b = BitmapFactory.decodeFile(file.getPath());
                customCanvas = (CanvasView) findViewById(R.id.canvas_drawing);
//                canvasDrawIV.setImageBitmap(b);
//                drawImageBg(b);
//                Picasso.with(image.getContext()).load(storagePath).fit().centerCrop().into(image);

                // SurfaceView code.
//                surfaceView = (MySurfaceView) findViewById(R.id.canvas_drawing);
            }
        }
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }

    public void saveCanvas(View view) {
        customCanvas.save(v);
        Intent intent = new Intent();
            intent.putExtra("PicAddress", file.toString());
            setResult(PhotoCanvasActivity.RESULT_OK, intent);
            Log.v("In save Canvas", "Success");
        finish();
//        FileOutputStream fileOutputStream;
//        fileOutputStream = null;
//        Bitmap  bitmap = Bitmap.createBitmap( customCanvas.getWidth(), customCanvas.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//
//        customCanvas.draw(canvas);
//        try {
//            fileOutputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//
//
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            fileOutputStream = null;
//            finish();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally
//        {
//            if (fileOutputStream != null)
//            {
//                try
//                {
//                    fileOutputStream.close();
//                    fileOutputStream = null;
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 9) {
                Toast.makeText(getApplicationContext(), "Erased on shake", Toast.LENGTH_LONG).show();
                clearCanvas(v);

            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
