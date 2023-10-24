package com.zerobase.weather.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.zerobase.weather.domain.Memo;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcMemoRepository {
	private final JdbcTemplate jdbcTemplate;
	
	public Memo save(Memo memo) {
		String sql = "INSERT INTO memo VALUES (?,?)";
		jdbcTemplate.update(sql, memo.getId(), memo.getText());

		return memo;
	}
	
	public List<Memo> findAll() {
		String sql = "SELECT id, text FROM memo";
		
		return jdbcTemplate.query(sql, memoRowMapper());
	}
	
	public Optional<Memo> findById(int id) {
		String sql = "SELECT id, text FROM memo WHERE id = ?";
		return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
	}
	
	private RowMapper<Memo> memoRowMapper() {
		return (rs, rowNum) -> new Memo(
					rs.getInt("id"),
					rs.getString("text")
		);
	}
}
