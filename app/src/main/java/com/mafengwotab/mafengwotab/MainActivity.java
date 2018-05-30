package com.mafengwotab.mafengwotab;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mafengwotab.mafengwotab.adapter.RecyclerAdapter;
import com.mafengwotab.mafengwotab.fragment.ContentFragment;
import com.mafengwotab.mafengwotab.util.AnimationUtil;
import com.mafengwotab.mafengwotab.util.UIUtil;
import com.mafengwotab.mafengwotab.util.ViewUtils;
import com.mafengwotab.mafengwotab.view.BizieViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContentFragment.OnFragmentInteractionListener, View.OnClickListener {
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    //初始化显示数据
    private List<String> datas = Arrays.asList("精选", "正在旅游", "国内", "国外",
          "带娃旅行", "海岛游", "情侣出行", "自驾游");
    //自定义指示线
    private BizieViewPagerIndicator mIndicator;
    //下拉显示更多
    private ImageView iv_showmore;
    //弹窗
    PopupWindow mPopupWindow;
    //头部图片
    ImageView iv_header;
    //透明背景
    private View bg_layout;
    //PopWindow的弹出位置控制
    int fromYDelta;
    //是否PopWindow在显示
    private boolean isPopWindowShowing=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListen();

    }

    private void initListen() {
        iv_showmore.setOnClickListener(this);
    }

    private void initData() {
        //初始化Fragment
         for(int i=0;i<datas.size();i++){
             ContentFragment contentFragment=ContentFragment.newInstance(datas.get(i));
             fragments.add(contentFragment);
         }
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mViewPager.setAdapter(mAdapter);

        //显示个数
        mIndicator.setVisibleTabCount(5);
        //设置Tab上的标题
        mIndicator.setTabItemTitles(datas);
        mViewPager.setAdapter(mAdapter);
        //设置关联的ViewPager
        mIndicator.setViewPager(mViewPager,0);
    }

    private void initView(){
        mViewPager=findViewById(R.id.viewpager);
        mIndicator=findViewById(R.id.id_indicator);
        iv_showmore=findViewById(R.id.iv_showmore);
        iv_header=findViewById(R.id.iv_header);
        bg_layout=findViewById(R.id.bg_layout);

        bg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPopWindowShowing){
                    mPopupWindow.getContentView().startAnimation(AnimationUtil.createOutAnimation(MainActivity.this, fromYDelta));
                    mPopupWindow.getContentView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                        }
                    },AnimationUtil.ANIMATION_OUT_TIME);
                }
            }
        });
    }


    private void showPopupWindow(){
        final View contentView= LayoutInflater.from(this).inflate(R.layout.common_dialog,null);
        mPopupWindow=new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        RecyclerView recyclerView=contentView.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));//这里用线性宫格显示 类似于grid view
        recyclerView.setAdapter(new RecyclerAdapter(MainActivity.this,datas));
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //将这两个属性设置为false，使点击popupwindow外面其他地方不会消失
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(false);
        bg_layout.setVisibility(View.VISIBLE);
        //获取popupwindow高度确定动画开始位置
        int contentHeight= ViewUtils.getViewMeasuredHeight(contentView);
        Log.i("contentview","contentview height="+contentHeight);
        mPopupWindow.showAsDropDown(iv_showmore, 0, 0);
        fromYDelta=-contentHeight-50;

        mPopupWindow.getContentView().startAnimation(AnimationUtil.createInAnimation(this, fromYDelta));

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopWindowShowing=false;
                bg_layout.setVisibility(View.GONE);
            }
        });


        isPopWindowShowing=true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.iv_showmore:
                if(isPopWindowShowing){
                    mPopupWindow.getContentView().startAnimation(AnimationUtil.createOutAnimation(MainActivity.this, fromYDelta));
                    mPopupWindow.getContentView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                        }
                    },AnimationUtil.ANIMATION_OUT_TIME);
                }else{
                    showPopupWindow();
                }
                break;
        }
    }




}
