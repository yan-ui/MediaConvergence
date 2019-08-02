package cn.tklvyou.mediaconvergence.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.helper.GlideManager;
import cn.tklvyou.mediaconvergence.model.NewsBean;
import cn.tklvyou.mediaconvergence.model.NewsMultipleItem;
import cn.tklvyou.mediaconvergence.model.HaveSecondModuleNewsModel;
import cn.tklvyou.mediaconvergence.ui.home.ImagePagerActivity;
import cn.tklvyou.mediaconvergence.ui.video_player.VodActivity;
import cn.tklvyou.mediaconvergence.utils.GlideCircleTransform;
import cn.tklvyou.mediaconvergence.utils.UrlUtils;
import cn.tklvyou.mediaconvergence.widget.ExpandTextView;
import cn.tklvyou.mediaconvergence.widget.MultiImageView;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * modify by AllenCoder
 */
public class NewsMultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<NewsMultipleItem, BaseViewHolder> {

    public NewsMultipleItemQuickAdapter(Context context, List data) {
        super(data);
        addItemType(NewsMultipleItem.VIDEO, R.layout.item_news_video);
        addItemType(NewsMultipleItem.TV, R.layout.item_news_suixi_tv);
        addItemType(NewsMultipleItem.NEWS, R.layout.item_news_news_layout);
        addItemType(NewsMultipleItem.SHI_XUN, R.layout.item_news_shi_xun);
        addItemType(NewsMultipleItem.WEN_ZHENG, R.layout.item_news_wen_zheng);
        addItemType(NewsMultipleItem.JU_ZHENG, R.layout.item_news_ju_zhen);
        addItemType(NewsMultipleItem.WECHAT_MOMENTS, R.layout.item_winxin_circle);
        addItemType(NewsMultipleItem.READING, R.layout.item_news_reading);
    }


    @Override
    protected void convert(BaseViewHolder helper, NewsMultipleItem item) {
        switch (helper.getItemViewType()) {
            case NewsMultipleItem.VIDEO:
                NewsBean bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvNewsTitle, bean.getName());
                helper.setText(R.id.tvVideoTime, bean.getTime());
                helper.setText(R.id.tvCommentNum, "" + bean.getComment_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());
                helper.setText(R.id.tvName, bean.getNickname());

                // 加载网络图片
                if (!StringUtils.isEmpty(bean.getAvatar().trim())) {
                    Glide.with(mContext).load(bean.getAvatar()).into((ImageView) helper.getView(R.id.ivAvatar));
                }

                Glide.with(mContext).load(bean.getImage()).into((ImageView) helper.getView(R.id.ivVideoBg));

                helper.addOnClickListener(R.id.videoLayout);

                break;
            case NewsMultipleItem.TV:
                HaveSecondModuleNewsModel suixiTvModel = (HaveSecondModuleNewsModel) item.getDataBean();

                if (suixiTvModel.getData().size() == 1) {
                    helper.getView(R.id.llSecondLayout).setVisibility(View.INVISIBLE);
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

                break;

            case NewsMultipleItem.NEWS:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvTitle, bean.getName());
                helper.setText(R.id.tvTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

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

                            GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));
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
                helper.setText(R.id.tvVideoTime, "" + bean.getTime());

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

                            GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));
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
                suixiTvModel = (HaveSecondModuleNewsModel) item.getDataBean();

                //todo:矩阵头像
