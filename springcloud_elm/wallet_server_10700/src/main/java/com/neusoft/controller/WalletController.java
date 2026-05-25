package com.neusoft.controller;

import com.neusoft.po.CommonResult;
import com.neusoft.po.Wallet;
import com.neusoft.po.Transaction;
import com.neusoft.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/WalletController")
@RefreshScope
public class WalletController {

	@Autowired
	private WalletService walletService;

	@GetMapping("/getWallet/{userId}")
	public CommonResult<Wallet> getWallet(@PathVariable("userId") String userId) throws Exception {
		Wallet wallet = walletService.getWallet(userId);
		return new CommonResult<>(200, "success", wallet);
	}

	@PostMapping("/addWallet")
	public CommonResult<Integer> addWallet(@RequestBody Wallet wallet) throws Exception {
		int result = walletService.addWallet(wallet);
		return new CommonResult<>(200, "success", result);
	}

	@PostMapping("/recharge/{userId}/{amount}")
	public CommonResult<Integer> recharge(@PathVariable("userId") String userId,
			@PathVariable("amount") Double amount) throws Exception {
		int result = walletService.recharge(userId, amount);
		return new CommonResult<>(200, "success", result);
	}

	@PostMapping("/pay/{userId}/{amount}")
	public CommonResult<Integer> pay(@PathVariable("userId") String userId,
			@PathVariable("amount") Double amount) throws Exception {
		int result = walletService.pay(userId, amount);
		return new CommonResult<>(200, "success", result);
	}

	@PostMapping("/freezeMoney/{userId}/{amount}")
	public CommonResult<Integer> freezeMoney(@PathVariable("userId") String userId,
			@PathVariable("amount") Double amount) throws Exception {
		int result = walletService.freezeMoney(userId, amount);
		return new CommonResult<>(200, "success", result);
	}

	@PostMapping("/unfreezeMoney/{userId}/{amount}")
	public CommonResult<Integer> unfreezeMoney(@PathVariable("userId") String userId,
			@PathVariable("amount") Double amount) throws Exception {
		int result = walletService.unfreezeMoney(userId, amount);
		return new CommonResult<>(200, "success", result);
	}

	@PostMapping("/setVipStatus/{userId}/{isVip}/{overdraftLimit}")
	public CommonResult<Integer> setVipStatus(@PathVariable("userId") String userId,
			@PathVariable("isVip") Integer isVip,
			@PathVariable("overdraftLimit") Double overdraftLimit) throws Exception {
		int result = walletService.setVipStatus(userId, isVip, overdraftLimit);
		return new CommonResult<>(200, "success", result);
	}

	@PostMapping("/repayOverdraft/{userId}/{amount}")
	public CommonResult<Integer> repayOverdraft(@PathVariable("userId") String userId,
			@PathVariable("amount") Double amount) throws Exception {
		int result = walletService.repayOverdraft(userId, amount);
		return new CommonResult<>(200, "success", result);
	}

	@GetMapping("/calculateInterest/{userId}")
	public CommonResult<Double> calculateInterest(@PathVariable("userId") String userId) throws Exception {
		Double interest = walletService.calculateInterest(userId);
		return new CommonResult<>(200, "success", interest);
	}

	@GetMapping("/listTransaction/{userId}")
	public CommonResult<List> listTransaction(@PathVariable("userId") String userId) throws Exception {
		List<Transaction> list = walletService.listTransaction(userId);
		return new CommonResult<>(200, "success", list);
	}
}
