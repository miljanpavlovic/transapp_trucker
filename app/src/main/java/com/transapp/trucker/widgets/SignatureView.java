package com.transapp.trucker.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.transapp.trucker.utils.IOUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Miljan on 6/28/2015.
 */
public class SignatureView extends View {

    public static final String TAG = "SignatureView";

    private Bitmap mBitmap;

    private static final float STROKE_WIDTH = 5f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private Paint paint = new Paint();
    private Path path = new Path();

    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();



    public SignatureView(Context context) {
        super(context);
    }

    public SignatureView(Context context, AttributeSet attr){
        super(context, attr);

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    // i will left this function commented for now, and will see later will i use it somewhere
//    public String getSignaturePath(Context context) {
//        try {
//            String url = MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, "title", null);
//            Log.d(TAG, "getSignaturePath insertImage url: " + url);
//            return url;
//
//        } catch (Exception ex) {
//            Log.d(TAG, "getSignaturePath exception occure");
//            return ex.getMessage();
//        }
//    }


    public File save(Context context){
        if(mBitmap == null) {
            mBitmap =  Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);;
        }
        Canvas canvas = new Canvas(mBitmap);
        try {
            File signaturePath = new File(IOUtil.getSignatureDir(context), IOUtil.getSignatureName());
            FileOutputStream mFileOutStream = new FileOutputStream(signaturePath);
            draw(canvas);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
            mFileOutStream.flush();
            mFileOutStream.close();
            return signaturePath;
        }
        catch(Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }


    public void clear() {
        path.reset();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;

            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                break;

            default:
                Log.d(TAG, "Ignored touch event: " + event.toString());
                return false;
        }

        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        }
        else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }

        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        }
        else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    private void resetDirtyRect(float eventX, float eventY) {
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }


}