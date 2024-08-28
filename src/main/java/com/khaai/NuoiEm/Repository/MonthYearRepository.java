package com.khaai.NuoiEm.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khaai.NuoiEm.Entities.MonthYear;

@Repository
public interface MonthYearRepository extends JpaRepository<MonthYear, Integer>{
	MonthYear findTopByOrderByMonthYearIDDesc();
}
