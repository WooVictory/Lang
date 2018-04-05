package com.sopt.lang.Intro;

import android.support.v4.app.FragmentStatePagerAdapter;

import com.sopt.lang.Intro.IntroFragment.FirstFragment;
import com.sopt.lang.Intro.IntroFragment.FourthFragment;
import com.sopt.lang.Intro.IntroFragment.SecondFragment;
import com.sopt.lang.Intro.IntroFragment.ThirdFragment;

/**
 * Created by johee on 2018-01-01.
 */

public class PagerAdapter extends FragmentStatePagerAdapter
{
    public PagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
            case 3:
                return new FourthFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount()
    {
        return 4;
    }
}
