package ba.kalishop.kalishop.Model;


import java.sql.Date;
import java.util.List;

/**
 * Created by Korisnik on 29/04/2018.
 */

public class InCartHistoryProduct {
    private Integer InCarHistorytId;
    private Integer ProizvodId;
    private Integer Id ;
    private Product Proizvod;
    private Integer InCartId;
    private String Datum;

   public InCartHistoryProduct(){

   }


   public String getDatum(){
       return Datum;
   }

   public void setDatum(String datum){
       Datum=datum;
   }
    public Integer getId(){
        return Id;
    }

    public void setId(Integer id){
       Id=id;
    }


    public Integer getInCartId(){
        return InCartId;
    }

    public void setInCartId(Integer cart){
        InCartId=cart;
    }


    public Integer getProizvodId(){
        return ProizvodId;
    }

    public void setProizvodId(Integer id){
        ProizvodId=id;
    }
    public Integer getInCarHistorytId(){
        return InCarHistorytId;
    }

    public void setInCarHistorytId(Integer id){
        InCarHistorytId=id;
    }

    public Product getProizvod(){
        return Proizvod;
    }

    public void setProduct(Product product){
        this.Proizvod=product;
    }
}
