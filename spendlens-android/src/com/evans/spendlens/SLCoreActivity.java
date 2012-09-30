package com.evans.spendlens;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;


public class SLCoreActivity extends Activity {

	public static final String PREFS_NAME = "slPrefs";
	
	private ArrayList<Expense> todayList=null;
	private ExpenseAdapter todayListAdapter;
	
	private float spent_today = 0;
	private float budget = 0;
	private SLDB db = null;
	
	public final static String EXPENSE_ADD_MESSAGE = "com.evans.spendlens.MESSAGE";
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		//db = SLDB(context);
		setContentView(R.layout.activity_slcore);
		
		todayList = new ArrayList<Expense>();
		todayListAdapter = new ExpenseAdapter(this, R.layout.daily_expense_row, todayList);
		ListView todayListView = (ListView)findViewById(R.id.today_list);
		todayListView.setAdapter(todayListAdapter);
		
		Button addExpenseButton = (Button)findViewById(R.id.expense_button);
		addExpenseButton.setOnClickListener(expenseHandler);
		
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec home_tab=tabHost.newTabSpec("Today");
		home_tab.setIndicator("Today");
		home_tab.setContent(R.id.home_tab);
		
		TabSpec hist_tab=tabHost.newTabSpec("History");
		hist_tab.setIndicator("History");
		hist_tab.setContent(R.id.history_tab);

		TabSpec sett_tab=tabHost.newTabSpec("Settings");
		sett_tab.setIndicator("Settings");
		sett_tab.setContent(R.id.settings_tab);

		tabHost.addTab(home_tab);
		tabHost.addTab(hist_tab);
		tabHost.addTab(sett_tab);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		budget = settings.getFloat("daily_budget", (float)0.0);
		//spent_today = settings.getFloat("spent_today", (float)0.0);
		TextView homeBudget = (TextView)findViewById(R.id.home_budget);
		homeBudget.setText(String.valueOf(budget));
		
		Button budget_button = (Button)findViewById(R.id.set_budget_button);
		budget_button.setOnClickListener(setBudgetHandler);
	}
	
	View.OnClickListener expenseHandler = new View.OnClickListener() {
		public void onClick(View view){

			EditText expenseField = (EditText)findViewById(R.id.expense_field);
			Calendar c = Calendar.getInstance();
			Expense new_expense = new Expense();
			new_expense.setDate(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
			new_expense.setCost(expenseField.getText().toString());
			
			spent_today += Float.parseFloat(expenseField.getText().toString());
			
			TextView spent_text = (TextView)findViewById(R.id.daily_spent_val);
			spent_text.setText(String.valueOf(spent_today));
			
			TextView remain_text = (TextView)findViewById(R.id.daily_remain_val);
			remain_text.setText(String.valueOf(50-spent_today));
			
			todayList.add(new_expense);
			todayListAdapter.notifyDataSetChanged();
		}
	};
	
	View.OnClickListener setBudgetHandler = new View.OnClickListener() {
		
		public void onClick(View view) {
			EditText budgetField = (EditText)findViewById(R.id.set_budget_field);
			budget = Float.parseFloat(budgetField.getText().toString());
			TextView homeBudget = (TextView)findViewById(R.id.home_budget);
			homeBudget.setText(String.valueOf(budget));
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putFloat("daily_budget", budget);

			editor.commit();
		}
	};
	
	View.OnClickListener setLocationStoreHandler = new View.OnClickListener() {
		
		public void onClick(View view) {
			EditText budgetField = (EditText)findViewById(R.id.set_budget_field);
			budget = Float.parseFloat(budgetField.getText().toString());
			TextView homeBudget = (TextView)findViewById(R.id.home_budget);
			homeBudget.setText(String.valueOf(budget));
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			
			editor.putBoolean("store_location", true);
			editor.putBoolean("location_dirty", true);
			editor.commit();
		}
	};
}


