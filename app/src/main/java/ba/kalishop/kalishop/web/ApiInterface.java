package ba.kalishop.kalishop.web;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ba.kalishop.kalishop.Model.Product;
import ba.kalishop.kalishop.Model.User;
import ba.kalishop.kalishop.Model.Incart;
import ba.kalishop.kalishop.UploadObject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Korisnik on 25/02/2018.
 */

public interface ApiInterface {
  //  @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})

    @GET("/Autentifikacija/Login/")//this sgould change to post but will do later
    Call<User> getWords (  @Query("username") String username,
                           @Query("password") String password);


    @GET("/Product/GetProizvode/")
    Call<List<Product>>getAll();

    @GET("/Autentifikacija/Register/")//this should change to post method,will do later
    Call<User> getUser (   @Query("username") String username,
                           @Query("password") String password,
                           @Query("name") String name,
                           @Query("lastname") String lastname,
                           @Query("email") String email);

    @GET("/Autentifikacija/ChangeUserData/") //this should change to post method,will do later
    Call<User> changeData (
                           @Query("Id") Integer Id,
                           @Query("username") String username,
                           @Query("password") String password,
                           @Query("name") String name,
                           @Query("lastname") String lastname,
                           @Query("email") String email);



    @POST("/InCart/storeInCart/") //this should change to post method,will do later
    Call<Integer> storeInCart (
            @Query("Id") Integer Id,
           @Query("Kid") Integer Kid,
          @Body Product proizvod
            );
    //@Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})

    @GET("/InCart/getInCart/") //this should change to post method,will do later
    Call<List< ba.kalishop.kalishop.Model.Product >> getInCart (
                              @Query("Id") Integer Id);

    @GET("/InCart/getAll/") //this should change to post method,will do later
    Call<List< ba.kalishop.kalishop.Model.InCartHistoryProduct >> getAll (
            @Query("Id") Integer Id);

    @GET("/InCart/removeFromCart/") //this should change to post method,will do later
    Call<List< ba.kalishop.kalishop.Model.Product >> removeFromCart (
            @Query("CartId") Integer CartId,
            @Query("Id") Integer Id);


    @GET("/InCart/Buy/") //this should change to post method,will do later
    Call<Void> Buy (
            @Query("Id") Integer Id);

    @Multipart
    @POST("/Image/postImage")
    Call<UploadObject> uploadFile(@Part MultipartBody.Part file);

}
