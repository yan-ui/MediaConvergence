package cn.tklvyou.mediaconvergence.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.netease.neliveplayer.playerkit.common.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.helper.GlideManager;
import cn.tklvyou.mediaconvergence.model.NewsBean;
import cn.tklvyou.mediaconvergence.model.NewsMultipleItem;
import cn.tklvyou.mediaconvergence.model.HaveSecondModuleNewsModel;
import cn.tklvyou.mediaconvergence.ui.home.AudioController;
import cn.tklvyou.mediaconvergence.ui.home.ImagePagerActivity;
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailActivity;
import cn.tklvyou.mediaconvergence.ui.video_player.VodActivity;
import cn.tklvyou.mediaconvergence.utils.GlideCircleTransform;
import cn.tklvyou.mediaconvergence.utils.UrlUtils;
import cn.tklvyou.mediaconvergence.widget.ExpandTextView;
import cn.tklvyou.mediaconvergence.widget.MultiImageView;
import cn.tklvyou.mediaconvergence.widget.TagTextView;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * modify by AllenCoder
 */
public class NewsMultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<NewsMultipleItem, BaseViewHolder> implements AudioController.AudioControlListener {

    public NewsMultipleItemQuickAdapter(Context context, List data) {
        super(data);
        addItemType(NewsMultipleItem.VIDEO, R.layout.item_news_video);
        addItemType(NewsMultipleItem.TV, R.layout.item_news_suixi_tv);
        addItemType(NewsMultipleItem.NEWS, R.layout.item_news_news_layout);
        addItemType(NewsMultipleItem.SHI_XUN, R.layout.item_news_shi_xun);
        addItemType(NewsMultipleItem.WEN_ZHENG, R.layout.item_news_wen_zheng);
        addItemType(NewsMultipleItem.JU_ZHENG, R.layout.item_news_news_layout);
        addItemType(NewsMultipleItem.WECHAT_MOMENTS, R.layout.item_winxin_circle);
        addItemType(NewsMultipleItem.READING, R.layout.item_news_reading);
        addItemType(NewsMultipleItem.LISTEN, R.layout.item_news_listen);
        addItemType(NewsMultipleItem.DANG_JIAN, R.layout.item_news_news_layout);
        addItemType(NewsMultipleItem.ZHUAN_LAN, R.layout.item_news_news_layout);
        addItemType(NewsMultipleItem.GONG_GAO, R.layout.item_news_news_layout);
        addItemType(NewsMultipleItem.ZHI_BO, R.layout.item_news_zhi_bo_layout);
    }

    private AudioController mAudioControl;

    public void setAudioController(AudioController audioController) {
        this.mAudioControl = audioController;
        mAudioControl.setOnAudioControlListener(this);
    }


