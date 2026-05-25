package com.neusoft.po;

import java.io.Serializable;

public class Points implements Serializable {
	private Integer pointsId;
	private String userId;
	private Integer totalPoints;
	private Integer availablePoints;

	public Integer getPointsId() { return pointsId; }
	public void setPointsId(Integer pointsId) { this.pointsId = pointsId; }
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public Integer getTotalPoints() { return totalPoints; }
	public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
	public Integer getAvailablePoints() { return availablePoints; }
	public void setAvailablePoints(Integer availablePoints) { this.availablePoints = availablePoints; }
}
