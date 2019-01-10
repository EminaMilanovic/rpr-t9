package ba.unsa.etf.rpr;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

        String kreiranjeGrada=("create table grad( id int primary key, naziv varchar, broj_stanovnika int)");
        String kreiranjeDrzave=("create table drzava(id int primary key,  naziv varchar, glavni_grad int references grad(id));");
        String alter=("alter table  grad  add column drzava int references drzava(id);");
        String insert1=("insert into grad values(1,'London',8825000,1);");
        String insert2=("insert into drzava values(1,'Velika Britanija',1);");
        String insert3=("insert into grad values (2,'Pariz',2206488,2);");
        String insert4=("insert into drzava values (2,'Francuska',2);");
        String insert5=("insert into grad values (3,'Beč',1899055,3);");
        String insert6=("insert into drzava values (3,'Austrija',3);");
        String insert7=("insert into grad values (4,'Manchester',545500,1);");
        String insert8=("insert into grad values (5,'Graz',280200,3);");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(kreiranjeGrada);
        stmt.executeUpdate(kreiranjeDrzave);
        stmt.executeUpdate(alter);
        stmt.executeUpdate(insert1);
        stmt.executeUpdate(insert2);
        stmt.executeUpdate(insert3);
        stmt.executeUpdate(insert4);
        stmt.executeUpdate(insert5);
        stmt.executeUpdate(insert6);
        stmt.executeUpdate(insert7);
        stmt.executeUpdate(insert8);
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
            id=result.getInt(5);
            naziv=result.getString(6);
            Drzava drzava=new Drzava(id,naziv,grad);
            grad.setDrzava(drzava);
            gradovi.add(grad);


        }
        Collections.sort(gradovi);
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
            naziv=result.getString(6);
            Drzava dr=new Drzava(id,naziv,grad);
            grad.setDrzava(dr);
            return grad;
    }

    public void obrisiDrzavu(String naziv) throws SQLException {
        Drzava d2= nadjiDrzavu(naziv);
        if(d2!=null) {
            PreparedStatement stmt = conn.prepareStatement("delete from drzava where naziv like ?;");

            stmt.setString(1, naziv);
            stmt.executeUpdate();
        }
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

    public void dodajGrad(Grad grad) throws SQLException {
        int id=1;
        Statement st=conn.createStatement();
        ResultSet result=st.executeQuery("select count(*) from grad;");
        if(result.next()) id=result.getInt(1);
        id++;
        PreparedStatement stmt=conn.prepareStatement("insert into grad values(?,?,?,?);");
        stmt.setInt(1,id);
        stmt.setString(2,grad.getNaziv());
        stmt.setInt(3,grad.getBrojStanovnika());
        stmt.setInt(4,grad.getDrzava().getId());
        stmt.executeUpdate();
    }

    public void dodajDrzavu(Drzava drzava) throws SQLException {
        int id1=1;
        Statement st=conn.createStatement();
        ResultSet result=st.executeQuery("select count(*) from grad;");
        if(result.next()) id1=result.getInt(1);
        id1++;
        PreparedStatement stmt=conn.prepareStatement("insert into grad values(?,?,?,?)");
        stmt.setInt(1,id1);
        stmt.setString(2,drzava.getGlavniGrad().getNaziv());
        stmt.setInt(3,drzava.getGlavniGrad().getBrojStanovnika());
        Statement st2=conn.createStatement();
        ResultSet result2=st2.executeQuery("select count(*) from drzava;");
        int id2=1;
        if(result2.next()) id2=result2.getInt(1);
        id2++;
        stmt.setInt(4,id2);
        stmt.executeUpdate();
        PreparedStatement ps=conn.prepareStatement("insert into drzava values(?,?,?)");
        ps.setInt(1,id2);
        ps.setString(2,drzava.getNaziv());
        ps.setInt(3,id1);
        ps.executeUpdate();

    }

    public void izmijeniGrad(Grad grad) throws SQLException {
        PreparedStatement ps=conn.prepareStatement("update grad set naziv=? where id= ?;");
        ps.setString(1,grad.getNaziv());
        ps.setInt(2,grad.getId());
        ps.executeUpdate();



    }
}
