package com.evans.spendlens;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

	private ArrayList<Expense> items;
	
	
	public ExpenseAdapter(Context context, int textViewResourceId, ArrayList<Expense> items) {
        super(context, textViewResourceId, items);
        this.items = items;
	}
	
	 @Override
     public View getView(int position, View convertView, ViewGroup parent) {
             View v = convertView;
             if (v == null) {
                 LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 v = vi.inflate(R.layout.daily_expense_row, null);
             }
             Expense e = items.get(position);
             if (e != null) {
                     TextView dt = (TextView) v.findViewById(R.id.datetext);
                     TextView ct = (TextView) v.findViewById(R.id.costtext);
                     if (dt != null) {
                           dt.setText(e.getDate());                            }
                     if(ct != null){
                           ct.setText(e.getCost());
                     }
             }
             return v;
     }

}
