/*
    UI
 */
package bookstore;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Anthony
 */
public class BookStore {

    public static Scanner scan = new Scanner(System.in);
    private static int customer_ID;
    
    public static void main(String[] args) {
        clearScreen();
        showLogin();

    }

    private static void showLogin() {
        String username, password;
        System.out.println("======================= LOGIN =======================");
        System.out.print("\n\tUsername: ");
        username = scan.next().trim();
        System.out.print("\tPassword: ");
        password = scan.next().trim();

        if (!attemptLogin(username, password)) {
            showLogin();
        } else {
            showWelcome();
        }
    }

    private static boolean attemptLogin(String username, String password) {
        //do login to database. return customer_id and set global variable
        return true;
    }

    private static void showWelcome() {
        clearScreen();
        String input;
        System.out.println("====================== WELCOME ======================");
        System.out.println("\nOPTIONS");
        System.out.println("-------");
        System.out.println(" 1. Search for a book");
        System.out.println(" 2. View checked out books");
        System.out.println(" 3. Check out a book");
        System.out.println(" 4. Reserve a book");
        System.out.print("\nWHAT WOULD YOU LIKE TO DO? ");
        input = scan.nextLine().trim();

        switch (input) {
            case "1":
                showSearch();
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
            default:
                showWelcome();
                break;
        }
    }

    private static void showSearch() {
        String input;
        clearScreen();
        System.out.println("================= SEARCH FOR A BOOK =================");
        System.out.println("\nSEARCH OPTIONS");
        System.out.println("--------------");
        System.out.println(" 1. Genre");
        System.out.println(" 2. Book Name");
        System.out.println(" 3. Author Name");
        System.out.println(" 4. Category");
        System.out.println(" 5. Book Length");
        System.out.println(" 6. EXIT");
        System.out.print("\nWHAT WOULD YOU LIKE TO SEARCH BY? "); input = scan.nextLine().trim();
        
        switch(input){
            case "1":
                System.out.print("ENTER GENRE NAME:"); input = scan.nextLine().trim();
                //Link to database to search where input - genre
                //display
                break;
            case "2":
                System.out.print("ENTER BOOK NAME:"); input = scan.nextLine().trim();
                //Link to database to search where input - book name
                //display
                break;
            case "3":
                System.out.print("ENTER AUTHOR NAME:"); input = scan.nextLine().trim();
                //Link to database to search where input - author
                //display
                break;
            case "4":
                System.out.print("ENTER CATEGORY:"); input = scan.nextLine().trim();
                //Link to database to search where input - category
                //display
                break;
            case "5":
                int min, max;
                System.out.print("ENTER BOOK LENGTH MIN:"); min = scan.nextInt();
                System.out.print("ENTER BOOK LENGTH MAX:"); max = scan.nextInt();
                //Link to database to search where length > min and length < max
                //display
                break;
            case "6":
                showWelcome();
                break;
            default:
                showSearch();
                break;
                
        }
    }

    private static void showCheckedOutBooks() {
        clearScreen();
        System.out.println("============== YOUR CHECKED OUT BOOKS ===============");
    }

    private static void showCheckOutABook() {
        String input;
        clearScreen();
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
        clearScreen();
        System.out.println("=================== RESERVE A BOOK ==================");
    }

    private static void clearScreen() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
}
