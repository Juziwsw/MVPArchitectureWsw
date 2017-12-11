/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */

package com.hc.mvparchitecturewsw.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 *
 *
 */
public class FsToast {

    private final Context mContext;
    private final Builder builder;
    private View mView;
    private WindowManager mWM;
    private CharSequence text;

    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();


	private FsToast(Context context, Builder builder) {
        this.builder = builder;
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(builder.layoutId, null);
	}

    private void initLayoutParams(){
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.windowAnimations = android.R.style.Animation_Toast;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mParams.gravity = builder.gravity;
        mParams.y = FSScreen.dip2px(mContext, builder.marginDp);
    }

    /**
     * Show the view for the specified duration.
     */
    public void show() {
    	ToastManager manager = ToastManager.getInstance();
        manager.addIgnoreDuplicate(this);
    }

    /**
     * @return <code>true</code> if the {@link FsToast} is being displayed, else <code>false</code>.
     */
    public boolean isShowing() {
      return mView != null && mView.getParent() != null;
    }

    /**
     * Close the view if it's showing, or don't show it if it isn't showing yet.
     * You do not normally have to call this.  Normally view will disappear on its own
     * after the appropriate duration.
     */
    public void cancel() {
    	ToastManager.getInstance().clearMsg(this);
    }

    /**
	 * Cancels all queued {@link FsToast}s. If there is a {@link FsToast}
	 * displayed currently, it will be the last one displayed.
	 */
	public static void cancelAll() {
		ToastManager.getInstance().clearAllMsg();
	}

    /**
     * Return the activity.
     */
    public Context getContext() {
      return mContext;
    }

    /**
     * Set the view to show.
     * @see #getView
     */
    public void setView(View view) {
        mView = view;
    }

    /**
     * Return the view.
     * @see #setView
     */
    public View getView() {
        return mView;
    }

    /**
     * Update the text in a FsToast that was previously created using one of the makeText() methods.
     * @param s The new text for the FsToast.
     */
    public void setText(CharSequence s) {
        if (mView == null) {
            throw new RuntimeException("This FsToast was not created with FsToast.makeText()");
        }
        TextView tv = (TextView) mView.findViewById(android.R.id.message);
        if (tv == null) {
            throw new RuntimeException("This FsToast was not created with FsToast.makeText()");
        }
        text = s;
        tv.setText(s);
    }

    public int getDuration(){
        return builder.duration;
    }



    public void handleHide() {
        handleRemove();
    }

    private void handleRemove(){
        if (mView != null && mWM != null) {
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
        }
    }

    private void prepareWindowManager(){
        if (mWM == null){
            Context context = mView.getContext().getApplicationContext();
            if (context == null) context = mView.getContext();
            mWM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            mParams.packageName = context.getPackageName();
        }
    }

    public void handleShow() {
        handleRemove();
        prepareWindowManager();
        if (mView == null) return;
        if (mView.getParent() != null) {
            mWM.removeView(mView);
        }
        try {
            mWM.addView(mView, mParams);
        }catch (Exception e){
            try {
                mWM.removeView(mView);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FsToast)) {
            return false;
        }
        FsToast other = (FsToast) o;

        return builder.equals(other.builder)
                && text != null
                && text.equals(other.text);
    }

    /**
	 * The builder for a {@link FsToast}.
	 */
    public static class Builder {
        public static final int DURATION_DEFAULT = 1500;
        public static final int DURATION_LONG = 2500;
        static final int MARGIN_DP_DEFAULT = 80;

		int duration = DURATION_LONG;

        final int layoutId;
        int gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        int marginDp = MARGIN_DP_DEFAULT;

        public Builder(int layoutId) {
            this.layoutId = layoutId;
        }

        public Builder setDuration(int duration){
            this.duration = duration;
            return this;
        }


        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setMarginDp(int marginDp) {
            this.marginDp = marginDp;
            return this;
        }

        public int getDuration() {
            return duration;
        }

        @Override
		public boolean equals(Object o) {
			if (!(o instanceof Builder)) {
				return false;
			}
            Builder builder = (Builder) o;
			return builder.duration == duration
					&& builder.layoutId == layoutId
                    && builder.gravity == gravity
                    && builder.marginDp == marginDp;
		}

        public FsToast build(Context context, CharSequence text){
            FsToast result = new FsToast(context, this);
            result.setText(text);
            result.initLayoutParams();
            return result;
        }
	}
}
