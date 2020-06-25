package com.example.qr6;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class InfoAdapter extends PagerAdapter {
List<View> pages = null;

public InfoAdapter(List<View> pages){
    this.pages=pages;
}

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {
        View v = pages.get(position);
        ((ViewPager) container).addView(v,0);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }


}
