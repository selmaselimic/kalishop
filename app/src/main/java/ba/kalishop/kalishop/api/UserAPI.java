package ba.kalishop.kalishop.api;

import ba.kalishop.kalishop.Model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Korisnik on 20/02/2018.
 */

public interface UserAPI {
    @FormUrlEncoded
    @POST("servicesdress")
    Call<User> userLogin(@Field("username") String username,
                       @Field("password") String password);


    @FormUrlEncoded
    @POST("login/views/signup.php")
    Call<User> userSignUp(@Field("username") String usernaname,
                          @Field("name") String name,
                          @Field("lastname") String lastname,
                         @Field("email") String email,
                         @Field("password") String password);
}
