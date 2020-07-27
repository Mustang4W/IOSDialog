package com.mustang4w.wang.iosdialog.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.FloatRange;

import com.mustang4w.wang.iosdialog.R;

/**
 * IOS风格Dialog
 *
 * @author Wangym
 */
public class IOSDialog extends PopupWindow {

    private Context mContext;
    private Activity mActivity;
    private static final int BACKGROUND_ALPHA_MIN_VALUE = 45;
    private static final int BACKGROUND_ALPHA_MAX_VALUE = 100;
    private static final int BACKGROUND_ALPHA_ANIM_INTERVAL = 6;

    /**
     * 初始化IOSDialog
     *
     * @param context 上下文
     * @return IOSDialog实例
     */
    public static IOSDialog with(Context context) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ios_center, null, false);
        return new IOSDialog(view, getScreenRelatedWidth(context), getScreenRelatedHeigth(context));
    }

    /**
     * 构造方法
     *
     * @param contentView 视图
     * @param width       宽
     * @param height      高
     */
    private IOSDialog(View contentView, int width, int height) {
        super(contentView, width, height);
        mContext = contentView.getContext();
        mActivity = (Activity) mContext;
        initView();
    }

    /**
     * 初始化PopupWindow部分属性
     */
    private void initView() {
        // 设置获取焦点
        setFocusable(true);
        // 设置动画进入、消失动画
        setAnimationStyle(R.style.IOSDialogAnimation);
    }

    /**
     * 设置标题（默认隐藏）
     *
     * @param title 标题
     * @return IOSDialog实例
     */
    public IOSDialog setTitle(String title) {
        TextView txtTitle = getContentView().findViewById(R.id.txt_title);
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(title);
        return this;
    }

    /**
     * 设置信息内容
     *
     * @param message 信息内容
     * @return IOSDialog实例
     */
    public IOSDialog setMessage(String message) {
        TextView txtMessage = getContentView().findViewById(R.id.txt_message);
        txtMessage.setText(message);
        return this;
    }


    /**
     * 设置输入框提示内容（默认隐藏）
     *
     * @param hint 提示内容
     * @return IOSDialog实例
     */
    public IOSDialog setInputHint(String hint) {
        EditText edtInput = getContentView().findViewById(R.id.edt_input);
        edtInput.setVisibility(View.VISIBLE);
        edtInput.setHint(hint);
        return this;
    }

    /**
     * 设置右部按钮（默认隐藏）
     *
     * @param text                       按钮展示内容
     * @param onRightButtonClickListener 按钮点击监听
     * @return IOSDialog实例
     */
    public IOSDialog setRightButton(String text, final OnRightButtonClickListener onRightButtonClickListener) {
        TextView txtRight = getContentView().findViewById(R.id.txt_right_button);
        txtRight.setVisibility(View.VISIBLE);
        txtRight.setText(text);
        txtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightButtonClickListener != null) {
                    EditText edtInput = getContentView().findViewById(R.id.edt_input);
                    onRightButtonClickListener.onClickListener(edtInput.getText().toString());
                }
                dismiss();
            }
        });
        return this;
    }

    /**
     * 设置左部按钮（默认隐藏）
     *
     * @param text                      按钮展示内容
     * @param onLeftButtonClickListener 按钮点击监听
     * @return IOSDialog实例
     */
    public IOSDialog setLeftButton(String text, final OnLeftButtonClickListener onLeftButtonClickListener) {
        TextView txtLeft = getContentView().findViewById(R.id.txt_left_button);
        txtLeft.setVisibility(View.VISIBLE);
        txtLeft.setText(text);
        txtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftButtonClickListener != null) {
                    onLeftButtonClickListener.onClickListener();
                }
                dismiss();
            }
        });
        return this;
    }

    /**
     * 设置触摸外部是否取消弹窗
     *
     * @param flag true 取消 false 不取消
     * @return IOSDialog实例
     */
    public IOSDialog setCanceledOnTouchOutside(boolean flag) {
        LinearLayout layoutPopup = getContentView().findViewById(R.id.layout_popup);
        LinearLayout layoutDialog = getContentView().findViewById(R.id.layout_dialog);
        if (flag) {
            layoutPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            layoutPopup.setOnClickListener(null);
        }
        layoutDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing
                // 为了吃掉外部的点击事件
            }
        });

        return this;
    }

    /**
     * 显示Dialog
     */
    public void show() {
        /*
         * 设置两个按钮的点击效果，这里要区分只显示一个和两个都显示的按下状态（默认不处理是用的只显示一个按钮）
         * 所以要特殊处理一下
         */
        TextView txtLeft = getContentView().findViewById(R.id.txt_left_button);
        TextView txtRight = getContentView().findViewById(R.id.txt_right_button);
        if (txtLeft.getVisibility() == View.VISIBLE && txtRight.getVisibility() == View.VISIBLE) {
            txtLeft.setBackgroundResource(R.drawable.dialog_left_button_selector);
            txtRight.setBackgroundResource(R.drawable.dialog_right_button_selector);
        }

        // show
        if (!isShowing()) {
            new AddBgAlphaThread().start();
            showAtLocation(getContentView().getRootView(), Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 重写方法，只增加背景渐变
     */
    @Override
    public void dismiss() {
        new ClearBgAlphaThread().start();
        super.dismiss();
    }

    /**
     * 设置背景透明度（实现置灰效果）
     *
     * @param i 置灰度（0 - 1） 0为完全不透明，1为完全透明
     */
    private void setWindowBackgroundAlpha(@FloatRange(from = 0.0, to = 1.0) final float i) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = i;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 获取可用显示大小的绝对宽度
     *
     * @param context 上下文
     * @return 显示大小的绝对宽度
     */
    private static int getScreenRelatedWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            //可用显示大小的绝对高度（以像素为单位）。
//            int heightPixels = outMetrics.heightPixels;
//            //屏幕密度表示为每英寸点数。
//            int densityDpi = outMetrics.densityDpi;
//            //显示器的逻辑密度。
//            float density = outMetrics.density;
//            //显示屏上显示的字体缩放系数。
//            float scaledDensity = outMetrics.scaledDensity;
            //可用显示大小的绝对宽度（以像素为单位）。
            return outMetrics.widthPixels;
        }
        return 0;
    }

    /**
     * 获取可用显示大小的绝对高度
     *
     * @param context 上下文
     * @return 显示大小的绝对高度
     */
    private static int getScreenRelatedHeigth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.heightPixels;
        }
        return 0;
    }

    /**
     * 增加背景置灰线程
     * 100-45，每6ms刷新一次
     */
    class AddBgAlphaThread extends Thread {
        @Override
        public void run() {
            int i = BACKGROUND_ALPHA_MAX_VALUE;
            while (i >= BACKGROUND_ALPHA_MIN_VALUE) {
                setWindowBackgroundAlpha(i / 100.0f);
                i--;
                try {
                    sleep(BACKGROUND_ALPHA_ANIM_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 增加背景置灰线程
     * 45-100，每6ms刷新一次
     */
    class ClearBgAlphaThread extends Thread {
        @Override
        public void run() {
            int i = BACKGROUND_ALPHA_MIN_VALUE;
            while (i <= BACKGROUND_ALPHA_MAX_VALUE) {
                setWindowBackgroundAlpha(i / 100.0f);
                i++;
                try {
                    sleep(BACKGROUND_ALPHA_ANIM_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 右侧按钮监听接口
     */
    public interface OnRightButtonClickListener {
        /**
         * 点击方法
         *
         * @param input 输入框内容
         */
        void onClickListener(String input);
    }

    /**
     * 左侧按钮监听接口
     */
    public interface OnLeftButtonClickListener {
        /**
         * 点击方法
         */
        void onClickListener();
    }

}
