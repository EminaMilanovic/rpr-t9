package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO  instance=null;
    private Connection conn ;
    private GeografijaDAO() throws SQLException {
        String url="jdbc:sqlite:baza.db";
        Connection conn =DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
    }
    private static void initialize() throws SQLException {
        if(instance==null) {
            instance=new GeografijaDAO();
        }

    }
    private void initializeDatabase() throws SQLException {
        String kreiranjeBaze="create table grad(\n" +
                "                   id int primary key,\n" +
                "                   naziv varchar,\n" +
                "                   broj_stanovnika int\n" +
                ");\n" +
                "create table drzava(\n" +
                "                     id int primary key,\n" +
                "                     naziv varchar,\n" +
                "                     glavni_grad int references grad(id)\n" +
                ");\n" +
                "alter table  grad\n" +
                "  add column drzava int references drzava(id);\n" +
                "/*\n" +
                "        String expected = \"London (Velika Britanija) - 8825000\\n\" +\n" +
                "                \"Pariz (Francuska) - 2206488\\n\" +\n" +
                "                \"Beč (Austrija) - 1899055\\n\" +\n" +
                "                \"Manchester (Velika Britanija) - 545500\\n\" +\n" +
                "                \"Graz (Austrija) - 280200\\n\n" +
                "*/\n" +
                "insert into grad\n" +
                "values(1,'London',8825000,1);\n" +
                "\n" +
                "insert into drzava\n" +
                "values(1,'Velika Britanija',1);\n" +
                "\n" +
                "insert into grad\n" +
                "values (2,'Pariz',2206488,2);\n" +
                "\n" +
                "insert into drzava\n" +
                "values (2,'Francuska',2);\n" +
                "\n" +
                "insert into grad\n" +
                "values (3,'Beč',1899055,3);\n" +
                "\n" +
                "insert into drzava\n" +
                "values (3,'Austrija',3);\n" +
                "\n" +
                "insert into grad\n" +
                "values (4,'Manchester',545500,1);\n" +
                "\n" +
                "insert into grad\n" +
                "values (5,'Graz',280200,3);\n";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(kreiranjeBaze);
    }
    public static void removeInstance() {
        instance=null;
    }

    public static GeografijaDAO getInstance() throws SQLException {
        if(instance==null) {
            initialize();
        }
        return instance;
    }

    public ArrayList<Grad> gradovi() {
    }

    public Grad glavniGrad(String bosna_i_hercegovina) {
    }

    public void obrisiDrzavu(String kina) {
    }

    public Drzava nadjiDrzavu(String francuska) {
    }

    public void dodajGrad(Grad grad) {
    }

    public void dodajDrzavu(Drzava bih) {
    }

    public void izmijeniGrad(Grad bech) {

    }
}
