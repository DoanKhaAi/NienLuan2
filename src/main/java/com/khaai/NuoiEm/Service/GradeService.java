package com.khaai.NuoiEm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khaai.NuoiEm.Entities.Grade;
import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Repository.GradeRepository;
import com.khaai.NuoiEm.Repository.StudyRepository;

import jakarta.transaction.Transactional;

@Service
public class GradeService {
	@Autowired
	private GradeRepository repo;
	
	@Autowired
	private StudyRepository repoStudy;
	
	public List<Grade> listAll(){
		return (List<Grade>) repo.findByEnabledTrue();
	}
	
	public List<Grade> listDeleted(){
		return (List<Grade>) repo.findByEnabledFalse();
	}
	
	@Transactional
	public String save(Grade grade) {
		if (grade.getGradeID()!=null) {
			Grade result= repo.findById(grade.getGradeID()).orElseThrow(() -> new RuntimeException("Không tìm thấy khối lớp"));
			if (checkNameBool(grade)){
					Grade name = repo.findByGradeName(grade.getGradeName()).orElseThrow(() -> new RuntimeException("Không tìm thấy khối lớp"));
					if (!name.equals(result)) 
						return "Name already exists";
			}
			else {
				result.setGradeName(grade.getGradeName());
				repo.save(result);
			}
		}
		else if (checkNameBool(grade)) return "Name already exists";
			else {
				grade.setEnabled(true);
				repo.save(grade);
			}
		return "OK";
	}
	
	
	public Boolean checkNameBool(Grade grade){
		return repo.existsByGradeName(grade.getGradeName());
	}
	
	public Grade get(Integer id){
		Optional<Grade> result =repo.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	
	public void deleted(Integer id) {
		Grade result= repo.findById(id)
			.orElseThrow(() -> new RuntimeException("Không tìm thấy khối lớp"));
		result.setEnabled(false);
		repo.save(result);
		
		List<Study> studies = repoStudy.findByGrade(result);
	        for (Study study : studies) {
	            //study.setEnabled(false);
	            study.setCount(study.getCount()-1);
	            repoStudy.save(study);
	        }
	}
	
	public void restore(Integer id) {
		Grade result= repo.findById(id)
			.orElseThrow(() -> new RuntimeException("Không tìm thấy khối lớp"));
		result.setEnabled(true);
		repo.save(result);
		
		List<Study> studies = repoStudy.findByGrade(result);
	        for (Study study : studies) {
	        	study.setCount(study.getCount()+1);
//	        	if (study.getCount()==4) {
//		            study.setEnabled(true);
//	        	}
	        	repoStudy.save(study);
	        }
	}
}
