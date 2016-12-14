package chitoiu.com.balance.forecasting.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitManager {

    public static final String BASE_URL = "path to your server";

    private CallbackListener mListener;

    public RetroFitManager(CallbackListener listener) {
        mListener = listener;
    }

    private RetroFitApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        final RetroFitApiInterface mInterfaceService = retrofit.create(RetroFitApiInterface.class);
        return mInterfaceService;
    }

    public void loginProcessWithRetrofit(final String email, String password) {

        RetroFitApiInterface mApiService = this.getInterfaceService();
        Call<RetroFitLogin> mService = mApiService.authenticate(email, password);

        mService.enqueue(new Callback<RetroFitLogin>() {
            @Override
            public void onResponse(Call<RetroFitLogin> call, Response<RetroFitLogin> response) {

                RetroFitLogin mRetroFitLoginObject = response.body();
                String returnedResponse = mRetroFitLoginObject.isLogin;

                if (returnedResponse.trim().equals("1")) {
                    // Success
                    if (mListener != null) mListener.loginSuccessful();
                }
                if (returnedResponse.trim().equals("0")) {
                    // Fail
                    if (mListener != null) mListener.loginError();
                }
            }

            @Override
            public void onFailure(Call<RetroFitLogin> call, Throwable t) {
                call.cancel();
                // Error
                if (mListener != null) mListener.retroFitError();
            }
        });
    }

    public void registrationProcessWithRetrofit(final String email, String password) {

        RetroFitApiInterface mApiService = this.getInterfaceService();
        Call<RetroFitLogin> mService = mApiService.registration(email, password);
        mService.enqueue(new Callback<RetroFitLogin>() {
            @Override
            public void onResponse(Call<RetroFitLogin> call, Response<RetroFitLogin> response) {

                RetroFitLogin mRetroFitLoginObject = response.body();
                String returnedResponse = mRetroFitLoginObject.isLogin;

                //showProgress(false);
                if (returnedResponse.trim().equals("1")) {
                    if (mListener != null) mListener.registrationSuccessful();
                }
                if (returnedResponse.trim().equals("0")) {
                    // use the registration failed
                    if (mListener != null) mListener.registrationError();
                }
            }

            @Override
            public void onFailure(Call<RetroFitLogin> call, Throwable t) {
                call.cancel();
                // Error
                if (mListener != null) mListener.retroFitError();
            }
        });
    }

    public interface CallbackListener {
        void loginSuccessful();

        void loginError();

        void registrationSuccessful();

        void registrationError();

        void retroFitError();
    }
}
