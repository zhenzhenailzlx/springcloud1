package com.zhenzhen.demo.springboot.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.fastjson.JSONObject;

public class BaseDao {

	private static final JsonRowMapper JSON_ROW_MAPPER = new JsonRowMapper();

	private NamedParameterJdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public List<JSONObject> queryForJsonList(String sql, Map<String,Object> paramMap) {
		return jdbcTemplate.query(sql, paramMap, JSON_ROW_MAPPER);
	}

	public List<JSONObject> queryForJsonList(String sql, MapSqlParameterSource paramMap) {
		return jdbcTemplate.query(sql, paramMap, JSON_ROW_MAPPER);
	}

	public List<JSONObject> queryForJsonList(String sql) {
		return jdbcTemplate.query(sql, JSON_ROW_MAPPER);
	}


	public JSONObject queryForJsonObject(String sql, Map<String,Object> paramMap) {
		List<JSONObject> jsonList = queryForJsonList(sql, paramMap);
		if (jsonList == null || jsonList.size() < 1) {
			return null;
		}
		return jsonList.get(0);
	}
	
	public JSONObject queryForJsonObject(String sql, MapSqlParameterSource paramMap) {
		List<JSONObject> jsonList = queryForJsonList(sql, paramMap);
		if (jsonList == null || jsonList.size() < 1) {
			return null;
		}
		return jsonList.get(0);
	}
	
	public JSONObject queryForJsonObject(String sql) {
		List<JSONObject> jsonList = queryForJsonList(sql);
		if (jsonList == null || jsonList.size() < 1) {
			return null;
		}
		return jsonList.get(0);
	}

	public int queryForInt(String sql, Map<String, Object> paramMap) {
		List<Integer> dataList = this.jdbcTemplate.queryForList(sql, paramMap, Integer.class);
		if (dataList == null || dataList.size() < 1) {
			return 0;
		}
		return dataList.get(0);
	}
	
	public int queryForInt(String sql, MapSqlParameterSource paramMap) {
		List<Integer> dataList = this.jdbcTemplate.queryForList(sql, paramMap, Integer.class);
		if (dataList == null || dataList.size() < 1) {
			return 0;
		}
		return dataList.get(0);
	}
	
	public int queryForInt(String sql) {
		return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
	}

}
