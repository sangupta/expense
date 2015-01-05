package com.sangupta.expense.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.sangupta.expense.Expense;
import com.sangupta.expense.ExpenseService;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.StringUtils;

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
		
		Date d = new Date(expense.getDate());
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		String date = "[" + format.format(d) + "] ";
		String line = expense.getExpenseID() + "," + expense.getDate() + "," + expense.getExpense() + "," + date + expense.getDescription() + StringUtils.SYSTEM_NEW_LINE;
		
		try {
			FileUtils.writeStringToFile(this.expenseFile, line, true);
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
		if(date > 0) {
			calendar.set(Calendar.DATE, date);
		}
		
		if(month > 0) {
			calendar.set(Calendar.MONTH, month);
		}
		
		if(year > 0) {
			calendar.set(Calendar.YEAR, year);
		}
		
		Expense expense = new Expense(calendar.getTimeInMillis(), value, description);
		return this.add(expense);
	}

	public boolean remove(String expenseID) {
		if(AssertUtils.isEmpty(expenseID)) {
			throw new IllegalArgumentException("Expense ID cannot be null/empty");
		}
		
		expenseID = expenseID + ",";
		try {
			List<String> lines= FileUtils.readLines(this.expenseFile);
			for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
				String line = iterator.next();
				if(line.startsWith(expenseID)) {
					iterator.remove();
				}
			}
			
			FileUtils.writeLines(this.expenseFile, lines);
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
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		
		final long start = calendar.getTimeInMillis();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		
		final long end = calendar.getTimeInMillis();
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
				if(!(start <= time && time <= end)) {
					expenses.add(expense);
				}
			}
			
			return expenses;
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return null;
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

}
