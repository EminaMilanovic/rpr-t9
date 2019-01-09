package ba.unsa.etf.rpr;

public class Grad implements Comparable <Grad>{
    private int id;
    private String naziv;
    private int brojStanovnika;
    private Drzava drzava;

    public Grad(){}
    public Grad(int id, String naziv, int broj_stanovnika, Drzava drzava) {

        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = broj_stanovnika;
        this.drzava = drzava;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava = drzava;
    }


    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int broj_stanovnika) {
        this.brojStanovnika = broj_stanovnika;
    }


    @Override
    public int compareTo(Grad o) {
        return  (o.brojStanovnika-this.brojStanovnika) ;

    }
}
