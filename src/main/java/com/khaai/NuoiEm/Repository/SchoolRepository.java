package com.khaai.NuoiEm.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khaai.NuoiEm.Entities.District;
import com.khaai.NuoiEm.Entities.School;

@Repository
public interface SchoolRepository  extends JpaRepository<School, Integer> {
	List<School> findByEnabledTrue();
	List<School> findByEnabledFalse();
	List<School> findByDistrict(District district);
	Page<School> findByEnabledTrue(PageRequest pageable);
	Page<School> findByDistrict_DistrictID(Integer districtID, Pageable pageable);
}