//                GlideManager.loadRoundImg(suixiTvModel.getModule_second(), helper.getView(R.id.ivImageFirst));
                helper.setText(R.id.tvModuleSecond, suixiTvModel.getModule_second());

                LinearLayout llNewsContainer = helper.getView(R.id.llNewsContainer);
                llNewsContainer.removeAllViews();

                for (int i = 0; i < suixiTvModel.getData().size(); i++) {
                    View view = View.inflate(mContext, R.layout.item_news_news_layout, null);

                    HaveSecondModuleNewsModel.DataBean dataBean = suixiTvModel.getData().get(i);

                    TextView tvTitle = view.findViewById(R.id.tvTitle);
                    TextView tvTime = view.findViewById(R.id.tvTime);
                    TextView tvSeeNum = view.findViewById(R.id.tvSeeNum);
                    TextView tvGoodNum = view.findViewById(R.id.tvGoodNum);
                    ImageView ivImageOne = view.findViewById(R.id.ivImageOne);
                    ImageView ivImageFirst = view.findViewById(R.id.ivImageFirst);
                    ImageView ivImageSecond = view.findViewById(R.id.ivImageSecond);
                    ImageView ivImageThree = view.findViewById(R.id.ivImageThree);
                    LinearLayout llMultiImage = view.findViewById(R.id.llMultiImage);

                    tvTitle.setText(dataBean.getName());
                    tvTime.setText(dataBean.getBegintime());
                    tvSeeNum.setText(dataBean.getVisit_num());
                    tvGoodNum.setText(dataBean.getLike_num());

                    if (!StringUtils.isEmpty(dataBean.getImage())) {
                        //一张图片
                        llMultiImage.setVisibility(View.GONE);
                        ivImageOne.setVisibility(View.VISIBLE);

                        GlideManager.loadRoundImg(dataBean.getImage(), ivImageOne);

                    } else {
                        if (dataBean.getImages() == null || dataBean.getImages().size() == 0) {
                            //没有图片
                            llMultiImage.setVisibility(View.GONE);
                            ivImageOne.setVisibility(View.GONE);

                        } else {

                            if (dataBean.getImages().size() < 3) {
                                //一张图片
                                llMultiImage.setVisibility(View.GONE);
                                ivImageOne.setVisibility(View.VISIBLE);

                                GlideManager.loadRoundImg(dataBean.getImage(), ivImageOne);
                            } else {
                                //多张图片
                                ivImageOne.setVisibility(View.GONE);
                                llMultiImage.setVisibility(View.VISIBLE);

                                GlideManager.loadRoundImg(dataBean.getImages().get(0), ivImageFirst);
                                GlideManager.loadRoundImg(dataBean.getImages().get(1), ivImageSecond);
                                GlideManager.loadRoundImg(dataBean.getImages().get(2), ivImageThree);
                            }
                        }
                    }

                    llNewsContainer.addView(view);

                }
                break;
            case NewsMultipleItem.WECHAT_MOMENTS:
                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.nameTv, bean.getNickname());
                helper.setText(R.id.timeTv, bean.getBegintime());

                helper.setText(R.id.tvCommentNum, "" + bean.getComment_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

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


                if (!StringUtils.isEmpty(bean.getAvatar().trim())) {
                    Glide.with(mContext).load(bean.getAvatar())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.color.bg_no_photo)
                            .transform(new GlideCircleTransform())
                            .into((ImageView) helper.getView(R.id.headIv));
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
                    ivVideo.setVisibility(View.GONE);

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

                    ImageView ivVideo = helper.getView(R.id.ivVideo);
                    ivVideo.setVisibility(View.VISIBLE);
                    ivVideo.setBackgroundColor(Color.parseColor("#abb1b6"));
                    ivVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, VodActivity.class);
//                    intent.putExtra("media_type", "livestream")
                            intent.putExtra("videoPath", bean.getVideo());
                            mContext.startActivity(intent);
                        }
                    });


                    Glide.with(mContext).load(bean.getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.color.bg_no_photo)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    ivVideo.setBackground(resource);
                                }
                            });

                }
                break;
            case NewsMultipleItem.READING:
                if(helper.getLayoutPosition()  == 0 ){
                    helper.getView(R.id.ivReadingImage).getLayoutParams().height = ConvertUtils.dp2px(90);
                }

                bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvName, bean.getName());
                helper.setText(R.id.tvBeginTime, bean.getBegintime());
                helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());

                GlideManager.loadImg(bean.getImage(), helper.getView(R.id.ivReadingImage));
                break;

            default:
                break;
        }
    }
}