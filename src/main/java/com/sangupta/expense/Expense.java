/**
 *
 * expense - CLI for managing daily expenses
 * Copyright (c) 2015, Sandeep Gupta
 * 
 * http://sangupta.com/projects/expense
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.expense;


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
		return this.expenseID + "," + this.date + "," + this.expense + ","  + this.description;
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
