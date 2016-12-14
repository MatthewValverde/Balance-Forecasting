package chitoiu.com.balance.forecasting.forms;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/* Core class for forms.  Add all code that is required for all forms here. */
public class CoreForm extends Fragment {

    public static String TAG = CoreForm.class.getSimpleName();
    public static String NAME_KEY = "name_string";
    public String mNameString = "";
    public View mView;

    public OnFormFragmentInteractionListener listener;

    TextView mInputTextView;

    public CoreForm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNameString = getArguments().getString(NAME_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listener = (OnFormFragmentInteractionListener) getParentFragment();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFormFragmentInteractionListener {
        void onFormFragmentSaveListener();
        void onFormFragmentCancelListener();
    }
}
