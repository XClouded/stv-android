package cn.com.supertv.cmslite.utils.meta;

/**
 * Description: 为构造数据方便的元数据类
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-4-28 下午04:02:35 MagicY created
 */
public class Product {
	private Long id;
	private String productId;
	private String productName;
	
	public Product() {
		
	}
	
	public Product(Long id, String productId, String productName) {
		this.id = id;
		this.productId = productId;
		this.productName = productName;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
}
