package com.xebest.plugin;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ywen on 15/7/10.
 */
public interface XECommand {

    public void jsCallNative(JSONArray args, CallbackContext callbackContext) throws JSONException;
}
