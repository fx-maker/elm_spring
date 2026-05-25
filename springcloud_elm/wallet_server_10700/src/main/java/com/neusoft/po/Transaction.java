package com.neusoft.po;

import java.io.Serializable;

public class Transaction implements Serializable {
	public static final int TYPE_RECHARGE = 0;
	public static final int TYPE_WITHDRAW = 1;
	public static final int TYPE_PAYMENT = 2;
	public static final int TYPE_FREEZE = 3;
	public static final int TYPE_UNFREEZE = 4;
	public static final int TYPE_OVERDRAFT = 5;
	public static final int TYPE_REPAY = 6;
	public static final int TYPE_INTEREST = 7;

	public static final int STATUS_PENDING = 0;
	public static final int STATUS_COMPLETED = 1;
	public static final int STATUS_FAILED = 2;
	public static final int STATUS_CANCELLED = 3;

	private Integer transactionId;
	private Double money;
	private Integer type;
	private Integer fromWallet;
	private Integer toWallet;
	private Integer status;
	private Integer orderId;
	private String description;
	private String createTime;

	public Integer getTransactionId() { return transactionId; }
	public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }
	public Double getMoney() { return money; }
	public void setMoney(Double money) { this.money = money; }
	public Integer getType() { return type; }
	public void setType(Integer type) { this.type = type; }
	public Integer getFromWallet() { return fromWallet; }
	public void setFromWallet(Integer fromWallet) { this.fromWallet = fromWallet; }
	public Integer getToWallet() { return toWallet; }
	public void setToWallet(Integer toWallet) { this.toWallet = toWallet; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public Integer getOrderId() { return orderId; }
	public void setOrderId(Integer orderId) { this.orderId = orderId; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getCreateTime() { return createTime; }
	public void setCreateTime(String createTime) { this.createTime = createTime; }
}
