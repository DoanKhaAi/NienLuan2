package com.khaai.NuoiEm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.khaai.NuoiEm.Entities.District;
import com.khaai.NuoiEm.Entities.Province;
import com.khaai.NuoiEm.Entities.School;
import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Repository.DistrictRepository;
import com.khaai.NuoiEm.Repository.ProvinceRepository;
import com.khaai.NuoiEm.Repository.SchoolRepository;
import com.khaai.NuoiEm.Repository.StudyRepository;

import jakarta.transaction.Transactional;


@Service
public class DistrictService {
	@Autowired
	private DistrictRepository repo;
	
	@Autowired
	private ProvinceRepository provinceRepo;
	
	@Autowired
	private StudyRepository repoStudy;
	
	@Autowired
	private SchoolRepository repoSchool;
	
	public List<District> listAll(){
		return (List<District>) repo.findByEnabledTrue();
	}
	
	public List<District> listDeleted(){
		return (List<District>) repo.findByEnabledFalse();
	}
	
	public Page<District> listAll(int page, int size){
		PageRequest pageable = PageRequest.of(page, size);
		return  (Page<District>) repo.findByEnabledTrue(pageable);
	}
	
	
	public Page<District> findByProvince(Integer provinceID, Pageable pageable) {
        return repo.findByProvince_ProvinceID(provinceID, pageable);
    }
	
	public List<District> findByProvince(Province province){
		return repo.findByProvince(province);
	}
	
	@Transactional
	public void save(District district) {
		if (district.getDistrictID()!=null) {
			District district_update= repo.findById(district.getDistrictID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy huyện"));
			district_update.setDistrictName(district.getDistrictName());
			
			Province province = provinceRepo.findById(district.getProvince().getProvinceID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy tỉnh"));
			district_update.setProvince(province);
			repo.save(district_update);
		}
		else {
			district.setEnabled(true);
			repo.save(district);
		}
	}
	
	public District get(Integer id){
		Optional<District> result =repo.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	
	public void deleted(Integer id) {
		District district= repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy huyện"));
		district.setEnabled(false);
		repo.save(district);
		
		List<School> schools = repoSchool.findByDistrict(district);
	        for (School school : schools) {
	            school.setEnabled(false);
	            repoSchool.save(school);

	            
	            List<Study> studies = repoStudy.findBySchool(school);
	            for (Study study : studies) {
	                //study.setEnabled(false);
	            	study.setCount(study.getCount()-1);
	                repoStudy.save(study);
	            }
	        }
	}
	
	public void restore(Integer id) {
		District district= repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy huyện"));
		district.setEnabled(true);
		repo.save(district);
		
		List<School> schools = repoSchool.findByDistrict(district);
	        for (School school : schools) {
	            school.setEnabled(true);
	            repoSchool.save(school);

	            
	            List<Study> studies = repoStudy.findBySchool(school);
	            for (Study study : studies) {
	                //study.setEnabled(true);
	            	study.setCount(study.getCount()+1);
	                repoStudy.save(study);
	            }
	        }
	}
	
}
