package login.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.vaibhavrustogi.parseldemo.R;
import com.example.vaibhavrustogi.parseldemo.activity.MapActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import models.Driver;
import networking.NetworkManager;
import utils.CommonUtils;
import utils.PermissionsUtils;

import static utils.CommonUtils.hideKeyBoard;
import static utils.CommonUtils.isEmailValid;
import static utils.CommonUtils.isPasswordValid;
import static utils.Constants.LOGIN_URL;

/**
 * Created by vaibhav.rustogi on 2/20/17.
 */

public class LoginFragment extends DialogFragment {

    @BindView(R.id.email_et)
    EditText mEmailView;
    @BindView(R.id.password_et)
    EditText mPasswordView;
    @BindView(R.id.login_button)
    Button mLoginButton;
    @BindView(R.id.login_progress)
    ProgressBar mLoginProgress;

    private static final int REQUEST_PHONE_STATE = 1;
    private boolean loginRequestInFlight;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, view);
        setUpListeners();
        return view;
    }

    private void setUpListeners() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.login_button)
    public void attemptLogin() {
        hideKeyBoard(mLoginButton, getActivity());
        if (!canRequestPhoneState() || loginRequestInFlight)
            return;

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.enter_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.required_field));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            makeLoginRequest(email, password);
        }
    }

    private void showLoginProgress(boolean show) {
        mLoginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean canRequestPhoneState() {
        if (PermissionsUtils.mayRequestPhoneState(getActivity()))
            return true;
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
            Snackbar.make(mEmailView, R.string.phone_state_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
                        }
                    }).show();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                attemptLogin();
            } else {

            }
        }
    }

    private void makeLoginRequest(final String email, String password) {
        showLoginProgress(true);
        loginRequestInFlight = true;
        NetworkManager.getsInstance(getActivity().getApplicationContext()).makeGsonRequestPost
                (LOGIN_URL, Driver.class, CommonUtils.getLoginParams(email, password),
                        CommonUtils.getLoginHeaders(getActivity()), new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                showLoginProgress(false);
                                if (response instanceof Driver) {
                                    loginRequestInFlight = false;
                                    handleLoginSuccess((Driver) response);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                showLoginProgress(false);
                                loginRequestInFlight = false;
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private void handleLoginSuccess(Driver driver) {
        if (driver.getResponseCode().equals("1000")) {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            getActivity().startActivity(intent);
        } else {
            Toast.makeText(getActivity(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
        }
    }

}
