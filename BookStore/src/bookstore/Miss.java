/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import java.util.IllegalFormatException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Miss {


    public static String checkYorN(String answer)
    {
        Scanner scanner = new Scanner(System.in);

        try {
        while (!answer.equalsIgnoreCase("y")&& !answer.equalsIgnoreCase("n") ) {
            System.out.println("Only options are Y or N. Please try again ");
            answer = scanner.next();
        }
        }
        catch(IllegalFormatException e)
        {
            System.out.println("Only accept the letters 'Y' or 'N'. Try again. ");
            answer=scanner.next();
            verifyYorNAnswer(answer);
        }
        return answer;
    }

    public static String checkZeroLen(String input) {
        String attempt = input;
        Scanner scanner = new Scanner(System.in);
        while (attempt.length() == 0) {
            attempt = scanner.nextLine();
        }
        return attempt;
    }



    public static String verifyYorNAnswer(String request)
    {String answer = request;


            answer = checkZeroLen(answer);
            answer = checkYorN(answer);

        return answer;
    }
/*while (bookLength==0)
        {
            bookLength=scanner.nextInt();
        }*/

public static int checkRange( int min, int max)
{Scanner scanner= new Scanner(System.in);
   int number =-1;
    try{
       number=checkNumberInput();

    while (number <min && number > max)
       {
        System.out.println("Out of Range. Try again.");
        number= scanner.nextInt();
        }
    }
    catch (InputMismatchException e)
    {
        System.out.println(e.getMessage());
        System.out.println("Try Again");
    }

    return number;
}

private static int checkNumberInput()
{
    boolean rightType= true;
    Scanner scanner= new Scanner(System.in);
    int number=-1;
    try{
        number = scanner.nextInt();
}
    catch (InputMismatchException e)
    {   rightType=false;

            System.out.println(e.getMessage());
            System.out.println("Try Again");
            {
                checkNumberInput();
            }


    }
    return  number;
}
}
