package com.neusoft.feign;

import com.neusoft.po.CommonResult;
import org.springframework.stereotype.Component;

@Component
public class PointsFeignClientCallBack implements PointsFeignClient {

    @Override
    public CommonResult getPoints(String userId) {
        return new CommonResult(500, "points-server is unavailable", null);
    }

    @Override
    public CommonResult addPoints(String userId, String channel, Double amount, Integer relatedId, String description) {
        return new CommonResult(500, "points-server is unavailable", null);
    }
}
