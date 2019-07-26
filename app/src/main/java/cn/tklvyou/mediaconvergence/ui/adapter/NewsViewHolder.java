//package cn.tklvyou.mediaconvergence.ui.adapter;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.LayoutRes;
//
//import com.blankj.utilcode.util.ToastUtils;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//
//import cn.tklvyou.mediaconvergence.R;
//import cn.tklvyou.mediaconvergence.base.BaseViewHolder;
//import cn.tklvyou.mediaconvergence.model.NewListModel;
//
//
///**用户View
// * @author Lemon
// * @use
// * <br> NewsViewHolder userView = new NewsViewHolder(context, resources);
// * <br> adapter中使用:[具体参考.BaseViewAdapter(getView使用自定义View的写法)]
// * <br> convertView = userView.createView(inflater);
// * <br> userView.bindView(position, data);
// * <br> 或  其它类中使用:
// * <br> containerView.addView(userView.createView(inflater));
// * <br> userView.bindView(data);
// * <br> 然后
// * <br> userView.setOnDataChangedListener(onDataChangedListener);data = userView.getData();//非必需
// * <br> userView.setOnClickListener(onClickListener);//非必需
// */
//public class NewsViewHolder extends BaseViewHolder<NewListModel.DataBean> implements OnClickListener {
//	private static final String TAG = "NewsViewHolder";
//
//	public NewsViewHolder(Activity context, @LayoutRes int layoutResId, ViewGroup parent) {
//		super(context, layoutResId, parent);
//	}
//
//	public ImageView ivUserViewHead;
//	public ImageView ivUserViewStar;
//
//	public TextView tvUserViewSex;
//
//	public TextView tvUserViewName;
//	public TextView tvUserViewId;
//	public TextView tvUserViewNumber;
//	@SuppressLint("InflateParams")
//	@Override
//	public View createView() {
//		ivUserViewHead = findView(R.id.ivUserViewHead, this);
//		ivUserViewStar = findView(R.id.ivUserViewStar, this);
//
//		tvUserViewSex = findView(R.id.tvUserViewSex, this);
//
//		tvUserViewName = findView(R.id.tvUserViewName);
//		tvUserViewId = findView(R.id.tvUserViewId);
//		tvUserViewNumber = findView(R.id.tvUserViewNumber);
//
//		return super.createView();
//	}
//
//	@Override
//	public void bindView(NewListModel.DataBean data_){
//		super.bindView(data_ != null ? data_ : new NewListModel.DataBean());
//
//		Glide.with(context).asBitmap().load(data.getHead()).into(new SimpleTarget<Bitmap>() {
//
//			@Override
//			public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//				ivUserViewHead.setImageBitmap(bitmap);
//			}
//		});
//
//		ivUserViewStar.setImageResource(data.getStarred() ? R.mipmap.icon_main_service : R.mipmap.icon_main_mine);
//
//		tvUserViewSex.setBackgroundResource(data.getSex() == User.SEX_FEMALE
//				? R.mipmap.icon_main_home : R.mipmap.icon_main_camera);
//		tvUserViewSex.setText(data.getSex() == User.SEX_FEMALE ?  "女" : "男");
//		tvUserViewSex.setTextColor(data.getSex() == User.SEX_FEMALE ?
//				context.getResources().getColor(R.color.colorAccent): context.getResources().getColor( R.color.colorPrimary));
//
//		tvUserViewName.setText(data.getName());
//		tvUserViewId.setText("ID:" + data.getId());
//		tvUserViewNumber.setText("Phone:" + data.getPhone());
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.ivUserViewHead:
//			ToastUtils.showShort("la");
//			break;
//		default:
//			switch (v.getId()) {
//			case R.id.ivUserViewStar:
//				data.setStarred(! data.getStarred());
//				break;
//			case R.id.tvUserViewSex:
//				data.setSex(data.getSex() == User.SEX_FEMALE ? User.SEX_MAIL : User.SEX_FEMALE);
//				break;
//			}
//			bindView(data);
//			break;
//		}
//	}
//}