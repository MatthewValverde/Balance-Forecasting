package chitoiu.com.balance.forecasting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import chitoiu.com.balance.forecasting.R;
import chitoiu.com.balance.forecasting.models.SampleTableLayout;

/*Adapter class for creating table-- columns*/
public class TableAdapter extends ArrayAdapter<SampleTableLayout> {
    private final Context context;
    private final SampleTableLayout[] values;

    public TableAdapter(Context context, SampleTableLayout[] values) {
        super(context, R.layout.listview_item_table_data, values);
        this.context = context;
        this.values = values;
    }

    /*Add columns to the table here.*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listview_item_table_data, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView column1TextView = (TextView) rowView.findViewById(R.id.column1);
        TextView column2TextView = (TextView) rowView.findViewById(R.id.column2);
        textView.setText(String.valueOf(position + 1));
        column1TextView.setText(values[position].column1);
        column2TextView.setText(values[position].column2);

        return rowView;
    }
}
