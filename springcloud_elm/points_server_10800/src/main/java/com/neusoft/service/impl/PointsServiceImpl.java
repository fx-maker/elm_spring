package com.neusoft.service.impl;

import com.neusoft.mapper.PointsMapper;
import com.neusoft.mapper.PointsDetailMapper;
import com.neusoft.mapper.PointsRuleMapper;
import com.neusoft.po.Points;
import com.neusoft.po.PointsDetail;
import com.neusoft.po.PointsRule;
import com.neusoft.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PointsServiceImpl implements PointsService {

	@Autowired
	private PointsMapper pointsMapper;

	@Autowired
	private PointsDetailMapper pointsDetailMapper;

	@Autowired
	private PointsRuleMapper pointsRuleMapper;

	private String getNowTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public Points getPoints(String userId) throws Exception {
		Points points = pointsMapper.getPointsByUserId(userId);
		if (points == null) {
			points = new Points();
			points.setUserId(userId);
			points.setTotalPoints(0);
			points.setAvailablePoints(0);
			pointsMapper.savePoints(points);
		}
		return points;
	}

	@Override
	@Transactional
	public Points addPoints(String userId, String channel, Double amount, Integer relatedId, String description) throws Exception {
		Points points = getPoints(userId);
		int earnedPoints = 0;

		List<PointsRule> rules = pointsRuleMapper.listPointsRule(channel, "EARN");
		for (PointsRule rule : rules) {
			if (rule.getIsEnabled() != 1) continue;
			if (rule.getRatio() != null && rule.getRatio() > 0) {
				earnedPoints += (int)(amount * rule.getRatio());
			} else if (rule.getFixedPoints() != null && rule.getFixedPoints() > 0) {
				earnedPoints += rule.getFixedPoints();
			}
		}

		if (earnedPoints > 0) {
			points.setTotalPoints(points.getTotalPoints() + earnedPoints);
			points.setAvailablePoints(points.getAvailablePoints() + earnedPoints);
			pointsMapper.updatePoints(points);

			PointsDetail detail = new PointsDetail();
			detail.setUserId(userId);
			detail.setPoints(earnedPoints);
			detail.setType(channel);
			detail.setRelatedId(relatedId);
			detail.setDescription(description != null ? description : "获得积分");
			detail.setStatus("AVAILABLE");
			detail.setCreateTime(getNowTime());
			if (rules.size() > 0 && rules.get(0).getValidDays() != null) {
				detail.setExpireTime(LocalDateTime.now().plusDays(rules.get(0).getValidDays())
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
			pointsDetailMapper.savePointsDetail(detail);
		}
		return points;
	}

	@Override
	@Transactional
	public Points consumePoints(String userId, Integer pointsAmount, String channel, Integer relatedId, String description) throws Exception {
		Points points = getPoints(userId);
		if (points.getAvailablePoints() < pointsAmount) {
			return points;
		}

		points.setAvailablePoints(points.getAvailablePoints() - pointsAmount);
		pointsMapper.updatePoints(points);

		PointsDetail detail = new PointsDetail();
		detail.setUserId(userId);
		detail.setPoints(-pointsAmount);
		detail.setType(channel);
		detail.setRelatedId(relatedId);
		detail.setDescription(description != null ? description : "消费积分");
		detail.setStatus("USED");
		detail.setCreateTime(getNowTime());
		pointsDetailMapper.savePointsDetail(detail);
		return points;
	}

	@Override
	public List<PointsDetail> listPointsDetail(String userId) throws Exception {
		getPoints(userId);
		return pointsDetailMapper.listPointsDetailByUserId(userId);
	}

	@Override
	public int addPointsRule(PointsRule rule) throws Exception {
		return pointsRuleMapper.savePointsRule(rule);
	}

	@Override
	public int updatePointsRule(PointsRule rule) throws Exception {
		return pointsRuleMapper.updatePointsRule(rule);
	}

	@Override
	public List<PointsRule> listPointsRule(String channel, String ruleType) throws Exception {
		return pointsRuleMapper.listPointsRule(channel, ruleType);
	}

	@Override
	public PointsRule getPointsRuleById(Integer ruleId) throws Exception {
		return pointsRuleMapper.getPointsRuleById(ruleId);
	}

	@Override
	public int deletePointsRule(Integer ruleId) throws Exception {
		return pointsRuleMapper.deletePointsRule(ruleId);
	}
}
