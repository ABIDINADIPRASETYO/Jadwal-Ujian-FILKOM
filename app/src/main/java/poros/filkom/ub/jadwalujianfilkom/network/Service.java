package poros.filkom.ub.jadwalujianfilkom.network;

import android.database.Observable;


import okhttp3.RequestBody;
import poros.filkom.ub.jadwalujianfilkom.model.JadwalResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Service {

    /*
    SIAM Service
     */
    //get cookie
    @GET("index.php")
    Call<String> getCookie();

    //authenticate cookie with username and password
    @Multipart
    @POST("index.php")
    Call<String> login(@Header("Cookie") String cookie,
                       @Part("username") RequestBody username,
                       @Part("password") RequestBody password,
                       @Part("login") RequestBody login);

    //get jadwal with authenticated cookie
    @GET("class.php")
    Call<String> jadwal(@Header("Cookie") String cookie);

    @GET("uts.JSON")
    Call<JadwalResponse> getJadwalUAS();

    @GET("test.txt")
    Call<String> getJadwalUtsTxt();
}
