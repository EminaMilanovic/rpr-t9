package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static String url="jdbc:sqlite:baza.db";
    private static  String upit = "select g.*,d.naziv from grad g,drzava d where g.drzava=d.id;";
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(upit);

            while(result.next()) {
                System.out.print(result.getString(2)+' ');
                System.out.println(result.getString(5));
            }
        }
        // System.out.println("Gradovi su:\n" + ispisiGradove());
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*public static String ispisiGradove() {
        return "";
    }*/
}
