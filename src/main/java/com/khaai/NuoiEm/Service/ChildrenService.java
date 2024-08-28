package com.khaai.NuoiEm.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khaai.NuoiEm.Entities.Children;
import com.khaai.NuoiEm.Entities.MyUser;
import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Repository.ChildrenRepository;
import com.khaai.NuoiEm.Repository.StudyRepository;

import jakarta.transaction.Transactional;

@Service
public class ChildrenService {
	
	@Autowired
	private ChildrenRepository repo;
	
	@Autowired
	private StudyRepository repoStudy;
	
	
	public List<Children> listAll(){
		return (List<Children>) repo.findByEnabledTrue();
	}
	
	public Page<Children> listAll(int page, int size){
		PageRequest pageable = PageRequest.of(page, size);
		return  (Page<Children>) repo.findByEnabledTrue(pageable);
	}
	
	
	public List<Children> listDeleted(){
		return (List<Children>) repo.findByEnabledFalse();
	}
	
	public Children get(Integer id){
		Optional<Children> result =repo.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	
	@Transactional
	public String save(Children children) throws IOException{
		children.setEnabled(true);
		children.setApdoted(false);
        repo.save(children);
        return "Children saved successfully";
	}
	
	@Transactional
	public String saveImage(Children children, MultipartFile imageFile) throws IOException{
		String file= saveChildrenImage(children.getChildID(), imageFile);
		children.setImage(file);
        repo.save(children);
        return "Children Image saved successfully";
	}
	
	@Transactional
	public String saveEdit(Children children) throws IOException{
		Children children_edit= repo.findById(children.getChildID())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ em"));
		if (children.getBirthday() !=null) {
			children_edit.setBirthday(children.getBirthday());
		}
        children_edit.setFullname(children.getFullname());
        children_edit.setGender(children.getGender());
        return "Children edited successfully";
	}
	
	@Transactional
	public void saveChildrenEditImage(Children children, MultipartFile imageFile) throws IOException {
		Children children_edit=repo.findById(children.getChildID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ em"));
		String image=children_edit.getImage();
		if (!image.equals("default_user.jpg")) {
			Path filePath = Paths.get("src/main/resources/static/img/children/", image);
			Files.delete(filePath);
		}
		String file= saveChildrenImage(children.getChildID(), imageFile);
		children_edit.setImage(file);	
		repo.save(children_edit);	
	}
	
	@Transactional
	 public String saveChildrenImage(Integer id, MultipartFile imageFile) throws IOException {
	    	if (!imageFile.isEmpty()) {
		    	Path uploadPath = Paths.get("src/main/resources/static/img/children/");
		        String originalFileName = imageFile.getOriginalFilename();
		        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		        String newFileName = "_" + id + fileExtension;
		        Path filePath = uploadPath.resolve(newFileName);
		        Files.copy(imageFile.getInputStream(), filePath);
		        return newFileName; 
	        }
	    	else return "default_user.jpg";
	 }
	 
	@Transactional
	 public void deleted(Integer id) {
		Children children=repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ em"));
		children.setEnabled(false);
		repo.save(children);
		
		List<Study> studies = repoStudy.findByChildren(children);
        for (Study study : studies) {
           // study.setEnabled(false);
            study.setCount(study.getCount()-1);
            repoStudy.save(study);
        }
	 }
	
	@Transactional
	 public void restore(Integer id) {
		Children children=repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ em"));
		children.setEnabled(true);
		repo.save(children);
		
		List<Study> studies = repoStudy.findByChildren(children);
       for (Study study : studies) {
    	   study.setCount(study.getCount()+1);
//	       if (study.getCount()==4) {
//		            study.setEnabled(true);
//	       }
	       repoStudy.save(study);
       }
	 }
	
	@Transactional
	public void saveAdopt(Integer id, MyUser user) {
		Children children=repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ em"));
		children.setApdoted(true);
		children.setUser(user);	
	}
	
	@Transactional
	public void cancelAdopt(Integer id) {
		Children children=repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ em"));
		children.setApdoted(false);
		children.setUser(null);	
	}
}
