package com.xebest.plugin;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class Xebest extends CordovaPlugin {
    private XECommand xeCommand = null;
    private XEToast xeToast = null;
    private XELoading xeLoading = null;

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

    public XECommand getXeCommand() {
        return xeCommand;
    }

    public void setXeCommand(XECommand xeCommand) {
        this.xeCommand = xeCommand;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("normalCommand") && xeCommand != null) {
            xeCommand.jsCallNative(args, callbackContext);

            return true;
        }
        else if (action.equals("toast") && xeToast != null)
        {
            String message = args.getString(1);
            Double time = 1.5;
            XEToast.ToastPosition position = XEToast.ToastPosition.BOTTOM;

            String firstArg = args.getString(0);

            if (args.length() > 2) {
                time = args.getDouble(2);
            }

            if (args.length() > 3) {
                String positionStr = args.getString(3);
                if ("top".equals(positionStr)){
                    position = XEToast.ToastPosition.TOP;
                }
                else if ("center".equals(positionStr))
                {
                    position = XEToast.ToastPosition.CENTER;
                }
            }


            if ("show".equals(firstArg)){
                xeToast.showToast(message, time, position);
            }
            else if ("success".equals(firstArg))
            {
                xeToast.showSuccess(message);
            }
            else if ("error".equals(firstArg))
            {
                xeToast.showErr(message);
            }

            return true;
        }
        else  if (action.equals("loading") && xeLoading != null)
        {
            String firstArg = args.getString(0);

            if ("show".equals(firstArg)) {
                String message = args.getString(1);
                boolean isForce = true;

                if (args.length() > 2) {
                    isForce = args.getBoolean(2);
                }

                xeLoading.show(message, isForce);
                return true;
            }
            else
            {
                xeLoading.hide();
                return true;
            }
        }

        return false;
    }

}
