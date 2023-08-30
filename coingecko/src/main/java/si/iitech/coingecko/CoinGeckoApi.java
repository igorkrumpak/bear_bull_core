package si.iitech.coingecko;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import si.iitech.coingecko.exception.CoinGeckoApiException;

public class CoinGeckoApi {
    private final String API_BASE_URL = "https://api.coingecko.com/api/v3/";

    private OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    public <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }

    public <T> T executeSync(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                try {
                    CoinGeckoApiError apiError = getCoinGeckoApiError(response);
                    apiError.setCode(response.code());
                    throw new CoinGeckoApiException(apiError);
                } catch (IOException e) {
                    throw new CoinGeckoApiException(response.toString(), e);
                }
            }
        } catch (IOException e) {
            throw new CoinGeckoApiException(e);
        }
    }

    public void shutdown() {
        okHttpClient.dispatcher().executorService().shutdown();
        okHttpClient.connectionPool().evictAll();
    }

    private CoinGeckoApiError getCoinGeckoApiError(Response<?> response) throws IOException{
        return (CoinGeckoApiError) retrofit.responseBodyConverter(CoinGeckoApiError.class,new Annotation[0])
                .convert(response.errorBody());

    }
}
