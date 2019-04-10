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

public class Customer {

    public static Scanner scan = new Scanner(System.in);
    /*The Login displayed for Employees*/
    public static void CustomerLogin() {
        System.out.println("======================= LOGIN =======================");
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\tCustomer ID #");
        System.out.print(" > ");
        String employee_id = scanner.next().trim();
        if (!checkID(employee_id)) {
            CustomerLogin();
        } else {
            System.out.println("\n\tPassword:");
            System.out.print(" > ");
            String password = scanner.next();
            if(password.equals("/EXIT")) Login.LoginDirector();
            while (!checkPassword(employee_id, password)) {
                System.out.println("\n\tPassword:");
                System.out.print(" > ");
                password = scanner.next();
            }
            showWelcome();

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
            String employeeInfo = "Select password From project_customer where customer_id = ?";
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
            CustomerLogin();
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
            String employeeInfo = "Select customer_id From project_customer where customer_id = ?";
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
            CustomerLogin();
        }

        return true;
    }
    
    
    
    
    
    private static void showWelcome() {
        SuperUI.clearScreen();
        String input;
        System.out.println("====================== WELCOME ======================");
        System.out.println("\nOPTIONS");
        System.out.println("-------");
        System.out.println(" 1. Search for a book");
        System.out.println(" 2. View checked out books");
        System.out.println(" 3. Check out a book");
        System.out.println(" 4. Reserve a book");
        System.out.println(" 5. LOGOUT");
        System.out.print("\nWHAT WOULD YOU LIKE TO DO? ");
        input = scan.nextLine().trim();

        switch (input) {
            case "1":
                if(SuperUI.showSearch() == 1) showWelcome();
                break;
            case "2":
                showCheckedOutBooks();
                break;
            case "3":
                showCheckOutABook();
                break;
            case "4":
                showReserveABook();
                break;
            case "5":
                Login.LoginDirector();
                break;
            default:
                showWelcome();
                break;
        }
    }

    private static void showCheckedOutBooks() {
        SuperUI.clearScreen();
        System.out.println("============== YOUR CHECKED OUT BOOKS ===============");
    }

    private static void showCheckOutABook() {
        String input;
        SuperUI.clearScreen();
        System.out.println("================== CHECK OUT A BOOK =================");
        System.out.println("\nOPTIONS");
        System.out.println("--------");
        System.out.println(" 1. Book Name");
        System.out.println(" 2. ISBN");
        System.out.print("\n CHOOSE AN OPTION: "); input = scan.nextLine().trim();
        
        switch(input){
            case "1":
            case "Book Name":
                //search by name
                break;
            case "2":
            case "ISBN":
                //search ISBN
        }
    }

    private static void showReserveABook() {
        SuperUI.clearScreen();
        System.out.println("=================== RESERVE A BOOK ==================");
    }

    
}
