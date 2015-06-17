/**    
 * FileName: ResultDto.java
 * Version Info: commonComponent
 * Date: 2015年6月17日 
 * Copyright  Corporation 2015     
 * All Rights Reserved  
 */

package com.lx.component.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * Project Name：commonComponent Class Description： Author ：xliu Create
 * Time：2015年6月17日 下午1:28:47
 * 
 */

// 增加字段过滤，即JSON中不显示该字段，取决于JsonMapper类中的写法定义
@JsonFilter("FB_UID_filter")
public class ResultDto {

    private String userName;

    @JsonSerialize(using = CustomDate.class)
    private Date registerDate;

    private Integer age;
    private List<String> orders;

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public Date getRegisterDate() {
	return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
	this.registerDate = registerDate;
    }

    public Integer getAge() {
	return age;
    }

    public void setAge(Integer age) {
	this.age = age;
    }

    public List<String> getOrders() {
	return orders;
    }

    public void setOrders(List<String> orders) {
	this.orders = orders;
    }

}