    @Override
    protected void convert(BaseViewHolder helper, NewsMultipleItem item) {
        switch (helper.getItemViewType()) {
            case NewsMultipleItem.VIDEO:
                NewsBean bean = (NewsBean) item.getDataBean();

                if(SPUtils.getInstance().getBoolean("delv",false)){
                    helper.setVisible(R.id.deleteBtn,true);
                }else {
                    helper.setVisible(R.id.deleteBtn,false);
                }

                helper.setText(R.id.tvNewsTitle, bean.getName());
                helper.setText(R.id.tvVideoTime, formatTime(Double.valueOf(bean.getTime()).longValue()));
                helper.setText(R.id.tvCommentNum, "" + bean.getComment_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());
                helper.setText(R.id.tvName, bean.getNickname());

                TextView tvGoodNum = helper.getView(R.id.tvGoodNum);

                Drawable[] drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }

                // 加载网络图片
                if (!StringUtils.isEmpty(bean.getAvatar().trim())) {
                    GlideManager.loadCircleImg(bean.getAvatar(), helper.getView(R.id.ivAvatar), R.mipmap.default_avatar);
                } else {
                    GlideManager.loadCircleImg(R.mipmap.default_avatar, helper.getView(R.id.ivAvatar));
                }

                GlideManager.loadImg(bean.getImage(), helper.getView(R.id.ivVideoBg));

                helper.addOnClickListener(R.id.ivStartPlayer,R.id.deleteBtn);
                break;
            case NewsMultipleItem.TV:
                HaveSecondModuleNewsModel suixiTvModel = (HaveSecondModuleNewsModel) item.getDataBean();

                if (suixiTvModel.getData().size() == 0) {
                    helper.getView(R.id.llSuixiTvFirst).setVisibility(View.GONE);
                    helper.getView(R.id.llSuixiTvSecond).setVisibility(View.GONE);
                } else if (suixiTvModel.getData().size() == 1) {
                    helper.getView(R.id.llSuixiTvFirst).setVisibility(View.VISIBLE);
                    helper.getView(R.id.llSuixiTvSecond).setVisibility(View.INVISIBLE);
                } else {
                    helper.getView(R.id.llSuixiTvFirst).setVisibility(View.VISIBLE);
                    helper.getView(R.id.llSuixiTvSecond).setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tvModuleSecond, suixiTvModel.getModule_second());

                for (int i = 0; i < suixiTvModel.getData().size(); i++) {
                    if (i == 0) {
                        helper.setText(R.id.tvNameFirst, suixiTvModel.getData().get(0).getName());
                        helper.setText(R.id.tvTimeFirst, suixiTvModel.getData().get(0).getTime());
                        Glide.with(mContext).load(suixiTvModel.getData().get(0).getImage()).into((ImageView) helper.getView(R.id.ivImageFirst));
                    } else if (i == 1) {
                        helper.setText(R.id.tvNameSecond, suixiTvModel.getData().get(1).getName());
                        helper.setText(R.id.tvTimeSecond, suixiTvModel.getData().get(1).getTime());
                        Glide.with(mContext).load(suixiTvModel.getData().get(1).getImage()).into((ImageView) helper.getView(R.id.ivImageSecond));
                    }
                }

                helper.addOnClickListener(R.id.ivSuiXiTVFirstStartPlayer);
                helper.addOnClickListener(R.id.ivSuiXiTVSecondStartPlayer);
                helper.addOnClickListener(R.id.llSuixiTvFirst);
                helper.addOnClickListener(R.id.llSuixiTvSecond);
                helper.addOnClickListener(R.id.tvSuixiTvAll);

                break;

            case NewsMultipleItem.NEWS:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvTitle, bean.getName());
                helper.setText(R.id.tvTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

                tvGoodNum = helper.getView(R.id.tvGoodNum);

                drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }

                if (!StringUtils.isEmpty(bean.getImage())) {
                    //一张图片
                    helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                    helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                    GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));

                } else {
                    if (bean.getImages() == null || bean.getImages().size() == 0) {
                        //没有图片
                        helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                        helper.getView(R.id.ivImageOne).setVisibility(View.GONE);

                    } else {
                        if (bean.getImages().size() < 3) {
                            //一张图片
                            helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                            helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageOne));
                        } else {
                            //多张图片
                            helper.getView(R.id.ivImageOne).setVisibility(View.GONE);
                            helper.getView(R.id.llMultiImage).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageFirst));
                            GlideManager.loadRoundImg(bean.getImages().get(1), helper.getView(R.id.ivImageSecond));
                            GlideManager.loadRoundImg(bean.getImages().get(2), helper.getView(R.id.ivImageThree));
                        }
                    }
                }
                break;
            case NewsMultipleItem.SHI_XUN:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvTitle, bean.getName());
                helper.setText(R.id.tvTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());
                helper.setText(R.id.tvVideoTime, formatTime(Double.valueOf(bean.getTime()).longValue()));

                tvGoodNum = helper.getView(R.id.tvGoodNum);

                drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }

                GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));

                break;
            case NewsMultipleItem.WEN_ZHENG:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvTitle, bean.getName());
                helper.setText(R.id.tvTime, bean.getBegintime());
                helper.setText(R.id.tvCommentNum, "" + bean.getComment_num());

                if (!StringUtils.isEmpty(bean.getImage())) {
                    //一张图片
                    helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                    helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                    GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));

                } else {
                    if (bean.getImages() == null || bean.getImages().size() == 0) {
                        //没有图片
                        helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                        helper.getView(R.id.ivImageOne).setVisibility(View.GONE);

                    } else {

                        if (bean.getImages().size() < 3) {
                            //一张图片
                            helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                            helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageOne));
                        } else {
                            //多张图片
                            helper.getView(R.id.ivImageOne).setVisibility(View.GONE);
                            helper.getView(R.id.llMultiImage).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageFirst));
                            GlideManager.loadRoundImg(bean.getImages().get(1), helper.getView(R.id.ivImageSecond));
                            GlideManager.loadRoundImg(bean.getImages().get(2), helper.getView(R.id.ivImageThree));
                        }
                    }
                }
                break;
            case NewsMultipleItem.JU_ZHENG:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvTitle, bean.getName());
                helper.setText(R.id.tvTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

                tvGoodNum = helper.getView(R.id.tvGoodNum);

                drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }

                if (!StringUtils.isEmpty(bean.getImage())) {
                    //一张图片
                    helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                    helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                    GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));

                } else {
                    if (bean.getImages() == null || bean.getImages().size() == 0) {
                        //没有图片
                        helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                        helper.getView(R.id.ivImageOne).setVisibility(View.GONE);

                    } else {
                        if (bean.getImages().size() < 3) {
                            //一张图片
                            helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                            helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageOne));
                        } else {
                            //多张图片
                            helper.getView(R.id.ivImageOne).setVisibility(View.GONE);
                            helper.getView(R.id.llMultiImage).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageFirst));
                            GlideManager.loadRoundImg(bean.getImages().get(1), helper.getView(R.id.ivImageSecond));
                            GlideManager.loadRoundImg(bean.getImages().get(2), helper.getView(R.id.ivImageThree));
                        }
                    }
                }

