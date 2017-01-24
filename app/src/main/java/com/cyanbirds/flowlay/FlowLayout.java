package com.cyanbirds.flowlay;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：wangyb
 * 时间：2017/1/23 10:32
 * 描述：
 */
public class FlowLayout extends ViewGroup {
	/**
	 * 所有的子view
	 */
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	/**
	 * 每一行的高度
	 */
	private List<Integer> mLineHeight = new ArrayList<>();
	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 测量viewgroup的宽高，
	 * 在自定义view或者viewgroup的时候，需要处理wrap_content这个特殊情况
	 * 这里是测量子view，得到所有子view的宽高，相加就是viewgroup的宽高
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		//warp_content
		int width = 0;
		int height = 0;

		//每一行的宽度和高度
		int lineWidth = 0;
		int lineHeight = 0;

		int mCount = getChildCount();
		for (int i = 0; i < mCount; i++) {
			View child = getChildAt(i);
			Log.d("test", "width = " + child.getMeasuredWidth());
			//测量子view的宽和高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			//子view占据的宽度和高度
			int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

			//每行的宽度+下一个子view的宽度 > viewgroup的宽度，就换行
			if (lineWidth + childWidth > widthSize) {
				width = Math.max(width, lineWidth);//确定上行的宽度
				lineWidth = childWidth;//刚换行之后，行的宽度等于新行的第一个view的宽度

				height += childHeight;//确定上行的高度
				lineHeight = childHeight;
			} else {//不换行
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}

			if (i == mCount - 1) {
				width = Math.max(width, lineWidth);
				height += lineHeight;
			}
		}

		setMeasuredDimension(
				widthMode == MeasureSpec.EXACTLY ? widthSize : width,
				heightMode == MeasureSpec.EXACTLY ? heightSize : height);
	}

	/**
	 * 对每个子view进行布局
	 * @param changed
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mAllViews.clear();
		mLineHeight.clear();

		//当前viewgroup的宽度
		int width = getWidth();
		int measuredWidth = getMeasuredWidth();
		Log.e("TAG", "width = " + width);
		Log.e("TAG", "measuredWidth = " + measuredWidth);//相同的值

		int lineWidth = 0;
		int lineHeight = 0;

		List<View> mLineViews = new ArrayList<>();//每一行的所有子view
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			//如果需要换行
			if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width) {
				mLineHeight.add(lineHeight);
				mAllViews.add(mLineViews);

				//重置行宽和行高
				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
				mLineViews = new ArrayList<>();
			}

			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
			mLineViews.add(child);
		} //for end

		//处理最后一行
		mLineHeight.add(lineHeight);
		mAllViews.add(mLineViews);

		//设置子view的位置
		int left = 0;
		int top = 0;

		for (int i = 0; i < mAllViews.size(); i++) {
			mLineViews = mAllViews.get(i);
			lineHeight = mLineHeight.get(i);

			for (int j = 0; j < mLineViews.size(); j++) {
				View child = mLineViews.get(j);
				//判断child的状态
				if (child.getVisibility() == View.GONE) {
					continue;
				}

				MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
				int lf = left + lp.leftMargin;
				int rg = lf + child.getMeasuredWidth() + lp.rightMargin;
				int tp = top + lp.topMargin;
				int bt = tp + child.getMeasuredHeight() + lp.bottomMargin;
				//为子view进行布局
				child.layout(lf, tp, rg, bt);

				left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			}

			left = 0;
			top += lineHeight;
		}
	}

	/**
	 * 与当前ViewGroup对应的布局参数
	 * @param attrs
	 * @return
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}


	/**
	 * 添加子view
	 * @param childViews
	 */
	public void setChildViews(List<View> childViews) {
		for (View view : childViews) {
			this.addView(view);
		}
	}

}
