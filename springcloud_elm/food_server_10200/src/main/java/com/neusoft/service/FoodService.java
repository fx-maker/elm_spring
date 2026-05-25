package com.neusoft.service;

import java.util.List;

import com.neusoft.po.Food;

public interface FoodService {

	public List<Food> listFoodByBusinessId(Integer businessId);
	public Food getFoodById(Integer foodId);
	public int saveFood(Food food);
	public int updateFood(Food food);
	public int deleteFood(Integer foodId);
}
