package com.xebest.plugin;

/**
 * Created by ywen on 15/7/15.
 */

public interface XEToast {
    public enum ToastPosition {
        BOTTOM, CENTER, TOP
    }

    public void showToast(String message, double time, ToastPosition position);
    public void showToast(String message);
    public void showSuccess(String message);
    public void showErr(String message);
}
