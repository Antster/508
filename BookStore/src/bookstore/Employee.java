
package bookstore;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Employee {
    private static Scanner scan = new Scanner(System.in);

    /* Choice that the Employee can make. It is basicly the choices that are
planned in the prompt. There is a method in DatabseHelp that has will read the array and
another that will verfy the request is in the range. */
    public static void showWelcome() {
        SuperUI.clearScreen();
        String input;
        System.out.println("====================== WELCOME ======================");
        System.out.println("\nOPTIONS");
        System.out.println("-------");
        System.out.println(" 1. Search for a book");
        System.out.println(" 2. Add a book");
        System.out.println(" 3. Delete a book");
        System.out.println(" 4. Update book info");
        System.out.println(" 5. Add a customer");
        System.out.println(" 6. Update or Delete customer info");
        System.out.println(" 7. Change book status");
        System.out.println(" 8. LOGOUT");
        System.out.print("\nWHAT WOULD YOU LIKE TO DO? ");
        input = scan.nextLine().trim();

        switch (input) {
            case "1":
                if (SuperUI.showSearch() == 1) {
                    showWelcome();
                }
                break;
            case "2":
                Book.addBook();

                break;
            case "3":
                Book.deleteBook();
                break;
            case "4":
                Book.UpdateBookPrompt();
                break;
            case "5":
                addACustomer();
                break;
            case "6":
                updateOrDeleteAPerson();
                break;
            case "7":
                changeStausPrompt();
                break;
            case "8":
                Login.LoginDirector();
                break;
            default:
                showWelcome();
                break;
        }
    }

    /*The Login displayed for Employees*/
    public static void EmployeeLogin() {
        System.out.println("======================= LOGIN =======================");
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\tEmployee ID #");
        System.out.print(" > ");
        String employee_id = scanner.next().trim();
        if (!checkID(employee_id)) {
            EmployeeLogin();
        } else {
            System.out.println("\n\tPassword:");
            System.out.print(" > ");
            String password = scanner.next();
            if (password.equals("/EXIT")) {
                Login.LoginDirector();
            } else {
                while (!checkPassword(employee_id, password)) {
                    System.out.println("\n\tPassword:");
                    System.out.print(" > ");
                    password = scanner.next();
                    if(password.equals("/EXIT")) Login.LoginDirector();
                }
                showWelcome();
            }
        }
    }

    /*checks the password for the employee*/
    /*Params are employees id, their guess of a password, and the connection to the database*/
    /*returns employees position. */
    private static boolean checkPassword(String employee_id, String password) {
        String employeePosition = "0";
        String employeePassword = "0";
        Connection conn = Login.ConnectToDatabase();
        try {
            Scanner scanner = new Scanner(System.in);
            String employeeInfo = "Select password From project_employee where employee_id = ?";
            // String employeeInfo= "Select password, position From project_employee where employee_id = ?";
            PreparedStatement help = conn.prepareCall(employeeInfo);
            help.clearParameters();
            help.setString(1, employee_id);
            ResultSet answer = help.executeQuery();

            while (answer.next()) {
                // System.out.println(answer.getString("PASSWORD"));
                employeePassword = answer.getString("PASSWORD");
                // employeePosition=answer.getString("POSITION");
            }

            while (!employeePassword.equals(password)) {
                System.out.println("Invalid Password. Try again");
                return false;

            }
            conn.close();
        } catch (SQLException s) {
            System.out.println(s.getMessage());
            EmployeeLogin();
        }

        return true;
    }


    /* This method checks the database to see if the ID entered is valid.  Still buggy.
    @ params are the id the employee gives and the database connection
    @ returns the employee id so that it can be checked against the password given
     */
    private static boolean checkID(String id) {
        Scanner scanner = new Scanner(System.in);
        // String employee_id=null;
        Connection conn = Login.ConnectToDatabase();
        try {
            String employeeInfo = "Select employee_id From project_employee where employee_id = ?";
            PreparedStatement help = conn.prepareCall(employeeInfo);
            help.clearParameters();
            help.setString(1, id);

            ResultSet answer = help.executeQuery();
            if (answer.next() == false) {
                System.out.print("ID was InValid. Try Again ");
                return false;

            }
            conn.close();
        } catch (SQLException s) {
            System.out.println(s.getMessage());
            EmployeeLogin();
        }

        return true;
    }

    /* This method gets the information  and to verify to add a Customer*/
    public static void addACustomer() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Adding a Customer? (Y/N)");
        String answer = scanner.next();
        answer = Miss.checkZeroLen(answer);
        answer = Miss.checkYorN(answer);
        if (answer.equalsIgnoreCase("Y")) {

            /* System.out.println("Customer Id");*/
            /*  String customerId=getNewCustomerID();*/
            System.out.println("Customer's first name");
            String customerFirstName = getCustomerName();
            System.out.println("Customer's Last Name");
            String customerLastName = getCustomerName();
            System.out.println("Customer Password (Must be at least 8 characters long)");
            String customerPassword = checkPassword();
            System.out.println("Confirm " + /*customerId +*/ " Name " + customerFirstName + customerLastName + " Password " + customerPassword + " Y/N ");
            String confirm = scanner.next();
            if (confirm.equalsIgnoreCase("Y")) {
                MakeACustomer(customerFirstName, customerLastName, customerPassword);
            } else {
                addACustomer();

            }

        } else if (answer.equalsIgnoreCase("N")) {
            showWelcome();
        }
    }

    /*This method is connecting to the database and actually trying to add a customer*/
    public static void MakeACustomer(String customerFirstName, String customerLastName, String customerPassword)/*throws SQLException*/ {
        Connection conn = Login.ConnectToDatabase();
        try {
            int id = 0;
            String help[] = {"customer_id"};
            String sql = "Insert into project_Customer (First_Name, Last_Name, Password) VALUES (?, ?, ?)";
            PreparedStatement createCustomer = conn.prepareStatement(sql, help);
            createCustomer.clearParameters();
            createCustomer.setString(1, customerFirstName);
            createCustomer.setString(2, customerLastName);
            createCustomer.setString(3, customerPassword);
            int createNewCustomer = createCustomer.executeUpdate();
            ResultSet customerId = createCustomer.getGeneratedKeys();
            if (createNewCustomer == 1) {
                if (customerId.next()) {
                    id = customerId.getInt(1);
                }
                System.out.println("Customer " + customerFirstName + " was created! " + " ID is " + id);
                showWelcome();
            }
        } catch (SQLException s) {

            System.out.println(customerFirstName + " was not created. Try Again.");
            System.out.println(s.getMessage());

            addACustomer();
        }
    }

    /*This method makes there is a Customer Name */
    public static String getCustomerName() {
        Scanner scanner = new Scanner(System.in);
        String CustomerName = scanner.nextLine();
        if (CustomerName.length() == 0) {
            getCustomerName();
        }
        return CustomerName;
    }

    public static String checkPassword() {
        Scanner scanner = new Scanner(System.in);
        String pass = scanner.next();
        while (pass.length() < 8) {
            System.out.println("Password must be 8 letters long");
            pass = scanner.next();
        }
        return pass;
    }

    public static void updateOrDeleteAPerson() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the Customer Id?");
        int id = scanner.nextInt();
        while (id < 10000000 || id > 20000000) {
            System.out.println("Not a valid Customer id. Try again");
            id = scanner.nextInt();
        }
        checkInfo(id);

    }

    public static void checkInfo(int id) {
        Scanner scanner = new Scanner(System.in);
        String firstName = null;
        String lastName = null;
        String password = null;
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "Select first_name, last_name, password from project_customer where customer_id=?";
            PreparedStatement info = conn.prepareCall(sql);
            info.clearParameters();
            info.setInt(1, id);
            ResultSet CustomerInfo = info.executeQuery();
            /*  if( CustomerInfo.next()==false)
            {
                System.out.println("InValid Id");

            }*/
            while (CustomerInfo.next()) {
                firstName = CustomerInfo.getString("FIRST_NAME");
                lastName = CustomerInfo.getString("LAST_NAME");
                password = CustomerInfo.getString("PASSWORD");
                System.out.println("Name: " + firstName + " " + lastName + " Password: " + password);
            }
            System.out.println("Do you want to 1. Update this Customer's info or 2. Delete this Customer or 3. Quit");
            int answer = scanner.nextInt();
            while (answer < 0 || answer > 3) {
                System.out.println("Out of Range. Try again");
                answer = scanner.nextInt();
            }
            if (answer == 1) {
                CustomerUpdatePrompt(id);
                /*  StringSql(statememt,id, firstName);*/
            } else if (answer == 2) {
                DeleteEmployee(id);
            } else if (answer == 3) {
                showWelcome();
            }

        } catch (SQLException e) {
            System.out.println("Oops! Something happened. Try Again " + e.getMessage());
            updateOrDeleteAPerson();
        }
    }

    public static void CustomerUpdatePrompt(int id) {

        Scanner scanner = new Scanner(System.in);
        String firstName = null;
        String lastname = null;
        String password = null;
        System.out.println("Would you like to update their first Name (Y/N)");
        String input = scanner.nextLine();
        Miss.checkZeroLen(input);
        if (input.equalsIgnoreCase("y")) {
            System.out.println("What would you like to change it to?");
            firstName = scanner.nextLine();
            Miss.checkZeroLen(firstName);
            UpdateFirstName(id, firstName);
            /* StringSql(id, firstName);*/
        }
        System.out.println("Do you want to update their last name (Y/N)");
        input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            System.out.println("What would you like to change it to?");
            lastname = scanner.nextLine();
            Miss.checkZeroLen(lastname);
            UpdateLastName(id, lastname);
        }
        System.out.println("Would you like to change their password (Y/N)");
        input = scanner.next();
        Miss.checkZeroLen(input);
        if (input.equalsIgnoreCase("y")) {
            System.out.println("What would you like to change it to?");
            password = scanner.next();
            Miss.checkZeroLen(password);
            UpdatePassword(id, password);

        }

        showWelcome();


    }


    private static void DeleteEmployee(int CustomerId) {

        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "delete from project_Customer where customer_id = ?";
            PreparedStatement createEmployee = conn.prepareCall(sql);
            createEmployee.clearParameters();

            createEmployee.setInt(1, CustomerId);

            int createNewEmployee = createEmployee.executeUpdate();
            if (createNewEmployee == 1) {
                System.out.println(CustomerId + " has been deleted");
                Login.request(1);

            } else {
                System.out.println("Error: Something went wrong. Try again");

            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());

        }
    }

    public static void UpdateFirstName(int id, String update) {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_customer SET FIRST_NAME= ? where customer_id = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setString(1, update);
            statement.setInt(2, id);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void UpdateLastName(int id, String update) {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_customer SET LAST_NAME= ? where customer_id = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setString(1, update);
            statement.setInt(2, id);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void UpdatePassword(int id, String update) {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_customer SET PASSWORD = ? where customer_id = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setString(1, update);
            statement.setInt(2, id);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }


    public static void changeStausPrompt() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to change a book's status 1. searching by book 2. searching by Customer 3. Quit ");

        int input = Miss.checkRange(1, 3);
        if (input == 1) {
            searchByISBNToChangeStatus();
        }

    }

    private static void searchByISBNToChangeStatus() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> bookDates = new ArrayList<>();
        long bookISBN = Book.ISBNvalidation();
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "select c.ISBN, b.title, e.first_name|| ' ' || e.last_name as Name, c.Date_Checked_out, c.Expected_Return_Date,  c.actual_return_date from project_cart c join project_book b on c.ISBN =b.isbn join project_customer e on c.customer_id= e.customer_id where c.ISBN= ? Order by b.title asc, c.Expected_return_date desc";
            PreparedStatement info = conn.prepareCall(sql);
            info.clearParameters();
            info.setLong(1, bookISBN);
            ResultSet bookInfo = info.executeQuery();

            while (bookInfo.next()) {
                bookISBN = bookInfo.getLong("ISBN");
                String title = bookInfo.getString("Title");
                String name = bookInfo.getString("Name");
                Date dateCheckedOut = bookInfo.getDate("Date_Checked_Out");
                Date dateExpectedReturn = bookInfo.getDate("Expected_Return_Date");
                Date dateActualReturn = bookInfo.getDate("actual_Return_Date");
                System.out.println("ISBN " + bookISBN + " Title " + title + " Name " + name + " Date Checked out " + dateCheckedOut + " Expected Return Date " + dateExpectedReturn);
                if (dateActualReturn != null) {
                    System.out.println(" Date actual Return " + dateActualReturn);
                }

                System.out.println("Would you like to add a return date? (Y/N)");
                String input = scanner.next();
                input = Miss.verifyYorNAnswer(input);
                if (input.equalsIgnoreCase("y")) {

                }
                // String results= bookISBN+ title+ name+ " Date Checked out "+dateCheckedOut+ " Expected Return Date "+dateExpectedReturn + " Date actual Return "+ dateActualReturn;
                // bookDates.add(results);
            }
            //  System.out.println(bookDates);
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

  /*  private static void updateReturnDatebyISBN(long bookISBN) {
        Connection conn = Login.ConnectToDatabase();

        try {
            String sql = "Update project_cart set actual_return_date = ? where ISBN =? and date_checked_out= (select Max(date_checked_out) from project_cart where ISBN = ?);";
            PreparedStatement info = conn.prepareCall(sql);
            info.clearParameters();
            info.setLong(1, bookISBN);
            info.setDate(2, );
            ResultSet bookInfo = info.executeQuery();





        }
        catch (SQLException n)
        {
            System.out.println(n.getMessage());
        }

    }

   /* private static Date getReturnDate()
    {Scanner scanner = new Scanner(System.in);
        System.out.println("What date has the book been returned?");
        String date=scanner.nextLine();
        date=Miss.checkZeroLen(date);
//        (Date)date= date;


    }*/
}