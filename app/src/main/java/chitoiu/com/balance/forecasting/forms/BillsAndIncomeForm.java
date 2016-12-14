package chitoiu.com.balance.forecasting.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import chitoiu.com.balance.forecasting.R;

public class BillsAndIncomeForm extends CoreForm {

    public BillsAndIncomeForm() {
        // Required empty public constructor
    }

    public static BillsAndIncomeForm newInstance(String name) {
        BillsAndIncomeForm fragment = new BillsAndIncomeForm();
        Bundle args = new Bundle();
        args.putString(NAME_KEY, name);
        fragment.setArguments(args);
        return fragment;
    }

    public static BillsAndIncomeForm newInstance() {
        BillsAndIncomeForm fragment = new BillsAndIncomeForm();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (mView != null) {
            return mView;
        }

        // UI for form fragment
        mView = inflater.inflate(R.layout.fragment_form, container, false);
        ((TextView) mView.findViewById(R.id.popupTitle)).setText("Bills and Income Form");
        mInputTextView = (TextView) mView.findViewById(R.id.inputTextField);
        Button saveButton = (Button) mView.findViewById(R.id.saveButton);
        Button cancelButton = (Button) mView.findViewById(R.id.cancelButton);
        mInputTextView.setText(mNameString);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener == null) return;
                // Logic for save button goes in listener class - Bills Fragment
                listener.onFormFragmentSaveListener();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener == null) return;
                // Logic for save button goes in listener class - Bills Fragment
                listener.onFormFragmentCancelListener();
            }
        });

        return mView;
    }
}
