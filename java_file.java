/**
 * Main application class for managing users, incomes, budgets, reminders, and expenses.
 */
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.text.SimpleDateFormat; // Improved: Added specific import for SimpleDateFormat for clarity
public class InvestmentApp {
    /**
     * Represents a user in the Investment App.
     * Each user has a unique email, a password, and a username.
     * The user can have multiple incomes, budgets, reminders, and expenses.
     */
    static class User {
        String email, password, username;
        List<Income> incomes = new ArrayList<>();
        List<Budget> budgets = new ArrayList<>();
        List<Reminder> reminders = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        /**
         * Constructs a new User.
         * @param email    the user's email
         * @param password the user's password
         * @param username the user's username
         */
        User(String email, String password, String username) {
            this.email = email;
            this.password = password;
            this.username = username;
        }
        
        // Improved: Added method to calculate total income for better data access
        /**
         * Calculates the total income from all income sources.
         * @return the total income amount
         */
        public double getTotalIncome() {
            double total = 0;
            for (Income income : incomes) {
                total += income.amount;
            }
            return total;
        }
        
        // Improved: Added method to calculate total expenses for better data access
        /**
         * Calculates the total expenses across all categories.
         * @return the total expense amount
         */
        public double getTotalExpenses() {
            double total = 0;
            for (Expense expense : expenses) {
                total += expense.amount;
            }
            return total;
        }
    }
    /**
     * Represents an income entry for a user.
     * Each income has a source and an amount.
     */
    static class Income {
        String source;
        double amount;
        /**
         * Constructs a new Income.
         * @param source the income source (e.g. salary)
         * @param amount the amount of income
         */
        Income(String source, double amount) {
            this.source = source;
            this.amount = amount;
        }
    }
    /**
     * Represents a budget entry set by a user.
     * Each budget has a category and an amount.
     */
    static class Budget {
        String category;
        double amount;
        double initialAmount; // Improved: Added to track original budget amount
        /**
         * Constructs a new Budget.
         * @param category the category for the budget (e.g. groceries)
         * @param amount   the budgeted amount
         */
        Budget(String category, double amount) {
            this.category = category;
            this.amount = amount;
            this.initialAmount = amount; // Improved: Store initial amount for reference
        }
        
        // Improved: Added method to calculate percentage of budget used
        /**
         * Calculates the percentage of the budget that has been used.
         * @return the percentage of budget used
         */
        public double getUsedPercentage() {
            return (initialAmount - amount) / initialAmount * 100;
        }
    }
    /**
     * Represents a reminder task with a specific date and time.
     */
    static class Reminder {
        String task;
        Date dateTime;
        /**
         * Constructs a new Reminder.
         * @param task     the reminder task
         * @param dateTime the date and time for the reminder
         */
        Reminder(String task, Date dateTime) {
            this.task = task;
            this.dateTime = dateTime;
        }
    }
    /**
     * Represents an expense entry made by a user.
     * Each expense has a category, an amount, and a date.
     */
    static class Expense {
        String category;
        double amount;
        Date date;
        /**
         * Constructs a new Expense.
         * @param category the category of the expense (e.g. rent)
         * @param amount   the amount spent
         * @param date     the date of the expense
         */
        Expense(String category, double amount, Date date) {
            this.category = category;
            this.amount = amount;
            this.date = date;
        }
    }
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd"); // Improved: Added constant for date formatting
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Improved: Added constant for datetime formatting
    
