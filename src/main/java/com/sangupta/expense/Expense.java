package com.sangupta.expense;

public class Expense {
	
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
