package networking;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

import networking.customrequests.GsonRequest;

/**
 * Created by vaibhav.rustogi on 2/20/17.
 */

public class NetworkManager {
    private static NetworkManager sInstance;
    private RequestQueue mRequestQueue;


    public static NetworkManager getsInstance(Context context) {
        if (sInstance == null)
            sInstance = new NetworkManager(context);
        return sInstance;
    }

    private NetworkManager(Context context) {
        mRequestQueue = Volley.newRequestQueue
                (context.getApplicationContext(), new OkHttpStack());
        mRequestQueue.start();
    }

    public Request<?> makeGsonRequestPost(String url, Class clazz, final Map<String, String> body, final Map<String, String> headers, Response.Listener successListener, Response.ErrorListener errorListener) {
        return makeGsonRequest(url, clazz, body, headers, Request.Method.POST, successListener, errorListener);
    }

    public Request<?> makeGsonRequest(String url, Class clazz, final Map<String, String> params, final Map<String, String> headers, int method, Response.Listener successListener, Response.ErrorListener errorListener) {
        if (method == Request.Method.GET) {
            url = generateGetUrl(url, params);
        }
        GsonRequest gsonRequest = new GsonRequest(url, method, clazz, headers, params == null ? null : new JSONObject(params).toString(), successListener, errorListener);
        mRequestQueue.add(gsonRequest);
        return gsonRequest;
    }

    public static String generateGetUrl(String url, Map<String, String> params) {
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (TextUtils.isEmpty(value)) {
                    value = "";
                }
                uriBuilder.appendQueryParameter(entry.getKey(), value);
            }
        }
        return uriBuilder.toString();
    }

}
