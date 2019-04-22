/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anthony
 */
public class Oracle {

    // CONNECT TO DATABASE AND QUERY
    public Oracle() {

    }

    protected static ResultSet search(String type, String input) {
        try {
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT * FROM project_book WHERE " + type + " = ?";
            PreparedStatement search = conn.prepareStatement(sql);

            search.clearParameters();

            search.setString(1, input);

            return search.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected static ResultSet searchAll() {
        try {
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT * FROM project_book";
            PreparedStatement search = conn.prepareStatement(sql);

            search.clearParameters();

            return search.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected static ResultSet searchAuthor(String input) {
        try {
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT * FROM project_book b natural join project_author a WHERE a.author_full_name like ?";
            PreparedStatement search = conn.prepareStatement(sql);

            search.clearParameters();

            search.setString(1, input);

            return search.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected static ResultSet search(int min, int max) {
        try {
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT * FROM project_book WHERE  BOOKLENGTH > ? and BOOKLENGTH < ?";
            PreparedStatement search = conn.prepareStatement(sql);
            search.clearParameters();

            search.setInt(1, min);
            search.setInt(2, max);

            return search.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected static ResultSet showCheckedOutBooks(String id) {
        try {
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT * FROM project_cart natural join project_book WHERE ACTUAL_RETURN_DATE is NULL and CUSTOMER_ID = ?";
            PreparedStatement search = conn.prepareStatement(sql);
            search.clearParameters();

            search.setString(1, id);
            
            return search.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    protected static void checkOutABook(int type, String input, String id) {
        try {
            ResultSet res;
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT 1 FROM project_cart WHERE ACTUAL_RETURN_DATE is null and ISBN = ?";

            PreparedStatement search = conn.prepareStatement(sql);
            search.clearParameters();

            if (type == 1) {
                search.setLong(1, Long.parseLong(getISBNByTitle(input)));
            } else {
                search.setLong(1, Long.parseLong(input));
            }
            res = search.executeQuery();

            if (res.next()) {
                System.out.println("\n===> Book: " + input + " is already checked out. Try again later or choose anthoer book. <===\n");
            } else {
                sql = "INSERT INTO project_cart (ISBN, CUSTOMER_ID, DATE_CHECKED_OUT, EXPECTED_RETURN_DATE, ACTUAL_RETURN_DATE) VALUES (?, ?, ?, ?, null)";
                search = conn.prepareStatement(sql);
                search.clearParameters();
                if (type == 1) {
                    search.setLong(1, Long.parseLong(getISBNByTitle(input)));
                } else {
                   search.setLong(1, Long.parseLong(input));
                }

                search.setString(2, id);
                search.setDate(3, getCurrentDate());
                search.setDate(4, getNextWeekDate());
                
                search.executeQuery();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static void returnABook(int type, String input, String id) {
        try {
            ResultSet res;
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT 1 FROM project_cart WHERE ACTUAL_RETURN_DATE is null and CUSTOMER_ID = ? and ISBN = ?";

            PreparedStatement search = conn.prepareStatement(sql);
            search.clearParameters();

            search.setString(1, id);
            if (type == 1) {
                search.setLong(2, Long.parseLong(getISBNByTitle(input)));
            } else {
                search.setLong(2, Long.parseLong(input));
            }
            res = search.executeQuery();

            if (res.next()) {
                sql = "UPDATE project_cart SET ACTUAL_RETURN_DATE = ? WHERE CUSTOMER_ID = ? and ISBN = ?";
                
                search = conn.prepareStatement(sql);
                search.clearParameters();

                search.setDate(1, getCurrentDate());
                search.setString(2, id);
                if (type == 1) {
                    search.setLong(3, Long.parseLong(getISBNByTitle(input)));
                } else {
                    search.setLong(3, Long.parseLong(input));
                }
                
                search.executeQuery();

            } else {
                System.out.println("\n===> Book: " + input + " is not checked out by you. <===\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static java.sql.Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return new java.sql.Date(cal.getTimeInMillis());
    }

    private static java.sql.Date getNextWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    private static String getTitleByISBN(String ISBN) {
        try {
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT bookname FROM project_book WHERE ISBN = ?";
            PreparedStatement search = conn.prepareStatement(sql);
            search.clearParameters();
            search.setString(1, ISBN);
            ResultSet res = search.executeQuery();
            if (res.next()) {
                return res.getString("TITLE").trim();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private static String getISBNByTitle(String title) {
        try {
            Connection conn = Login.ConnectToDatabase();
            String sql = "SELECT ISBN FROM project_book WHERE TITLE = ?";
            PreparedStatement search = conn.prepareStatement(sql);
            search.clearParameters();
            search.setString(1, title);
            ResultSet res = search.executeQuery();
            if (res.next()) {
                return res.getString("ISBN").trim();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
