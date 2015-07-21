package com.xebest.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.PluginManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ywen on 15/7/20.
 */
public class XEWebView extends LinearLayout  {

    public CordovaWebView getWebView() {
        return webView;
    }

    public void setWebView(CordovaWebView webView) {
        this.webView = webView;
    }

    private CordovaWebView webView;
    private int resource;
    // Read from config.xml:
    protected CordovaPreferences preferences;
    protected String launchUrl;
    protected ArrayList<PluginEntry> pluginEntries;
    private CordovaInterface cordova;

    public CordovaInterface getCordova() {
        return cordova;
    }

    public void setCordova(CordovaInterface cordova) {
        this.cordova = cordova;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private Activity activity;

    protected void loadConfig() {
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(getActivity());
        preferences = parser.getPreferences();
        preferences.setPreferencesBundle(getActivity().getIntent().getExtras());
//        preferences.copyIntoIntentExtras(getActivity());
        if (launchUrl == null) {
            launchUrl = parser.getLaunchUrl();
        }

        pluginEntries = parser.getPluginEntries();
        // Config.parser = parser;
    }

    public XEWebView(Context context) {
        super(context);
    }

    public XEWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init(Activity activity, String launchUrl, XECommand xeCommand, XEToast xeToast, XELoading xeLoading, CordovaInterface cordova) {
        this.activity = activity;
        this.launchUrl = launchUrl;
        this.cordova = cordova;
        CordovaContext cordovaContext =  new CordovaContext(getActivity());
        cordovaContext.init();

        loadConfig();
        CordovaWebViewImpl cordovaWebView =  new CordovaWebViewImpl(CordovaWebViewImpl.createEngine(getActivity(), preferences));

        webView = cordovaWebView;


        webView.getView().setId(new Integer(100));
        RelativeLayout.LayoutParams wvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        webView.getView().setLayoutParams(wvlp);

        if (!webView.isInitialized()) {
            webView.init(cordovaContext, pluginEntries, preferences);
        }
        cordovaContext.onCordovaInit(webView.getPluginManager());
        Xebest xePlugin = (Xebest)webView.getPluginManager().getPlugin("XEPlugin");
        xePlugin.setXeCommand(xeCommand);
        if (xeToast != null) xePlugin.setXeToast(xeToast);
        if (xeLoading != null) xePlugin.setXeLoading(xeLoading);
        this.addView(webView.getView());
        webView.loadUrl(launchUrl);
    }

    public void nativeCallJs(String command) {
        this.webView.loadUrl(command);
    }

    public void execOnPageLoad() {

    }

    public class CordovaContext extends ContextWrapper implements CordovaInterface
    {
        public CordovaContext(Context base) {
            super(base);
            this.activity = (Activity)base;
        }

        private static final String TAG = "CordovaInterfaceImpl";
        protected Activity activity;
        protected ExecutorService threadPool;
        protected PluginManager pluginManager;

        protected ActivityResultHolder savedResult;
        protected CordovaPlugin activityResultCallback;
        protected String initCallbackService;
        protected int activityResultRequestCode;

        public void  init () {
            this.init(activity, Executors.newCachedThreadPool());
        }

        public void  init (Activity activity, ExecutorService threadPool) {
            this.activity = activity;
            this.threadPool = threadPool;
        }

        @Override
        public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
            setActivityResultCallback(command);
            if (cordova != null) {
                cordova.startActivityForResult(command, intent, requestCode);
                return;
            }

            try {
                activity.startActivityForResult(intent, requestCode);
            } catch (RuntimeException e) { // E.g.: ActivityNotFoundException
                activityResultCallback = null;
                throw e;
            }
        }

        @Override
        public void setActivityResultCallback(CordovaPlugin plugin) {
            // Cancel any previously pending activity.
            if (cordova != null) {
                cordova.setActivityResultCallback(plugin);
                return;
            }

            if (activityResultCallback != null) {
                activityResultCallback.onActivityResult(activityResultRequestCode, Activity.RESULT_CANCELED, null);
            }
            activityResultCallback = plugin;
        }

        @Override
        public Activity getActivity() {
            if (cordova != null) {
                return  cordova.getActivity();
            }
            return activity;
        }

        @Override
        public Object onMessage(String id, Object data) {
            if (cordova != null) {
                return cordova.onMessage(id, data);
            }

            if ("exit".equals(id)) {
                activity.finish();
            }

            if ("onPageFinished".equals(id)) {
                execOnPageLoad();
            }
            return null;
        }

        @Override
        public ExecutorService getThreadPool() {
            if (cordova != null) {
                return cordova.getThreadPool();
            }
            return threadPool;
        }

        /**
         * Dispatches any pending onActivityResult callbacks.
         */
        public void onCordovaInit(PluginManager pluginManager) {

            this.pluginManager = pluginManager;
            if (savedResult != null) {
                onActivityResult(savedResult.requestCode, savedResult.resultCode, savedResult.intent);
            }
        }

        /**
         * Routes the result to the awaiting plugin. Returns false if no plugin was waiting.
         */
        public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
            CordovaPlugin callback = activityResultCallback;
            if(callback == null && initCallbackService != null) {
                // The application was restarted, but had defined an initial callback
                // before being shut down.
                savedResult = new ActivityResultHolder(requestCode, resultCode, intent);
                if (pluginManager != null) {
                    callback = pluginManager.getPlugin(initCallbackService);
                }
            }
            activityResultCallback = null;

            if (callback != null) {
                Log.d(TAG, "Sending activity result to plugin");
                initCallbackService = null;
                savedResult = null;
                callback.onActivityResult(requestCode, resultCode, intent);
                return true;
            }
            Log.w(TAG, "Got an activity result, but no plugin was registered to receive it" + (savedResult != null ? " yet!": "."));
            return false;
        }

        /**
         * Call this from your startActivityForResult() overload. This is required to catch the case
         * where plugins use Activity.startActivityForResult() + CordovaInterface.setActivityResultCallback()
         * rather than CordovaInterface.startActivityForResult().
         */
        public void setActivityResultRequestCode(int requestCode) {

            activityResultRequestCode = requestCode;
        }

        /**
         * Saves parameters for startActivityForResult().
         */
        public void onSaveInstanceState(Bundle outState) {
            if (activityResultCallback != null) {
                String serviceName = activityResultCallback.getServiceName();
                outState.putString("callbackService", serviceName);
            }
        }

        /**
         * Call this from onCreate() so that any saved startActivityForResult parameters will be restored.
         */
        public void restoreInstanceState(Bundle savedInstanceState) {
            initCallbackService = savedInstanceState.getString("callbackService");
        }

        private  class ActivityResultHolder {
            private int requestCode;
            private int resultCode;
            private Intent intent;

            public ActivityResultHolder(int requestCode, int resultCode, Intent intent) {
                this.requestCode = requestCode;
                this.resultCode = resultCode;
                this.intent = intent;
            }
        }
    }
}
