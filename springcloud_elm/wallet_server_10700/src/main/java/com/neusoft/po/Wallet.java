package com.neusoft.po;

import java.io.Serializable;

public class Wallet implements Serializable {
	private Integer walletId;
	private Double money;
	private String userId;
	private Double frozenMoney;
	private Double overdraftLimit;
	private Double overdraftAmount;
	private String overdraftTime;
	private Integer isVip;

	public Integer getWalletId() { return walletId; }
	public void setWalletId(Integer walletId) { this.walletId = walletId; }
	public Double getMoney() { return money; }
	public void setMoney(Double money) { this.money = money; }
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public Double getFrozenMoney() { return frozenMoney; }
	public void setFrozenMoney(Double frozenMoney) { this.frozenMoney = frozenMoney; }
	public Double getOverdraftLimit() { return overdraftLimit; }
	public void setOverdraftLimit(Double overdraftLimit) { this.overdraftLimit = overdraftLimit; }
	public Double getOverdraftAmount() { return overdraftAmount; }
	public void setOverdraftAmount(Double overdraftAmount) { this.overdraftAmount = overdraftAmount; }
	public String getOverdraftTime() { return overdraftTime; }
	public void setOverdraftTime(String overdraftTime) { this.overdraftTime = overdraftTime; }
	public Integer getIsVip() { return isVip; }
	public void setIsVip(Integer isVip) { this.isVip = isVip; }
}
