package com.khaai.NuoiEm.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khaai.NuoiEm.Entities.District;
import com.khaai.NuoiEm.Entities.Province;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer>{
	List<District> findByEnabledTrue();
	List<District> findByProvince(Province province);
	List<District> findByEnabledFalse();
	Page<District> findByEnabledTrue(PageRequest pageable);
	Page<District> findByProvince_ProvinceID(Integer provinceID, Pageable pageable);
}
