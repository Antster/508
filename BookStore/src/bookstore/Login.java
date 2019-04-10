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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Arrays;

public class Login {

    public static Connection ConnectToDatabase() {
        Connection conn = null;
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:/xe", "", "");
            Statement stmt = con.createStatement();
            conn = con;
        } catch (SQLException s) {
            System.out.print(s.getMessage() + "\n");
            System.exit(0);
        }
        return conn;

    }


    /*the prompt for the begining*/
    public static int LoginScreen() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===================== BOOKSTORE =====================");
        System.out.println("How are you login in:" + "\n" + "1. Employee " + "\n" + "2. Customer" + "\n" + "3. EXIT");
        System.out.print(" > ");
        int type = scanner.nextInt();
        type = typeValidation(type);
        return type;
    }

    /*Checks users input to see that they put in a valid number from the login screen.*/
    public static int typeValidation(int type) {
        Scanner scanner = new Scanner(System.in);
        int attempt = type;
        while (attempt > 3 || attempt < 1)// Type 4 is supposed to be people who can make employees. Computer decides that, not the user
        {
            System.out.println("Invalid Choice");
            System.out.print(" > ");
            attempt = scanner.nextInt();
        }
        return attempt;

    }

    /* this method takes the type of user and decides which prompt they see It then very the request is in range of the
    prompt and directs the request to the right homepage*/
    public static void request(int type) {

        String array[] = null;
        Scanner scanner = new Scanner(System.in);

        switch (type) {
            case 1:
//                array=Employee.EmployeeChoices();
                break;
            /* min=1;
        max=8;*/
            case 2:
                break;// type 3 is to quit
            case 4:
//                array= HREmployee.employeeHRChoices();
                break;
            default:
                System.out.println("Error! Call tech team");
                break;
        }
        int min = 1;// 0 is the name of the prompt, not a choice
        int max = array.length;
        printPrompt(array);
        int request = scanner.nextInt();
        validRequest(request, min, max);
        //directs to Homepage where the actions are
        if (type == 4) {
//            HREmployee.HRhomePage(request);
        } else if (type == 1) {
//            Employee.employeeHomePage(request);
        }


        /*   choices(type,request);*/
    }

    //verifies that the request is a valid request.
    public static int validRequest(int request, int min, int max) {
        Scanner scanner = new Scanner(System.in);
        while (request > max || request < min) {
            System.out.println("Invalid Choice " + request + " is not an option");
            request = scanner.nextInt();
        }
        return request;
    }

    //prints the correct prompt for the user
    public static void printPrompt(String array[]) {
        String choices[] = array;
        for (int x = 0; x < choices.length; x++) {
            if (x == 0) {
                System.out.println(choices[x]);
            } else {
                System.out.println(x + " " + choices[x]);
            }
        }
    }

    //Contains the login screen and figures out what is the next user
    // made so that multiple people can use the application
    public static void LoginDirector() {
        SuperUI.clearScreen();
        int type = LoginScreen();

        try {

            Connection conn = ConnectToDatabase();
            if (type == 1) {
                SuperUI.clearScreen();
                Employee.EmployeeLogin();
//                if(employeePosition.equalsIgnoreCase("HR Manager"))
//                {
//                    type=4;
//                }
            } else if (type == 2) {
                Customer.CustomerLogin();
            }
            if (type == 3) {
                System.out.println("Goodbye");
                conn.close();
            }

        } catch (SQLException excpt) {
            System.out.println(excpt.getMessage());

        }

    }

}
