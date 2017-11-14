package com.example.kolin.fintechhomework7.ui.parentchild;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kolin.fintechhomework7.R;
import com.example.kolin.fintechhomework7.model.Node;
import com.example.kolin.fintechhomework7.ui.adapters.ViewPagerAdapter;

public class ChildParentActivity extends AppCompatActivity
        implements ChildFragment.ChildFragmentListener, ParentFragment.ParentFragmentListener {

    private static final String KEY_ID = "ID";

    private ViewPager viewPager;
    private TabLayout tabs;
    private ViewPagerAdapter viewPagerAdapter;

    public static void start(Context context, Node node) {
        Intent starter = new Intent(context, ChildParentActivity.class);
        starter.putExtra(KEY_ID, node);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_parent);


        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                new String[]{getString(R.string.children), getString(R.string.parents)}, getIntent().getParcelableExtra(KEY_ID));

        viewPager = findViewById(R.id.cp_activity_view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabs = findViewById(R.id.cp_activity_tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void childReferenceChange() {
        ((Updatable) viewPagerAdapter.getFragmentAtPosition(1)).update();
    }


    @Override
    public void parentReferenceChanged() {
        ((Updatable) viewPagerAdapter.getFragmentAtPosition(0)).update();
    }
}
