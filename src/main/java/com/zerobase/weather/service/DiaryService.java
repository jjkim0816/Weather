package com.zerobase.weather.service;

import org.jboss.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zerobase.weather.WeatherApplication;
import com.zerobase.weather.domain.DateWeather;
import com.zerobase.weather.domain.Diary;
import com.zerobase.weather.repository.DateWeatherRepository;
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
	private final DateWeatherRepository dateWeatherRepository;
	
	private static final Logger logger = Logger.getLogger(WeatherApplication.class);

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void createDiary(LocalDate date, String text) {
		logger.info("started to create diary");
		DateWeather dateWeather = findDateWeather(date);

		Diary diary = new Diary();
		diary.setDateWeather(dateWeather);
		diary.setText(text);
		diary.setDate(date);
		
		diaryRepository.save(diary);
		logger.info("end to create diary");
	}
	
	private DateWeather findDateWeather(LocalDate date) {
		List<DateWeather> dateWeatherFromDBMS = dateWeatherRepository.findAllByDate(date);
		if (dateWeatherFromDBMS.size() == 0) {
			return getWeatherDateFromApi();
		} else {
			return dateWeatherFromDBMS.get(0);
		}
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

	@Transactional(readOnly = true)
	public List<Diary> findDiary(LocalDate date) {
//		if(date.isAfter(LocalDate.ofYearDay(3050, 1))) {
//			throw new InvalidDate();
//		}

		return diaryRepository.findAllByDate(date);
	}

	@Transactional(readOnly = true)
	public List<Diary> findDiaries(LocalDate startDate, LocalDate endDate) {
		return diaryRepository.findAllByDateBetween(startDate, endDate);
	}

	@Transactional
	public void modifyDiary(LocalDate date, String text) {
		Diary nowDiary = diaryRepository.findFirstByDate(date);
		nowDiary.setText(text);
		diaryRepository.save(nowDiary);
	}

	@Transactional
	public void deleteDiary(LocalDate date) {
		diaryRepository.deleteAllByDate(date);
	}
	
	@Scheduled(cron = "0 0 1 * * *")
	@Transactional
	public void saveWeatherData() {
		dateWeatherRepository.save(getWeatherDateFromApi());
	}
	
	private DateWeather getWeatherDateFromApi() {
		String weatherData = getWeatherString();
		
		Map<String, Object> parseWeather = parseWeather(weatherData);
		
		DateWeather dateWeather = new DateWeather();
		
		System.out.println(dateWeather);
		
		dateWeather.setDate(LocalDate.now());
		dateWeather.setWeather(parseWeather.get("main").toString());
		dateWeather.setIcon(parseWeather.get("icon").toString());
		dateWeather.setTemperature(Double.parseDouble(parseWeather.get("temp").toString()));
		
		return dateWeather;
	}
}
