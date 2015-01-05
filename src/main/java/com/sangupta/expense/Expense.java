package com.sangupta.expense;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense implements Comparable<Expense> {
	
	// expense id
	private String expenseID;
	
	// date of expense
	private long date;
	
	// amount for expense
	private int expense;
	
	// description of the expense
	private String description;
	
	/**
	 * Default contructor
	 */
	public Expense() {
		
	}
	
	/**
	 * Convenience constructor
	 * 
	 * @param expense
	 * @param description
	 */
	public Expense(int expense, String description) {
		this(System.currentTimeMillis(), expense, description);
	}
	
	/**
	 * Convenience constructor
	 * 
	 * @param time
	 * @param expense
	 * @param description
	 */
	public Expense(long time, int expense, String description) {
		this.date = time;
		this.expense = expense;
		this.description = description;
	}
	
	@Override
	public String toString() {
		Date d = new Date(this.getDate());
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		String date = "[" + format.format(d) + "] ";
		String line = this.expenseID + "," + this.date + "," + this.expense + "," + date + this.description;

		return line;
	}
	
	public int compareTo(Expense o) {
		if(o == null) {
			return -1;
		}
		
		if(this == o) {
			return 0;
		}
		
		if(this.date < o.date) {
			return -1;
		}
		
		if(this.date > o.date) {
			return 1;
		}
		
		// same date
		return this.expense - o.expense;
	}
	
	// Usual accessors follow

	/**
	 * @return the expenseID
	 */
	public String getExpenseID() {
		return expenseID;
	}

	/**
	 * @param expenseID the expenseID to set
	 */
	public void setExpenseID(String expenseID) {
		this.expenseID = expenseID;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * @return the expense
	 */
	public int getExpense() {
		return expense;
	}

	/**
	 * @param expense the expense to set
	 */
	public void setExpense(int expense) {
		this.expense = expense;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
