package com.zerobase.weather.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerobase.weather.domain.Diary;
import com.zerobase.weather.service.DiaryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DiaryController {
	
	private final DiaryService diaryService;
	
	@ApiOperation("나의 일기를 찾아 보아요~")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "일기 조회 성공"),
		@ApiResponse(code = 404, message = "일기 데이터 미존재"),
		@ApiResponse(code = 500, message = "서버 내부 에러")
	} )
	@GetMapping("/diary")
	List<Diary> findDiary(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@ApiParam(value = "yyyy-MM-dd", example = "2020-02-02")
			LocalDate date
	) {
		System.out.println("get controller date: " + date);
		return diaryService.findDiary(date);
	}
	
	@ApiOperation("기간별로 나의 일기를 찾아 보아요~")
	@GetMapping("/diaries")
	List<Diary> findDiaries(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@ApiParam(value = "시작날짜 : yyyy-MM-dd", example = "2020-02-02")
			LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@ApiParam(value = "종료날짜 : yyyy-MM-dd", example = "2020-02-03")
			LocalDate endDate
	) {
		return diaryService.findDiaries(startDate, endDate);
	}
	
	@ApiOperation(value = "일기를 저장해 보아요~", notes = "일기 저장")
	@PostMapping("/diary")
	void createDiary(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@ApiParam(value = "yyyy-MM-dd", example = "2020-02-02")
			LocalDate date,
		@RequestBody
		@ApiParam(value = "문자열 데이터 입력", example = "나의 일기 입니다.")
			String text
	) {
		diaryService.createDiary(date, text);
	}
	
	@ApiOperation("일기를 수정해 보아요~")
	@PutMapping("/diary")
	void modifyDiary(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@ApiParam(value = "yyyy-MM-dd", example = "2020-02-02")
			LocalDate date,
		@RequestBody
		@ApiParam(value = "문자열 데이터 입력", example = "나의 일기 입니다.")
			String text
	) {
		diaryService.modifyDiary(date, text);
	}
	
	@ApiOperation("일기를 삭제해 보아요~")
	@DeleteMapping("/diary")
	void deleteDiary(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@ApiParam(value = "yyyy-MM-dd", example = "2020-02-02")
			LocalDate date
	) {
		diaryService.deleteDiary(date);
	}
}
