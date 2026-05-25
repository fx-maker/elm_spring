package com.neusoft.po;

import java.io.Serializable;

public class PointsRule implements Serializable {
	private Integer ruleId;
	private String ruleName;
	private String ruleType;
	private String channel;
	private Double ratio;
	private Integer fixedPoints;
	private Integer validDays;
	private Integer isEnabled;
	private String startTime;
	private String endTime;
	private String description;

	public Integer getRuleId() { return ruleId; }
	public void setRuleId(Integer ruleId) { this.ruleId = ruleId; }
	public String getRuleName() { return ruleName; }
	public void setRuleName(String ruleName) { this.ruleName = ruleName; }
	public String getRuleType() { return ruleType; }
	public void setRuleType(String ruleType) { this.ruleType = ruleType; }
	public String getChannel() { return channel; }
	public void setChannel(String channel) { this.channel = channel; }
	public Double getRatio() { return ratio; }
	public void setRatio(Double ratio) { this.ratio = ratio; }
	public Integer getFixedPoints() { return fixedPoints; }
	public void setFixedPoints(Integer fixedPoints) { this.fixedPoints = fixedPoints; }
	public Integer getValidDays() { return validDays; }
	public void setValidDays(Integer validDays) { this.validDays = validDays; }
	public Integer getIsEnabled() { return isEnabled; }
	public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
	public String getStartTime() { return startTime; }
	public void setStartTime(String startTime) { this.startTime = startTime; }
	public String getEndTime() { return endTime; }
	public void setEndTime(String endTime) { this.endTime = endTime; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
}
