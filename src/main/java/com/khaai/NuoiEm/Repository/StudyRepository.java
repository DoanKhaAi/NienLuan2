package com.khaai.NuoiEm.Repository;

import com.khaai.NuoiEm.Entities.Children;
import com.khaai.NuoiEm.Entities.Grade;
import com.khaai.NuoiEm.Entities.School;
import com.khaai.NuoiEm.Entities.SchoolYear;
import com.khaai.NuoiEm.Entities.Study;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface StudyRepository extends JpaRepository<Study, Integer> {
	Optional<Study> findByChildrenAndGradeAndSchoolYear(
	        Children children, 
	        Grade grade, 
	        SchoolYear schoolYear
	    );
	void deleteByStudyID(Integer studyID);
	
	List<Study> findByCountAndEnabledTrue(Integer count);
	List<Study> findByCountAndEnabledFalse(Integer count);
	Page<Study> findByEnabledTrue(PageRequest pageable);
	
	List<Study> findByGrade(Grade grade);
	List<Study> findBySchool(School school);
	List<Study> findByChildren(Children children);
	List<Study> findBySchoolYear(SchoolYear schoolYear);
	
	
	@Query("SELECT s FROM Study s WHERE " +
	           "(:childID = 0 OR s.children.childID = :childID) AND " +
	           "(:schoolID = 0 OR s.school.schoolID = :schoolID) AND " +
	           "(:schoolYearID = 0 OR s.schoolYear.schoolYearID = :schoolYearID) AND " +
	           "(:gradeID = 0 OR s.grade.gradeID = :gradeID)")
	    Page<Study> findByCriteria(@Param("childID") Integer childID,
	                               @Param("schoolID") Integer  schoolID,
	                               @Param("schoolYearID") Integer schoolYearID,
	                               @Param("gradeID") Integer gradeID,
	                               Pageable pageable);
}
