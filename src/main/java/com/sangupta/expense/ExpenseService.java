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

import java.util.List;

public interface ExpenseService {

	/**
	 * Add the given expense
	 * 
	 * @param expense
	 */
	public boolean add(Expense expense);
	
	/**
	 * Add expense for today
	 * 
	 * @param expense
	 * @param description
	 */
	public boolean add(int expense, String description);
	
	/**
	 * Add expense for the given date of the year
	 * 
	 * @param expense
	 * @param description
	 * @param date
	 * @param month
	 * @param year
	 */
	public boolean add(int expense, String description, int date, int month, int year);
	
	/**
	 * Remove expense from store
	 * 
	 * @param expenseID
	 */
	public boolean remove(String expenseID);

	/**
	 * Remove given expense
	 * 
	 * @param expense
	 */
	public boolean remove(Expense expense);

	/**
	 * Find total expense for this month
	 * 
	 * @return
	 */
	public long total();
	
	/**
	 * Find total expense for the given month in given year
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	public long total(int month, int year);
	
	/**
	 * Return a list of all expenses for the current month and year
	 * 
	 * @return
	 */
	public List<Expense> list();
	
	/**
	 * Return a list of all expenses for the given month and year
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	public List<Expense> list(int month, int year);

	/**
	 * Sort data in data store chronologically
	 * 
	 */
	public void sort();
	
}
