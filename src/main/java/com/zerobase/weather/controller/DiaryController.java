package com.zerobase.weather.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerobase.weather.domain.Diary;
import com.zerobase.weather.service.DiaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DiaryController {
	
	private final DiaryService diaryService;
	
	@GetMapping("/diary")
	List<Diary> findDiary(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate date
	) {
		System.out.println("get controller date: " + date);
		return diaryService.findDiary(date);
	}
	
	@GetMapping("/diaries")
	List<Diary> findDiaries(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate endDate
	) {
		return diaryService.findDiaries(startDate, endDate);
	}
	
	@PostMapping("/diary")
	void createDiary(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate date,
		@RequestBody String text
	) {
		diaryService.createDiary(date, text);
	}
	
	@PutMapping("/diary")
	void modifyDiary(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate date,
		@RequestBody String text
	) {
		diaryService.modifyDiary(date, text);
	}
}
