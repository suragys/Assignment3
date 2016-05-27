package edu.scu.suragys.assignment3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPhotoActivity extends AppCompatActivity {

    private boolean flag;

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

    private Uri file;
    private Uri thumbFile;
    private ImageView iv;
    private EditText captionText;
    private Button takePhoto;
    private Button savePhoto;
    private String path;
    private String thumbNailPath;
    private String caption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        iv = (ImageView) findViewById(R.id.imageView);
        captionText = (EditText) findViewById(R.id.editTextCaption);
        takePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        savePhoto = (Button) findViewById(R.id.buttonSavePhoto);


    }

    public void takePhoto(View view) {
        flag =false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//        Log.v("File path", "----------------" + file.getPath());
        path = file.getPath();
        thumbFile = Uri.fromFile(new File(path.substring(0, path.indexOf(".")) + "_thumbNail.jpg"));
        thumbNailPath = thumbFile.getPath();

        startActivityForResult(intent, 100);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
//                iv.setImageURI(file);
                flag = true;
                iv.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, this.getResources().getDisplayMetrics()));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;      // 1/8 of original image
                Bitmap b = BitmapFactory.decodeFile(file.getPath(), options);
                iv.setImageBitmap(b);

//                Picasso.with(image.getContext()).load(storagePath).fit().centerCrop().into(image);
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

            Thumbify.generateThumbnail(path, thumbNailPath);

            Intent intent = new Intent();
            intent.putExtra("object", o);
            setResult(AddPhotoActivity.RESULT_OK, intent);
            finish();
        }

    }


}
