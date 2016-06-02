package edu.scu.suragys.assignment3;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class AddAudioActivity extends AppCompatActivity {


    private ImageButton recordButton;
    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton deleteButton;
    private TextView timerText;
    private int secs;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private String total_time = "";
    private long updatedTime = 0L;
    private MediaRecorder audioRecorder;
    private String audioFile = null;
    private long startTime = 0L;
    private Handler handler;
    private Button saveAudioAndReturn;
    private Runnable updateTimerThread = new Runnable() {


        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int hrs = mins / 60;
            mins = mins % 60;
            timerText.setText("" + String.format("%02d", hrs) + ":" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));

            handler.postDelayed(this, 0);
            if (timerText.getText().toString().equalsIgnoreCase(total_time)) {
                handler.removeCallbacks(updateTimerThread);
            }

        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_audio);


        playButton = (ImageButton) findViewById(R.id.play_icon);
        stopButton = (ImageButton) findViewById(R.id.stop_icon);
        recordButton = (ImageButton) findViewById(R.id.rec_icon);
        deleteButton = (ImageButton) findViewById(R.id.delete_icon);
        timerText = (TextView) findViewById(R.id.textView_timer);
        saveAudioAndReturn = (Button) findViewById(R.id.saveAudioButton);
        stopButton.setEnabled(false);
        stopButton.setImageResource(R.drawable.stop);
        stopButton.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        saveAudioAndReturn.setVisibility(View.INVISIBLE);

        Bundle ext = getIntent().getExtras();
        audioFile = ext.getString(AddPhotoActivity.AUDIO_FILE_KEY);
//        audioFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";


        try {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, 0);

            }
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please give app MIC Permissions", Toast.LENGTH_LONG).show();
            return;
        }


        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    audioRecorder = new MediaRecorder();
                    audioRecorder.setOutputFile(audioFile);
                    audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    audioRecorder.prepare();
                    audioRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                recordButton.setEnabled(false);
                recordButton.setImageResource(R.drawable.record);
                stopButton.setEnabled(true);
                stopButton.setImageResource(R.drawable.stop);
                stopButton.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Recording Audio", Toast.LENGTH_SHORT).show();
                startTime = SystemClock.uptimeMillis();
                handler = new Handler();
                handler.postDelayed(updateTimerThread, 1000);

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecorder.stop();
                audioRecorder.release();
                audioRecorder = null;

                stopButton.setEnabled(false);
                stopButton.setImageResource(R.drawable.stop);
                saveAudioAndReturn.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                playButton.setEnabled(true);
                deleteButton.setEnabled(true);
                playButton.setImageResource(R.drawable.play);
                deleteButton.setImageResource(R.drawable.delete);

                Toast.makeText(getApplicationContext(), "Audio recorded ", Toast.LENGTH_SHORT).show();
                timeSwapBuff += timeInMilliseconds;
                total_time = timerText.getText().toString();
                handler.removeCallbacks(updateTimerThread);


            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(audioFile);
                    m.prepare();
                    m.start();
                    timeSwapBuff = 0;
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 1000);
                    Toast.makeText(getApplicationContext(), "Playing recorded audio", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC), audioFile);
                Toast.makeText(getApplicationContext(), "About to delete "+ file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                if (!file.mkdirs()) {
                    Toast.makeText(getApplicationContext(), "directory does not exsits", Toast.LENGTH_SHORT).show();
                } else {
                    file.delete();
                }
                Toast.makeText(getApplicationContext(), "audio deleted", Toast.LENGTH_LONG).show();
                timerText.setText("00:00:00");
                total_time = "";
                recordButton.setEnabled(true);
                recordButton.setImageResource(R.drawable.record);
                stopButton.setImageResource(R.drawable.stop);
                playButton.setEnabled(false);

                deleteButton.setEnabled(false);

                playButton.setImageResource(R.drawable.play);
                deleteButton.setImageResource(R.drawable.delete);
                playButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                saveAudioAndReturn.setVisibility(View.INVISIBLE);
            }
        });

    }


    public void returnToParentActivity(View view) {

        finish();
    }
}
