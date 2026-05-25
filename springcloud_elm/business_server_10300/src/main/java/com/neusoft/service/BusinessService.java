package com.neusoft.service;

import java.util.List;

import com.neusoft.po.Business;

public interface BusinessService {

	public List<Business> listBusinessByOrderTypeId(Integer orderTypeId);
	public Business getBusinessById(Integer businessId);
	public int saveBusiness(Business business);
	public int updateOwner(Integer businessId, String ownerId);
}
