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
@DisplayName("jpa 테스트")
class JpaMemoRepositoryTest {
	@Autowired
	JpaMemoRepository jpaMemoRepository;

	@Test
	@DisplayName("메모 저장 테스트")
	void insertMemoTest () {
		// given
		Memo memo = new Memo(2, "this is the second memo");
		
		// when
		jpaMemoRepository.save(memo);
		
		// then
		List<Memo> result = jpaMemoRepository.findAll();
		assertTrue(result.size() > 0);
	}
	
	@Test
	@DisplayName("아이디로 메모 조회")
	void findByIdTest() {
		// given
		Memo newMemo = new Memo(6, "this is the seventh memo");
		
		// when
		Memo memo = jpaMemoRepository.save(newMemo);
		System.out.println("memo id : " + memo.getId());
		
		// then
		Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
		assertEquals("this is the seventh memo", result.get().getText());
	}
}
