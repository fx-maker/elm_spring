package com.neusoft.service;

import com.neusoft.po.Points;
import com.neusoft.po.PointsDetail;
import com.neusoft.po.PointsRule;
import java.util.List;

public interface PointsService {
	Points getPoints(String userId) throws Exception;
	Points addPoints(String userId, String channel, Double amount, Integer relatedId, String description) throws Exception;
	Points consumePoints(String userId, Integer pointsAmount, String channel, Integer relatedId, String description) throws Exception;
	List<PointsDetail> listPointsDetail(String userId) throws Exception;
	int addPointsRule(PointsRule rule) throws Exception;
	int updatePointsRule(PointsRule rule) throws Exception;
	List<PointsRule> listPointsRule(String channel, String ruleType) throws Exception;
	PointsRule getPointsRuleById(Integer ruleId) throws Exception;
	int deletePointsRule(Integer ruleId) throws Exception;
}
