package com.zerobase.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zerobase.weather.domain.Diary;
import com.zerobase.weather.repository.DiaryRepository;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {

	@Value("${openweather.key}")
	private String apiKey;
	
	private final DiaryRepository diaryRepository;

	public void createDiary(LocalDate date, String text) {
		String weatherData = getWeatherString();

		Map<String, Object> parsedWeather = parseWeather(weatherData);

		Diary diary = new Diary();
		diary.setWeather(parsedWeather.get("main").toString());
		diary.setIcon(parsedWeather.get("icon").toString());
		diary.setTemperature(Double.parseDouble(parsedWeather.get("temp").toString()));
		diary.setText(text);
		diary.setDate(date);
		
		diaryRepository.save(diary);
	}
	
	private String getWeatherString() {
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Gwangju&appid=" + apiKey;
		
		try {
			URL url = new URL(apiUrl);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			}

			String inputLine; 
			StringBuilder sb = new StringBuilder();
			while((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			br.close();
			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "fail to get response";
		}
	}
	
	private Map<String, Object> parseWeather(String jsonString) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject;
		
		try {
			jsonObject = (JSONObject) jsonParser.parse(jsonString);
			
			Map<String, Object> resultMap = new HashMap<>();

			JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
			JSONObject weatherData = (JSONObject) weatherArray.get(0); 
			resultMap.put("main", weatherData.get("main"));
			resultMap.put("icon", weatherData.get("icon"));

			JSONObject mainData = (JSONObject) jsonObject.get("main");
			resultMap.put("temp", mainData.get("temp"));
			
			return resultMap;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Diary> findDiary(LocalDate date) {
		return diaryRepository.findAllByDate(date);
	}

	public List<Diary> findDiaries(LocalDate startDate, LocalDate endDate) {
		return diaryRepository.findAllByDateBetween(startDate, endDate);
	}
}
