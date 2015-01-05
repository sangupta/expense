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
