package chitoiu.com.balance.forecasting.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import chitoiu.com.balance.forecasting.R;
import chitoiu.com.balance.forecasting.adapter.TableAdapter;
import chitoiu.com.balance.forecasting.forms.AccountForm;
import chitoiu.com.balance.forecasting.forms.CoreForm;
import chitoiu.com.balance.forecasting.models.SampleTableLayout;

public class AccountFragment extends CoreFragment
        implements CoreForm.OnFormFragmentInteractionListener {

    public static String TAG = AccountFragment.class.getSimpleName();
    private View mView;
    private FloatingActionButton mFabButton;

    private static final String ACCOUNT_FORM_TAG = "AccountFragment.ACCOUNT_FORM_TAG";

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
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
        mView = inflater.inflate(R.layout.fragment_account, container, false);

        // Adding Fab Button.
        mFabButton = (FloatingActionButton) mView.findViewById(R.id.fab);

        // Fab Button click handler for adding a new form.
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleViewFragments(AccountForm.newInstance(), ACCOUNT_FORM_TAG, true);

                mFabButton.setVisibility(View.GONE);
            }
        });

        // Loading an Ad View.  Toggle this on or off.
        loadAdView((AdView) mView.findViewById(R.id.adView));

        /*Using a Table Layout as Opposed to a ListView below*/
        //fillGrid((TableLayout) mView.findViewById(R.id.grid));

        // Create a List View for tables.
        ListView listView = (ListView) mView.findViewById(R.id.listView);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(new TableAdapter(getActivity(), SampleTableLayout.TABLE_DATA));

        // Logic for an item click.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get a specific text view from the column in order to extract string for displaying in the form.
                TextView nameColumn = (TextView) view.findViewWithTag(COLUMN1);

                // Display a toast message.
                /*Toast.makeText(getActivity(), "Clicked: " + nameColumn.getText(),
                        Toast.LENGTH_SHORT).show();*/

                // Get string to display in form.
                String nameString = nameColumn.getText().toString();

                // Add form fragment to view-- sending the nameString as a parameter.
                boolean isVisible =
                        toggleViewFragments(AccountForm.newInstance(nameString), ACCOUNT_FORM_TAG,
                                true);

                // Show or Hide the Fab Button.
                if (isVisible) {
                    mFabButton.setVisibility(View.GONE);
                } else {
                    mFabButton.setVisibility(View.VISIBLE);
                }
            }
        });
        return mView;
    }

    /* Receiving save callback from form*/
    @Override
    public void onFormFragmentSaveListener() {
        toggleViewFragments(AccountForm.newInstance(), ACCOUNT_FORM_TAG, true);

        mFabButton.setVisibility(View.VISIBLE);
    }

    /* Receiving cancel callback from form*/
    @Override
    public void onFormFragmentCancelListener() {
        toggleViewFragments(AccountForm.newInstance(), ACCOUNT_FORM_TAG, true);

        mFabButton.setVisibility(View.VISIBLE);
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
}
