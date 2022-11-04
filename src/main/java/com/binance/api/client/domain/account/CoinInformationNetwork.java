package com.binance.api.client.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Coin information network.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinInformationNetwork {
    private String addressRegex;
    private String coin;
    private String depositDesc;
    private boolean depositEnable;
    private boolean isDefault;
    private String memoRegex;
    private int minConfirm;
    private String name;
    private String network;
    private String withdrawDesc;
    private boolean withdrawEnable;
    private String withdrawFee;
    private String withdrawMax;
    private String withdrawMin;
    private boolean sameAddress;
    private int estimatedArrivalTime;
    private boolean busy;

    public String getAddressRegex() {
        return addressRegex;
    }

    public void setAddressRegex(String addressRegex) {
        this.addressRegex = addressRegex;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getDepositDesc() {
        return depositDesc;
    }

    public void setDepositDesc(String depositDesc) {
        this.depositDesc = depositDesc;
    }

    public boolean isDepositEnable() {
        return depositEnable;
    }

    public void setDepositEnable(boolean depositEnable) {
        this.depositEnable = depositEnable;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getMemoRegex() {
        return memoRegex;
    }

    public void setMemoRegex(String memoRegex) {
        this.memoRegex = memoRegex;
    }

    public int getMinConfirm() {
        return minConfirm;
    }

    public void setMinConfirm(int minConfirm) {
        this.minConfirm = minConfirm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getWithdrawDesc() {
        return withdrawDesc;
    }

    public void setWithdrawDesc(String withdrawDesc) {
        this.withdrawDesc = withdrawDesc;
    }

    public boolean isWithdrawEnable() {
        return withdrawEnable;
    }

    public void setWithdrawEnable(boolean withdrawEnable) {
        this.withdrawEnable = withdrawEnable;
    }

    public String getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(String withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public String getWithdrawMax() {
        return withdrawMax;
    }

    public void setWithdrawMax(String withdrawMax) {
        this.withdrawMax = withdrawMax;
    }

    public String getWithdrawMin() {
        return withdrawMin;
    }

    public void setWithdrawMin(String withdrawMin) {
        this.withdrawMin = withdrawMin;
    }

    public boolean isSameAddress() {
        return sameAddress;
    }

    public void setSameAddress(boolean sameAddress) {
        this.sameAddress = sameAddress;
    }

    public int getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(int estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
