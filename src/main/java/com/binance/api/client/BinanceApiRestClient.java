package com.binance.api.client;

import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.Deposit;
import com.binance.api.client.domain.account.DepositAddress;
import com.binance.api.client.domain.account.DustTransferResponse;
import com.binance.api.client.domain.account.NewOCO;
import com.binance.api.client.domain.account.NewOCOResponse;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.OrderList;
import com.binance.api.client.domain.account.SubAccountTransfer;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.account.TradeHistoryItem;
import com.binance.api.client.domain.account.Withdraw;
import com.binance.api.client.domain.account.WithdrawResult;
import com.binance.api.client.domain.account.request.AllOrderListRequest;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import com.binance.api.client.domain.account.request.CancelOrderListRequest;
import com.binance.api.client.domain.account.request.CancelOrderListResponse;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.CancelOrderResponse;
import com.binance.api.client.domain.account.request.OrderListStatusRequest;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.general.Asset;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.market.AggTrade;
import com.binance.api.client.domain.market.BookTicker;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.OrderBook;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.domain.market.TickerStatistics;

import java.util.List;

/**
 * Binance API facade, supporting synchronous/blocking access Binance's REST API.
 */
public interface BinanceApiRestClient {

    // General endpoints

    /**
     * Test connectivity to the Rest API.
     */
    void ping();

    /**
     * Test connectivity to the Rest API and get the current server time.
     *
     * @return current server time.
     */
    Long getServerTime();

    /**
     * @return Current exchange trading rules and symbol information
     */
    ExchangeInfo getExchangeInfo();

    /**
     * @return All the supported assets and whether or not they can be withdrawn.
     */
    List<Asset> getAllAssets();

    // Market Data endpoints

    /**
     * Get order book of a symbol.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  depth of the order book (max 100)
     */
    OrderBook getOrderBook(String symbol, Integer limit);

    /**
     * Get recent trades (up to last 500). Weight: 1
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  of last trades (Default 500; max 1000.)
     */
    List<TradeHistoryItem> getTrades(String symbol, Integer limit);

    /**
     * Get older trades. Weight: 5
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  of last trades (Default 500; max 1000.)
     * @param fromId TradeId to fetch from. Default gets most recent trades.
     */
    List<TradeHistoryItem> getHistoricalTrades(String symbol, Integer limit, Long fromId);

    /**
     * Get compressed, aggregate trades. Trades that fill at the time, from the same order, with
     * the same price will have the quantity aggregated.
     * <p>
     * If both <code>startTime</code> and <code>endTime</code> are sent, <code>limit</code>should not
     * be sent AND the distance between <code>startTime</code> and <code>endTime</code> must be less than 24 hours.
     *
     * @param symbol    symbol to aggregate (mandatory)
     * @param fromId    ID to get aggregate trades from INCLUSIVE (optional)
     * @param limit     Default 500; max 1000 (optional)
     * @param startTime Timestamp in ms to get aggregate trades from INCLUSIVE (optional).
     * @param endTime   Timestamp in ms to get aggregate trades until INCLUSIVE (optional).
     * @return a list of aggregate trades for the given symbol
     */
    List<AggTrade> getAggTrades(String symbol, String fromId, Integer limit, Long startTime, Long endTime);

