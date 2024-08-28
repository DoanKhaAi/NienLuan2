package com.khaai.NuoiEm.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khaai.NuoiEm.Entities.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer>{
	Page<Province> findByEnabledTrue(PageRequest pageable);
	List<Province> findByEnabledFalse();
	List<Province> findByEnabledTrue();
}
