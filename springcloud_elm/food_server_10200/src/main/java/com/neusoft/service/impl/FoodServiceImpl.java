package com.neusoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.mapper.FoodMapper;
import com.neusoft.po.Food;
import com.neusoft.service.FoodService;

@Service
public class FoodServiceImpl implements FoodService{
	
	@Autowired
	private FoodMapper foodMapper;

	@Override
	public List<Food> listFoodByBusinessId(Integer businessId) {
		return foodMapper.listFoodByBusinessId(businessId);
	}

	@Override
	public Food getFoodById(Integer foodId) {
		return foodMapper.getFoodById(foodId);
	}

	@Override
	public int saveFood(Food food) {
		return foodMapper.saveFood(food);
	}

	@Override
	public int updateFood(Food food) {
		return foodMapper.updateFood(food);
	}

	@Override
	public int deleteFood(Integer foodId) {
		return foodMapper.deleteFood(foodId);
	}
}
