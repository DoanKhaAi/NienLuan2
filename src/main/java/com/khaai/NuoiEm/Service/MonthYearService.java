package com.khaai.NuoiEm.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.khaai.NuoiEm.Entities.MonthYear;
import com.khaai.NuoiEm.Repository.MonthYearRepository;

@Service
public class MonthYearService {
	@Autowired
    private MonthYearRepository repo;

    public void createMonthYearForCurrentMonth() {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        MonthYear monthYear = new MonthYear(currentMonth, currentYear, null);
        repo.save(monthYear);
    }
    
    @Scheduled(cron = "0 0 0 21 * ?")
    public void createMonthYearOnFirstDayOfMonth() {
        createMonthYearForCurrentMonth();
    }
}
