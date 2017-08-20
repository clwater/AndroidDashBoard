# AndroidDashBoard


android自定义View之仪表盘


## 背景
随着项目开发 越来越多的需求被摆在面前 其中不免涉及到定制的功能
其中仪表盘也是一个很常用的功能
<!-- more -->

## 效果图
![效果图](http://ooymoxvz4.bkt.clouddn.com/17-8-20/25082465.jpg)

## 设计过程
### 外侧渐变圆环
![外侧圆环效果](http://ooymoxvz4.bkt.clouddn.com/17-8-20/78850293.jpg)
### 外侧刻度盘及文字显示
![外侧刻度盘及文字显示](http://ooymoxvz4.bkt.clouddn.com/17-8-20/30532330.jpg)
### 指针显示
![指针显示](http://ooymoxvz4.bkt.clouddn.com/17-8-20/72116334.jpg)
### 内部圆环及文字展示
![内部圆环及文字展示](http://ooymoxvz4.bkt.clouddn.com/17-8-20/81417498.jpg)

## 代码实现
### 自定义组件显示优化

设置自定义组件的时候要优化组件的高度

```java
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      int width = MeasureSpec.getSize(widthMeasureSpec);
      int heitht = width / 2 / 4 * 5;
      initIndex(width / 2);
      //优化组件高度
      setMeasuredDimension(width, heitht);
  }
```

### onDraw()过程

```java
  protected void onDraw(Canvas canvas) {
      //禁用硬件加速
      setLayerType(LAYER_TYPE_SOFTWARE, null);
      //外侧颜色指示圆环
      initRing(canvas);
      //刻度文字
      initScale(canvas);
      //指针
      initPointer(canvas);
      //提示内容
      initText(canvas);
  }
```

主要还是这个四个绘制的过程

## 外侧颜色指示圆环
1. 首先绘制的前一部分的红黄渐变圆环
这个圆环并不是一个180度的圆环 而是一个两百度的圆环 下侧再实现水平的效果
2. 绘制后一部分的绿色渐变圆环
3. 修正底部的效果 修改成水平的效果
4. 绘制内部半圆 遮盖住渐变的半圆

```java
private void initRing(Canvas canvas) {
    paint.setAntiAlias(true);
    paint.setStrokeWidth(2);
    canvas.save();
    //canvas中心移动到中间
    canvas.translate(canvas.getWidth()/2, r);


    //前100红黄渐变圆环
    paint.setStyle(Paint.Style.FILL);
    //设置渐变的颜色范围
    int[] colors = {Color.parseColor("#F95A37"), Color.parseColor("#f9cf45")};
    //设置的渐变起止位置
    float[] positions = {0.5f - 10f/180f * 0.5f, 0.5f + 0.5f * 5f / 6f};
    //设置渐变的蒙版
    SweepGradient sweepGradient = new SweepGradient(0, 0, colors, positions);
    paint.setShader(sweepGradient);
    rect = new RectF( -length, -length, length, length);
    //绘制圆环
    canvas.drawArc(rect, 170, 10f + 180f / 6f * 5f, true, paint);



    //100之后绿色渐变圆环
    paint.setStyle(Paint.Style.FILL);
    canvas.rotate(10,0f,0f);
    int[] colors2 = {Color.parseColor("#79D062"),  Color.parseColor("#3FBF55")};
    float[] positions2 = {0.5f + 0.5f * ( 144f / 180f), 1.0f};
    sweepGradient = new SweepGradient(0, 0, colors2, positions2);
    paint.setShader(sweepGradient);
    rect = new RectF( -length, -length, length, length);
    canvas.drawArc(rect, 180f + 180f * (140f / 180f), 180f / 6 + 10, true, paint);



    canvas.restore();
    canvas.save();
    canvas.translate(canvas.getWidth()/2, r);

    //绘制描边效果的画笔
    strokePain = new Paint(paint);
    strokePain.setColor(0x3f979797);
    strokePain.setStrokeWidth(10);
    strokePain.setShader(null);
    strokePain.setStyle(Paint.Style.STROKE);
    canvas.drawArc(rect, 170, 200, true, strokePain);



    canvas.restore();
    canvas.save();
    canvas.translate(canvas.getWidth()/2, r);

    //底边水平
    paint.setShader(null);
    paint.setColor(backGroundColor);
    paint.setStyle(Paint.Style.FILL);
    canvas.drawRect(-length  , (float) (Math.sin(Math.toRadians(10) ) * length /3f * 2f), length  ,  (float) (Math.sin(Math.toRadians(10)) * length  + 100) , paint);
    canvas.drawRect(-length  , (float) (Math.sin(Math.toRadians(10) ) * length /3f * 2f), length  ,  (float) (Math.sin(Math.toRadians(10) ) * length /3f * 2f) , strokePain);


    //内部背景色填充
    paint.setColor(backGroundColor);
    paint.setShader(null);
    rect = new RectF( - (length - length / 3f  - 2), -(length / 3f * 2f - 2), length - length / 3f -2 , length / 3f * 2f - 2);
    canvas.drawArc(rect, 170, 200, true, strokePain);
    canvas.drawArc(rect, 0, 360, true, paint);



}
```

## 外侧刻度盘及文字显示
旋转画布绘制对应角度的显示及刻度

```java
  private void initScale(Canvas canvas) {
      canvas.restore();
      canvas.save();
      canvas.translate(canvas.getWidth()/2, r);
      paint.setColor(Color.parseColor("#999999"));

      tmpPaint = new Paint(paint); //刻度画笔对象
      tmpPaint.setStrokeWidth(1);
      tmpPaint.setTextSize(35);
      tmpPaint.setTextAlign(Paint.Align.CENTER);

      canvas.rotate(-90,0f,0f);

      float  y = length;
      y = - y;
      int count = 12; //总刻度数
      paint.setColor(backGroundColor);

      float tempRou = 180 / 12f;
      //每次旋转的角度
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
```

## 指针显示

指针显示的比较简单也是唯二需要变化的之一

指针的绘制比较简单 根据传入的角度(百分比)旋转对应的角度 填充绘制一个三角形

```java
  private void initPointer(Canvas canvas) {
      paint.setColor(Color.BLACK);


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
      path.lineTo(-10 , 0);
      path.lineTo(10,0);
      path.lineTo(0 , pointLength);
      path.close();

      canvas.drawPath(path, paint);

  }
```

## 内部圆环及文字展示

先绘制一个带阴影的圆环 再居中绘制提示的文本信息

```java
  private void initText(Canvas canvas) {
      //抗锯齿
      canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
      canvas.restore();
      canvas.save();
      canvas.translate(canvas.getWidth()/2, r);

      float rIndex = length ;

      //设置文字展示的圆环
      paint.setColor(Color.parseColor("#eeeeee"));
      paint.setShader(null);
      paint.setShadowLayer(5, 0, 0, 0x54000000);
      rect = new RectF( - (rIndex/ 3 ), - (rIndex / 3), rIndex / 3, rIndex / 3);
      canvas.drawArc(rect, 0, 360, true, paint);

      paint.clearShadowLayer();

      canvas.restore();
      canvas.save();
      canvas.translate(canvas.getWidth()/2f , r);


      textPaint.setStrokeWidth(1);
      textPaint.setAntiAlias(true);

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

      float swidth = textPaint.measureText(String.valueOf(_per));
      //计算偏移量 是的数字和百分号整体居中显示
      swidth =   (swidth - (swidth + 22) / 2);


      canvas.translate( swidth , 0);
      canvas.drawText("" + _per, 0, 0, textPaint);

      textPaint.setTextSize(30);
      textPaint.setTextAlign(Paint.Align.LEFT);

      canvas.drawText("%" , 0, 0, textPaint);
      textPaint.setTextAlign(Paint.Align.CENTER);
      textPaint.setColor(Color.parseColor("#999999"));


      canvas.restore();
      canvas.save();
      canvas.translate(canvas.getWidth()/2  , r + length / 3 /2 );
      canvas.drawText("完成率" , 0, 0, textPaint);

  }
```

## 更新动画
使用ValueAnimator实现指针的转动动画效果

```java
  public void cgangePer(float per ){
      this.perOld = this.per;
      this.per = per;
      ValueAnimator va =  ValueAnimator.ofFloat(perOld,per);
      va.setDuration(1000);
      va.setInterpolator(new OvershootInterpolator());
      va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
              perPoint = (float) animation.getAnimatedValue();
              invalidate();
          }
      });
      va.start();

  }
```


