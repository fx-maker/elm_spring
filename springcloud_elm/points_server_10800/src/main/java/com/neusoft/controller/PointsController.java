package com.neusoft.controller;

import com.neusoft.po.CommonResult;
import com.neusoft.po.Points;
import com.neusoft.po.PointsDetail;
import com.neusoft.po.PointsRule;
import com.neusoft.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/PointsController")
@RefreshScope
public class PointsController {

	@Autowired
	private PointsService pointsService;

	@GetMapping("/getPoints/{userId}")
	public CommonResult<Points> getPoints(@PathVariable("userId") String userId) throws Exception {
		Points points = pointsService.getPoints(userId);
		return new CommonResult<>(200, "success", points);
	}

	@PostMapping("/addPoints")
	public CommonResult<Points> addPoints(@RequestParam("userId") String userId,
			@RequestParam("channel") String channel,
			@RequestParam(value = "amount", required = false) Double amount,
			@RequestParam(value = "relatedId", required = false) Integer relatedId,
			@RequestParam(value = "description", required = false) String description) throws Exception {
		Points points = pointsService.addPoints(userId, channel, amount, relatedId, description);
		return new CommonResult<>(200, "success", points);
	}

	@PostMapping("/consumePoints")
	public CommonResult<Points> consumePoints(@RequestParam("userId") String userId,
			@RequestParam("pointsAmount") Integer pointsAmount,
			@RequestParam("channel") String channel,
			@RequestParam(value = "relatedId", required = false) Integer relatedId,
			@RequestParam(value = "description", required = false) String description) throws Exception {
		Points points = pointsService.consumePoints(userId, pointsAmount, channel, relatedId, description);
		return new CommonResult<>(200, "success", points);
	}

	@GetMapping("/listPointsDetail/{userId}")
	public CommonResult<List> listPointsDetail(@PathVariable("userId") String userId) throws Exception {
		List<PointsDetail> list = pointsService.listPointsDetail(userId);
		return new CommonResult<>(200, "success", list);
	}

	@PostMapping("/addPointsRule")
	public CommonResult<Integer> addPointsRule(@RequestBody PointsRule rule) throws Exception {
		int result = pointsService.addPointsRule(rule);
		return new CommonResult<>(200, "success", result);
	}

	@PostMapping("/updatePointsRule")
	public CommonResult<Integer> updatePointsRule(@RequestBody PointsRule rule) throws Exception {
		int result = pointsService.updatePointsRule(rule);
		return new CommonResult<>(200, "success", result);
	}

	@GetMapping("/listPointsRule")
	public CommonResult<List> listPointsRule(
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "ruleType", required = false) String ruleType) throws Exception {
		List<PointsRule> list = pointsService.listPointsRule(channel, ruleType);
		return new CommonResult<>(200, "success", list);
	}

	@GetMapping("/getPointsRuleById/{ruleId}")
	public CommonResult<PointsRule> getPointsRuleById(@PathVariable("ruleId") Integer ruleId) throws Exception {
		PointsRule rule = pointsService.getPointsRuleById(ruleId);
		return new CommonResult<>(200, "success", rule);
	}

	@PostMapping("/deletePointsRule/{ruleId}")
	public CommonResult<Integer> deletePointsRule(@PathVariable("ruleId") Integer ruleId) throws Exception {
		int result = pointsService.deletePointsRule(ruleId);
		return new CommonResult<>(200, "success", result);
	}
}