    /**
     * Return the most recent aggregate trades for <code>symbol</code>
     *
     * @see #getAggTrades(String, String, Integer, Long, Long)
     */
    List<AggTrade> getAggTrades(String symbol);

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @param symbol    symbol to aggregate (mandatory)
     * @param interval  candlestick interval (mandatory)
     * @param limit     Default 500; max 1000 (optional)
     * @param startTime Timestamp in ms to get candlestick bars from INCLUSIVE (optional).
     * @param endTime   Timestamp in ms to get candlestick bars until INCLUSIVE (optional).
     * @return a candlestick bar for the given symbol and interval
     */
    List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval interval, Integer limit, Long startTime, Long endTime);

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @see #getCandlestickBars(String, CandlestickInterval, Integer, Long, Long)
     */
    List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval interval);

    /**
     * Get 24 hour price change statistics.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     */
    TickerStatistics get24HrPriceStatistics(String symbol);

    /**
     * Get 24 hour price change statistics for all symbols.
     */
    List<TickerStatistics> getAll24HrPriceStatistics();

    /**
     * Get Latest price for all symbols.
     */
    List<TickerPrice> getAllPrices();

    /**
     * Get latest price for <code>symbol</code>.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     */
    TickerPrice getPrice(String symbol);

    /**
     * Get best price/qty on the order book for all symbols.
     */
    List<BookTicker> getBookTickers();

    // Account endpoints

    /**
     * Send in a new order.
     *
     * @param order the new order to submit.
     * @return a response containing details about the newly placed order.
     */
    NewOrderResponse newOrder(NewOrder order);

    /**
     * Test new order creation and signature/recvWindow long. Creates and validates a new order but does not send it into the matching engine.
     *
     * @param order the new TEST order to submit.
     */
    void newOrderTest(NewOrder order);

    /**
     * Check an order's status.
     *
     * @param orderStatusRequest order status request options/filters
     * @return an order
     */
    Order getOrderStatus(OrderStatusRequest orderStatusRequest);

    /**
     * Cancel an active order.
     *
     * @param cancelOrderRequest order status request parameters
     */
    CancelOrderResponse cancelOrder(CancelOrderRequest cancelOrderRequest);

    /**
     * Get all open orders on a symbol.
     *
     * @param orderRequest order request parameters
     * @return a list of all account open orders on a symbol.
     */
    List<Order> getOpenOrders(OrderRequest orderRequest);

    /**
     * Get all account orders; active, canceled, or filled.
     *
     * @param orderRequest order request parameters
     * @return a list of all account orders
     */
    List<Order> getAllOrders(AllOrdersRequest orderRequest);

    /**
     * Send in a new OCO;
     *
     * @param oco the OCO to submit
     * @return a response containing details about the newly placed OCO.
     */
    NewOCOResponse newOCO(NewOCO oco);

    /**
     * Cancel an entire Order List
     *
     * @return
     */
    CancelOrderListResponse cancelOrderList(CancelOrderListRequest cancelOrderListRequest);

    /**
     * Check an order list status
     *
     * @param orderListStatusRequest
     * @return an orderList
     */
    OrderList getOrderListStatus(OrderListStatusRequest orderListStatusRequest);

    /**
     * Get all list os orders
     *
     * @param allOrderListRequest
     * @return
     */
    List<OrderList> getAllOrderList(AllOrderListRequest allOrderListRequest);

    /**
     * Get current account information.
     */
    Account getAccount(Long recvWindow, Long timestamp);

    /**
     * Get current account information using default parameters.
     */
    Account getAccount();

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @param limit  default 500; max 1000
     * @param fromId TradeId to fetch from. Default gets most recent trades.
     * @return a list of trades
     */
    List<Trade> getMyTrades(String symbol, Integer limit, Long fromId, Long recvWindow, Long timestamp);

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @param limit  default 500; max 1000
     * @return a list of trades
     */
    List<Trade> getMyTrades(String symbol, Integer limit);

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @return a list of trades
     */
    List<Trade> getMyTrades(String symbol);

    List<Trade> getMyTrades(String symbol, Long fromId);

    /**
     * Submit a withdraw request.
     * <p>
     * Enable Withdrawals option has to be active in the API settings.
     *
     * @param coin               asset symbol to withdraw
     * @param withdrawOrderId    com.binance.api.client id for withdraw
     * @param network            the network to use for the withdrawal
     * @param address            address to withdraw to
     * @param addressTag         Secondary address identifier for coins like XRP,XMR etc.
     * @param amount             amount to withdraw
     * @param transactionFeeFlag When making internal transfer, true for returning the fee to the destination account; false for returning the fee back to the departure account. Default false.
     * @param name               Description of the address. Space in name should be encoded into %20.
     */
    WithdrawResult withdraw(String coin, String withdrawOrderId, String network, String address, String amount,
                            String name, String addressTag, Boolean transactionFeeFlag);

    /**
     * Conver a list of assets to BNB
     *
     * @param asset the list of assets to convert
     */
    DustTransferResponse dustTranfer(List<String> asset);

    /**
     * Fetch account deposit history.
     *
     * @param coin the asset to get the history for
     * @return deposit history, containing a list of deposits
     */
    List<Deposit> getDepositHistory(String coin);

    /**
     * Fetch account deposit history.
     *
     * @param coin      the asset to get the history for
     * @param status    0(0:pending,6: credited but cannot withdraw, 1:success)
     * @param startTime Default: 90 days from current timestamp
     * @param endTime   Default: present timestamp
     * @param offset    Default:0
     * @param limit     Default:1000, Max:1000
     * @return deposit history, containing a list of deposits
     */
    List<Deposit> getDepositHistory(String coin, int status, Long startTime, Long endTime,
                                    int offset, int limit);

    /**
     * Fetch account withdraw history.
     *
     * @param coin the asset to get the history for
     * @return withdraw history, containing a list of withdrawals
     */
    List<Withdraw> getWithdrawHistory(String coin);

    /**
     * Fetch account withdraw history.
     *
     * @param coin            the asset to get the history for
     * @param withdrawOrderId com.binance.api.client id for withdraw
     * @param status          0(0:Email Sent,1:Cancelled 2:Awaiting Approval 3:Rejected 4:Processing 5:Failure 6:Completed)
     * @param startTime       Default: 90 days from current timestamp
     * @param endTime         Default: present timestamp
     * @param offset          Default:0
     * @param limit           Default:1000, Max:1000
     * @return withdraw history, containing a list of withdrawals
     */
    List<Withdraw> getWithdrawHistory(String coin, String withdrawOrderId, Integer status, Long startTime, Long endTime,
                                      Integer offset, Integer limit);

    /**
     * Fetch sub-account transfer history.
     *
     * @return sub-account transfers
     */
    List<SubAccountTransfer> getSubAccountTransfers();

    /**
     * Fetch deposit address supported network.
     *
     * @param asset   coin property
     * @param network network property
     * @return deposit address for given network and asset
     */
    DepositAddress getDepositAddress(String asset, String network);

    // User stream endpoints

    /**
     * Start a new user data stream.
     *
     * @return a listen key that can be used with data streams
     */
    String startUserDataStream();

    /**
     * PING a user data stream to prevent a time out.
     *
     * @param listenKey listen key that identifies a data stream
     */
    void keepAliveUserDataStream(String listenKey);

    /**
     * Close out a new user data stream.
     *
     * @param listenKey listen key that identifies a data stream
     */
    void closeUserDataStream(String listenKey);
}
