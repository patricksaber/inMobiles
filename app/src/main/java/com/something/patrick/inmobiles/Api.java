package com.something.patrick.inmobiles;

/**
 * Created by patrick on 3/2/2018.
 */
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL ="http://test.inmobiles.net/testapi/api/Initialization/";
    @GET("SelectAllImages")
    Call<List<Item>> getItems();
}
