package com.khaai.NuoiEm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.khaai.NuoiEm.Entities.District;
import com.khaai.NuoiEm.Entities.School;
import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Repository.DistrictRepository;
import com.khaai.NuoiEm.Repository.SchoolRepository;
import com.khaai.NuoiEm.Repository.StudyRepository;

@Service 
public class SchoolService {
	@Autowired
	private SchoolRepository repo;
	
	@Autowired
	private DistrictRepository repoDisrict;
	
	@Autowired
	private StudyRepository repoStudy;
	
	public List<School> listAll(){
		return (List<School>) repo.findByEnabledTrue();
	}
	public List<School> listDeleted(){
		return (List<School>) repo.findByEnabledFalse();
	}
	
	public Page<School> listAll(int page, int size){
		PageRequest pageable = PageRequest.of(page, size);
		return  (Page<School>) repo.findByEnabledTrue(pageable);
	}
	
	 public Page<School> findByDistrict(Integer districtID, Pageable pageable) {
	        return repo.findByDistrict_DistrictID(districtID, pageable);
	  }
	 
	 public List<School> findByDistrict(District district) {
	        return repo.findByDistrict(district);
	  }
	
	
	public void save(School school) {
		if (school.getSchoolID()!=null) {
			School school_update= repo.findById(school.getSchoolID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy trường"));
			school_update.setSchoolName(school.getSchoolName());
			
			District district = repoDisrict.findById(school.getDistrict().getDistrictID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy huyện"));
			school_update.setDistrict(district);
			repo.save(school_update);
		}
		else {
			school.setEnabled(true);
			repo.save(school);
		}
	}
	
	public School get(Integer id){
		Optional<School> result =repo.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	
	public void deleted(Integer id) {
		School school= repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy trường"));
		school.setEnabled(false);
		repo.save(school);
	
		List<Study> studies = repoStudy.findBySchool(school);
        for (Study study : studies) {
            //study.setEnabled(false);
            study.setCount(study.getCount()-1);
            repoStudy.save(study);
        }
		
	}
	
	public void restore(Integer id) {
		School school= repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy trường"));
		school.setEnabled(true);
		repo.save(school);
	
		List<Study> studies = repoStudy.findBySchool(school);
        for (Study study : studies) {
        	study.setCount(study.getCount()+1);
//        	if (study.getCount()==4) {
//	            study.setEnabled(true);
//        	}
        	repoStudy.save(study);
        }
		
	}
}
