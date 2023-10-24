package com.zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.weather.domain.Memo;

public interface JpaMemoRepository extends JpaRepository<Memo, Integer>{
}