    static Map<String, User> users = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);
    static User loggedInUser = null;
    /**
     * Main method that runs the Investment App.
     * Displays the main menu and handles user input.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Improved: Added sample data for testing
        addSampleData();
        
        while (true) {
            System.out.println("\n1. Sign Up\n2. Login\n3. Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": signUp(); break;
                case "2": login(); break;
                case "3": 
                    System.out.println("Thank you for using the Investment App. Goodbye!"); // Improved: Added exit message
                    scanner.close(); // Improved: Properly close scanner before exiting
                    System.exit(0); 
                    break;
                default: System.out.println("Invalid choice");
            }
        }
    }
    
    // Improved: Added method to create sample data for testing purposes
    /**
     * Adds sample data for testing the application.
     */
    private static void addSampleData() {
        User testUser = new User("test@example.com", "password", "TestUser");
        testUser.incomes.add(new Income("Salary", 5000));
        testUser.budgets.add(new Budget("Groceries", 500));
        testUser.budgets.add(new Budget("Rent", 1200));
        users.put("test@example.com", testUser);
        System.out.println("Sample user created for testing: test@example.com / password");
    }
    /**
     * Handles user registration.
     * Prompts for email, password, and username, and registers the new user.
     */
    static void signUp() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        if (users.containsKey(email)) {
            System.out.println("Email already registered.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        users.put(email, new User(email, password, username));
        System.out.println("Sign up successful!");
    }
    /**
     * Handles user login.
     * Verifies email and password, and opens dashboard if successful.
     */
    static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = users.get(email);
        if (user != null && user.password.equals(password)) {
            loggedInUser = user;
            System.out.println("Login successful. Welcome " + user.username + "!");
            dashboard();
        } else {
            System.out.println("Invalid credentials.");
        }
    }
    /**
     * Displays the user dashboard and handles options like tracking income, setting budgets, etc.
     */
    static void dashboard() {
        while (true) {
            System.out.println("\nDashboard - Select an option:");
            System.out.println("1. Track Income");
            System.out.println("2. Create Budget");
            System.out.println("3. Set Reminder");
            System.out.println("4. Track Expense");
            System.out.println("5. View Summary");
            System.out.println("6. Logout");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": trackIncome(); break;
                case "2": createBudget(); break;
                case "3": setReminder(); break;
                case "4": trackExpense(); break;
                case "5": viewSummary(); break;
                case "6": 
                    System.out.println("Logging out..."); // Improved: Added logout message
                    loggedInUser = null; 
                    return;
                default: System.out.println("Invalid choice");
            }
        }
    }
    /**
     * Adds an income record to the logged-in user's profile.
     */
    static void trackIncome() {
        System.out.print("Enter income source (e.g. salary): ");
        String source = scanner.nextLine();
        System.out.print("Enter amount: ");
        try { // Improved: Added try-catch for input validation
            double amount = Double.parseDouble(scanner.nextLine());
            loggedInUser.incomes.add(new Income(source, amount));
            System.out.println("Income saved and displayed on dashboard.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    /**
     * Adds a budget entry to the logged-in user's profile.
     */
    static void createBudget() {
        System.out.print("Enter budget category (e.g. groceries): ");
        String category = scanner.nextLine();
        System.out.print("Enter budget amount: ");
        try { // Improved: Added try-catch for input validation
            double amount = Double.parseDouble(scanner.nextLine());
            loggedInUser.budgets.add(new Budget(category, amount));
            System.out.println("Budget saved and displayed on dashboard.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    /**
     * Schedules a reminder for the logged-in user.
     */
    static void setReminder() {
        System.out.print("Enter reminder task (e.g. electricity bill): ");
        String task = scanner.nextLine();
        System.out.print("Enter date and time (yyyy-MM-dd HH:mm): ");
        try {
            String datetimeStr = scanner.nextLine();
            Date date = DATE_TIME_FORMAT.parse(datetimeStr); // Improved: Used constant for date parsing
            Reminder reminder = new Reminder(task, date);
            loggedInUser.reminders.add(reminder);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    System.out.println("\n🔔 Reminder: " + task + " is due now!");
                }
            }, date);
            System.out.println("Reminder saved. You will be notified at the specified time.");
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm format.");
        }
    }
    /**
     * Adds an expense entry and updates the corresponding budget.
     */
    static void trackExpense() {
        System.out.print("Enter expense category (e.g. groceries): ");
        String category = scanner.nextLine();
        System.out.print("Enter expense amount: ");
        try { // Improved: Added try-catch for input validation
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            try {
                Date date = DATE_FORMAT.parse(dateStr); // Improved: Used constant for date parsing
                loggedInUser.expenses.add(new Expense(category, amount, date));
                updateBudget(category, amount);
                System.out.println("Expense saved and budget updated.");
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    /**
     * Updates the budget for the specified category after an expense is added.
     * @param category     the budget category
     * @param spentAmount  the amount spent
     */
    static void updateBudget(String category, double spentAmount) {
        for (Budget b : loggedInUser.budgets) {
            if (b.category.equalsIgnoreCase(category)) {
                b.amount -= spentAmount;
                System.out.println("Remaining budget for " + category + ": $" + b.amount); // Improved: Added $ symbol for clarity
                if (b.amount < 0) { // Improved: Added warning for budget overrun
                    System.out.println("Warning: You have exceeded your budget for " + category + "!");
                }
                return;
            }
        }
        System.out.println("No budget set for this category.");
    }
    /**
     * Displays a summary of incomes, budgets, expenses, and reminders.
     */
    static void viewSummary() {
        System.out.println("\n--- Dashboard Summary ---");
        
        // Improved: Added total income calculation
        double totalIncome = loggedInUser.getTotalIncome();
        System.out.println("Total Income: $" + totalIncome);
        
        System.out.println("\nIncomes:");
        loggedInUser.incomes.forEach(i -> System.out.println(i.source + ": $" + i.amount));
        
        System.out.println("\nBudgets:");
        loggedInUser.budgets.forEach(b -> {
            System.out.println(b.category + ": $" + b.amount + " (Used: " + 
                               String.format("%.1f", b.getUsedPercentage()) + "%)"); // Improved: Added percentage used
        });
        
        // Improved: Added total expenses calculation
        double totalExpenses = loggedInUser.getTotalExpenses();
        System.out.println("\nTotal Expenses: $" + totalExpenses);
        
        System.out.println("\nExpenses:");
        loggedInUser.expenses.forEach(e -> System.out.println(e.category + ": $" + e.amount + " on " + DATE_FORMAT.format(e.date))); // Improved: Formatted date output
        
        System.out.println("\nReminders:");
        loggedInUser.reminders.forEach(r -> System.out.println(r.task + " at " + DATE_TIME_FORMAT.format(r.dateTime))); // Improved: Formatted datetime output
        
        // Improved: Added net savings/spending calculation
        double netAmount = totalIncome - totalExpenses;
        System.out.println("\nNet Savings: $" + netAmount);
        if (netAmount < 0) {
            System.out.println("Warning: You are spending more than your income!");
        }
    }
}