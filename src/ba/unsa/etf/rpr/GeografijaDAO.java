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
                "values (5,'Graz',280200,3);\ncreate table grad(\n" +
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
        ResultSet result=stmt.executeQuery(kreiranjeBaze);

    }
    public static void removeInstance() throws SQLException {
        instance=null;
        if(conn!=null) conn.close();
    }

    public static GeografijaDAO getInstance() throws SQLException {
        if(instance==null) {
            initialize();
        }
        return instance;
    }

    public ArrayList<Grad> gradovi() throws SQLException {
        ArrayList<Grad> gradovi=new ArrayList<>();
        String upit_gradovi="select g.*,d.* from grad g, drzava d where  g.drzava=d.id;";
        Statement gr=conn.createStatement();
        ResultSet result=gr.executeQuery(upit_gradovi);
        while(result.next()){
            int id=result.getInt(1);
            String naziv=result.getString(2);
            int broj_stanovnika=result.getInt(3);
            Grad grad=new Grad(id,naziv,broj_stanovnika,null);
            id=result.getInt(4);
            naziv=result.getString(5);
            Drzava drzava=new Drzava(id,naziv,grad);
            grad.setDrzava(drzava);
            gradovi.add(grad);


        }
        return gradovi;

    }

    public Grad glavniGrad(String drzava) throws SQLException {
        PreparedStatement stmt=conn.prepareStatement("select g.*,d.* from grad g,drzava d where g.id=d.glavni_grad and d.naziv = ?;");
        stmt.setString(1,drzava);
        ResultSet result=stmt.executeQuery();
        if(!result.next()) return null;

            int id=result.getInt(1);
            String naziv=result.getString(2);
            int broj_stanovnika=result.getInt(3);
            Grad grad=new Grad(id,naziv,broj_stanovnika,null);
            id=result.getInt(4);
            naziv=result.getString(5);
            Drzava dr=new Drzava(id,naziv,grad);
            grad.setDrzava(dr);
            return grad;
    }

    public void obrisiDrzavu(String naziv) throws SQLException {
        Drzava d2= new Drzava(nadjiDrzavu(naziv));
        if(d2==null) return;
        PreparedStatement stmt=conn.prepareStatement("delete from drzava where naziv like ?;");

        stmt.setString(1,naziv);
        ResultSet result=stmt.executeQuery();
    }

    public Drzava nadjiDrzavu(String naziv) throws SQLException {
        PreparedStatement stmt=conn.prepareStatement("select d.*,g.* from drzava d,grad g where g.id=d.glavni_grad and d.naziv like ?;");
        stmt.setString(1,naziv);
        ResultSet result=stmt.executeQuery();
        if(!result.next()) return null;
        int id=result.getInt(1);
        Drzava drzava=new Drzava(id,naziv,null);
        id=result.getInt(4);
        String grad_naziv=result.getString(5);
        int broj_stanovnika=result.getInt(6);
        drzava.setGlavniGrad(new Grad(id,grad_naziv,broj_stanovnika,drzava));
        return drzava;

    }

    public void dodajGrad(Grad grad) {
    }

    public void dodajDrzavu(Drzava bih) {
    }

    public void izmijeniGrad(Grad bech) {

    }
}
