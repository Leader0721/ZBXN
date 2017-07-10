package com.pub.widget;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.pub.R;

/**
 * @author: ysj
 * @date: 2017-02-04 14:41
 */
public class PopupWindowHelper {

    private View popupView;
    private PopupWindow mPopupWindow;
    private static final int TYPE_WRAP_CONTENT = 0, TYPE_MATCH_PARENT = 1;
    private Context mContext;
    private Window window;

    public PopupWindowHelper(Context context, View view, Window window) {
        popupView = view;
        this.mContext = context;
        this.window = window;
        context.getContentResolver().registerContentObserver(Settings.System.getUriFor("navigationbar_is_min"), true, mNavigationStatusObserver);
    }

    /**
     * 适配有虚拟键的手机，当虚拟键显示或隐藏时，如果popupwindow正在显示，则隐藏
     */
    private ContentObserver mNavigationStatusObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            //TODO: deal with data change
            dismiss();
        }
    };

    /**
     * 显示在view下方 无偏移
     *
     * @param anchor
     */
    public void showAsDropDown(View anchor) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        mPopupWindow.showAsDropDown(anchor);
        setAlpha(0.5f);
    }

    /**
     * 显示在view下方 xoff,yoff 偏移量
     *
     * @param anchor
     * @param xoff
     * @param yoff
     */
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
        setAlpha(0.5f);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        mPopupWindow.showAtLocation(parent, gravity, x, y);
        setAlpha(0.5f);
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * popupwindow是否正在显示
     *
     * @return
     */
    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    /**
     * 显示在view上方 无偏移
     *
     * @param anchor
     */
    public void showAsPopUp(View anchor) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        showAsPopUp(anchor, 0, 0);
        setAlpha(0.7f);
    }

    /**
     * 显示在view上方 xoff,yoff 偏移量
     *
     * @param anchor
     * @param xoff
     * @param yoff
     */
    public void showAsPopUp(View anchor, int xoff, int yoff) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.AnimationUpPopup);
        popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = popupView.getMeasuredHeight();
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, location[0] + xoff, location[1] - height + yoff);
        setAlpha(0.85f);
    }

    public void showFromBottom(View anchor) {
        initPopupWindow(TYPE_MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.AnimationFromButtom);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
        setAlpha(0.5f);
    }

    public void showFromTop(View anchor) {
        initPopupWindow(TYPE_MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.AnimationFromTop);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, getStatusBarHeight());
        setAlpha(0.5f);
    }

    /**
     * touch outside dismiss the popupwindow, default is ture
     *
     * @param isCancelable
     */
    public void setCancelable(boolean isCancelable) {
        if (isCancelable) {
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
        } else {
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setFocusable(false);
        }
    }

    public void initPopupWindow(int type) {
        if (type == TYPE_WRAP_CONTENT) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (type == TYPE_MATCH_PARENT) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1f);
            }
        });
    }

    /**
     * 添加阴影效果
     *
     * @param num 0.0-1.0
     */
    private void setAlpha(float num) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = num; // 0.0-1.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
    }

    private int getStatusBarHeight() {
        return Math.round(25 * Resources.getSystem().getDisplayMetrics().density);
    }
}