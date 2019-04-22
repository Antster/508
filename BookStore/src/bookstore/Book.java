/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Book {

    public static void BookPrompt()
    {
        Scanner scanner = new Scanner(System.in);
        long bookISBN=0;
        String bookTitle=null;
        int bookLength=0;
        int bookYear=0;
        String bookType=null;
        int bookAnswer=0;
        String bookSubject=null;
        String bookGenre= null;


        bookISBN=ISBNvalidation();
        bookTitle=TitleValidation();
        bookLength= bookLengthValidation();
        bookYear= bookYearValidation();

        System.out.println("Is this book 1. Fiction or 2. NonFiction ?");
       // bookAnswer=scanner.nextInt();
        bookAnswer= Miss.checkRange(1,2);
        String confirmation= "Book ISBN: " + bookISBN + " Title: " + bookTitle + " Length: " + bookLength + " Year: " + bookYear + " Type: ";
        if(bookAnswer==1)
        {
            bookType="Fiction";
            bookGenre=bookGenreValidation();
            confirmation= confirmation + bookType +" Genre: " + bookGenre;
        }
        else{
            bookType="NonFiction";
           bookSubject=bookSubjectValidation();
            confirmation = confirmation + bookType +" Subject: " + bookSubject;
        }
        System.out.println( "Confirm "+ confirmation+ " (Y/N) ?");
        String confirm= scanner.next();
        confirm=Miss.verifyYorNAnswer(confirm);
        if(confirm.equalsIgnoreCase("y"))
        {
            MakeABook(bookISBN,bookTitle,bookLength,bookYear,bookType,bookSubject,bookGenre);
           Employee.showWelcome();
        }
    }


    public static void addBook() {
        System.out.println("Would you like to add a book (Y/N)");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        answer = Miss.verifyYorNAnswer(answer);
        if (answer.equalsIgnoreCase("y")) {
            BookPrompt();


        }
    }

    public static  long  ISBNvalidation() {
        long bookISBN=0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the ISBN?");
        bookISBN = scanner.nextLong();
        while (bookISBN < 1000000000000L) {
            System.out.println("ISBN is too short. Try Again");
            bookISBN = scanner.nextLong();
        }

        return bookISBN;
    }


    public static String TitleValidation()
    { String bookTitle=null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the title?");
            bookTitle = scanner.nextLine();
            bookTitle = Miss.checkZeroLen(bookTitle);


        return bookTitle;

    }

    public static int bookLengthValidation () {
        int bookLength=0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many pages are in the book?");
        bookLength = scanner.nextInt();
        while (bookLength == 0) {
            bookLength = scanner.nextInt();
        }

        return  bookLength;
    }

    public static int bookYearValidation()
    {
        int bookYear=0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("What year was it written?");
        bookYear=scanner.nextInt();
        while (bookYear<1 && bookYear> 9999)
        {
            System.out.println("Please give a valid date");
            bookYear=scanner.nextInt();
        }

        return  bookYear;
    }

    public static  String bookGenreValidation ()
    {
        String bookGenre=null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("What Genre is the book?"+ "\n"+ "1. Fantasy" + "\n"+
                "2. Historical Fiction" + "\n"+ "3. Horror"+ "\n"+ "4.Mystery "+ "\n"+ "5.Romance "+ "\n" + "6. Science-fiction");
        //int genre_choice=scanner.nextInt();
        int genre_choice=Miss.checkRange(1,6);
        if (genre_choice==1)
            bookGenre= "Fantasy";
        if (genre_choice==2)
            bookGenre= "Historical Fiction";
        if(genre_choice==3)
            bookGenre= "Horror";
        if(genre_choice==4)
            bookGenre= "Mystery";
        if(genre_choice==5)
            bookGenre="Romance";
        if (genre_choice==6)
            bookGenre="Science-fiction";
        return bookGenre;
    }

    private static void MakeABook(long ISBN, String title, int bookLegth, int year, String type, String subject, String genre)
    {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "Insert into project_book VALUES (?,?,?,?,?,?,?)";
            PreparedStatement createBook = conn.prepareStatement(sql);
            createBook.clearParameters();
            createBook.setLong(1, ISBN);
            createBook.setString(2, title);
            createBook.setInt(3,bookLegth );
            createBook.setInt(4,year );
            createBook.setString(5,type);
            createBook.setString(6, subject);
            createBook.setString(7,genre);
            int createNewBook = createBook.executeUpdate();

            if (createNewBook == 1) {

                System.out.println("Book  " + title + " was created! " );
                authorsPrompt(ISBN);
            }
        } catch (SQLException s) {

            System.out.println( "Book was not created. Try Again.");
            System.out.println(s.getMessage());

            addBook();
        }
    }

    private static void authorsPrompt(long ISBN)
    {
        Scanner scanner = new Scanner(System.in);
        boolean done= false;
        System.out.println("How many authors are there?");
        int numberOfAuthors=scanner.nextInt();
        while (numberOfAuthors==0)
        {
            numberOfAuthors=scanner.nextInt();
        }
        for(int index=1; index<=numberOfAuthors;index++)
        {
            System.out.println("What is one of the authors name");
            String author= scanner.nextLine();
            author= Miss.checkZeroLen(author);
            author(ISBN, author);


        }

    }

    private static void author(long ISBN, String authorName)
    {
        Connection conn= Login.ConnectToDatabase();
        try {
            String sql = "Insert into project_author VALUES (?,?)";
            PreparedStatement createBook = conn.prepareStatement(sql);
            createBook.clearParameters();
            createBook.setLong(1, ISBN);
            createBook.setString(2, authorName);

            int createNewBook = createBook.executeUpdate();


        } catch (SQLException s) {

            System.out.println( "Author was not added. Try Again.");
            System.out.println(s.getMessage());

            addBook();
        }
    }

    public static String bookSubjectValidation()
    {
        String bookSubject=null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the subject?");
        bookSubject = scanner.nextLine();
        bookSubject = Miss.checkZeroLen(bookSubject);
        scanner.close();
        return bookSubject;
    }

    public static  void UpdateBookPrompt()
    { Scanner scanner= new Scanner(System.in);
        System.out.println("Would you like to update a book?");
        String input=scanner.next();
        input=Miss.verifyYorNAnswer(input);
        if (input.equalsIgnoreCase("y")) {
            long bookISBN = selectBook();
            BookUpdateQuestions(bookISBN);
        }
        Employee.showWelcome();
    }

    public static void BookUpdateQuestions(long bookISBN)
    {Scanner scanner= new Scanner(System.in);
        String input;

       System.out.println("Do you want to update the title (Y/N)");
       input=scanner.next();
       input=Miss.verifyYorNAnswer(input);
       if(input.equalsIgnoreCase("y"))
       {
        String title= TitleValidation();
        UpdateBookTitle(bookISBN, title);
        }
        System.out.println("Do you want to update the length (Y/N)");
        input=scanner.next();
        input=Miss.verifyYorNAnswer(input);
        if(input.equalsIgnoreCase("y"))
        {
            int length=bookLengthValidation();
            UpdateBookLength(bookISBN,length);
        }
        System.out.println("Do you want to update the year (Y/N)");
        input=scanner.next();
        input=Miss.verifyYorNAnswer(input);
        if(input.equalsIgnoreCase("y"))
        {
            int year=bookYearValidation();
            UpdateBookYear(bookISBN,year);
        }
        System.out.println("Do you want to update the Genre (Y/N)");
        input=scanner.next();
        input=Miss.verifyYorNAnswer(input);
        if(input.equalsIgnoreCase("y"))
        {
            String genre= bookGenreValidation();
            UpdateBookGenre(bookISBN, genre);
        }




    }

    private static long selectBook()
    {
        String bookTitle=null;
        int bookLength=0;
        int bookYear=0;
        String bookType=null;
        String bookSubject=null;
        String bookGenre= null;


        Scanner scanner= new Scanner(System.in);
        long bookISBN= ISBNvalidation();
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "Select * from project_book where ISBN=?";
            PreparedStatement info = conn.prepareCall(sql);
            info.clearParameters();
            info.setLong(1, bookISBN);
            ResultSet bookInfo = info.executeQuery();

            while (bookInfo.next()) {
                bookTitle = bookInfo.getString("Title");
                bookLength = bookInfo.getInt("booklength");
                bookYear = bookInfo.getInt("Year_Published");
                bookType= bookInfo.getString("book_Type");
                bookSubject= bookInfo.getString("book_subject");
                bookGenre= bookInfo.getString("book_genre");
                System.out.print(bookTitle +" "+ bookLength + " "+ bookYear + " " + bookType );
                if(bookSubject!=null)
                {
                    System.out.println( " "+ bookSubject);
                }
                if (bookGenre != null)
                    System.out.println( " " + bookGenre);
            }

            getAuthors(bookISBN);
            System.out.println("Is this the right book (Y/N)");
            String answer= scanner.next();
            answer= Miss.verifyYorNAnswer(answer);
            if(answer.equalsIgnoreCase("n"))
                Employee.showWelcome();

        }
         catch(SQLException e)
            {
               System.out.println(e.getMessage());
               Employee.showWelcome();
            }
        return bookISBN;
    }

    private static  void UpdateBookTitle(Long ISBN, String update)
    {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_book SET title = ? where ISBN = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setString(1, update);
            statement.setLong(2,ISBN);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static  void UpdateBookYear(Long ISBN, int update)
    {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_book SET Year_Published = ? where ISBN = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setInt(1, update);
            statement.setLong(2,ISBN);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }
    private static  void UpdateBookLength(Long ISBN, int update) {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_book SET booklength = ? where ISBN = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setInt(1, update);
            statement.setLong(2, ISBN);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static  void UpdateBookType(Long ISBN, String update )
    {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_book SET book_Type= ?  where ISBN = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setString(1, update);
            statement.setLong(2,ISBN);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static  void UpdateBookSubject(Long ISBN, String update)
    {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_book SET book_Subject= ?  where ISBN = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setString(1, update);
            statement.setLong(2,ISBN);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static  void UpdateBookGenre(Long ISBN, String update)
    {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "UPDATE project_book SET book_Genre= ?  where ISBN = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.clearParameters();
            statement.setString(1, update);
            statement.setLong(2,ISBN);
            int updatedName = statement.executeUpdate();
            if (updatedName == 1) {
                System.out.println(update + " was updated!");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }
    protected static void getAuthors(long bookISBN) {
        Connection conn= Login.ConnectToDatabase();
        try {
            ArrayList<String> authors = new ArrayList<String>();
            String authorSql = " select a.author_full_name from project_book b join project_author a on b.ISBN= a.ISBN where b.ISBN= ?";
            PreparedStatement author = conn.prepareCall(authorSql);
            author.clearParameters();
            author.setLong(1, bookISBN);
            ResultSet authorInfo = author.executeQuery();
            while (authorInfo.next()) {
                authors.add(authorInfo.getString("author_full_name"));

            }
            
            System.out.println("   Author(s): ");
            for(String s : authors){
                System.out.println("\t- " + s);
            }
            
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public static void deleteBook()
    { Scanner scanner= new Scanner(System.in);
        System.out.println("Would you like to delete a book?");
        String input=scanner.next();
        input=Miss.verifyYorNAnswer(input);
        if (input.equalsIgnoreCase("y")) {
            long bookISBN = selectBook();
            deleteBookStatement(bookISBN);

        }
        Employee.showWelcome();
    }

    private static void deleteBookStatement(long bookISBN)
    {
        Connection conn = Login.ConnectToDatabase();
        try {
            String sql = "delete from project_book where ISBN = ?";
            PreparedStatement createEmployee = conn.prepareCall(sql);
            createEmployee.clearParameters();

            createEmployee.setLong(1, bookISBN);

            int createNewEmployee = createEmployee.executeUpdate();
            if (createNewEmployee == 1) {
                System.out.println(bookISBN+ " has been deleted");
                Login.request(1);

            } else {
                System.out.println("Error: Something went wrong. Try again");

            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());

        }
    }
}
