package com.xebest.plugin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ywen on 15/7/21.
 */
public class XEFragment extends Fragment implements XECommand{
    private int resource;
    private XEWebView xeWebView;
    private String launchUrl;
    private CordovaInterface cordovaInterface;

    public CordovaInterface getCordovaInterface() {
        return cordovaInterface;
    }

    public void setCordovaInterface(CordovaInterface cordovaInterface) {
        this.cordovaInterface = cordovaInterface;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public XEWebView getXeWebView() {
        return xeWebView;
    }

    public void setXeWebView(XEWebView xeWebView) {
        this.xeWebView = xeWebView;
    }

    public String getLaunchUrl() {
        return launchUrl;
    }

    public void setLaunchUrl(String launchUrl) {
        this.launchUrl = launchUrl;
    }

    public XEToast getXeToast() {
        return xeToast;
    }

    public void setXeToast(XEToast xeToast) {
        this.xeToast = xeToast;
    }

    public XELoading getXeLoading() {
        return xeLoading;
    }

    public void setXeLoading(XELoading xeLoading) {
        this.xeLoading = xeLoading;
    }

    private XEToast xeToast;
    private XELoading xeLoading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.id.fragment_hello, container, false)
        xeWebView = new XEWebView(this.getActivity());
        xeWebView.init(this.getActivity(), launchUrl, this, xeToast, xeLoading, cordovaInterface);
        xeWebView.setId(new Integer(101));
        RelativeLayout.LayoutParams wvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        xeWebView.setLayoutParams(wvlp);
        container.addView(xeWebView);

        return inflater.inflate(this.resource, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (xeWebView.getWebView() != null) {
            xeWebView.getWebView().handleDestroy();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void jsCallNative(JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d("js call native", args.toString());
    }

    public void nativeCallJs(String js) {
        Log.d("native call js", js);
        xeWebView.getWebView().loadUrl(js);
    }


}
