package utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaibhav.rustogi on 2/20/17.
 */

public class CommonUtils {

    public static boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password);
    }

    public static boolean isEmailValid(String password) {
        return !TextUtils.isEmpty(password) && password.contains("@");
    }

    public static void hideKeyBoard(View view, Context context) {
        if (view != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Map<String, String> getLoginParams(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", userName);
        map.put("password", password);
        return map;
    }

    public static Map<String, String> getLoginHeaders(Context context) {
        if (context == null)
            return new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("device_info", android.os.Build.MODEL);
        if (!TextUtils.isEmpty(FirebaseInstanceId.getInstance().getToken()))
            map.put("device_token", FirebaseInstanceId.getInstance().getToken());
        if (PermissionsUtils.mayRequestPhoneState(context)) {
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            map.put("uuid", tManager.getDeviceId());
        }
        return map;
    }
}
