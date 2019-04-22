/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuperUI {

    private static Scanner scan = new Scanner(System.in);

    protected static int showSearch() {
        String input;
        ResultSet res = null;
        clearScreen();
        System.out.println("================= SEARCH FOR A BOOK =================");
        System.out.println("\nSEARCH OPTIONS");
        System.out.println("--------------");
        System.out.println(" 1. Genre");
        System.out.println(" 2. Book Title");
        System.out.println(" 3. Author Name");
        System.out.println(" 4. Category");
        System.out.println(" 5. Book Length");
        System.out.println(" 6. ALL BOOKS");
        System.out.println(" 7. EXIT");
        System.out.print("\nWHAT WOULD YOU LIKE TO SEARCH BY? ");
        input = scan.nextLine().trim();

        switch (input) {
            case "1":
                System.out.print("ENTER GENRE NAME: ");
                input = scan.nextLine().trim();
                res = Oracle.search("BOOK_GENRE", input);

                //display
                break;
            case "2":
                System.out.print("ENTER BOOK TITLE: ");
                input = scan.nextLine().trim();
                res = Oracle.search("TITLE", input);

                //display
                break;
            case "3":
                System.out.print("ENTER AUTHOR NAME: ");
                input = scan.nextLine().trim();
                res = Oracle.searchAuthor(input);

                //display
                break;
            case "4":
                System.out.print("ENTER CATEGORY: ");
                input = scan.nextLine().trim();
                res = Oracle.search("CATEGORY", input);

                //display
                break;
            case "5":
                int min,
                 max;
                System.out.print("ENTER BOOK LENGTH MIN: ");
                min = scan.nextInt();
                System.out.print("ENTER BOOK LENGTH MAX: ");
                max = scan.nextInt();
                res = Oracle.search(min, max);

                //display
                break;
            case "6":
                res = Oracle.searchAll();
                break;
            case "7":
                return 1;
            default:
                showSearch();
                break;

        }

        printSet(res);

        System.out.print("SEARCH AGAIN? (Y/N)");
        input = scan.next();
        boolean flag = true;
        while (flag) {
            switch (input) {
                case "y":
                case "Y":
                    flag = false;
                    showSearch();
                    break;
                case "n":
                case "N":
                    flag = false;
                    Customer.showWelcome();
                    break;
            }
        }

        return 0;
    }

    protected static void printSet(ResultSet res) {
        try {
            System.out.println("\nResults:\n");
            int bookLength, bookYear;
            String bookTitle, bookType, bookSubject, bookGenre, bookOutD, bookInD;
            Long ISBN;
            while (res.next()) {
                bookTitle = res.getString("Title");
                ISBN = res.getLong("ISBN");
                bookLength = res.getInt("booklength");
                bookYear = res.getInt("Year_Published");
                bookType = res.getString("book_Type");
                bookSubject = res.getString("book_subject");
                bookGenre = res.getString("book_genre");

                System.out.println("   " + bookTitle);
                System.out.println("------------------------------");
                Book.getAuthors(ISBN);
                System.out.println("   ISBN: " + ISBN);
                if (bookSubject != null) {
                    System.out.println("   SUBJECT: " + bookSubject);
                } else if (bookGenre != null) {
                    System.out.println("   GENRE: " + bookGenre);
                }
                System.out.println("   YEAR PUBLISHED: " + bookYear);
                System.out.println("   TYPE: " + bookType);
                System.out.println("   LENGTH(# of pages): " + bookLength);
                System.out.println("\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SuperUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected static void printRSet(ResultSet res) {
        try {
            System.out.println("\nResults:\n");
            int bookLength, bookYear;
            String bookTitle, bookType, bookSubject, bookGenre, bookOutD, bookInD, bookAInD = "--/--/---";
            Long ISBN;
            while (res.next()) {
                bookTitle = res.getString("Title");
                ISBN = res.getLong("ISBN");
                bookLength = res.getInt("booklength");
                bookYear = res.getInt("Year_Published");
                bookType = res.getString("book_Type");
                bookSubject = res.getString("book_subject");
                bookGenre = res.getString("book_genre");
                bookOutD = res.getString("date_checked_out");
                bookInD = res.getString("expected_return_date");
                if(res.getString("actual_return_date") != null) bookAInD = res.getString("actual_return_date");

                System.out.println("   " + bookTitle);
                System.out.println("------------------------------");
                if (bookOutD != null && bookInD != null) {
                    System.out.println("   CHECKED OUT DATE: " + bookOutD);
                    System.out.println("   EXPECTED RETURN DATE: " + bookInD);
                    System.out.println("   ACTUAL RETURN DATE: " + bookAInD + "\n");
                }
                Book.getAuthors(ISBN);
                System.out.println("   ISBN: " + ISBN);
                if (bookSubject != null) {
                    System.out.println("   SUBJECT: " + bookSubject);
                } else if (bookGenre != null) {
                    System.out.println("   GENRE: " + bookGenre);
                }
                System.out.println("   YEAR PUBLISHED: " + bookYear);
                System.out.println("   TYPE: " + bookType);
                System.out.println("   LENGTH(# of pages): " + bookLength);
                System.out.println("\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SuperUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
