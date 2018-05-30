package com.mafengwotab.mafengwotab.view;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mafengwotab.mafengwotab.R;
import com.mafengwotab.mafengwotab.interf.PageChangeListener;
import com.mafengwotab.mafengwotab.util.UIUtil;
import java.util.List;
/**
 * Created by ouyang on 2018/5/23.
 */

public class BizieViewPagerIndicator extends LinearLayout{
    //初始化画笔
    private Paint mPaint;
    //水平滚动 如果不想其水平滚动，这里可以用其他代替
    private HorizontalScrollView mScrollView;
    //tab对应的文字区域
    private LinearLayout mTitleContainer;
    //tab对应的下划线区域
    private LinearLayout mIndicatorContainer;
    //tab标题列表
    private List<String> mTabTitles;
    //当滑动的时候，mViewPager也要切换
    public ViewPager mViewPager;
    //颜色
    private static final int COLOR_TEXT_NORMAL = 0x77666666;
    private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFF000000;
    //显示个数
    private int mTabVisibleCount = 4;
    //下划线
    BizierView mIndicator;
    Context context;
    public BizieViewPagerIndicator(Context context){
        super(context);
        this.context=context;
        init(context);
    }

    // 设置关联的ViewPager
    public void setViewPager(ViewPager mViewPager, int pos)
    {
        this.mViewPager = mViewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                //先重置所有字体
                resetTextViewColor();
                // 设置字体颜色高亮
                highLightTextView(position);
                // 回调
                if (onPageChangeListener != null)
                {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels)
            {

                //当viewpager滚动时，下划线滚动
                scroll(position,positionOffset);
                // 回调
                if (onPageChangeListener != null)
                {
                    onPageChangeListener.onPageScrolled(position,
                            positionOffset, positionOffsetPixels);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                // 回调
                if (onPageChangeListener != null){
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        // 设置当前页
        mViewPager.setCurrentItem(pos);
    }

    private void scroll(int position,float offset){
        // 不断改变偏移量，invalidate
        int tabWidth = UIUtil.getScreenWidth(context)/mTabVisibleCount;
        // 容器滚动，当移动到倒数第二个的时候，开始滚动
        if (offset > 0 && position >= (mTabVisibleCount - 3)
                && mTitleContainer.getChildCount() > mTabVisibleCount)
        {
            if (mTabVisibleCount != 1)
            {
                //当viewpager滚动的时候，mScrollView也要跟着滚动
                mScrollView.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth
                       + (int) (tabWidth * offset) ,0);
             }else
            // 为count为1时 的特殊处理
            {
                mScrollView.scrollTo(
                        position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        //下划线跟着滚动
        mIndicator.scroll(position,offset,mTabVisibleCount);
    }

    /**
     * 高亮文本
     *
     * @param position
     */
    protected void highLightTextView(int position)
    {
        View view = mTitleContainer.getChildAt(position);
        if (view instanceof TextView)
        {
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
        }
    }

    /**
     * 设置可见的tab的数量
     *
     * @param count
     */
    public void setVisibleTabCount(int count)
    {
        this.mTabVisibleCount = count;
    }

    /**
     * 设置tab的标题内容 可选，可以自己在布局文件中写死
     *
     * @param datas
     */
    public void setTabItemTitles(List<String> datas)
    {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (datas != null && datas.size() > 0)
        {
          //this.removeAllViews();
            this.mTabTitles = datas;
            for (String title : mTabTitles)
            {
                    //根据标题生成我们的TextView
                    TextView tv = new TextView(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    lp.width = UIUtil.getScreenWidth(context) / mTabVisibleCount;
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.parseColor("#666666"));
                    tv.setText(title);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    tv.setLayoutParams(lp);
                    // 添加view
                    mTitleContainer.addView(tv);
            }


            //获取mTitleContainer的高度
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mTitleContainer.measure(w,h);
            mScrollView.measure(w,h);
            int height = mTitleContainer.getMeasuredHeight();

            //为mIndicatorContainer区域添加下划线
            FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mIndicator=new BizierView(getContext(), UIUtil.getScreenWidth(context) / mTabVisibleCount,height);
            mIndicatorContainer.addView(mIndicator,lp1);
            // 设置item的click事件
            setItemClickEvent();
        }

    }
    /**
     * 重置文本颜色
     */
    private void resetTextViewColor()
    {
        for (int i = 0; i < mTitleContainer.getChildCount(); i++)
        {
            View view = mTitleContainer.getChildAt(i);
            if (view instanceof TextView)
            {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }


    // 对外的ViewPager的回调接口
    private PageChangeListener onPageChangeListener;

    // 对外的ViewPager的回调接口的设置
    public void setOnPageChangeListener(PageChangeListener pageChangeListener)
    {
        this.onPageChangeListener = pageChangeListener;
    }


    private void init(Context context){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        View root = LayoutInflater.from(getContext()).inflate(R.layout.pager_navigator_layout,this);
        mScrollView =root.findViewById(R.id.scroll_view);
        mTitleContainer = root.findViewById(R.id.title_container);
        mIndicatorContainer = root.findViewById(R.id.indicator_container);
    }

    public BizieViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
        // 获得自定义属性，tab的数量
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ViewPagerIndicator);
        mTabVisibleCount = a.getInt(R.styleable.ViewPagerIndicator_item_count,
                mTabVisibleCount);
        // 初始化画笔
        mPaint.setColor(Color.parseColor("#E91E63"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(dpToPixel(5));

    }

    public  float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    public BizieViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    //布局加载完成调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = mTitleContainer.getChildCount();
        if (cCount == 0)
            return;
        for (int i = 0; i < cCount; i++)
        {
            View view = mTitleContainer.getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view
                    .getLayoutParams();
            lp.weight = 0;
            lp.width = UIUtil.getScreenWidth(context) / mTabVisibleCount;
            view.setLayoutParams(lp);
        }
        // 设置点击事件
        setItemClickEvent();
    }

    /**
     * 设置点击事件
     */
    public void setItemClickEvent()
    {
        int cCount = mTitleContainer.getChildCount();
        for (int i = 0; i < cCount;i++)
        {
            final int j = i;
            View view = mTitleContainer.getChildAt(i);
            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
