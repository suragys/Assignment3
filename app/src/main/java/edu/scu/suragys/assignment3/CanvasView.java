package edu.scu.suragys.assignment3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Mj on 31-May-16.
 */
public class CanvasView extends View {

    public CanvasView(Context context) {
        super(context);
    }
    public int width;
    public int height;
    public static Bitmap b;
    private static Bitmap mBitmap;
    private static Canvas mCanvas;
    private Path mPath;
    Context context;
    private Canvas temp;
    InputStream inputStream;
    private static Paint mPaint;
    Bitmap resized;
    private float mX, mY;
    private Paint transparentPaint;
    private static final float TOLERANCE = 5;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
//        transparentPaint = new Paint();
//        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
//        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }


            // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Bitmap mutableBitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);  //Bitmap.Config.RGB_565
        mBitmap.eraseColor(Color.TRANSPARENT);
//        b = BitmapFactory.decodeFile(PhotoCanvasActivity.fileUri.getPath());
        mCanvas = new Canvas(mBitmap);

        resized = Bitmap.createScaledBitmap(PhotoCanvasActivity.b, w, h, true);
//        Paint p = new Paint();
//        p.setAlpha(127);
//        mCanvas.drawBitmap(mBitmap, 0, 0, p);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;      // 1/8 of original image
//        try {
//            b = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(PhotoCanvasActivity.fileUri), null, options);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        Log.v("In Canvas View", PhotoCanvasActivity.fileUri.getPath());

//        mBitmap = Bitmap.createScaledBitmap(b, 100, 100, true);


        //        b = BitmapFactory.decodeFile(PhotoCanvasActivity.fileUri.getPath(), options);

    }

    // To save Canvas.

    public void save(View v)
    {
        Log.v("log_tag", "Width: " + v.getWidth());
        Log.v("log_tag", "Height: " + v.getHeight());
        if(mBitmap == null)
        {
            mBitmap =  Bitmap.createBitmap (this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(mBitmap);
        try
        {
            FileOutputStream mFileOutStream = new FileOutputStream(PhotoCanvasActivity.file);
            v.draw(canvas);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
            mFileOutStream.flush();
            mFileOutStream.close();
//            String url = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
//            Log.v("log_tag","url: " + url);
            //In case you want to delete the file
            //boolean deleted = mypath.delete();
            //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
            //If you want to convert the image to string use base64 converter

        }
        catch(Exception e)
        {
            Log.v("log_tag", e.toString());
        }
    }
            // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        mCanvas.setBitmap(PhotoCanvasActivity.b);
//        mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
        // draw the mPath with the mPaint on the canvas when onDraw
//        mCanvas.drawColor(Color.BLACK);
//        mCanvas.drawColor(0xFFAAAAAA);
//        Log.v("In Canvas with image", String.valueOf(b));
        Log.v("In canvas view", "------------------" + PhotoCanvasActivity.file.getPath());
        canvas.drawBitmap(resized, 0, 0, null);

        canvas.drawPath(mPath, mPaint);
    }

            // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

            // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
//        canvas.drawBitmap(resized, 0, 0, null);
        invalidate();
    }

            // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

            //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}

