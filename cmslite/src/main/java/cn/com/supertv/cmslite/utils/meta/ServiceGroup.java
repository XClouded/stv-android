package cn.com.supertv.cmslite.utils.meta;

/**
 * Description: 为构造数据方便的元数据类
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-4-28 下午04:05:49 MagicY created
 */
public class ServiceGroup {
	private Long id;
	private String serviceGroupId;
	private String serviceGroupName;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getServiceGroupId() {
		return serviceGroupId;
	}
	
	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
	
	public String getServiceGroupName() {
		return serviceGroupName;
	}
	
	public void setServiceGroupName(String serviceGroupName) {
		this.serviceGroupName = serviceGroupName;
	}
}
