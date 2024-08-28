package com.khaai.NuoiEm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.khaai.NuoiEm.Entities.SchoolYear;
import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Repository.SchoolYearRepository;
import com.khaai.NuoiEm.Repository.StudyRepository;

import jakarta.transaction.Transactional;

@Service
public class SchoolYearService {
	@Autowired
	private SchoolYearRepository repo;
	
	@Autowired
	private StudyRepository repoStudy;
	
	public List<SchoolYear> listAll(){
		return (List<SchoolYear>) repo.findByEnabledTrue();
	}
	
	public List<SchoolYear> listDeleted(){
		return (List<SchoolYear>) repo.findByEnabledFalse();
	}
	
	@Transactional
	public String save(SchoolYear schoolYear, @RequestParam("startYear") int startYear) {
		int endYear = startYear + 1;
        String schoolYearName = startYear + "-" + endYear;
        schoolYear.setSchoolYearName(schoolYearName);
		if (repo.existsBySchoolYearName(schoolYear.getSchoolYearName())) {
            return "School_year name already exists";
        }
		if (schoolYear.getSchoolYearID()!=null) {
			SchoolYear schoolYear_update= repo.findById(schoolYear.getSchoolYearID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy năm học"));
			schoolYear_update.setSchoolYearName(schoolYear.getSchoolYearName());
			repo.save(schoolYear_update);
		} else {
			schoolYear.setEnabled(true);
			repo.save(schoolYear);
		}
		
		return "OK";
	}
	
	public SchoolYear get(Integer id){
		Optional<SchoolYear> result =repo.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	
	public void deleted(Integer id) {
		SchoolYear schoolYear= repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy năm học"));
		schoolYear.setEnabled(false);		
		repo.save(schoolYear);
		
		List<Study> studies = repoStudy.findBySchoolYear(schoolYear);
        for (Study study : studies) {
           // study.setEnabled(false);
            study.setCount(study.getCount()-1);
            repoStudy.save(study);
        }
	}
	
	public void restore(Integer id) {
		SchoolYear schoolYear= repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy năm học"));
		schoolYear.setEnabled(true);		
		repo.save(schoolYear);
		
		List<Study> studies = repoStudy.findBySchoolYear(schoolYear);
        for (Study study : studies) {
        	study.setCount(study.getCount()+1);
//        	if (study.getCount()==4) {
//	            study.setEnabled(true);
//        	}
        	repoStudy.save(study);
        }
	}
}
