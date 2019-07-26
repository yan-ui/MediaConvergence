package cn.tklvyou.mediaconvergence.ui.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import cn.tklvyou.mediaconvergence.model.Channel;

/**
 * @author ChayChan
 * @description: 频道的adapter
 * @date 2017/6/16  9:45
 */

public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private List<RxFragment> mFragments;

    public ChannelPagerAdapter(List<RxFragment> fragmentList, FragmentManager fm) {
        super(fm);
        mFragments = fragmentList != null ? fragmentList : new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}