package com.neusoft.service.impl;

import com.neusoft.mapper.WalletMapper;
import com.neusoft.mapper.TransactionMapper;
import com.neusoft.po.Wallet;
import com.neusoft.po.Transaction;
import com.neusoft.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private TransactionMapper transactionMapper;

	private String getNowTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public Wallet getWallet(String userId) throws Exception {
		Wallet wallet = walletMapper.getWalletByUserId(userId);
		if (wallet == null) {
			wallet = new Wallet();
			wallet.setUserId(userId);
			wallet.setMoney(0.0);
			wallet.setFrozenMoney(0.0);
			wallet.setOverdraftLimit(0.0);
			wallet.setOverdraftAmount(0.0);
			wallet.setIsVip(0);
			walletMapper.saveWallet(wallet);
		}
		return wallet;
	}

	@Override
	@Transactional
	public int addWallet(Wallet wallet) throws Exception {
		Wallet existing = walletMapper.getWalletByUserId(wallet.getUserId());
		if (existing != null) {
			return 0;
		}
		if (wallet.getMoney() == null) wallet.setMoney(0.0);
		if (wallet.getFrozenMoney() == null) wallet.setFrozenMoney(0.0);
		if (wallet.getOverdraftLimit() == null) wallet.setOverdraftLimit(0.0);
		if (wallet.getOverdraftAmount() == null) wallet.setOverdraftAmount(0.0);
		if (wallet.getIsVip() == null) wallet.setIsVip(0);
		return walletMapper.saveWallet(wallet);
	}

	@Override
	public int recharge(String userId, Double amount) throws Exception {
		Wallet wallet = getWallet(userId);
		wallet.setMoney(wallet.getMoney() + amount);
		walletMapper.updateWallet(wallet);

		Transaction tx = new Transaction();
		tx.setMoney(amount);
		tx.setType(Transaction.TYPE_RECHARGE);
		tx.setToWallet(wallet.getWalletId());
		tx.setStatus(Transaction.STATUS_COMPLETED);
		tx.setDescription("钱包充值");
		tx.setCreateTime(getNowTime());
		transactionMapper.saveTransaction(tx);
		return 1;
	}

	@Override
	@Transactional
	public int pay(String userId, Double amount) throws Exception {
		Wallet wallet = getWallet(userId);
		double available = wallet.getMoney() - wallet.getFrozenMoney();
		double canPay = available + wallet.getOverdraftLimit() - wallet.getOverdraftAmount();

		if (canPay < amount) {
			return 0;
		}

		wallet.setMoney(wallet.getMoney() - amount);
		walletMapper.updateWallet(wallet);

		Transaction tx = new Transaction();
		tx.setMoney(amount);
		tx.setType(Transaction.TYPE_PAYMENT);
		tx.setFromWallet(wallet.getWalletId());
		tx.setStatus(Transaction.STATUS_COMPLETED);
		tx.setDescription("订单支付");
		tx.setCreateTime(getNowTime());
		transactionMapper.saveTransaction(tx);
		return 1;
	}

	@Override
	@Transactional
	public int freezeMoney(String userId, Double amount) throws Exception {
		Wallet wallet = getWallet(userId);
		if (wallet.getMoney() < amount) {
			return 0;
		}
		wallet.setMoney(wallet.getMoney() - amount);
		wallet.setFrozenMoney(wallet.getFrozenMoney() + amount);
		walletMapper.updateWallet(wallet);

		Transaction tx = new Transaction();
		tx.setMoney(amount);
		tx.setType(Transaction.TYPE_FREEZE);
		tx.setFromWallet(wallet.getWalletId());
		tx.setStatus(Transaction.STATUS_COMPLETED);
		tx.setDescription("冻结资金");
		tx.setCreateTime(getNowTime());
		transactionMapper.saveTransaction(tx);
		return 1;
	}

	@Override
	@Transactional
	public int unfreezeMoney(String userId, Double amount) throws Exception {
		Wallet wallet = getWallet(userId);
		if (wallet.getFrozenMoney() < amount) {
			return 0;
		}
		wallet.setMoney(wallet.getMoney() + amount);
		wallet.setFrozenMoney(wallet.getFrozenMoney() - amount);
		walletMapper.updateWallet(wallet);

		Transaction tx = new Transaction();
		tx.setMoney(amount);
		tx.setType(Transaction.TYPE_UNFREEZE);
		tx.setToWallet(wallet.getWalletId());
		tx.setStatus(Transaction.STATUS_COMPLETED);
		tx.setDescription("解冻资金");
		tx.setCreateTime(getNowTime());
		transactionMapper.saveTransaction(tx);
		return 1;
	}

	@Override
	public int setVipStatus(String userId, Integer isVip, Double overdraftLimit) throws Exception {
		Wallet wallet = getWallet(userId);
		wallet.setIsVip(isVip);
		wallet.setOverdraftLimit(overdraftLimit);
		return walletMapper.updateWallet(wallet);
	}

	@Override
	@Transactional
	public int repayOverdraft(String userId, Double amount) throws Exception {
		Wallet wallet = getWallet(userId);
		if (wallet.getOverdraftAmount() <= 0) {
			return 0;
		}
		double repay = Math.min(amount, wallet.getOverdraftAmount());
		wallet.setMoney(wallet.getMoney() - repay);
		wallet.setOverdraftAmount(wallet.getOverdraftAmount() - repay);
		walletMapper.updateWallet(wallet);

		Transaction tx = new Transaction();
		tx.setMoney(repay);
		tx.setType(Transaction.TYPE_REPAY);
		tx.setFromWallet(wallet.getWalletId());
		tx.setStatus(Transaction.STATUS_COMPLETED);
		tx.setDescription("偿还透支");
		tx.setCreateTime(getNowTime());
		transactionMapper.saveTransaction(tx);
		return 1;
	}

	@Override
	public Double calculateInterest(String userId) throws Exception {
		Wallet wallet = getWallet(userId);
		if (wallet.getIsVip() == 0 || wallet.getOverdraftAmount() <= 0 || wallet.getOverdraftTime() == null) {
			return 0.0;
		}
		long days = java.time.LocalDateTime.now()
			.compareTo(java.time.LocalDateTime.parse(wallet.getOverdraftTime(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		if (days <= 7) {
			return 0.0;
		}
		double rate = 0.001;
		return wallet.getOverdraftAmount() * rate * (days - 7);
	}

	@Override
	public List<Transaction> listTransaction(String userId) throws Exception {
		getWallet(userId);
		return transactionMapper.listTransactionByUserId(userId);
	}
}
