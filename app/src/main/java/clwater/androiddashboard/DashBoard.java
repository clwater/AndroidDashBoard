package clwater.androiddashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by gengzhibo on 17/8/3.
 */

public class DashBoard extends View {



    private Paint paint;
    private int backGroundColor;    //背景色
    private float pointLength;      //指针长度
    private float per ;             //指数百分比
    private float perPoint ;        //缓存(变化中)指针百分比
    private float perOld ;          //变化前指针百分比
    private float length ;          //仪表盘半径
    private float r ;


    //指针移动变化
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            perPoint = perOld +  (float) msg.obj;
            invalidate();
        }
    };

    class SetIndex implements Runnable {

        @Override
        public void run() {
            int count = 48; //比较流畅
            for (int i = 0 ; i < count ; i ++ ){
                try {
                    Message message = Message.obtain();
                    message.obj = (per - perOld) / count * ( i + 1) ;
                    handler.sendMessage(message);
                    Thread.sleep(1000 / count);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        }
    }






    public DashBoard(Context context) {
        super(context);
        backGroundColor = Color.WHITE;
        length = 300  / 4 * 3;


        pointLength = 180;
        per = 0;
        perOld = 0;
        r = 300;
    }



    public DashBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DashBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public void setLength(float length) {
//        this.length = length;
//    }

    public void setR(float r) {
        this.r = r;
        this.length = r  / 4 * 3;
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


    }

    private void initText(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, r);

        float rIndex = length ;
//        if (rIndex > 300) rIndex = 300;

        //绘制圆盘阴影及圆盘
        paint.setColor(Color.parseColor("#eeeeee"));
        paint.setShader(null);
        RectF rect = new RectF( - (rIndex/ 3 + rIndex/ 60), - (rIndex / 3 + rIndex/ 60) , rIndex / 3 + rIndex/ 60, rIndex / 3 + rIndex/ 60);
        canvas.drawArc(rect, 0, 360, true, paint);

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setShader(null);
        rect = new RectF( - (rIndex/ 3 ), - (rIndex / 3), rIndex / 3, rIndex / 3);
        canvas.drawArc(rect, 0, 360, true, paint);

        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2f  +  30   , r);

        Paint textPaint = new Paint();
        textPaint.setStrokeWidth(1);

        textPaint.setTextSize(60);
        textPaint.setColor(Color.parseColor("#fc6555"));
        textPaint.setTextAlign(Paint.Align.RIGHT);


        //判断指数变化及颜色设定

        int _per = (int) (per * 120);


        if (_per < 60){
            textPaint.setColor(Color.parseColor("#ff6450"));
        }else if (_per < 100) {
            textPaint.setColor(Color.parseColor("#f5a623"));
        }else {
            textPaint.setColor(Color.parseColor("#79d062"));
        }


        canvas.drawText("" + _per, 0, 0, textPaint);

        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("%" , 0, 0, textPaint);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.parseColor("#cecece"));


        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2 + rIndex/3f /6f , r);

        canvas.translate(- rIndex/3f /6f,40);
        canvas.drawText("完成率" , 0, 0, textPaint);



    }


    public void setBackGroundColor(int color){
        this.backGroundColor = color;
    }

    public void setPointLength1(float pointLength1){
        this.pointLength = -length * pointLength1 ;
    }

    private void initScale(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, r);
        paint.setColor(Color.BLACK);
        Paint tmpPaint = new Paint(paint); //小刻度画笔对象
        tmpPaint.setStrokeWidth(1);
        tmpPaint.setTextSize(35);
        tmpPaint.setTextAlign(Paint.Align.CENTER);



        canvas.rotate(-90,0f,0f);

        float  y = length;
        y = - y;
        int count = 12; //总刻度数
        paint.setColor(backGroundColor);

        float tempRou = 180 / 12f;

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);

        //绘制刻度和百分比
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
        canvas.translate(canvas.getWidth()/2, r);
        float change;

        if (perPoint < 1 ){
            change = perPoint * 180;
        }else {
            change = 180;
        }

        //根据参数得到旋转角度
        canvas.rotate(-90 + change,0f,0f);

        //绘制三角形形成指针
        Path path = new Path();
        path.moveTo(0 , pointLength);
        path.lineTo(-15 , 0);
        path.lineTo(15,0);
        path.lineTo(0 , pointLength);
        path.close();
        canvas.drawPath(path, paint);

    }

    private void initRing(Canvas canvas) {

        canvas.save();
        canvas.translate(canvas.getWidth()/2, r);

        //圆环外侧描边
        paint.setColor(Color.parseColor("#979797"));
        RectF rect = new RectF( -length - 1, -length - 1, length + 1, length + 1);
        canvas.drawArc(rect, 175, 190, true, paint);

        //前100红黄渐变圆环
        int[] colors = {Color.parseColor("#F95A37"), Color.parseColor("#f9cf45"), Color.parseColor("#00ff00")};
        float[] positions = {0.5f - 5f/180f * 0.5f, 0.5f + 0.5f * 5f / 6f, 1.0f};
        SweepGradient sweepGradient = new SweepGradient(0, 0, colors, positions);
        paint.setShader(sweepGradient);
        rect = new RectF( -length, -length, length, length);
        canvas.drawArc(rect, 175, 5f + 180f / 6f * 5f, true, paint);


        canvas.rotate(5,0f,0f);
        int[] colors2 = {Color.parseColor("#79D062"),  Color.parseColor("#3FBF55")};
        float[] positions2 = {0.5f + 0.5f * ( 145f / 180f), 1.0f};


        //100之后绿色渐变圆环
        sweepGradient = new SweepGradient(0, 0, colors2, positions2);
        paint.setShader(sweepGradient);
        rect = new RectF( -length, -length, length, length);
        canvas.drawArc(rect, 180f + 180f * (145f / 180f), 180f / 6 + 5, true, paint);

        canvas.rotate(-5,0f,0f);
        //圆环内侧描边
        paint.setShader(null);
        paint.setColor(Color.parseColor("#979797"));
        rect = new RectF( - (length / 3f * 2f  ), -(length / 3f * 2f), length / 3f  * 2f, length / 3f * 2f);
        canvas.drawArc(rect, 175, 190, true, paint);


        //内部背景色填充
        paint.setColor(backGroundColor);
        paint.setShader(null);
        rect = new RectF( - (length - length / 3f  - 1), -(length / 3f * 2f - 1), length - length / 3f -1 , length / 3f * 2f - 1);
        canvas.drawArc(rect, 174, 192, true, paint);


        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, r);


        //圆环两侧描边
        paint.setColor(Color.BLACK);
        canvas.rotate(85 ,0f , 0f);
        canvas.drawLine(0f, 0 , 0, length / 3, paint);

        canvas.rotate(-85 + -85  ,0f , 0f);
        canvas.drawLine(0f, 0 , 0, length / 3 , paint);


    }



    public void cgangePer(float per ){
        this.perOld = this.per;
        this.per = per;
        new Thread(new SetIndex()).start();

    }
}