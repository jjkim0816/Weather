package com.zerobase.weather.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerobase.weather.domain.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer>{
	List<Diary> findAllByDate(LocalDate date);
	List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
	Diary findFirstByDate(LocalDate date);
	
	@Transactional
	void deleteAllByDate(LocalDate date);
}
