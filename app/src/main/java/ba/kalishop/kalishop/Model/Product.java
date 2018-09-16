package ba.kalishop.kalishop.Model;

/**
 * Created by Korisnik on 17/03/2018.
 */

public class Product  {
    private Integer Id ;

    private String naziv ;
    private String opis;
    private float cijena ;
    private String velicina ;
    private String boje ;
    private int thumbnail;
    private int kolicina;

    public Product() {
        this.naziv = "";
        this.opis = "";
        this.thumbnail = 0;
        this.velicina="";
        this.boje="";
        this.cijena=0;
        this.kolicina=0;
    }

    public Product( Integer id,String name, String description, int thumbnail,float cijena,String boje,String veliicna,int kolicina) {
        this.Id=id;
        this.naziv = name;
        this.opis = description;
        this.thumbnail = thumbnail;
        this.velicina=veliicna;
        this.boje=boje;
        this.cijena=cijena;
        this.kolicina=kolicina;
    }

    public Integer getKolicina(){
        return  kolicina;
    }

    public void setKolicina (Integer kolicina){
        this.kolicina=kolicina;
    }


    public Integer getId(){
        return Id;
    }

    public void setId(Integer id){
        Id=id;
    }

    public String getNaziv(){
        return naziv;
    }

    public void setNaziv(String naziv){
        this.naziv=naziv;
    }

    public String getOpis(){
        return opis;
    }

    public void setOpis(String opis){
        this.opis=opis;
    }

    public float getCijena(){
        return cijena;
    }

    public void setCijena(float cijena){
        this.cijena=cijena;
    }

    public String getVelicina(){
        return velicina;
    }

    public void setVelicina(String velicina){
        this.velicina=velicina;
    }

    public String getBoje(){
        return boje;
    }

    public void setBoje(String boje){
        this.boje=boje;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }


}
