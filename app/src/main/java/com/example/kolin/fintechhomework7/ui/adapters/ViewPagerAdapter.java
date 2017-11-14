package com.example.kolin.fintechhomework7.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.kolin.fintechhomework7.model.Node;
import com.example.kolin.fintechhomework7.ui.parentchild.ChildFragment;
import com.example.kolin.fintechhomework7.ui.parentchild.ParentFragment;

/**
 * Created by kolin on 14.11.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Node node;

    private SparseArray<Fragment> fragments = new SparseArray<>();

    public ViewPagerAdapter(FragmentManager fm, String[] titles, Node node) {
        super(fm);
        this.titles = titles;
        this.node = node;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ChildFragment.newInstance(node);
            case 1:
                return ParentFragment.newInstance(node);
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    public Fragment getFragmentAtPosition(int position){
        return position >= 0 && position <= fragments.size() - 1
                ? fragments.get(position)
                : null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles.length < getCount())
            return "";
        return titles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }


}
