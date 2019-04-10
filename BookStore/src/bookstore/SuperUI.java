/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import java.util.Scanner;

/**
 *
 * @author Anthony
 */
public class SuperUI {
    
    private static Scanner scan = new Scanner(System.in);
    
    protected static int showSearch() {
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
                Oracle.search(input);

                //display
                break;
            case "2":
                System.out.print("ENTER BOOK NAME:"); input = scan.nextLine().trim();
                Oracle.search(input);

                //display
                break;
            case "3":
                System.out.print("ENTER AUTHOR NAME:"); input = scan.nextLine().trim();
                Oracle.search(input);
        
                //display
                break;
            case "4":
                System.out.print("ENTER CATEGORY:"); input = scan.nextLine().trim();
                Oracle.search(input);
  
                //display
                break;
            case "5":
                int min, max;
                System.out.print("ENTER BOOK LENGTH MIN:"); min = scan.nextInt();
                System.out.print("ENTER BOOK LENGTH MAX:"); max = scan.nextInt();
                Oracle.search(min, max);

                //display
                break;
            case "6":
                return 1;
            default:
                showSearch();
                break;
                
        }
        return 0;
    }
    
    protected static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
