package com.neusoft.feign;

import com.neusoft.po.CommonResult;
import org.springframework.stereotype.Component;

@Component
public class WalletFeignClientCallBack implements WalletFeignClient {
    @Override
    public CommonResult getWallet(String userId) {
        return new CommonResult(403, "钱包服务不可用", null);
    }

    @Override
    public CommonResult pay(String userId, Double amount) {
        return new CommonResult(403, "支付服务不可用", null);
    }
}
