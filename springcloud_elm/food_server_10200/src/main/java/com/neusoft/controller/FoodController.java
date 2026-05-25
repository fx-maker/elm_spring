package com.neusoft.controller;

import java.util.List;

import com.neusoft.po.CommonResult;
import com.neusoft.po.Food;
import com.neusoft.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin("*")
@RestController
@RequestMapping("/FoodController")
@RefreshScope //开启动态刷新
public class FoodController {

	@Autowired
	private FoodService foodService;

	@GetMapping("/listFoodByBusinessId/{businessId}")
	public CommonResult<List> listFoodByBusinessId(@PathVariable("businessId") Integer businessId) throws Exception {
		List<Food> list = foodService.listFoodByBusinessId(businessId);
		return new CommonResult(200, "success", list);
	}

	@GetMapping("/getFoodById/{foodId}")
	public CommonResult<Food> getFoodById(@PathVariable("foodId") Integer foodId) throws Exception {
		Food food = foodService.getFoodById(foodId);
		return new CommonResult<>(200, "success", food);
	}

	@PostMapping("/saveFood")
	public CommonResult<Integer> saveFood(@RequestBody Food food) throws Exception {
		int result = foodService.saveFood(food);
		return new CommonResult<>(200, "success", result);
	}

	@PutMapping("/updateFood")
	public CommonResult<Integer> updateFood(@RequestBody Food food) throws Exception {
		int result = foodService.updateFood(food);
		return new CommonResult<>(200, "success", result);
	}

	@DeleteMapping("/deleteFood/{foodId}")
	public CommonResult<Integer> deleteFood(@PathVariable("foodId") Integer foodId) throws Exception {
		int result = foodService.deleteFood(foodId);
		return new CommonResult<>(200, "success", result);
	}
}

