package com.binance.api.client;

import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.Deposit;
import com.binance.api.client.domain.account.DepositAddress;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.account.TradeHistoryItem;
import com.binance.api.client.domain.account.Withdraw;
import com.binance.api.client.domain.account.WithdrawResult;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.CancelOrderResponse;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.event.ListenKey;
import com.binance.api.client.domain.general.Asset;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.ServerTime;
import com.binance.api.client.domain.market.AggTrade;
import com.binance.api.client.domain.market.BookTicker;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.OrderBook;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.domain.market.TickerStatistics;

import java.util.List;

/**
 * Binance API facade, supporting asynchronous/non-blocking access Binance's REST API.
 */
public interface BinanceApiAsyncRestClient {

  // General endpoints

  /**
   * Test connectivity to the Rest API.
   */
  void ping(BinanceApiCallback<Void> callback);

  /**
   * Check server time.
   */
  void getServerTime(BinanceApiCallback<ServerTime> callback);

  /**
   * Current exchange trading rules and symbol information
   */
  void getExchangeInfo(BinanceApiCallback<ExchangeInfo> callback);

  /**
   * ALL supported assets and whether or not they can be withdrawn.
   */
  void getAllAssets(BinanceApiCallback<List<Asset>> callback);

  // Market Data endpoints

  /**
   * Get order book of a symbol (asynchronous)
   *
   * @param symbol ticker symbol (e.g. ETHBTC)
   * @param limit depth of the order book (max 100)
   * @param callback the callback that handles the response
   */
  void getOrderBook(String symbol, Integer limit, BinanceApiCallback<OrderBook> callback);

  /**
   * Get recent trades (up to last 500). Weight: 1
   *
   * @param symbol ticker symbol (e.g. ETHBTC)
   * @param limit of last trades (Default 500; max 1000.)
   * @param callback the callback that handles the response
   */
  void getTrades(String symbol, Integer limit, BinanceApiCallback<List<TradeHistoryItem>> callback);

  /**
   * Get older trades. Weight: 5
   *
   * @param symbol ticker symbol (e.g. ETHBTC)
   * @param limit of last trades (Default 500; max 1000.)
   * @param fromId TradeId to fetch from. Default gets most recent trades.
   * @param callback the callback that handles the response
   */
  void getHistoricalTrades(String symbol, Integer limit, Long fromId, BinanceApiCallback<List<TradeHistoryItem>> callback);

  /**
   * Get compressed, aggregate trades. Trades that fill at the time, from the same order, with
   * the same price will have the quantity aggregated.
   *
   * If both <code>startTime</code> and <code>endTime</code> are sent, <code>limit</code>should not
   * be sent AND the distance between <code>startTime</code> and <code>endTime</code> must be less than 24 hours.
   *
   * @param symbol symbol to aggregate (mandatory)
   * @param fromId ID to get aggregate trades from INCLUSIVE (optional)
   * @param limit Default 500; max 1000 (optional)
   * @param startTime Timestamp in ms to get aggregate trades from INCLUSIVE (optional).
   * @param endTime Timestamp in ms to get aggregate trades until INCLUSIVE (optional).
   * @param callback the callback that handles the response
   * @return a list of aggregate trades for the given symbol
   */
  void getAggTrades(String symbol, String fromId, Integer limit, Long startTime, Long endTime, BinanceApiCallback<List<AggTrade>> callback);

  /**
   * Return the most recent aggregate trades for <code>symbol</code>
   *
   * @see #getAggTrades(String, String, Integer, Long, Long, BinanceApiCallback)
   */
  void getAggTrades(String symbol, BinanceApiCallback<List<AggTrade>> callback);

  /**
   * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
   *
   * @param symbol symbol to aggregate (mandatory)
   * @param interval candlestick interval (mandatory)
   * @param limit Default 500; max 1000 (optional)
   * @param startTime Timestamp in ms to get candlestick bars from INCLUSIVE (optional).
   * @param endTime Timestamp in ms to get candlestick bars until INCLUSIVE (optional).
   * @param callback the callback that handles the response containing a candlestick bar for the given symbol and interval
   */
  void getCandlestickBars(String symbol, CandlestickInterval interval, Integer limit, Long startTime, Long endTime, BinanceApiCallback<List<Candlestick>> callback);

