package com.example.qr6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class Info extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<View>();
        pages.add(inflater.inflate(R.layout.view_screen_1,null));
        View page = inflater.inflate(R.layout.view_screen_2,null);
        Button exit = (Button) page.findViewById(R.id.buttonExit);
        pages.add(page);
        InfoAdapter adapter = new InfoAdapter(pages);
        ViewPager viewPager=new ViewPager(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        setContentView(viewPager);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
