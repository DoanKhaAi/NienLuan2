package com.khaai.NuoiEm.Repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khaai.NuoiEm.Entities.Children;


@Repository
public interface ChildrenRepository extends JpaRepository<Children, Integer> {
	List<Children> findByEnabledTrue();
	List<Children> findByEnabledFalse();
	
	Page<Children> findByEnabledTrue(PageRequest pageable);
}
