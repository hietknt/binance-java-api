package com.binance.api.client.impl;

import com.binance.api.client.BinanceApiError;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.exception.BinanceApiException;
import com.binance.api.client.security.AuthenticationInterceptor;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Generates a Binance API implementation based on @see {@link BinanceApiService}.
 */
public class BinanceApiServiceGenerator {

    private static OkHttpClient sharedClient;
    private static final Converter.Factory converterFactory = JacksonConverterFactory.create();

    static {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);
        sharedClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .pingInterval(20, TimeUnit.SECONDS)
                .build();
    }

    @SuppressWarnings("unchecked")
    private static final Converter<ResponseBody, BinanceApiError> errorBodyConverter =
            (Converter<ResponseBody, BinanceApiError>) converterFactory.responseBodyConverter(
                    BinanceApiError.class, new Annotation[0], null);

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    /**
     * Create a Binance API service.
     *
     * @param serviceClass the type of service.
     * @param apiKey       Binance API key.
     * @param secret       Binance secret.
     * @return a new implementation of the API endpoints for the Binance API service.
     */
    public static <S> S createService(Class<S> serviceClass, String apiKey, String secret) {
        String baseUrl = null;
        if (!BinanceApiConfig.useTestnet) {
            baseUrl = BinanceApiConfig.getApiBaseUrl();
        } else {
            baseUrl = /*BinanceApiConfig.useTestnetStreaming ?
                BinanceApiConfig.getStreamTestNetBaseUrl() :*/
                    BinanceApiConfig.getTestNetBaseUrl();
        }

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory);

        if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(secret)) {
            retrofitBuilder.client(sharedClient);
        } else {
            // `adaptedClient` will use its own interceptor, but share thread pool etc with the 'parent' com.binance.api.client
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret);
            OkHttpClient adaptedClient = sharedClient.newBuilder().addInterceptor(interceptor).build();
            retrofitBuilder.client(adaptedClient);
        }

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    /**
     * Execute a REST call and block until the response is received.
     */
    public static <T> T executeSync(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                BinanceApiError apiError = getBinanceApiError(response);
                throw new BinanceApiException(apiError);
            }
        } catch (IOException e) {
            throw new BinanceApiException(e);
        }
    }

    /**
     * Extracts and converts the response error body into an object.
     */
    public static BinanceApiError getBinanceApiError(Response<?> response) throws IOException, BinanceApiException {
        return errorBodyConverter.convert(response.errorBody());
    }

    /**
     * Returns the shared OkHttpClient instance.
     */
    public static OkHttpClient getSharedClient() {
        return sharedClient;
    }

    public static void changeSharedClientProxy() {
        Properties systemProperties = System.getProperties();

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);
        sharedClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .pingInterval(20, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        systemProperties.getProperty("https.proxyHost"),
                        Integer.parseInt(systemProperties.getProperty("https.proxyPort")))))
                .proxyAuthenticator(Authenticator.JAVA_NET_AUTHENTICATOR)
                .build();

    }

    public static void changeSharedClientProxy(String ip, String port, String login, String password) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);

        Authenticator proxyAuthenticator = (route, response) -> {
            String credential = Credentials.basic(login, password);
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build();
        };

        sharedClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .pingInterval(20, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, Integer.parseInt(port))))
                .proxyAuthenticator(proxyAuthenticator)
                .build();

    }
}
