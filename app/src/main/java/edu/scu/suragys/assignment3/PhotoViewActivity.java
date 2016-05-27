package edu.scu.suragys.assignment3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoViewActivity extends AppCompatActivity {

    private TextView caption;
    private ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        Bundle ext = getIntent().getExtras();


        caption = (TextView) findViewById(R.id.textCaption);
        i = (ImageView) findViewById(R.id.image);
        MyObject o = (MyObject) ext.get("object");
        caption.setText(o.getCaption());

        i.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, this.getResources().getDisplayMetrics()));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;      // 1/8 of original image
        Bitmap b = BitmapFactory.decodeFile(o.getPath(), options);
        i.setImageBitmap(b);
    }
}
