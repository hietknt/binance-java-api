package com.binance.api.client;

import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.impl.BinanceApiAsyncMarginRestClientImpl;
import com.binance.api.client.impl.BinanceApiAsyncRestClientImpl;
import com.binance.api.client.impl.BinanceApiMarginRestClientImpl;
import com.binance.api.client.impl.BinanceApiRestClientImpl;
import com.binance.api.client.impl.BinanceApiSwapRestClientImpl;
import com.binance.api.client.impl.BinanceApiWebSocketClientImpl;

import static com.binance.api.client.impl.BinanceApiServiceGenerator.getSharedClient;

/**
 * A factory for creating BinanceApi com.binance.api.client objects.
 */
public class BinanceApiClientFactory {

    /**
     * API Key
     */
    private String apiKey;

    /**
     * Secret.
     */
    private String secret;

    /**
     * Instantiates a new binance api com.binance.api.client factory.
     *
     * @param apiKey the API key
     * @param secret the Secret
     */
    private BinanceApiClientFactory(String apiKey, String secret) {
        this.apiKey = apiKey;
        this.secret = secret;
        BinanceApiConfig.useTestnet = false;
        BinanceApiConfig.useTestnetStreaming = false;
    }

    /**
     * Instantiates a new binance api com.binance.api.client factory.
     *
     * @param apiKey              the API key
     * @param secret              the Secret
     * @param useTestnet          true if endpoint is spot test network URL; false if endpoint is production spot API URL.
     * @param useTestnetStreaming true for spot test network websocket streaming; false for no streaming.
     */
    private BinanceApiClientFactory(String apiKey, String secret, boolean useTestnet, boolean useTestnetStreaming) {
        this(apiKey, secret);
        if (useTestnet) {
            BinanceApiConfig.useTestnet = true;
            BinanceApiConfig.useTestnetStreaming = useTestnetStreaming;
        }
    }

    /**
     * New instance.
     *
     * @param apiKey the API key
     * @param secret the Secret
     * @return the binance api com.binance.api.client factory
     */
    public static BinanceApiClientFactory newInstance(String apiKey, String secret) {
        return new BinanceApiClientFactory(apiKey, secret);
    }

    /**
     * New instance with optional Spot Test Network endpoint.
     *
     * @param apiKey              the API key
     * @param secret              the Secret
     * @param useTestnet          true if endpoint is spot test network URL; false if endpoint is production spot API URL.
     * @param useTestnetStreaming true for spot test network websocket streaming; false for no streaming.
     * @return the binance api com.binance.api.client factory.
     */
    public static BinanceApiClientFactory newInstance(String apiKey, String secret, boolean useTestnet, boolean useTestnetStreaming) {
        return new BinanceApiClientFactory(apiKey, secret, useTestnet, useTestnetStreaming);
    }

    /**
     * New instance without authentication.
     *
     * @return the binance api com.binance.api.client factory
     */
    public static BinanceApiClientFactory newInstance() {
        return new BinanceApiClientFactory(null, null);
    }

    /**
     * New instance without authentication and with optional Spot Test Network endpoint.
     *
     * @param useTestnet          true if endpoint is spot test network URL; false if endpoint is production spot API URL.
     * @param useTestnetStreaming true for spot test network websocket streaming; false for no streaming.
     * @return the binance api com.binance.api.client factory.
     */
    public static BinanceApiClientFactory newInstance(boolean useTestnet, boolean useTestnetStreaming) {
        return new BinanceApiClientFactory(null, null, useTestnet, useTestnetStreaming);
    }

    /**
     * Creates a new synchronous/blocking REST com.binance.api.client.
     */
    public BinanceApiRestClient newRestClient() {
        return new BinanceApiRestClientImpl(apiKey, secret);
    }

    /**
     * Creates a new synchronous/blocking REST com.binance.api.client with proxy.
     */
    public BinanceApiRestClient newRestClientWithProxy() {
        return new BinanceApiRestClientImpl(apiKey, secret, true);
    }

    /**
     * Creates a new synchronous/blocking REST com.binance.api.client with proxy.
     */
    public BinanceApiRestClient newRestClientWithProxy(String ip, String port, String login, String password) {
        return new BinanceApiRestClientImpl(apiKey, secret, ip, port, login, password);
    }

    /**
     * Creates a new asynchronous/non-blocking REST com.binance.api.client.
     */
    public BinanceApiAsyncRestClient newAsyncRestClient() {
        return new BinanceApiAsyncRestClientImpl(apiKey, secret);
    }

    /**
     * Creates a new asynchronous/non-blocking Margin REST com.binance.api.client.
     */
    public BinanceApiAsyncMarginRestClient newAsyncMarginRestClient() {
        return new BinanceApiAsyncMarginRestClientImpl(apiKey, secret);
    }

    /**
     * Creates a new synchronous/blocking Margin REST com.binance.api.client.
     */
    public BinanceApiMarginRestClient newMarginRestClient() {
        return new BinanceApiMarginRestClientImpl(apiKey, secret);
    }

    /**
     * Creates a new web socket com.binance.api.client used for handling data streams.
     */
    public BinanceApiWebSocketClient newWebSocketClient() {
        return new BinanceApiWebSocketClientImpl(getSharedClient());
    }

    /**
     * Creates a new synchronous/blocking Swap REST com.binance.api.client.
     */
    public BinanceApiSwapRestClient newSwapRestClient() {
        return new BinanceApiSwapRestClientImpl(apiKey, secret);
    }
}
