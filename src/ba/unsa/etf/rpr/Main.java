package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static String url="jdbc:sqlite:baza.db";
    private static  String upit = "select g.*,d.naziv from grad g,drzava d where g.drzava=d.id;";
    public static void main(String[] args) {

         System.out.println("Gradovi su:\n" + ispisiGradove());

    }
    public static String ispisiGradove() {
        String s="";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(upit);

            while(result.next()) {
                s+=(result.getString(2)+" (" );
                s+=(result.getString(5)+") - ");
                s+=(result.getString(3)+"\n");
            }
        } catch (SQLException e) {
                e.printStackTrace();
            }
        return s;
        }


}
