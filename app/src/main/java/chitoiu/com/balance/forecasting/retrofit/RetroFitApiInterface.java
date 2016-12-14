package chitoiu.com.balance.forecasting.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetroFitApiInterface {

    @GET("api/{email}/{password}")
    Call<RetroFitLogin> authenticate(@Path("email") String email, @Path("password") String password);

    @POST("api/{email}/{password}")
    Call<RetroFitLogin> registration(@Path("email") String email, @Path("password") String password);
}
