package com.khaai.NuoiEm.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khaai.NuoiEm.Entities.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer>{
	 Boolean existsByGradeName(String gradeName);
	 Optional<Grade> findByGradeName(String gradeName);
	 List<Grade> findByEnabledTrue();
	 List<Grade> findByEnabledFalse();
}
