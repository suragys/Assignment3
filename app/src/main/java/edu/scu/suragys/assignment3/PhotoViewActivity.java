package edu.scu.suragys.assignment3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class PhotoViewActivity extends AppCompatActivity {

    private TextView caption;
    private ImageView i;
    private TextView latlang;
    private Button playAudio;
    private MyObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        Bundle ext = getIntent().getExtras();


        caption = (TextView) findViewById(R.id.textCaption);
        i = (ImageView) findViewById(R.id.image);
        obj = (MyObject) ext.get("object");
        caption.setText(obj.getCaption());

        latlang = (TextView) findViewById(R.id.latlang);
        latlang.setText(obj.getLattitude()+","+obj.getLongitude());

        i.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, this.getResources().getDisplayMetrics()));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;      // 1/8 of original image
        Bitmap b = BitmapFactory.decodeFile(obj.getPath(), options);
        i.setImageBitmap(b);
    }

    public void ShowMap(View view) {
        Toast.makeText(this,"Call map intent",Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        Log.i("Lattitude and Langitude", latlang.getText().toString());
        i.putExtra("LATLANG",latlang.getText().toString());
        startActivity(i);
    }

    public void playAudio(View view) {
//        Intent i = new Intent(getApplicationContext(), PlayAudioActivity.class);
//
        Toast.makeText(getApplicationContext(), "Audio File Path: " + obj.getAudioPath(), Toast.LENGTH_SHORT).show();
        Log.i("Audio File Path:", "Audio File Path: " + obj.getAudioPath());
        MediaPlayer m = new MediaPlayer();

        try {
            Toast.makeText(getApplicationContext(), "Audio File Path: " + obj.getAudioPath(), Toast.LENGTH_SHORT).show();
//            File audioFile = new File(obj.getAudioPath());
            m.setDataSource(obj.getAudioPath());
            m.prepare();
            m.start();
//            timeSwapBuff = 0;
//            startTime = SystemClock.uptimeMillis();
//            customHandler.postDelayed(updateTimerThread, 1000);
            Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
