/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

/**
 *
 * @author Anthony
 */
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;
import java.util.Scanner;

public class Employee {

    private static Scanner scan = new Scanner(System.in);

    /* Choice that the Employee can make. It is basicly the choices that are
planned in the prompt. There is a method in DatabseHelp that has will read the array and
another that will verfy the request is in the range. */
    private static void showWelcome() {
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
        System.out.println(" 7. LOGOUT");
        System.out.print("\nWHAT WOULD YOU LIKE TO DO? ");
        input = scan.nextLine().trim();

        switch (input) {
            case "1":
                if (SuperUI.showSearch() == 1) {
                    showWelcome();
                }
                break;
            case "2":

                break;
            case "3":

                break;
            case "4":

                break;
            case "5":
                addACustomer();
                break;
            case "6":
                updateOrDeleteAPerson();
                break;
            case "7":
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
                }
                showWelcome();
            }
        }
    }

    /*checks the password for the employee*/
 /*Params are employees id, their guess of a password, and the connection to the database*/
 /*returns employees position. */
    public static boolean checkPassword(String employee_id, String password) {
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
    public static boolean checkID(String id) {
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
            // else{
            //     while(answer.next())
            //     {
            //         employee_id=answer.getString("employee_id");

            //     }
            //     employee_id=id;
            // }
            conn.close();
        } catch (SQLException s) {
            System.out.println(s.getMessage());
            EmployeeLogin();
        }

        return true;
    }

    /* This is suppose to connect the choice the user wants to the actions that are needed*/
//    public static void employeeHomePage(int choice) {
//        Connection conn = Login.ConnectToDatabase();
//        String choices[] = EmployeeChoices();
//        System.out.println(choices[choice]);
//        if (choice == 1) {
//            //Search for a book
//        } else if (choice == 2) {
//            //Add a book
//        } else if (choice == 3) {
//            //Delete a book
//        } else if (choice == 4) {
//            //Update a book
//        } else if (choices[choice].equalsIgnoreCase("Add a Customer")) {
//            addACustomer();
//        } else if (choice == 6) {
//            updateOrDeleteAPerson();
//        } else if (choices[choice].equals("Quit")) {
//            Login.LoginDirector();
//            //Quit
//        }
//    }

    /* This method gets the information  and to verify to add a Customer*/
    public static void addACustomer() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Adding a Customer? (Y/N)");
        String answer = scanner.next();
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
            Login.request(1);
        } else {
            System.out.print("Not a valid choice. Y or N. Try Again");
            addACustomer();
        }

    }

    /*This method is connecting to the database and actually trying to add a customer*/
    public static void MakeACustomer(String customerFirstName, String customerLastName, String customerPassword)/*throws SQLException*/ {
        Connection conn = Login.ConnectToDatabase();
        try {/*RowId id=null;*/
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
                Login.request(1);
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
            System.out.println("Do you want to 1. Update this Customer's info or 2. Delete this Customer");
            int answer = scanner.nextInt();
            while (answer < 0 || answer > 2) {
                System.out.println("Out of Range. Try again");
                answer = scanner.nextInt();
            }
            if (answer == 1) {
                String statememt = CustomerUpdatePrompt(id);
                /*  StringSql(statememt,id, firstName);*/
            } else if (answer == 2) {
                DeleteEmployee(id);
            }

        } catch (SQLException e) {
            System.out.println("Oops! Something happened. Try Again " + e.getMessage());
            updateOrDeleteAPerson();
        }
    }

    public static String CustomerUpdatePrompt(int id) {
        String sql = null;
        Scanner scanner = new Scanner(System.in);
        String firstName = null;
        String lastname = null;
        String password = null;
        System.out.println("Would you like to update their first Name (Y/N)");
        String input = scanner.nextLine();
        checkZeroLen(input);
        if (input.equalsIgnoreCase("y")) {
            System.out.println("What would you like to change it to?");
            firstName = scanner.nextLine();
            checkZeroLen(firstName);
            sql = "first_name = ?";
            UpdateFirstName(id, firstName);
            /* StringSql(id, firstName);*/
        }
        System.out.println("Do you want to update their last name (Y/N)");
        input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            System.out.println("What would you like to change it to?");
            lastname = scanner.nextLine();
            checkZeroLen(lastname);
            if (sql == null) {
                sql = "last_name = " + lastname;
            } else {
                sql = sql + ", " + "last_name = " + lastname;
            }
        }
        System.out.println("Would you like to change their password (Y/N)");
        input = scanner.next();
        checkZeroLen(input);
        if (input.equalsIgnoreCase("y")) {
            System.out.println("What would you like to change it to?");
            password = scanner.next();
            checkZeroLen(password);
            if (sql == null) {
                sql = "password " + password;
            } else {
                sql = sql + ", " + "password = " + password;
            }
        }

        /*confirmChanges(firstName,lastname,password);*/
        return sql;
    }/*
public  static String confirmChanges(String firstName,String lastname, String password) {
        Scanner scanner = new Scanner(System.in);
    String changes = null;
    if (firstName != null) {
        changes = "First Name: "+ firstName;
    }
    if (lastname != null) {
        if (changes != null) {
            changes = changes + " Last Name: " + lastname;
        } else {
            changes = "Last Name: " + lastname;
        }
    }
    if (password != null) {
        if (changes != null) {
            changes = changes + " Password: " + password;
        } else {
            changes = "Password: " + password;
        }
    }
    System.out.println("Changes are: " + changes + "(Y/N)");

    String input=scanner.next();
    while( input.length()==0|| input.length()>1)
    {
        System.out.println("Did not  catch it. Try again");
        input=scanner.next();
    }

   return input;
}*/

    public static String checkZeroLen(String input) {
        String attempt = input;
        Scanner scanner = new Scanner(System.in);
        while (attempt.length() == 0) {
            attempt = scanner.nextLine();
        }
        return attempt;
    }

    /* public static void StringSql( String sql, int id, String update) {
        Connection conn = DatabseHelp.ConnectToDatabase();
        String statement = "Update project_Customer Set FIRST_NAME = ?  where customer_id= ?";
        /*Update project_Customer
Set first_name = 'Jimmy',
   password = '12345678'
   where customer_id= 10000001;
   Set first_name = ?*/
 /*   String sqlstatement = statement;
        System.out.println(statement);
        try {
            PreparedStatement help = conn.prepareCall(sqlstatement);
            System.out.println(sqlstatement);
            help.clearParameters();
            help.setString(1, update);
            help.setInt(2, id);
            int up = help.executeUpdate();
            if (up == 1) {

                System.out.println(id + " was updated ");
            }
            else {
                System.out.println("Oops");
            }
        } catch (SQLException s) {
            s.getMessage();
        }


    }*/
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

}
