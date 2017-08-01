package com.yingt.xdtouch;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

import com.fiona.fwindow.WindowLayoutParams;
import com.fiona.fwindow.WindowWrapper;
import com.fiona.panel.CenterPanelView;
import com.fiona.panel.ControlCenterPanel;
import com.fiona.panel.FloatView;
import com.fiona.panel.NotificationPanelView;
import com.fiona.panel.widget.Workspace;
import com.yingt.uimain.ui.MainTabActivity;

/**
 * Created by laihuan.wan on 2017/7/12 0012.
 */

public class MainActivity extends MainTabActivity {

    private WindowWrapper mWindowWrapper;
    private FloatView floatView;
    private CenterPanelView centerPanelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWindowWrapper = new WindowWrapper(this);

        // 控制中心
        ControlCenterPanel controlCenterPanel = new ControlCenterPanel(this);
        controlCenterPanel.setBackgroundColor(0xff5f5f5f);
        mWindowWrapper.addView(controlCenterPanel);
        mWindowWrapper.goneView(controlCenterPanel);

        // 通知面板
        NotificationPanelView notificationPanelView = new NotificationPanelView(this);
        notificationPanelView.setBackgroundColor(0x6f6f6f6f);
        mWindowWrapper.addView(notificationPanelView);
        mWindowWrapper.goneView(notificationPanelView);

        // 中央面板
        centerPanelView = new CenterPanelView(this);
        mWindowWrapper.addView(centerPanelView);
        mWindowWrapper.goneView(centerPanelView);
        centerPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startExpendAnimator(floatView, centerPanelView.getWorkspace(), false, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mWindowWrapper.visibleView(floatView);
                        mWindowWrapper.goneView(centerPanelView);
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
        });

        // 浮动小白点
        creatFloatWindow();
    }

    /**
     * 创建面板视图
     */
    private void creatFloatWindow() {

        floatView = new FloatView(this);
        // 设置默认显示位置
        floatView.setWindowLayout(WindowLayoutParams.getWidth(this) - 100,
                (int)(WindowLayoutParams.getHeight(this) * 0.7f), 100, 100);
        floatView.setImageBitmap(null);
        mWindowWrapper.addView(floatView);

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowWrapper.goneView(floatView);
                mWindowWrapper.visibleView(centerPanelView);
                //  展开
                startExpendAnimator(floatView, centerPanelView.getWorkspace(), true, null);
            }
        });
        floatView.setOnMoveListener(new FloatView.OnMoveListener() {
            public void onMove(int x, int y) {
                // TODO Auto-generated method stub
                System.out.println("setOnMoveListener x=" + x + ",y=" + y);
                floatView.setWindowLayout(x, y);
                mWindowWrapper.updateViewLayout(floatView);
            }
        });
    }

    /**
     * 展开动画
     *
     * @param floatView
     * @param workspace
     */
    private void startExpendAnimator(final FloatView floatView, Workspace workspace, final boolean isExpendFlag, Animator.AnimatorListener listener) {
        int duration = 200;
        float fvWidth = floatView.getWidth(); // 浮动小白点宽度
        float wsWidth = workspace.getWidth(); // 中央面板宽度
        float fvHeight = floatView.getHeight();
        float wsHeight = workspace.getHeight();

        float offsetX = (wsWidth - fvWidth) / 2; // 因为有缩放动画，那么会产生额外的偏移
        float offsetY = (wsHeight - fvHeight) / 2;

        float sx = floatView.getWindowLayoutParams().x - workspace.getLeft() - offsetX;
        float sy = floatView.getWindowLayoutParams().y - workspace.getTop() - offsetY;
        // 缩放的系数
        float scaleX = fvWidth / wsWidth;
        float scaleY = fvHeight / wsHeight;

        if (isExpendFlag) {
            // 展开位移动画
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(workspace, "translationX", sx, 0);
            animatorX.setDuration(duration);
            animatorX.start();
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(workspace, "translationY", sy, 0);
            animatorY.setDuration(duration);
            animatorY.start();
            // 展开放大动画
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(workspace, "scaleX", scaleX, 1.0f);
            animator1.setDuration(duration);
            animator1.start();
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(workspace, "scaleY", scaleY, 1.0f);
            animator2.setDuration(duration);
            animator2.start();
            if (listener != null)
                animator2.addListener(listener);// 动画结束监听器

        } else {
            // 收缩位移动画
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(workspace, "translationX", 0, sx);
            animatorX.setDuration(duration);
            animatorX.start();
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(workspace, "translationY", 0, sy);
            animatorY.setDuration(duration);
            animatorY.start();
            // 收缩缩小动画
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(workspace, "scaleX", 1.0f, scaleX);
            animator1.setDuration(duration);
            animator1.start();
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(workspace, "scaleY", 1.0f, scaleY);
            animator2.setDuration(duration);
            animator2.start();
            if (listener != null)
                animator2.addListener(listener);// 动画结束监听器
        }
    }
}