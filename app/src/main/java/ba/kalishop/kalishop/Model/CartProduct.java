package ba.kalishop.kalishop.Model;

/**
 * Created by Korisnik on 26/03/2018.
 */

public class CartProduct {
    private Integer Id ;

    private Integer ProductId ;
    private Integer InCartId;

    public CartProduct(){

    }
    public Integer getId(){
        return Id;
    }

    public void setId(Integer id){
        Id=id;
    }

    public Integer getProductId(){return ProductId;}
    public void setProductId(Integer ProductId){this.ProductId=ProductId;}


    public Integer getInCartId(){return InCartId;}
    public void setInCartId(Integer InCartId){this.InCartId=InCartId;}




}