  /**
   * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
   *
   * @see #getCandlestickBars(String, CandlestickInterval, BinanceApiCallback)
   */
  void getCandlestickBars(String symbol, CandlestickInterval interval, BinanceApiCallback<List<Candlestick>> callback);

  /**
   * Get 24 hour price change statistics (asynchronous).
   *
   * @param symbol ticker symbol (e.g. ETHBTC)
   * @param callback the callback that handles the response
   */
  void get24HrPriceStatistics(String symbol, BinanceApiCallback<TickerStatistics> callback);
  
  /**
   * Get 24 hour price change statistics for all symbols (asynchronous).
   * 
   * @param callback the callback that handles the response
   */
   void getAll24HrPriceStatistics(BinanceApiCallback<List<TickerStatistics>> callback);

  /**
   * Get Latest price for all symbols (asynchronous).
   *
   * @param callback the callback that handles the response
   */
  void getAllPrices(BinanceApiCallback<List<TickerPrice>> callback);
  
  /**
   * Get latest price for <code>symbol</code> (asynchronous).
   * 
   * @param symbol ticker symbol (e.g. ETHBTC)
   * @param callback the callback that handles the response
   */
   void getPrice(String symbol , BinanceApiCallback<TickerPrice> callback);

  /**
   * Get best price/qty on the order book for all symbols (asynchronous).
   *
   * @param callback the callback that handles the response
   */
  void getBookTickers(BinanceApiCallback<List<BookTicker>> callback);

  // Account endpoints

  /**
   * Send in a new order (asynchronous)
   *
   * @param order the new order to submit.
   * @param callback the callback that handles the response
   */
  void newOrder(NewOrder order, BinanceApiCallback<NewOrderResponse> callback);

  /**
   * Test new order creation and signature/recvWindow long. Creates and validates a new order but does not send it into the matching engine.
   *
   * @param order the new TEST order to submit.
   * @param callback the callback that handles the response
   */
  void newOrderTest(NewOrder order, BinanceApiCallback<Void> callback);

  /**
   * Check an order's status (asynchronous).
   *
   * @param orderStatusRequest order status request parameters
   * @param callback the callback that handles the response
   */
  void getOrderStatus(OrderStatusRequest orderStatusRequest, BinanceApiCallback<Order> callback);

  /**
   * Cancel an active order (asynchronous).
   *
   * @param cancelOrderRequest order status request parameters
   * @param callback the callback that handles the response
   */
  void cancelOrder(CancelOrderRequest cancelOrderRequest, BinanceApiCallback<CancelOrderResponse> callback);

  /**
   * Get all open orders on a symbol (asynchronous).
   *
   * @param orderRequest order request parameters
   * @param callback the callback that handles the response
   */
  void getOpenOrders(OrderRequest orderRequest, BinanceApiCallback<List<Order>> callback);

  /**
   * Get all account orders; active, canceled, or filled.
   *
   * @param orderRequest order request parameters
   * @param callback the callback that handles the response
   */
  void getAllOrders(AllOrdersRequest orderRequest, BinanceApiCallback<List<Order>> callback);

  /**
   * Get current account information (async).
   */
  void getAccount(Long recvWindow, Long timestamp, BinanceApiCallback<Account> callback);

  /**
   * Get current account information using default parameters (async).
   */
  void getAccount(BinanceApiCallback<Account> callback);

  /**
   * Get trades for a specific account and symbol.
   *
   * @param symbol symbol to get trades from
   * @param limit default 500; max 1000
   * @param fromId TradeId to fetch from. Default gets most recent trades.
   * @param callback the callback that handles the response with a list of trades
   */
  void getMyTrades(String symbol, Integer limit, Long fromId, Long recvWindow, Long timestamp, BinanceApiCallback<List<Trade>> callback);

