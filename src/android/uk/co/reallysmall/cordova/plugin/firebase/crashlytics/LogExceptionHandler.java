package uk.co.reallysmall.cordova.plugin.firebase.crashlytics;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import java.lang.reflect.Array;

import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;

public class LogExceptionHandler implements ActionHandler {
    @Override
    public boolean handle(final JSONArray args, CordovaInterface cordova) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String fullMsg = args.getString(0);
                    String[] splitMessage = fullMsg.split(";");

                    String userData = splitMessage[0];
                    String[] splitUserData = userData.split("\\|");

                    final StackTraceElement headerElementElement = new StackTraceElement(fullMsg, "", splitUserData[splitUserData.length-1], 0);
                    StackTraceElement[] stackTraces = new StackTraceElement[1];
                    stackTraces[0] = headerElementElement;

                    Exception exception = new Exception(fullMsg);
                    exception.setStackTrace(stackTraces);

                    Crashlytics.logException(exception);
                } catch (JSONException e) {
                    Log.e(FirebaseCrashlyticsPlugin.TAG, "Error logging exception", e);
                }
            }
        });

        return true;
    }
}
