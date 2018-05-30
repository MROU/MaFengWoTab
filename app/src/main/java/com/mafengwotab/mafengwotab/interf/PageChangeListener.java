package com.mafengwotab.mafengwotab.interf;

/**
 * Created by ouyang on 2018/5/25.
 */

public interface PageChangeListener {
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels);

    public void onPageSelected(int position);

    public void onPageScrollStateChanged(int state);
}