  /**
   * Get trades for a specific account and symbol.
   *
   * @param symbol symbol to get trades from
   * @param limit default 500; max 1000
   * @param callback the callback that handles the response with a list of trades
   */
  void getMyTrades(String symbol, Integer limit, BinanceApiCallback<List<Trade>> callback);

  /**
   * Get trades for a specific account and symbol.
   *
   * @param symbol symbol to get trades from
   * @param callback the callback that handles the response with a list of trades
   */
  void getMyTrades(String symbol, BinanceApiCallback<List<Trade>> callback);

  /**
   * Submit a withdraw request.
   *
   * Enable Withdrawals option has to be active in the API settings.
   *
   * @param coin asset symbol to withdraw
   * @param withdrawOrderId com.binance.api.client id for withdraw
   * @param network the network to use for the withdrawal
   * @param address address to withdraw to
   * @param addressTag Secondary address identifier for coins like XRP,XMR etc.
   * @param amount amount to withdraw
   * @param transactionFeeFlag When making internal transfer, true for returning the fee to the destination account; false for returning the fee back to the departure account. Default false.
   * @param name Description of the address. Space in name should be encoded into %20.
   * @param callback the callback that handles the response with a list of trades
   */
  void withdraw(String coin, String withdrawOrderId, String network, String address, String addressTag,
                String amount, Boolean transactionFeeFlag, String name, BinanceApiCallback<WithdrawResult> callback);

  /**
   * Fetch account deposit history.
   *
   * @param coin the asset to get the history for
   * @param callback the callback that handles the response and returns the deposit history
   */
  void getDepositHistory(String coin, BinanceApiCallback<List<Deposit>> callback);


  /**
   * Fetch account deposit history.
   *
   * @param coin the asset to get the history for
   * @param status 0(0:pending,6: credited but cannot withdraw, 1:success)
   * @param startTime Default: 90 days from current timestamp
   * @param endTime Default: present timestamp
   * @param offset Default:0
   * @param limit Default:1000, Max:1000
   * @param callback the callback that handles the response and returns the deposit history
   */
  void getDepositHistory(String coin, Integer status, Long startTime, Long endTime,
                         Integer offset, Integer limit, BinanceApiCallback<List<Deposit>> callback);

  /**
   * Fetch account withdraw history.
   *
   * @param coin the asset to get the history for
   * @param callback the callback that handles the response and returns the withdraw history
   */
  void getWithdrawHistory(String coin, BinanceApiCallback<List<Withdraw>> callback);

  /**
   * Fetch account withdraw history.
   *
   * @param coin the asset to get the history for
   * @param withdrawOrderId com.binance.api.client id for withdraw
   * @param status 0(0:Email Sent,1:Cancelled 2:Awaiting Approval 3:Rejected 4:Processing 5:Failure 6:Completed)
   * @param startTime Default: 90 days from current timestamp
   * @param endTime Default: present timestamp
   * @param offset Default:0
   * @param limit Default:1000, Max:1000
   * @param callback the callback that handles the response and returns the withdraw history
   */
  void getWithdrawHistory(String coin, String withdrawOrderId, Integer status, Long startTime, Long endTime,
                          Integer offset, Integer limit, BinanceApiCallback<List<Withdraw>> callback);

  /**
   * Fetch deposit address.
   *
   * @param callback the callback that handles the response and returns the deposit address
   */
   void getDepositAddress(String asset, String network, BinanceApiCallback<DepositAddress> callback);

  // User stream endpoints

  /**
   * Start a new user data stream.
   *
   * @param callback the callback that handles the response which contains a listenKey
   */
  void startUserDataStream(BinanceApiCallback<ListenKey> callback);

  /**
   * PING a user data stream to prevent a time out.
   *
   * @param listenKey listen key that identifies a data stream
   * @param callback the callback that handles the response which contains a listenKey
   */
  void keepAliveUserDataStream(String listenKey, BinanceApiCallback<Void> callback);

  /**
   * Close out a new user data stream.
   *
   * @param listenKey listen key that identifies a data stream
   * @param callback the callback that handles the response which contains a listenKey
   */
  void closeUserDataStream(String listenKey, BinanceApiCallback<Void> callback);
}