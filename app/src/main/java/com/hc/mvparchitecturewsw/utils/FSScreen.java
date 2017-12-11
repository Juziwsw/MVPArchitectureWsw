package com.hc.mvparchitecturewsw.utils;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/** 
 *  
 * @author : clw 
 */
public class FSScreen {
	public static Context myCtx;
	public static DisplayMetrics getDisplayMetrics(Context ctx) {
		return ctx.getApplicationContext().getResources().getDisplayMetrics();
	}

	/**
	 * obtain the dpi of screen
	 */
	public static float getScreenDpi(Context ctx) {
		return getDisplayMetrics(ctx).densityDpi;
	}
	public static float getScreenDpi( ) {
		return getDisplayMetrics(myCtx).densityDpi;
	}
	/**
	 * obtain the density of screen
	 */
	public static float getScreenDensity(Context ctx) {
		return getDisplayMetrics(ctx).density;
	}
	public static float getScreenDensity() {
		return getDisplayMetrics(myCtx).density;
	}
	/**
	 * obtain the scaled density of screen
	 */
	public static float getScreenScaledDensity(Context ctx) {
		return getDisplayMetrics(ctx).scaledDensity;
	}
	public static float getScreenScaledDensity( ) {
		return getDisplayMetrics(myCtx).scaledDensity;
	}

	/**
	 * obtain the width of screen
	 */
	public static int getScreenWidth(Context ctx) {
		return getDisplayMetrics(ctx).widthPixels;
	}
	public static int getScreenWidth() {
		return getDisplayMetrics(myCtx).widthPixels;
	}
	/**
	 * obtain the height of screen
	 */
	public static int getScreenHeight(Context ctx) {
		return getDisplayMetrics(ctx).heightPixels;
	}
	public static int getScreenHeight() {
		return getDisplayMetrics(myCtx).heightPixels;
	}
	/**
	 * According to the resolution of the phone from the dp unit will become a px (pixels)
	 */
	public static int dip2px(Context ctx, float dip) {
		float density = getScreenDensity(ctx);
		return (int) (dip * density + 0.5f);
	}
	/**
	 * According to the resolution of the phone from the dp unit will become a px (pixels)
	 */


	public static int dip2px(float dip) {
		float density = getScreenDensity(myCtx);
		return (int) (dip * density + 0.5f);
	}
	public static int dp2px(float dip) {
		return dip2px(dip);
	}
	public static int dp2px(Context ctx, float dip) {
		return dip2px(ctx,dip);
	}
	/**
	 * Turn from the units of px (pixels) become dp according to phone resolution
	 */
	public static int px2dip(float px) {
		float density = getScreenDensity(myCtx);
		return (int) (px / density + 0.5f);
	}
	/**
	 * Turn from the units of px (pixels) become dp according to phone resolution
	 */
	public static int px2dip(Context ctx, float px) {
		float density = getScreenDensity(ctx);
		return (int) (px / density + 0.5f);
	}

	/**
	 * Turn from the units of px (pixels) become sp according to phone scaledDensity
	 * @param ctx
	 * @param px
	 * @return
	 */
	public static int px2sp(Context ctx, float px) {
		float scale = getScreenScaledDensity(ctx);
		return (int) (px / scale + 0.5f);
	}
	public static int px2sp( float px) {
		float scale = getScreenScaledDensity(myCtx);
		return (int) (px / scale + 0.5f);
	}


	/**
	 * According to the scaledDensity of the phone from the sp unit will become a px (pixels)
	 * @param ctx
	 * @param sp
	 * @return
	 */
	public static int sp2px(Context ctx, int sp){
		return sp2px(ctx,(float)sp);
	}
	public static int sp2px(Context context, float spValue){
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int sp2px(int sp){
		float scale = getScreenScaledDensity(myCtx);
		return (int) (sp * scale + 0.5f);
	}


	private static int mMaxListHeight;

	/**
	 * 获取列表最大高度
	 * @return
	 */
	public static int getMaxListHeight(Context context) {
		if (mMaxListHeight <= 0) {
			mMaxListHeight = (int) (getScreenHeight(context) * 0.66);
		}
		return mMaxListHeight;
	}
	/**
	 * 获取列表最大高度
	 * @return
	 */
	public static int getMaxListHeight() {
		if (mMaxListHeight <= 0) {
			mMaxListHeight = (int) (getScreenHeight(myCtx) * 0.66);
		}
		return mMaxListHeight;
	}

	public static int getStatusBarHeight(Context context){
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	public static int getStatusBarHeight( ){
		int result = 0;
		int resourceId = myCtx.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = myCtx.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	static Paint paint = null;
	public static int getLayoutPixWidth(String text, int textSizeSpValue) {
		int textLayoutWidth = 0;
		if(!TextUtils.isEmpty(text)){
			if(paint == null){
				paint = new Paint();
			}
			paint.setTextSize(FSScreen.sp2px(textSizeSpValue));
			textLayoutWidth = (int) paint.measureText(text);
		}
		return textLayoutWidth;
	}

	/**
	 * 获取屏幕高度包括虚拟按键
	 * @return
	 */
	public static int getHasVirtualKey() {
		int dpi = 0;
		WindowManager windowManager = (WindowManager) myCtx
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
        Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			dpi = dm.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dpi;
	}




}
