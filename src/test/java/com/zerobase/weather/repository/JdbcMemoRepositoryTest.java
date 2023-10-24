package com.zerobase.weather.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.zerobase.weather.domain.Memo;

@SpringBootTest
@Transactional
@DisplayName("jdbc 테스트")
class JdbcMemoRepositoryTest {

	@Autowired
	JdbcMemoRepository jdbcMemoRepository;
	
	@Test
	@DisplayName("메모 테이블 insert test")
	void insertMemo() {
		// given
		Memo memo = new Memo (2, "this is second memo");
		
		// when
		jdbcMemoRepository.save(memo);

		// then
		Optional<Memo> result = jdbcMemoRepository.findById(2);
		
		assertEquals(2, result.get().getId());
		assertEquals('t', result.get().getText().charAt(0));
	}
	
	@Test
	@DisplayName("모든 메모 가져오기")
	void findAllMemoTest() {
		// given
		// when
		List<Memo> memos = jdbcMemoRepository.findAll();
		System.out.println("List: " +  memos);

		// then
		assertNotNull(memos);
	}

}
