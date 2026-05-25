package com.neusoft.service;

import com.neusoft.po.Wallet;
import com.neusoft.po.Transaction;
import java.util.List;

public interface WalletService {
	Wallet getWallet(String userId) throws Exception;
	int addWallet(Wallet wallet) throws Exception;
	int recharge(String userId, Double amount) throws Exception;
	int pay(String userId, Double amount) throws Exception;
	int freezeMoney(String userId, Double amount) throws Exception;
	int unfreezeMoney(String userId, Double amount) throws Exception;
	int setVipStatus(String userId, Integer isVip, Double overdraftLimit) throws Exception;
	int repayOverdraft(String userId, Double amount) throws Exception;
	Double calculateInterest(String userId) throws Exception;
	List<Transaction> listTransaction(String userId) throws Exception;
}
