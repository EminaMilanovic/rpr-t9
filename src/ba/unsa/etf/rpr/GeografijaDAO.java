package ba.unsa.etf.rpr;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {

    private static GeografijaDAO  instance=null;
    private static Connection conn ;

    private GeografijaDAO() throws SQLException {
        File db = new File("baza.db");
        boolean ima= db.exists();
        String url="jdbc:sqlite:baza.db";
        conn =DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        if(!ima) initializeDatabase();
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
        Grad paris = new Grad(1, "Pariz", 2206488, null);
        Grad london = new Grad(2, "London", 8825000, null);
        Grad betsch = new Grad(3, "Beč", 1899055, null);
        Grad manch = new Grad(5, "Manchester", 545500, null);
        Grad graz = new Grad(6, "Graz", 280200, null);

        Drzava fran = new Drzava(1, "Francuska", paris);
        Drzava vBrit = new Drzava(2, "Velika Britanija", london);
        Drzava aut = new Drzava(3, "Austrija", betsch);

        paris.setDrzava(fran);
        london.setDrzava(vBrit);
        betsch.setDrzava(aut);
        manch.setDrzava(vBrit);
        graz.setDrzava(aut);

        dodajGrad(paris);
        dodajGrad(london);
        dodajGrad(betsch);
        dodajGrad(manch);
        dodajGrad(graz);
        dodajDrzavu(fran);
        dodajDrzavu(vBrit);
        dodajDrzavu(aut);
    }
    public static void removeInstance() {
        instance=null;
        if(conn!=null) conn.close();
    }

    public static GeografijaDAO getInstance() throws SQLException {
        if(instance==null) {
            initialize();
        }
        return instance;
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> gradovi=new ArrayList<>();
        ArrayList<Drzava> drzave=new ArrayList<>();
        String upit_gradovi="";
        stmt.
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
