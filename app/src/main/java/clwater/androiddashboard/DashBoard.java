package clwater.androiddashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by gengzhibo on 17/8/3.
 */

public class DashBoard extends View {


    Paint paint;
    int backGroundColor;
    float pointLength1;
    float per ;
    float length ;



    public DashBoard(Context context) {
        super(context);
        backGroundColor = Color.WHITE;
        pointLength1 = 180;
        per = 0;
        length = 300;

    }


    public DashBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DashBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLength(float length) {
        this.length = length;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint = new Paint();

        //颜色指示的环
        initRing(canvas);

        //刻度文字
        initScale(canvas);

        //指针
        initPointer(canvas);
        
        //提示内容
        initText(canvas);

//        paint.setColor(Color.BLACK);
//        canvas.drawText("text1", 10, 20, paint);


    }

    private void initText(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, 400);


//        paint.setColor(Color.BLACK);
//        RectF rect = new RectF( -length / 3 , length /3 , -length / 3, length /3);
//        canvas.drawArc(rect, 0, 360, true, paint);
//
//        Paint pp = new Paint();
//        pp.setTextSize(50);
//        canvas.drawText("aaa" , 0 ,0 , pp);

        paint.setColor(Color.parseColor("#eeeeee"));
        paint.setShader(null);
        RectF rect = new RectF( - (length/ 3 + length/ 60), - (length / 3 + length/ 60) , length / 3 + length/ 60, length / 3 + length/ 60);
        canvas.drawArc(rect, 0, 360, true, paint);

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setShader(null);
        rect = new RectF( - (length/ 3 ), - (length / 3), length / 3, length / 3);
        canvas.drawArc(rect, 0, 360, true, paint);

        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2 + length/3f /6f, 400);

        Paint textPaint = new Paint();
        textPaint.setStrokeWidth(1);

        textPaint.setTextSize(60);
        textPaint.setColor(Color.parseColor("#fc6555"));
        textPaint.setTextAlign(Paint.Align.RIGHT);

        canvas.drawText("" + (int) (per * 100), 0, 0, textPaint);

        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("%" , 0, 0, textPaint);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.parseColor("#cecece"));
        canvas.translate(- length/3f /6f,40);
        canvas.drawText("完成率" , 0, 0, textPaint);



    }


    public void setBackGroundColor(int color){
        this.backGroundColor = color;
    }

    public void setPointLength1(float pointLength1){
        this.pointLength1 = -length * pointLength1 ;
    }

    private void initScale(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, 400);
        paint.setColor(Color.BLACK);
        Paint tmpPaint = new Paint(paint); //小刻度画笔对象
        tmpPaint.setStrokeWidth(1);
        tmpPaint.setTextSize(35);
        tmpPaint.setTextAlign(Paint.Align.CENTER);

        canvas.rotate(-90,0f,0f);

        float  y = 300;
        y = - y;
        int count = 12; //总刻度数
        paint.setColor(backGroundColor);

        float tempRou = 180 / 12f;
        Log.d("gzb" , "tempRou :" + tempRou);
//        canvas.rotate(  10 ,0f,0f);


        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        for (int i = 0 ; i <= count ; i++){

            if (i % 2 == 0 ) {
                canvas.drawText(String.valueOf((i) * 10), 0, y - 20f, tmpPaint);
            }

            canvas.drawLine(0f, y , 0, y + length / 15, paint);


            canvas.rotate(tempRou,0f,0f);
        }

    }


    private void initPointer(Canvas canvas) {
        paint.setColor(Color.BLACK);

        Paint pointerPaint = new Paint(paint);
        pointerPaint.setStrokeWidth(1);

        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, 400);
        float change = per * 180;
        canvas.rotate(-90 + change,0f,0f);
//
//        canvas.drawLine(0f, pointLength1 - 20f, 0,  0 , pointerPaint);


        Path path = new Path();
//        Log.d("gzb" , "" + pointLength1);
        path.moveTo(0 , pointLength1);
        path.lineTo(-15 , 0);
        path.lineTo(15,0);
        path.lineTo(0 , pointLength1);
        path.close();
        canvas.drawPath(path, paint);

    }

    private void initRing(Canvas canvas) {

        canvas.save();
        canvas.translate(canvas.getWidth()/2, 400);

        int[] colors = {Color.parseColor("#F95A37"), Color.parseColor("#f9cf45"), Color.parseColor("#00ff00")};
//        float[] positions = {0.5f, 0.75f, (0.7f + 0.5f / 3 * 2)};
        float[] positions = {0.5f - 5f/180f * 0.5f, 0.5f + 0.5f * 5f / 6f, 1.0f};
        SweepGradient sweepGradient = new SweepGradient(0, 0, colors, positions);
        paint.setShader(sweepGradient);
        RectF rect = new RectF( -length, -length, length, length);
//        canvas.drawRect(100, 100, 500, 300, paint);
        canvas.drawArc(rect, 175, 5f + 180f / 6f * 5f, true, paint);


//        canvas.rotate(-5,0f,0f);

        int[] colors2 = {Color.parseColor("#79D062"),  Color.parseColor("#3FBF55")};

        float[] positions2 = {0.5f + 0.5f * 5f / 6f, 1.0f + 5f/180f * 0.5f};
        sweepGradient = new SweepGradient(0, 0, colors2, positions2);
        paint.setShader(sweepGradient);
        rect = new RectF( -length, -length, length, length);
//        canvas.drawRect(100, 100, 500, 300, paint);
//        canvas.drawArc(rect, 175, 5f + 180f / 6f * 5f, true, paint);

//        paint.setColor(Color.parseColor("#74cc65"));
//        paint.setShader(null);
//        rect = new RectF( -length, -length, length, length);
        canvas.drawArc(rect, 180f + 180f / 6f * 5f, 180f / 6 + 5, true, paint);

        paint.setColor(backGroundColor);
        paint.setShader(null);
        rect = new RectF( - (length - 100 ), -(length / 3f * 2f), length - 100, length / 3f * 2f);
        canvas.drawArc(rect, 165, 210, true, paint);
    }



    public void cgangePer(float per ){
        this.per = per;
        invalidate();
    }
}
