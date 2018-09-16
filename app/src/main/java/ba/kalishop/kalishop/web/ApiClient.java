package ba.kalishop.kalishop.web;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Korisnik on 25/02/2018.
 */

public class ApiClient {
    public static String APIURL ="http://rks1632.app.fit.ba";
    public static Retrofit retrofit=null;
    public static Retrofit getApiClient(){
        if (retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(APIURL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
