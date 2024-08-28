package com.khaai.NuoiEm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Repository.StudyRepository;

import jakarta.transaction.Transactional;

@Service
public class StudyService {

	@Autowired
    private StudyRepository repo;
	
	
	public List<Study> listAll(){
		return (List<Study>) repo.findByCountAndEnabledTrue(4);
	}
	
	
	public List<Study> listDeleted(){
		return (List<Study>) repo.findByCountAndEnabledFalse(4);
	}
	
	public Page<Study> listAll(int page, int size){
		PageRequest pageable = PageRequest.of(page, size);
		return  (Page<Study>) repo.findByEnabledTrue(pageable);
	}
	
	public Page<Study> findByCriteria(Integer childID, Integer schoolID, Integer schoolYearID, Integer gradeID, Pageable pageable) {
        return repo.findByCriteria(childID, schoolID, schoolYearID, gradeID, pageable);
    }
	
	
	@Transactional
	public void save(Study study){
		study.setEnabled(true);
		study.setCount(4);
		repo.save(study);
	}
	
	public Optional<Study> checkExists(Study study){
		 Optional<Study> found= repo.findByChildrenAndGradeAndSchoolYear(study.getChildren(), study.getGrade(), study.getSchoolYear());
		 if (found!=null) return found;
		 else return null;
	}
	
	public Study get(Integer id){
		Optional<Study> result =repo.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	
	@Transactional
	public void deleted(Integer id) {
		Study result =repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy việc học"));
		result.setEnabled(false);
		repo.save(result);
	}
	
	@Transactional
	public void restore(Integer id) {
		Study result =repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy việc học"));
		result.setEnabled(true);
		repo.save(result);
	}
	
}
