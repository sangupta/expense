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

package com.sangupta.expense.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.sangupta.expense.Expense;
import com.sangupta.expense.ExpenseService;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.StringUtils;

/**
 * A single CSV file based implementation of the {@link ExpenseService}.
 * 
 * @author sangupta
 *
 */
public class SingleFileExpenseServiceImpl implements ExpenseService {
	
	private final File expenseFile;
	
	public SingleFileExpenseServiceImpl() {
		this.expenseFile = new File(FileUtils.getUserDirectory(), "expense.csv");
	}

	public boolean add(Expense expense) {
		if(expense == null) {
			throw new IllegalArgumentException("Expense object cannot be null");
		}
		
		if(AssertUtils.isEmpty(expense.getExpenseID())) {
			expense.setExpenseID(UUID.randomUUID().toString());
		}
		
		try {
			FileUtils.writeStringToFile(this.expenseFile, expense.toString() + StringUtils.SYSTEM_NEW_LINE, true);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public boolean add(int value, String description) {
		Expense expense = new Expense(value, description);
		return this.add(expense);
	}

	public boolean add(int value, String description, int date, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, date);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		
		Expense expense = new Expense(calendar.getTimeInMillis(), value, description);
		return this.add(expense);
	}

	public boolean remove(String expenseID) {
		if(AssertUtils.isEmpty(expenseID)) {
			throw new IllegalArgumentException("Expense ID cannot be null/empty");
		}
		
		boolean success = false;
		try {
			List<String> lines= FileUtils.readLines(this.expenseFile);
			for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
				Expense expense = parseExpense(iterator.next());
				if(expenseID.equals(expense.getExpenseID())) {
					iterator.remove();
					success = true;
				}
			}
			
			FileUtils.writeLines(this.expenseFile, lines);
			return success;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public boolean remove(Expense expense) {
		if(expense == null) {
			throw new IllegalArgumentException("Expense object cannot be null");
		}
		
		return this.remove(expense.getExpenseID());
	}

	public long total() {
		Calendar calendar = Calendar.getInstance();
		return this.total(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
	}

	public long total(int month, int year) {
		List<Expense> list = this.list(month, year);
		if(AssertUtils.isEmpty(list)) {
			return 0;
		}
		
		long total = 0;
		for(Expense expense : list) {
			total += expense.getExpense();
		}
		
		return total;
	}

	public List<Expense> list() {
		Calendar calendar = Calendar.getInstance();
		return this.list(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
	}

	public List<Expense> list(int month, int year) {
		final List<Expense> expenses = new ArrayList<Expense>();
		try {
			List<String> lines = FileUtils.readLines(this.expenseFile);
			for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
				String line = iterator.next();
				Expense expense = parseExpense(line);
				if(expense == null) {
					continue;
				}
				
				final long time = expense.getDate();
				Date date = new Date(time);
				if(dateInMonth(date, month, year)) {
					expenses.add(expense);
				}
			}
			
			return expenses;
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return null;
	}
	
	public void sort() {
		final List<Expense> expenses = new ArrayList<Expense>();
		try {
			List<String> lines = FileUtils.readLines(this.expenseFile);
			for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
				String line = iterator.next();
				Expense expense = parseExpense(line);
				if(expense == null) {
					continue;
				}

				expenses.add(expense);
			}
			
			// sort expenses
			Collections.sort(expenses);

			// rebuild text file
			lines.clear();
			for(Expense expense : expenses) {
				lines.add(expense.toString());
			}
			
			FileUtils.writeLines(this.expenseFile, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private Expense parseExpense(String line) {
		if(AssertUtils.isEmpty(line)) {
			return null;
		}
		
		Expense expense = new Expense();
		int index = line.indexOf(',');
		if(index == -1) {
			return null;
		}
		expense.setExpenseID(line.substring(0, index));
		
		int start = index + 1;
		index = line.indexOf(',', index + 1);
		if(index == -1) {
			return null;
		}
		expense.setDate(StringUtils.getLongValue(line.substring(start, index), -1));
		if(expense.getDate() == -1) {
			return null;
		}
		
		start = index + 1;
		index = line.indexOf(',', index + 1);
		if(index == -1) {
			return null;
		}
		expense.setExpense(StringUtils.getIntValue(line.substring(start, index), -1));
		if(expense.getExpense() == -1) {
			return null;
		}
		
		expense.setDescription(line.substring(index + 1));
		return expense;
	}

	private static boolean dateInMonth(Date date, int month, int year) {
		if(date.getMonth() == month && (date.getYear() + 1900) == year) {
			return true;
		}
		
		return false;
	}

}
