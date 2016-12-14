package chitoiu.com.balance.forecasting;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import chitoiu.com.balance.forecasting.fragments.AboutFragment;
import chitoiu.com.balance.forecasting.fragments.AccountFragment;
import chitoiu.com.balance.forecasting.fragments.BillsAndIncomeFragment;
import chitoiu.com.balance.forecasting.fragments.ContactSupportFragment;
import chitoiu.com.balance.forecasting.fragments.ForecastFragment;
import chitoiu.com.balance.forecasting.fragments.SettingsFragment;
import chitoiu.com.balance.forecasting.utils.SavedPreferences;

/*
* Main activity class which extends an activity dedicated to subscriptions.
* Subscriptions must be set up via your Gmail account and require the submission of the compiled apk prior to starting in-app purchases.
* After they have granted permission to the app for in-app, we'll need to connect the UI / Logic.
* The Retro Fit implementation is in the Settings Fragment
* */

public class BalanceForecastingActivity extends SubscriptionsActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsFragmentListener {

    public static String TAG = BalanceForecastingActivity.class.getSimpleName();
    private static String EMAIL_SUBJECT = "Hi";
    private static String EMAIL_BODY = "Thanks";

    // TAGS that identify each Fragment my a unique string.
    private static final String ACCOUNT_FRAGMENT_TAG =
            "BalanceForecastingActivity.ACCOUNT_FRAGMENT_TAG";
    private static final String BILLS_INCOME_FRAGMENT_TAG =
            "BalanceForecastingActivity.BILLS_INCOME_FRAGMENT_TAG";
    private static final String CONTACT_SUPPORT_FRAGMENT_TAG =
            "BalanceForecastingActivity.CONTACT_SUPPORT_FRAGMENT_TAG";
    private static final String ABOUT_FRAGMENT_TAG =
            "BalanceForecastingActivity.ABOUT_FRAGMENT_TAG";
    private static final String SETTINGS_FRAGMENT_TAG =
            "BalanceForecastingActivity.SETTINGS_FRAGMENT_TAG";
    private static final String FORECAST_FRAGMENT_TAG =
            "BalanceForecastingActivity.FORECAST_FRAGMENT_TAG";

    private SavedPreferences mSavedPreferences;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get saved user prefs.
        mSavedPreferences = new SavedPreferences(this, SavedPreferences.SHARED_PREFERENCES_LOGIN);

        // Adding drawer layout to view
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Looking for previous login credentials
        loginEmailUpdateHandler();

        // Start the first view.
        toggleMainViewFragments(ForecastFragment.newInstance(), FORECAST_FRAGMENT_TAG, false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /* Handling logic for navigation menu selections*/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            toggleMainViewFragments(AccountFragment.newInstance(), ACCOUNT_FRAGMENT_TAG, false);
        } else if (id == R.id.nav_bills_income) {
            toggleMainViewFragments(BillsAndIncomeFragment.newInstance(), BILLS_INCOME_FRAGMENT_TAG,
                    false);
        } else if (id == R.id.nav_forecast) {
            toggleMainViewFragments(ForecastFragment.newInstance(), FORECAST_FRAGMENT_TAG, false);
        } else if (id == R.id.nav_settings) {
            toggleMainViewFragments(SettingsFragment.newInstance(), SETTINGS_FRAGMENT_TAG, false);
        } else if (id == R.id.nav_support) {
            toggleMainViewFragments(ContactSupportFragment.newInstance(),
                    CONTACT_SUPPORT_FRAGMENT_TAG, false);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + EMAIL_SUBJECT + "&body=" + EMAIL_BODY);
            intent.setData(data);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            toggleMainViewFragments(AboutFragment.newInstance(), ABOUT_FRAGMENT_TAG, false);
        }

        // Get drawer and then close it after logic preformed
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Toggling the main fragments.
     */
    private void toggleMainViewFragments(Fragment fragment, String fragmentTag,
                                         boolean addToBackStack) {
        // If fragment is no null, remove it.
        if (getFragmentManager().findFragmentByTag(fragmentTag) != null) {
            getFragmentManager().popBackStack();
        } else {
            // Start fragment transaction.
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Set custom animations.
            transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right,
                    R.animator.slide_in_left, R.animator.slide_out_right);
            transaction.replace(R.id.main_fragment_container, fragment, fragmentTag);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }

            transaction.commit();
        }
    }

    /* Looking for previous login credentials */
    @Override
    public void loginEmailUpdateHandler() {
        TextView navigationViewEmailTextView =
                (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.emailTextView);
        if (mSavedPreferences.getLoginString() != null) {
            navigationViewEmailTextView.setText(mSavedPreferences.getLoginString());
        } else {
            navigationViewEmailTextView.setText(getString(R.string.not_logged_in));
        }
    }
}