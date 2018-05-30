package com.mafengwotab.mafengwotab.view;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.mafengwotab.mafengwotab.util.UIUtil;

/**
 * Created by fengyun on 2018/5/23.
 */

public class BizierView extends View{
    //画笔
    private Paint mPaint;
    //划线路径
    private Path mPath = new Path();
    //控制下划线长度
    private static final float RADIO =1.0f/3;
    //一个tab的宽度
    private int  width;
    //文字区域的高度
    int height;
    //MAXRAD 最大弧度
    int MAXRAD=10;
    //這裡用於控制弧度變化區域
    float viewoffset;
    //下面這兩個變量，可以使其下劃線位置變化
    private float  mTranslationX;
    private float  mTranslationY;
    Context context;
    public BizierView(Context context){
        super(context);
        this.context=context;
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        mPath = new Path();
        //cubicTo mPath.cubicTo(x1, y1, x2, y2, x3, y3) (x1,y1) 为控制点，(x2,y2)为控制点，(x3,y3) 为结束点。
        //cubicTo 同样是用来实现贝塞尔曲线的
        //对于quadTo  mPath.quadTo(x1, y1, x2, y2) (x1,y1) 为控制点，(x2,y2)为结束点。
        //这里暂时先用quadTo来控制一个点实现波浪线效果，如果要完全模仿蚂蜂窝，可以使用cubicTo，控制两个节点的Y坐标变化
        mPath.moveTo(width*RADIO+mTranslationX,height*2);
        if(viewoffset<=0.5){
            mPath.quadTo(width/2+mTranslationX,height*2+MAXRAD-mTranslationY*(MAXRAD*2/width),width-width*RADIO+mTranslationX,height*2);
        }else{
            mPath.quadTo(width/2+mTranslationX,height*2-(MAXRAD*2)+mTranslationY*(MAXRAD*2/width),width-width*RADIO+mTranslationX,height*2);
        }
        canvas.drawPath(mPath, mPaint);
    }


    public void scroll(int position,float offset,int mTabVisibleCount){

        mTranslationX = UIUtil.getScreenWidth(context)/mTabVisibleCount * (position + offset);
        viewoffset=offset;
        if(offset<=0.5){
            mTranslationY=UIUtil.getScreenWidth(context)/mTabVisibleCount * (offset);
        }else{
            mTranslationY= (float) (UIUtil.getScreenWidth(context)/mTabVisibleCount * (offset-0.5));
        }
        //通过此方法，可以重复调用draw，这样就让mTranslationY不断变化
        invalidate();
    }



    private void init(Context context){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 初始化画笔
        mPaint.setColor(Color.parseColor("#E91E63"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(dpToPixel(2));
    }

    public BizierView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public BizierView(Context context,int width,int height){
        super(context);
        this.context=context;
        this.width=width;
        this.height=height;
        init(context);
    }

    public  float dpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    public BizierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }
}
