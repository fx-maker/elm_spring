package com.neusoft.feign;

import com.neusoft.po.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "wallet-server", fallback = WalletFeignClientCallBack.class)
public interface WalletFeignClient {

    @GetMapping("/WalletController/getWallet/{userId}")
    public CommonResult getWallet(@PathVariable("userId") String userId);

    @PostMapping("/WalletController/pay/{userId}/{amount}")
    public CommonResult pay(@PathVariable("userId") String userId, @PathVariable("amount") Double amount);
}
