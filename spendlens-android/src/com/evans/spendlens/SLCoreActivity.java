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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
	private ArrayList<Summary> summaryList=null;
	private SummaryAdapter summaryListAdapter=null;
	
	private float spent_today = 0;
	private float budget = 50;
	private ExpenseDataSource eds = null;
	private SummaryDataSource sds = null;
	private Context context = null;
	
	public final static String EXPENSE_ADD_MESSAGE = "com.evans.spendlens.MESSAGE";
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_slcore);
		
		eds = new ExpenseDataSource(this);
		sds = new SummaryDataSource(this);
		eds.open();
		todayList = eds.getAll();
		eds.close();
		/*
		sds.add("2012-09-12", "76.00", budget);
		sds.add("2012-09-13", "66.00", budget);
		sds.add("2012-09-14", "45.00", budget);
		sds.add("2012-09-15", "23.10", budget);
		sds.add("2012-09-16", "12.05", budget);
		sds.add("2012-09-17", "73.00", budget);
		sds.add("2012-09-18", "50.00", budget);
		*/
		sds.open();
		summaryList = sds.getAll();
		sds.close();
		
		
		
		todayListAdapter = new ExpenseAdapter(this, R.layout.daily_expense_row, todayList);
		ListView todayListView = (ListView)findViewById(R.id.today_list);
		todayListView.setAdapter(todayListAdapter);
		
		summaryListAdapter = new SummaryAdapter(this, R.layout.summary_row, summaryList);
		ListView summaryListView = (ListView)findViewById(R.id.history_list);
		summaryListView.setAdapter(summaryListAdapter);
		
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
		
		context = this;
		
		Button budget_button = (Button)findViewById(R.id.set_budget_button);
		budget_button.setOnClickListener(setBudgetHandler);
	}
	
	View.OnClickListener expenseHandler = new View.OnClickListener() {
		public void onClick(View view){

			EditText expenseField = (EditText)findViewById(R.id.expense_field);
			expenseField.setText("");
			Calendar c = Calendar.getInstance();
			String date = formatCalendar(c);
			eds.open();
			String spent = expenseField.getText().toString();
			Expense new_expense = eds.add(date, spent);
			eds.close();
			spent_today += Float.parseFloat(spent);
			
			TextView spent_text = (TextView)findViewById(R.id.daily_spent_val);
			spent_text.setText(String.valueOf(spent_today));
			
			TextView remain_text = (TextView)findViewById(R.id.daily_remain_val);
			remain_text.setText(String.valueOf(budget-spent_today));
			
			todayList.add(new_expense);
			todayListAdapter.notifyDataSetChanged();
			
		}
	};
	
	public String formatCalendar(Calendar c){
		return c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
	}
	
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
			
			popupAlert("Daily budget set to" + budget + ".");
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
			popupAlert("We will now give you location sensitive reports on your spending!");
		}
	};
	
	public void popupAlert(String text){
		AlertDialog.Builder popupBuilder = new AlertDialog.Builder(context);
		TextView myMsg = new TextView(context);
		myMsg.setText(text);
		myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
		popupBuilder.setView(myMsg);
		popupBuilder.show();
	}
	
	public void updateStats(){
		
	}
}



