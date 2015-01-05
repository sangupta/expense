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

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.HelpOption;
import io.airlift.airline.Option;
import io.airlift.airline.SingleCommand;

import javax.inject.Inject;

import com.sangupta.expense.impl.SingleFileExpenseServiceImpl;
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
	private int date;
	
	@Option(name = { "--month", "-m" }, description = "The month in which expense was incurred")
	private int month;
	
	@Option(name = { "--year", "-y" }, description = "The year in which expense was incurred")
	private int year;
	
	@Option(name = { "--amount", "-a" }, description = "The round-off integer expense that was incurred")
	private int amount;
	
	@Option(name = { "--total", "-t" }, description = "The total amount of expenses incurred in the month")
	private boolean showTotal;
	
	@Option(name = { "--sort", "-s" }, description = "Sort line chronologically")
	private boolean sortLines;
	
	@Arguments(description = "The description for the expense")
	private String description;
	
	public static void main(String[] args) {
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
		ExpenseService service = new SingleFileExpenseServiceImpl();
		if(this.showTotal) {
			long total = service.total(this.month, this.year);
			System.out.println("Total expenses incurred for month: " + total);
			return true;
		}
		
		if(this.sortLines) {
			service.sort();
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
		
		System.out.println("Expense added.");
		return true;
	}
}
