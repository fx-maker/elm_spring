package com.neusoft.mapper;

import com.neusoft.po.PointsRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PointsRuleMapper {
	List<PointsRule> listPointsRule(@Param("channel") String channel, @Param("ruleType") String ruleType);
	PointsRule getPointsRuleById(@Param("ruleId") Integer ruleId);
	int savePointsRule(PointsRule rule);
	int updatePointsRule(PointsRule rule);
	int deletePointsRule(@Param("ruleId") Integer ruleId);
}
