package com.khaai.NuoiEm.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khaai.NuoiEm.Entities.SchoolYear;


@Repository
public interface SchoolYearRepository extends JpaRepository<SchoolYear, Integer> {
	Boolean existsBySchoolYearName(String schoolYearName);
	List<SchoolYear> findByEnabledTrue();
	List<SchoolYear> findByEnabledFalse();
}
