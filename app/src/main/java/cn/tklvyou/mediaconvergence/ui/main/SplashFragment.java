package cn.tklvyou.mediaconvergence.ui.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.tklvyou.mediaconvergence.R;

/**
 * Created by Administrator on 2017/11/16.
 */

public class SplashFragment extends Fragment {

    public SplashFragment(){

    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ret  = inflater.inflate(R.layout.activity_splash_first,container,false);
        initView(ret);
        return ret;
    }

    private void initView(View ret) {
        if(ret != null){
            ImageView imageView = ret.findViewById(R.id.ivSplash);
            playAnimator(imageView);
        }
    }

    private void playAnimator(ImageView imageView) {
        if(imageView != null){
            PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat("alpha",1f,0.7f,0.1f);
            ObjectAnimator.ofPropertyValuesHolder(imageView,pvhA).setDuration(2000).start();
        }
    }
}