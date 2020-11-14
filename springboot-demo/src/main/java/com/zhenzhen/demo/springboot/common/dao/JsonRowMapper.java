package com.zhenzhen.demo.springboot.common.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.alibaba.fastjson.JSONObject;


public class JsonRowMapper implements RowMapper<JSONObject> {

	
	public JSONObject mapRow(ResultSet rs, int row) throws SQLException {
		String key = null;
		Object obj = null;
		JSONObject jsonObject = new JSONObject();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			key = JdbcUtils.lookupColumnName(rsmd, i);
			obj = JdbcUtils.getResultSetValue(rs, i);
			jsonObject.put(key, obj);
		}
		return jsonObject;
	}
}