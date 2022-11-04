package com.binance.api.client.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Coin information.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinInformation {
    private String coin;
    private boolean depositAllEnable;
    private String free;
    private String freeze;
    private String locked;
    private List<CoinInformationNetwork> networkList;
    private String storage;
    private boolean trading;
    private boolean withdrawAllEnable;
    private String withdrawing;

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public boolean isDepositAllEnable() {
        return depositAllEnable;
    }

    public void setDepositAllEnable(boolean depositAllEnable) {
        this.depositAllEnable = depositAllEnable;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public List<CoinInformationNetwork> getNetworkList() {
        return networkList;
    }

    public void setNetworkList(List<CoinInformationNetwork> networkList) {
        this.networkList = networkList;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public boolean isTrading() {
        return trading;
    }

    public void setTrading(boolean trading) {
        this.trading = trading;
    }

    public boolean isWithdrawAllEnable() {
        return withdrawAllEnable;
    }

    public void setWithdrawAllEnable(boolean withdrawAllEnable) {
        this.withdrawAllEnable = withdrawAllEnable;
    }

    public String getWithdrawing() {
        return withdrawing;
    }

    public void setWithdrawing(String withdrawing) {
        this.withdrawing = withdrawing;
    }
}
