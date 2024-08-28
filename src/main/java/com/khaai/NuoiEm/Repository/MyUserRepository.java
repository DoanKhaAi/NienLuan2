package com.khaai.NuoiEm.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.khaai.NuoiEm.Entities.MyUser;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Integer> {
	Optional<MyUser> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	Page<MyUser> findAll(Pageable pageable);
	
	@Query("SELECT u FROM MyUser u WHERE SIZE(u.children) > 0")
	    List<MyUser> findAllWithChildren();
	
	
}
