package chitoiu.com.balance.forecasting.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import chitoiu.com.balance.forecasting.R;
import chitoiu.com.balance.forecasting.listeners.ISubscriptionListener;
import chitoiu.com.balance.forecasting.retrofit.RetroFitManager;
import chitoiu.com.balance.forecasting.utils.SavedPreferences;

public class SettingsFragment extends Fragment implements RetroFitManager.CallbackListener {

    public static String TAG = SettingsFragment.class.getSimpleName();

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mLoginSuccessTextView;
    private View mProgressView;
    private View mLoginFormView;
    private LinearLayout mEmailLoginForm;
    private Button mSignOutButton;

    // Main view
    private View mView;

    // Callback listener to parent activity.
    private OnSettingsFragmentListener mListener;

    // Callback listener set for SubscriptionsActivity.
    private ISubscriptionListener mSubscriptionListener;

    private SavedPreferences mSavedPreferences;

    // Retro Fit reference.
    private RetroFitManager mRetroFitManager;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView != null) {
            return mView;
        }

        // Start user saved prefs.
        mSavedPreferences =
                new SavedPreferences(getActivity(), SavedPreferences.SHARED_PREFERENCES_LOGIN);

        // Inflate main layout.
        mView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Start Retro Fit
        mRetroFitManager = new RetroFitManager(this);

        // Get UI from Layout for class access.
        mEmailView = (AutoCompleteTextView) mView.findViewById(R.id.email);

        mPasswordView = (EditText) mView.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    // Add logic can start login if so desired...
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) mView.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start login.
                attemptLogin();
            }
        });

        Button mRegisterButton = (Button) mView.findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptRegister();
            }
        });

        Button mMonthlySubscribeButton = (Button) mView.findViewById(R.id.monthly_subscribe_button);
        mMonthlySubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send callback to SubscriptionsActivity
                if (mSubscriptionListener != null)
                    mSubscriptionListener.purchaseMonthlySubscription();
            }
        });

        Button mYearlySubscribeButton = (Button) mView.findViewById(R.id.yearly_subscribe_button);
        mYearlySubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send callback to SubscriptionsActivity
                if (mSubscriptionListener != null)
                    mSubscriptionListener.purchaseYearlySubscription();
            }
        });

        // Ui references.
        mLoginFormView = mView.findViewById(R.id.login_form);
        mProgressView = mView.findViewById(R.id.login_progress);
        mEmailLoginForm = (LinearLayout) mView.findViewById(R.id.email_login_form);
        mLoginSuccessTextView = (TextView) mView.findViewById(R.id.loginSuccessTextView);
        mSignOutButton = (Button) mView.findViewById(R.id.sign_out_button);

        // Sign out and remove user prefs.
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayLoginFields();
                clearLoginPreferences();
            }
        });

        // If previous credentials are found, display them.
        if (mSavedPreferences.getLoginString() != null) {
            displayEmail(mSavedPreferences.getLoginString());
        }

        // Set call back listener for email login.
        mListener = (OnSettingsFragmentListener) getActivity();

        // Set call back listener for the SubscriptionsActivity.
        mSubscriptionListener = (ISubscriptionListener) getActivity();

        return mView;
    }

    /*Remove listeners if they exist when closing Fragmentt*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mSubscriptionListener = null;
    }

    /*Call back for login handlers*/
    public interface OnSettingsFragmentListener {
        void loginEmailUpdateHandler();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            // show success message straight away-- no api.
           showSuccessMsg();

            // Start Retro Fit Process.
           mRetroFitManager.loginProcessWithRetrofit(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*Showing success messages*/
    private void showSuccessMsg() {
        Log.d(TAG, "------------------------------------- SUCCESS! ");
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        displayEmail(email);
        mSavedPreferences.setLoginSharedPreferences(email, password);
        mListener.loginEmailUpdateHandler();
    }

    /*Showing and setting email UI*/
    private void displayEmail(String email) {
        mEmailLoginForm.setVisibility(View.GONE);
        mLoginSuccessTextView.setText(getString(R.string.action_success) + " " + email);
        mLoginSuccessTextView.setVisibility(View.VISIBLE);
        mSignOutButton.setVisibility(View.VISIBLE);
    }

    /*Showing login fields*/
    private void displayLoginFields() {
        mEmailLoginForm.setVisibility(View.VISIBLE);
        mLoginSuccessTextView.setVisibility(View.GONE);
        mSignOutButton.setVisibility(View.GONE);
    }

    /*Clearing all login prefs*/
    private void clearLoginPreferences() {
        mSavedPreferences.setLoginSharedPreferences(null, null);
        mListener.loginEmailUpdateHandler();
    }

    /* Callbacks from Retro Fit.
    Implement the RetroFitManager.CallbackListener is any Activity or Fragment.*/
    @Override
    public void loginSuccessful() {

        // Add logic for success.
        showSuccessMsg();
    }

    @Override
    public void loginError() {
        // Add logic for error.
    }

    @Override
    public void registrationSuccessful() {
        // Add logic for success.
    }

    @Override
    public void registrationError() {
        // Add logic for error.
    }

    @Override
    public void retroFitError() {
        // Add logic for retro fit error.
    }
}