package com.zhenzhen.demo.springboot.dto;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("helloDto测试")
@Data
public class HelloDto {
	@ApiModelProperty("名字")
	@NotEmpty(message = "名字不能为空")
	private String name;
	@ApiModelProperty("日期")
	private Date date;
	
	public HelloDto(String name, Date date) {
		super();
		this.name = name;
		this.date = date;
	}

	public HelloDto() {
		super();
	}
	
	
	
	
}
