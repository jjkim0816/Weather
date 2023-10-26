package com.zerobase.weather.error;

@SuppressWarnings("serial")
public class InvalidDate extends RuntimeException{
	private static final String MESSAGE = "너무 미래의 날짜 입니다.";
	
	public InvalidDate() {
		super(MESSAGE);
	}
}
