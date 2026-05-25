package com.neusoft.po;

import java.io.Serializable;

public class PointsDetail implements Serializable {
	private Integer detailId;
	private String userId;
	private Integer points;
	private String type;
	private String expireTime;
	private Integer relatedId;
	private String description;
	private String status;
	private String createTime;

	public Integer getDetailId() { return detailId; }
	public void setDetailId(Integer detailId) { this.detailId = detailId; }
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public Integer getPoints() { return points; }
	public void setPoints(Integer points) { this.points = points; }
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public String getExpireTime() { return expireTime; }
	public void setExpireTime(String expireTime) { this.expireTime = expireTime; }
	public Integer getRelatedId() { return relatedId; }
	public void setRelatedId(Integer relatedId) { this.relatedId = relatedId; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public String getCreateTime() { return createTime; }
	public void setCreateTime(String createTime) { this.createTime = createTime; }
}