//                suixiTvModel = (HaveSecondModuleNewsModel) item.getDataBean();
//
//                //todo:矩阵头像
////                GlideManager.loadRoundImg(suixiTvModel.getModule_second(), helper.getView(R.id.ivImageFirst));
//                helper.setText(R.id.tvJuzhengModuleSecond, suixiTvModel.getModule_second());
//                helper.addOnClickListener(R.id.tvJuzhengModuleSecond);
//
//                LinearLayout llNewsContainer = helper.getView(R.id.llNewsContainer);
//                llNewsContainer.removeAllViews();
//
//                for (int i = 0; i < suixiTvModel.getData().size(); i++) {
//                    View view = View.inflate(mContext, R.layout.item_news_news_layout, null);
//
//                    HaveSecondModuleNewsModel.DataBean dataBean = suixiTvModel.getData().get(i);
//
//                    TextView tvTitle = view.findViewById(R.id.tvTitle);
//                    TextView tvTime = view.findViewById(R.id.tvTime);
//                    TextView tvSeeNum = view.findViewById(R.id.tvSeeNum);
//                    tvGoodNum = view.findViewById(R.id.tvGoodNum);
//                    ImageView ivImageOne = view.findViewById(R.id.ivImageOne);
//                    ImageView ivImageFirst = view.findViewById(R.id.ivImageFirst);
//                    ImageView ivImageSecond = view.findViewById(R.id.ivImageSecond);
//                    ImageView ivImageThree = view.findViewById(R.id.ivImageThree);
//                    LinearLayout llMultiImage = view.findViewById(R.id.llMultiImage);
//
//                    tvTitle.setText(dataBean.getName());
//                    tvTime.setText(dataBean.getBegintime());
//                    tvSeeNum.setText(dataBean.getVisit_num());
//                    tvGoodNum.setText(dataBean.getLike_num());
//
//                    drawables = tvGoodNum.getCompoundDrawables();
//
//                    if (dataBean.getLike_status() == 1) {
//                        Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
//                        redGoodDrawable.setBounds(drawables[0].getBounds());
//                        tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
//                    } else {
//                        Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
//                        grayGoodDrawable.setBounds(drawables[0].getBounds());
//                        tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
//                    }
//
//                    if (!StringUtils.isEmpty(dataBean.getImage())) {
//                        //一张图片
//                        llMultiImage.setVisibility(View.GONE);
//                        ivImageOne.setVisibility(View.VISIBLE);
//
//                        GlideManager.loadRoundImg(dataBean.getImage(), ivImageOne);
//
//                    } else {
//                        if (dataBean.getImages() == null || dataBean.getImages().size() == 0) {
//                            //没有图片
//                            llMultiImage.setVisibility(View.GONE);
//                            ivImageOne.setVisibility(View.GONE);
//
//                        } else {
//
//                            if (dataBean.getImages().size() < 3) {
//                                //一张图片
//                                llMultiImage.setVisibility(View.GONE);
//                                ivImageOne.setVisibility(View.VISIBLE);
//
//                                GlideManager.loadRoundImg(dataBean.getImage(), ivImageOne);
//                            } else {
//                                //多张图片
//                                ivImageOne.setVisibility(View.GONE);
//                                llMultiImage.setVisibility(View.VISIBLE);
//
//                                GlideManager.loadRoundImg(dataBean.getImages().get(0), ivImageFirst);
//                                GlideManager.loadRoundImg(dataBean.getImages().get(1), ivImageSecond);
//                                GlideManager.loadRoundImg(dataBean.getImages().get(2), ivImageThree);
//                            }
//                        }
//                    }
//
//                    llNewsContainer.addView(view);
//                    view.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int id = dataBean.getId();
//                            String type = "文章";
//                            NewsDetailActivity.Companion.startNewsDetailActivity(mContext, type, id);
//                        }
//                    });
//
//                    View dividerView = new View(mContext);
//                    dividerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
//                    dividerView.setBackgroundColor(mContext.getResources().getColor(R.color.common_bg));
//
//                    llNewsContainer.addView(dividerView);
//
//                }

                break;
            case NewsMultipleItem.WECHAT_MOMENTS:
                bean = (NewsBean) item.getDataBean();
                helper.setVisible(R.id.deleteBtn, false);
                helper.setText(R.id.nameTv, bean.getNickname());
                helper.setText(R.id.timeTv, bean.getBegintime());

                helper.setText(R.id.tvCommentNum, "" + bean.getComment_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

                tvGoodNum = helper.getView(R.id.tvGoodNum);

                drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }


                if (!StringUtils.isEmpty(bean.getAvatar().trim())) {
                    GlideManager.loadRoundImg(bean.getAvatar(), helper.getView(R.id.headIv), 5f);
                } else {
                    GlideManager.loadRoundImg(R.mipmap.default_avatar, helper.getView(R.id.headIv));
                }

                ExpandTextView expandTextView = helper.getView(R.id.contentTv);
                if (!TextUtils.isEmpty(bean.getName())) {
                    expandTextView.setExpand(bean.isExpand());
                    expandTextView.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                        @Override
                        public void statusChange(boolean isExpand) {
                            bean.setExpand(isExpand);
                        }
                    });

                    expandTextView.setText(UrlUtils.formatUrlString(bean.getName()));
                }
                expandTextView.setVisibility(TextUtils.isEmpty(bean.getName()) ? View.GONE : View.VISIBLE);

                if (bean.getImages() != null && bean.getImages().size() > 0) {
                    //上传的是图片
                    ImageView ivVideo = helper.getView(R.id.ivVideo);
                    FrameLayout llVideo = helper.getView(R.id.llVideo);
                    llVideo.setVisibility(View.GONE);


                    MultiImageView multiImageView = helper.getView(R.id.multiImagView);
                    multiImageView.setVisibility(View.VISIBLE);
                    multiImageView.setList(bean.getImages());
                    multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //imagesize是作为loading时的图片size
                            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                            ImagePagerActivity.startImagePagerActivity(mContext, bean.getImages(), position, imageSize);

                        }
                    });

                } else {
                    //上传的是视频

                    MultiImageView multiImageView = helper.getView(R.id.multiImagView);
                    multiImageView.setVisibility(View.GONE);
                    FrameLayout llVideo = helper.getView(R.id.llVideo);
                    llVideo.setVisibility(View.VISIBLE);

                    ImageView ivVideo = helper.getView(R.id.ivVideo);
                    ivVideo.setBackgroundColor(Color.parseColor("#abb1b6"));
                    ivVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, VodActivity.class);
                            intent.putExtra("videoPath", bean.getVideo());
                            mContext.startActivity(intent);
                        }
                    });

                    GlideManager.loadImg(bean.getImage(), ivVideo);

                }
                break;
            case NewsMultipleItem.READING:
                bean = (NewsBean) item.getDataBean();

                if (helper.getLayoutPosition() == 0) {
                    helper.getView(R.id.ivReadingImage).getLayoutParams().height = ConvertUtils.dp2px(90);
                } else {
                    helper.getView(R.id.ivReadingImage).getLayoutParams().height = ConvertUtils.dp2px(115);
                }

                helper.setText(R.id.tvName, bean.getName());
                helper.setText(R.id.tvBeginTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());

                GlideManager.loadImg(bean.getImage(), helper.getView(R.id.ivReadingImage));
                break;
            case NewsMultipleItem.LISTEN:
                bean = (NewsBean) item.getDataBean();

                helper.setText(R.id.tvName, bean.getName());
                helper.setText(R.id.tvBeginTime, bean.getBegintime());

                if (bean.getTime().contains(":") || bean.getTime().contains("：")) {
                    helper.setText(R.id.tvTime, bean.getTime());
                } else {
                    helper.setText(R.id.tvTime, formatTime(Double.valueOf(bean.getTime()).longValue()));
                }

                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

                tvGoodNum = helper.getView(R.id.tvGoodNum);

                drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }


                if (bean.getPlayStatus()) {
                    helper.getView(R.id.play).setVisibility(View.GONE);
                    helper.getView(R.id.pause).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.play).setVisibility(View.VISIBLE);
                    helper.getView(R.id.pause).setVisibility(View.GONE);
                }

                helper.getView(R.id.play).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initStatus(mAudioControl.getPosition(), helper.getLayoutPosition());
                        mAudioControl.onPrepare(((NewsBean) mData.get(helper.getLayoutPosition()).getDataBean()).getAudio());
                        mAudioControl.onStart(helper.getLayoutPosition());
                    }
                });

                helper.getView(R.id.pause).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean playNClickIsSame = playNClickIsSame(mAudioControl.getPosition(), helper.getLayoutPosition());
                        if (playNClickIsSame) {
                            mAudioControl.onPause();
                        }
                    }
                });
                break;

            case NewsMultipleItem.DANG_JIAN:
                bean = (NewsBean) item.getDataBean();

                if (helper.getLayoutPosition() == 0) {
                    TagTextView tagTextView = helper.getView(R.id.tvTitle);
                    List<String> tags = new ArrayList<>();
                    tags.add("置顶");
                    tagTextView.setContentAndTag(bean.getName(), tags);
                } else {
                    helper.setText(R.id.tvTitle, bean.getName());
                }


                helper.setText(R.id.tvTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

                tvGoodNum = helper.getView(R.id.tvGoodNum);

                drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }

                if (!StringUtils.isEmpty(bean.getImage())) {
                    //一张图片
                    helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                    helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                    GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));

                } else {
                    if (bean.getImages() == null || bean.getImages().size() == 0) {
                        //没有图片
                        helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                        helper.getView(R.id.ivImageOne).setVisibility(View.GONE);

                    } else {
                        if (bean.getImages().size() < 3) {
                            //一张图片
                            helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                            helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageOne));
                        } else {
                            //多张图片
                            helper.getView(R.id.ivImageOne).setVisibility(View.GONE);
                            helper.getView(R.id.llMultiImage).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageFirst));
                            GlideManager.loadRoundImg(bean.getImages().get(1), helper.getView(R.id.ivImageSecond));
                            GlideManager.loadRoundImg(bean.getImages().get(2), helper.getView(R.id.ivImageThree));
                        }
                    }
                }

                break;
            case NewsMultipleItem.ZHUAN_LAN:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvTitle, bean.getName());
                helper.setText(R.id.tvTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

                tvGoodNum = helper.getView(R.id.tvGoodNum);

                drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }

                if (!StringUtils.isEmpty(bean.getImage())) {
                    //一张图片
                    helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                    helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                    GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));

                } else {
                    if (bean.getImages() == null || bean.getImages().size() == 0) {
                        //没有图片
                        helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                        helper.getView(R.id.ivImageOne).setVisibility(View.GONE);

                    } else {
                        if (bean.getImages().size() < 3) {
                            //一张图片
                            helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                            helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageOne));
                        } else {
                            //多张图片
                            helper.getView(R.id.ivImageOne).setVisibility(View.GONE);
                            helper.getView(R.id.llMultiImage).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageFirst));
                            GlideManager.loadRoundImg(bean.getImages().get(1), helper.getView(R.id.ivImageSecond));
                            GlideManager.loadRoundImg(bean.getImages().get(2), helper.getView(R.id.ivImageThree));
                        }
                    }
                }
                break;
            case NewsMultipleItem.GONG_GAO:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvTitle, bean.getName());
                helper.setText(R.id.tvTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

                tvGoodNum = helper.getView(R.id.tvGoodNum);

                drawables = tvGoodNum.getCompoundDrawables();

                if (bean.getLike_status() == 1) {
                    Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
                    redGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
                } else {
                    Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
                    grayGoodDrawable.setBounds(drawables[0].getBounds());
                    tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
                }

                if (!StringUtils.isEmpty(bean.getImage())) {
                    //一张图片
                    helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                    helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                    GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));

                } else {
                    if (bean.getImages() == null || bean.getImages().size() == 0) {
                        //没有图片
                        helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                        helper.getView(R.id.ivImageOne).setVisibility(View.GONE);

                    } else {
                        if (bean.getImages().size() < 3) {
                            //一张图片
                            helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                            helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageOne));
                        } else {
                            //多张图片
                            helper.getView(R.id.ivImageOne).setVisibility(View.GONE);
                            helper.getView(R.id.llMultiImage).setVisibility(View.VISIBLE);

                            GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageFirst));
                            GlideManager.loadRoundImg(bean.getImages().get(1), helper.getView(R.id.ivImageSecond));
                            GlideManager.loadRoundImg(bean.getImages().get(2), helper.getView(R.id.ivImageThree));
                        }
                    }
                }
                break;
            case NewsMultipleItem.ZHI_BO:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvName, bean.getName());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvTime, "" + bean.getBegintime());

                Glide.with(mContext).load(bean.getImage()).into((ImageView) helper.getView(R.id.ivVideoBg));

                helper.addOnClickListener(R.id.ivStartPlayer);
                break;
            default:
                break;
        }
    }


    /*
     * ----------------------------------------------------------------------------------
     * 音频播放处理  start
     * ----------------------------------------------------------------------------------
     */

    private boolean playNClickIsSame(int playIndex, int clickIndex) {
        return playIndex == clickIndex ? true : false;
    }

    private void initStatus(int playIndex, int clickIndex) {
        NewsBean oldEntity = (NewsBean) mData.get(playIndex).getDataBean();
        oldEntity.setPlayStatus(false);
//        oldEntity.setTime("00:00");
        if (playIndex >= ((LinearLayoutManager) getRecyclerView().getLayoutManager())
                .findFirstVisibleItemPosition()
                && playIndex <= ((LinearLayoutManager) getRecyclerView().getLayoutManager())
                .findLastVisibleItemPosition()) {
            if (getViewByPosition(getRecyclerView(), playIndex,
                    R.id.exo_progress) != null) {
                DefaultTimeBar timeBar = (DefaultTimeBar) getViewByPosition(getRecyclerView(),
                        playIndex,
                        R.id.exo_progress);
                timeBar.setPosition(0);
                timeBar.setBufferedPosition(0);
            }
            if (getViewByPosition(getRecyclerView(), playIndex,
                    R.id.tvTime) != null) {
                TextView startTime = (TextView) getViewByPosition(getRecyclerView(), playIndex,
                        R.id.tvTime);
                startTime.setText(oldEntity.getTime());
            }
            if (getViewByPosition(getRecyclerView(), playIndex, R.id.play) != null) {
                ImageView oldplay = (ImageView) getViewByPosition(getRecyclerView(), playIndex,
                        R.id.play);
                oldplay.setVisibility(View.VISIBLE);
            }
            if (getViewByPosition(getRecyclerView(), playIndex,
                    R.id.pause) != null) {
                ImageView oldpause = (ImageView) getViewByPosition(getRecyclerView(), playIndex,
                        R.id.pause);
                oldpause.setVisibility(View.GONE);
            }
            if (getViewByPosition(getRecyclerView(), clickIndex, R.id.play) != null) {
                ImageView newplay = (ImageView) getViewByPosition(getRecyclerView(), clickIndex,
                        R.id.play);
                newplay.setVisibility(View.GONE);
            }
            if (getViewByPosition(getRecyclerView(), clickIndex, R.id.pause) != null) {
                ImageView onewpause = (ImageView) getViewByPosition(getRecyclerView(), clickIndex,
                        R.id.pause);
                onewpause.setVisibility(View.VISIBLE);
            }
        } else {
            notifyItemChanged(playIndex);
        }
    }

    @Override
    public void setCurPositionTime(int position, long curPositionTime) {
//        if (getViewByPosition(getRecyclerView(), position,
//                R.id.exo_progress) != null) {
//            DefaultTimeBar timeBar = (DefaultTimeBar) getViewByPosition(getRecyclerView(), position,
//                    R.id.exo_progress);
//            timeBar.setPosition(curPositionTime);
//        }
    }

    @Override
    public void setDurationTime(int position, long durationTime) {
//        if (getViewByPosition(getRecyclerView(), position, R.id.exo_progress) != null) {
//            DefaultTimeBar timeBar = (DefaultTimeBar) getViewByPosition(getRecyclerView(), position,
//                    R.id.exo_progress);
//            timeBar.setDuration(durationTime);
//        }
    }

    @Override
    public void setBufferedPositionTime(int position, long bufferedPosition) {
//        if (getViewByPosition(getRecyclerView(), position,
//                R.id.exo_progress) != null) {
//            DefaultTimeBar timeBar = (DefaultTimeBar) getViewByPosition(getRecyclerView(), position,
//                    R.id.exo_progress);
//            timeBar.setBufferedPosition(bufferedPosition);
//        }
    }

    @Override
    public void setCurTimeString(int position, String curTimeString) {
        if (getViewByPosition(getRecyclerView(), position, R.id.tvTime) != null) {
            TextView startTime = (TextView) getViewByPosition(getRecyclerView(), position,
                    R.id.tvTime);
            startTime.setText(curTimeString);
        }
        int length = mData.size();
        if (mData != null && length > position && mData.get(position) != null) {
            NewsBean mediaEntity = (NewsBean) mData.get(position).getDataBean();
            mediaEntity.setTime(curTimeString);
        }
    }

    @Override
    public void isPlay(int position, boolean isPlay) {
        int length = mData.size();
        if (mData != null && length > position && mData.get(position) != null) {
            NewsBean mediaEntity = (NewsBean) mData.get(position).getDataBean();
            mediaEntity.setPlayStatus(isPlay);
            if (getViewByPosition(getRecyclerView(), position, R.id.play) != null
                    && getViewByPosition(getRecyclerView(), position, R.id.pause) != null) {
                ImageView play = (ImageView) getViewByPosition(getRecyclerView(), position, R.id.play);
                ImageView pause = (ImageView) getViewByPosition(getRecyclerView(), position,
                        R.id.pause);
                if (isPlay) {
                    if (play != null) {
                        play.setVisibility(View.GONE);
                    }
                    if (pause != null) {
                        pause.setVisibility(View.VISIBLE);
                    }


                } else {
                    if (play != null) {
                        play.setVisibility(View.VISIBLE);
                    }
                    if (pause != null) {
                        pause.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void setDurationTimeString(int position, String durationTimeString) {
//        if (getViewByPosition(getRecyclerView(), position,
//                R.id.tv_end_time) != null) {
//            TextView endTime = (TextView) getViewByPosition(getRecyclerView(), position,
//                    R.id.tv_end_time);
//            endTime.setText(durationTimeString);
//        }
//        MediaEntity mediaEntity = mData.get(position);
//        mediaEntity.setEndTime(durationTimeString);
    }

    /*
     * ----------------------------------------------------------------------------------
     * 音频播放处理  end
     * ----------------------------------------------------------------------------------
     */

    private String formatTime(Long position) {
        int totalSeconds = (int) (position + 0.5);
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60 % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

}