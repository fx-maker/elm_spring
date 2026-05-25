package com.neusoft.feign;

import com.neusoft.po.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "points-server", fallback = PointsFeignClientCallBack.class)
public interface PointsFeignClient {

    @GetMapping("/PointsController/getPoints/{userId}")
    public CommonResult getPoints(@PathVariable("userId") String userId);

    @PostMapping("/PointsController/addPoints")
    public CommonResult addPoints(@RequestParam("userId") String userId,
                                  @RequestParam("channel") String channel,
                                  @RequestParam(value = "amount", required = false) Double amount,
                                  @RequestParam(value = "relatedId", required = false) Integer relatedId,
                                  @RequestParam(value = "description", required = false) String description);
}
