package ba.kalishop.kalishop.Model;

import java.util.List;

/**
 * Created by Korisnik on 22/03/2018.
 */

public class Incart {
    private Integer Id ;

    private Integer kolicina ;
    private Float ukupno;
    private User user;
    private List< Product> product;

    public Incart(){

    }

    //getteri i setteri mi trebaju

    public Integer getId(){
        return Id;
    }

    public void setId(Integer id){
        Id=id;
    }

    public Integer getKolicina(){
        return kolicina;
    }

    public void setKolicina(Integer kolicina){
        this.kolicina=kolicina;
    }

    public Float getUkupno(){
        return ukupno;
    }

    public void setUkupno(Float ukupno){
        this.ukupno=ukupno;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user=user;
    }

    public List<Product> getProduct(){
        return product;
    }

    public void setProduct(List<Product> product){
        this.product=product;
    }


}
