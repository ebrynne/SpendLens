package com.evans.spendlens;

public class Expense {
	    
    private String date;
    private float amount;
    private int tag;
    
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    
    public void setAmount(String amount){
    	this.amount = Float.parseFloat(amount);
    }
    
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	
	public void setTag(String tag) {
		this.tag = Integer.parseInt(tag);
	}
}
