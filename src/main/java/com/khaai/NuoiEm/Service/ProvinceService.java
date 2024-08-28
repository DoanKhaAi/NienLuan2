package com.khaai.NuoiEm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class ProvinceService {
	@Autowired
	private ProvinceRepository repo;
	
	 @Autowired
	 private DistrictRepository repoDistrict;

	 @Autowired
	 private SchoolRepository repoSchool;

	 @Autowired
	 private StudyRepository repoStudy;
	
	
	public List<Province> listAll(){
		return (List<Province>) repo.findByEnabledTrue();
	}
	
	public List<Province> listDeleted(){
		return (List<Province>) repo.findByEnabledFalse();
	}
	
	public Page<Province> listAll(int page, int size){
		PageRequest pageable = PageRequest.of(page, size);
		return  (Page<Province>) repo.findByEnabledTrue(pageable);
	}
	
	@Transactional
	public void save(Province province) {
		if (province.getProvinceID()!=null) {
			Province province_update= repo.findById(province.getProvinceID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy tỉnh"));
	        province_update.setProvinceName(province.getProvinceName());
			repo.save(province_update);	
		}
		else {
			province.setEnabled(true);
			repo.save(province);
		}
	}
	
	public Province get(Integer id){
		Optional<Province> result =repo.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	
	public void deleted(Integer id) {
		Province province= repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tỉnh"));
		province.setEnabled(false);
		repo.save(province);
		
		 List<District> districts = repoDistrict.findByProvince(province);
	        for (District district : districts) {
	            district.setEnabled(false);
	            repoDistrict.save(district);

	            
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
	}
	
	
	
	public void restore(Integer id) {
		Province province= repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tỉnh"));
		province.setEnabled(true);
		repo.save(province);
		
		 List<District> districts = repoDistrict.findByProvince(province);
	        for (District district : districts) {
	            district.setEnabled(true);
	            repoDistrict.save(district);

	            
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
	
	
}
