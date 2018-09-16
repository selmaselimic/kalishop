package ba.kalishop.kalishop.Model;

import java.io.Serializable;

/**
 * Created by Korisnik on 20/02/2018.
 */

public class User implements Serializable {
    private Integer Id;
    private String username ;
    private String password;
    private String ime;
    private String prezime;
    private String email;


    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }
    public Integer getId(){
        return Id;
    }

    public void setId(Integer id){
        Id=id;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public String getName(){
        return ime;
    }

    public void setName(String name){
       ime=name;
    }

    public String getPrezime(){
        return prezime;
    }

    public void setPrezime(String prezime){
        this.prezime=prezime;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }



 public User(){
      username="";
      password="";
     ime="";
      prezime="";
     email="";
         Id=0;
  }
   public User(String username,String password,String name,String lastName,String email){

        this.username=username;
       this.password=password;
       this.ime=name;
       this.prezime=lastName;
     this.email=email;

  }
}
