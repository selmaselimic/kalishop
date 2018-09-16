package ba.kalishop.kalishop.web;

/**
 * Created by Korisnik on 30/03/2018.
 */

public class APIUtils {
    public static String APIURL ="http://192.168.2.100:62225";

    public static ApiInterface getFileService(){

        return ApiClient.getApiClient().create(ApiInterface.class);
    }
}
