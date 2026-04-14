package com.neusoft.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.neusoft.po.CommonResult;

//@RefreshScope //开启动态刷新
@RestController
public class FallbackController {
	@RequestMapping("/fallback")
	public CommonResult fallback() {
		return new CommonResult(403, "Gateway触发了熔断降级", null);
	}
}
