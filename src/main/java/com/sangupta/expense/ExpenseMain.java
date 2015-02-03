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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.HelpOption;
import io.airlift.airline.Option;
import io.airlift.airline.SingleCommand;

import javax.inject.Inject;

import com.sangupta.expense.impl.SingleFileExpenseServiceImpl;
import com.sangupta.jerry.print.ConsoleTable;
import com.sangupta.jerry.print.ConsoleTable.ConsoleTableLayout;
import com.sangupta.jerry.util.AssertUtils;

/**
 * The main entry point from the command line.
 * 
 * @author sangupta
 *
 */
@Command(name = "expense", description = "Simple CLI tool to manage daily expenses")
public class ExpenseMain {
	
	@Inject
    public HelpOption helpOption;
	
	@Option(name = { "--date", "-d" }, description = "The date on which expense was incurred")
	private int date = -1;
	
	@Option(name = { "--month", "-m" }, description = "The month in which expense was incurred")
	private int month = -1;
	
	@Option(name = { "--year", "-y" }, description = "The year in which expense was incurred")
	private int year = -1;
	
	@Option(name = { "--amount", "-a" }, description = "The round-off integer expense that was incurred")
	private int amount;
	
	@Option(name = { "--total", "-t" }, description = "The total amount of expenses incurred in the month")
	private boolean showTotal;
	
	@Option(name = { "--sort", "-s" }, description = "Sort line chronologically")
	private boolean sortLines;
	
	@Option(name = { "--remove", "-r" }, description = "Remove the given expense")
	private String removeID;
	
	@Option(name = { "--list", "-l" }, description = "Show list of expenses")
	private boolean showList;
	
	@Arguments(description = "The description for the expense")
	private String description;
	
	public static void main(String[] args) {
		args = new String[] { "-l" };
		
		ExpenseMain main = SingleCommand.singleCommand(ExpenseMain.class).parse(args);
		if(main.helpOption.showHelpIfRequested()) {
			return;
		}
		
		boolean handled = main.run();
		if(!handled) {
			// display help
			main.helpOption.help = true;
			main.helpOption.showHelpIfRequested();
			return;
		}
	}

	private boolean run() {
		// check and fix month
		if(this.month > 0) {
			this.month = this.month - 1; // in java jan is 0, whereas for humans it is 1
		}
		
		ExpenseService service = new SingleFileExpenseServiceImpl();
		if(this.showTotal) {
			long total;
			if(this.month == -1 && this.year == -1) {
				total = service.total();
			} else {
				total = service.total(this.month, this.year);
			}
			
			System.out.println("Total expenses incurred for month: " + total);
			return true;
		}
		
		if(this.sortLines) {
			service.sort();
			return true;
		}
		
		if(this.showList) {
			List<Expense> list;
			if(this.month == -1 && this.year == -1) {
				list = service.list();
			} else {
				list = service.list(this.month, this.year);
			}
			
			if(AssertUtils.isEmpty(list)) {
				System.out.println("No expenses for the given/current month");
				return true;
			}
			
			ConsoleTable table = new ConsoleTable(ConsoleTableLayout.MULTI_LINE);
			table.addHeaderRow("ID", "Date", "Expense", "Total", "Description");
			long total = 0;
			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			for(Expense expense : list) {
				total += expense.getExpense();
				table.addRow(expense.getExpenseID(), format.format(new Date(expense.getDate())), expense.getExpense(), total, expense.getDescription());
			}
			
			table.write(System.out);
			return true;
		}
		
		if(AssertUtils.isNotEmpty(this.removeID)) {
			boolean removed = service.remove(removeID);
			if(removed) {
				System.out.println("Expense removed.");
				return true;
			}
			
			System.out.println("No such expense found!");
			return true;
		}
		
		if(this.amount < 0) {
			System.out.println("Amount of expense cannot be negative");
			return true;
		}
		
		if(this.amount == 0 && AssertUtils.isEmpty(this.description)) {
			return false;
		}
		
		boolean added = service.add(this.amount, this.description, this.date, this.month, this.year);
		if(!added) {
			System.out.println("Unable to add expense!");
			return true;
		}
		
		// as the expense was added - show the total amount
		long total = service.total(this.month, this.year);
		System.out.println("Total expenses incurred for month: " + total);
		
		// we could process fine
		return true;
	}
}
