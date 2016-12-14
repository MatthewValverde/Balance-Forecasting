package chitoiu.com.balance.forecasting.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import chitoiu.com.balance.forecasting.R;
import chitoiu.com.balance.forecasting.forms.CoreForm;

public class CoreFragment extends Fragment {

    // Test device ID needed for ADs.
    public static String TEST_DEVICE_ID = "74AA8399C47BF524A457C8D76BCD19A1";

    public static String TAG = CoreForm.class.getSimpleName();
    public static String COLUMN1 = "column1";
    public static String COLUMN2 = "column2";
    private View mView;
    public OnFragmentInteractionListener mListener;
    private FloatingActionButton mFabButton;

    private static final String ACCOUNT_FORM_TAG = "AccountFragment.ACCOUNT_FORM_TAG";

    public CoreFragment() {
        // Required empty public constructor
    }

    public static CoreFragment newInstance() {
        CoreFragment fragment = new CoreFragment();
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

    /*Loading an Ad View*/
    public void loadAdView(AdView adView) {
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice(TEST_DEVICE_ID);
        AdRequest adRequest = builder.build();
        adView.loadAd(adRequest);
    }

    /* mListener null on fragment detach*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface can be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Toggling the main fragments.
     */
    public boolean toggleViewFragments(Fragment fragment, String fragmentTag,
                                       boolean addToBackStack) {
        // If fragment is no null, remove it.
        if (getChildFragmentManager().findFragmentByTag(fragmentTag) != null) {
            getChildFragmentManager().popBackStack();
            return false;
        } else {
            // Start fragment transaction.
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            // Set custom animations.
            transaction.setCustomAnimations(R.animator.slide_up, R.animator.slide_down,
                    R.animator.slide_up, R.animator.slide_down);
            transaction.replace(R.id.child_fragment_container, fragment, fragmentTag);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }

            transaction.commit();
            // transaction.detach(fragment).attach(fragment).commit();
            return true;
        }
    }

    /*Using a Table Layout as Opposed to a ListView above*/
    /*  private void fillGrid(TableLayout view) {
          for(int i=0; i<10; i++) {
              TableRow row = new TableRow(getActivity());
              //set row
              for(int j=0; j<4; j++) {
                  TextView actualData = new TextView(getActivity());
                  actualData.setText("RASTA - " + j);
                  actualData.setBackgroundColor(0x999999);

                  //set properties
                  row.addView(actualData);
              }

              view.addView(row);
          }
      }*/

}
