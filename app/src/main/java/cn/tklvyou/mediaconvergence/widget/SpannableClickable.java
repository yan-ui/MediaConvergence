package cn.tklvyou.mediaconvergence.widget;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import cn.tklvyou.mediaconvergence.base.MyApplication;


/**
 * @author yiw
 * @Description:
 * @date 16/1/2 16:32
 */
public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR = Color.parseColor("#8290AF");
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = DEFAULT_COLOR;
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
