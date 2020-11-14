package com.zhenzhen.demo.springboot.condition;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HelloCondition {
	@ApiModelProperty(value = "名字",required = true)
	@NotEmpty(message = "名字不能为空")
	private String name;
	
	@ApiModelProperty(value = "开始日期",required = true)
	@NotNull(message = "开始时间不能为空")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	
	@ApiModelProperty(value = "结束日期",required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "接收时间不能为空")
	private Date endDate;
	
	public void setStartDate(Date startDate) {
		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);
		this.startDate = startDate;
	}
	
	public void setEndDate(Date endDate) {
		endDate.setHours(23);
		endDate.setMinutes(59);
		endDate.setSeconds(59);
		this.endDate = endDate;
	}
}
