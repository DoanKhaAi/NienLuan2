package com.khaai.NuoiEm.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khaai.NuoiEm.Entities.Children;
import com.khaai.NuoiEm.Entities.District;
import com.khaai.NuoiEm.Entities.Province;
import com.khaai.NuoiEm.Entities.School;
import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Service.ChildrenService;
import com.khaai.NuoiEm.Service.DistrictService;
import com.khaai.NuoiEm.Service.GradeService;
import com.khaai.NuoiEm.Service.ProvinceService;
import com.khaai.NuoiEm.Service.SchoolService;
import com.khaai.NuoiEm.Service.StudyService;



@Controller
public class ChildrenController {
	@Autowired
	private ChildrenService service;
	
	@Autowired
	private StudyService serviceStudy;
	
	@Autowired
	private ProvinceService serviceProvince;
	
	@Autowired
	private DistrictService serviceDistrict;
	
	@Autowired
	private SchoolService serviceSchool;
	
	@Autowired
	private GradeService serviceGrade;
	
	@GetMapping("/admin/children/list")
	public String childrenList(Model model, @RequestParam(defaultValue = "0") int page,
	           @RequestParam(defaultValue = "1") int size) {
		Page<Children> listChildren=service.listAll(page, size);
		model.addAttribute("listChildren",listChildren);
		return "/children/admin/listChildren";
	}
	
	@GetMapping("/admin/children/listDeleted")
	public String childrenListDeleted(Model model) {
		List<Children> listChildren=service.listDeleted();
		model.addAttribute("listChildren",listChildren);
		return "/children/admin/listChildrenDeleted";
	}
	
	
	@GetMapping("/user/children/list")
	public String childrenListUser(
	        @RequestParam(value = "provinceId", required = false) Integer provinceId,
	        @RequestParam(value = "districtId", required = false) Integer districtId,
	        @RequestParam(value = "schoolId", required = false) Integer schoolId,
	        @RequestParam(value = "gradeId", required = false) Integer gradeId,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "1") int size,
	        Model model) {
		List<Children> listChildren = service.listAll();
	    List<Study> listStudies = serviceStudy.listAll();

	    if (provinceId != null) {
	        listStudies = listStudies.stream()
	            .filter(study -> study.getSchool().getDistrict().getProvince().getProvinceID().equals(provinceId))
	            .collect(Collectors.toList());
	    }

	    if (districtId != null) {
	        listStudies = listStudies.stream()
	            .filter(study -> study.getSchool().getDistrict().getDistrictID().equals(districtId))
	            .collect(Collectors.toList());
	    }

	    if (schoolId != null) {
	        listStudies = listStudies.stream()
	            .filter(study -> study.getSchool().getSchoolID().equals(schoolId))
	            .collect(Collectors.toList());
	    }

	    if (gradeId != null) {
	        listStudies = listStudies.stream()
	            .filter(study -> study.getGrade().getGradeID().equals(gradeId))
	            .collect(Collectors.toList());
	    }

	

	    Map<Integer, Study> studyMap = listStudies.stream()
	            .collect(Collectors.toMap(
	                study -> study.getChildren().getChildID(), 
	                study -> study, 
	                (existingStudy, newStudy) -> {                   
	                    String existingYear = existingStudy.getSchoolYear().getSchoolYearName();
	                    String newYear = newStudy.getSchoolYear().getSchoolYearName();
	                    return compareSchoolYears(existingYear, newYear) > 0 ? existingStudy : newStudy;
	                }
	            ));

	  
	    List<Children> filteredChildren = listChildren.stream()
	            .filter(child -> studyMap.containsKey(child.getChildID()) && !child.getApdoted())
	            .collect(Collectors.toList());
	    
	    int totalItems = filteredChildren.size();
	    int start = Math.min(page * size, totalItems);
	    int end = Math.min((page + 1) * size, totalItems);
	    List<Children> paginatedChildren = filteredChildren.subList(start, end);

	  
	    model.addAttribute("studyMap", studyMap);
	    model.addAttribute("listChildren", paginatedChildren);
   
	    model.addAttribute("provinces", serviceProvince.listAll());
	    model.addAttribute("grades", serviceGrade.listAll());
	    
	    //Tính số trang
	    int totalPages = (int) Math.ceil((double) totalItems / size);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    
	    return "/children/listChildren";
	}
	public int compareSchoolYears(String year1, String year2) {
	    int year1Start = Integer.parseInt(year1.split("-")[0]);
	    int year2Start = Integer.parseInt(year2.split("-")[0]);
	    return Integer.compare(year1Start, year2Start);
	}
	
	@GetMapping("/get-provinces")
	@ResponseBody
	public String getProvinces() {
	    List<Province> provinces = serviceProvince.listAll();
	    StringBuilder sb = new StringBuilder();
	    sb.append("<option value=''>Tất cả các tỉnh</option>");
	    for (Province province : provinces) {
	        sb.append("<option value='").append(province.getProvinceID()).append("'>")
	          .append(province.getProvinceName()).append("</option>");
	    }
	    return sb.toString();
	}


	@GetMapping("/get-districts")
	@ResponseBody
	public String getDistricts(@RequestParam Integer provinceId) {
	    Province province = serviceProvince.get(provinceId);
	    List<District> districts = serviceDistrict.findByProvince(province);
	    StringBuilder sb = new StringBuilder();
	    sb.append("<option value=''>Tất cả các huyện</option>");
	    for (District district : districts) {
	        sb.append("<option value='").append(district.getDistrictID()).append("'>")
	          .append(district.getDistrictName()).append("</option>");
	    }
	    return sb.toString();
	}

	
	@GetMapping("/get-schools")
	@ResponseBody
	public String getSchools(@RequestParam Integer districtId) {
	    District district = serviceDistrict.get(districtId);
	    List<School> schools = serviceSchool.findByDistrict(district);
	    StringBuilder sb = new StringBuilder();
	    sb.append("<option value=''>Tất cả các trường</option>");
	    for (School school : schools) {
	        sb.append("<option value='").append(school.getSchoolID()).append("'>")
	          .append(school.getSchoolName()).append("</option>");
	    }
	    return sb.toString();
	}


	
	@GetMapping("/admin/children/new")
	public String showNewFormCreate(Model model) {
		model.addAttribute("children", new Children());
		model.addAttribute("pageTitle", "Thêm trẻ em mới");
		return "/children/admin/newChildren";
	}
	
	@PostMapping("/admin/children/save")
	public String saveCreateChildren(Children children, RedirectAttributes ra, @RequestParam("imageFile")MultipartFile imageFile) throws IOException {
		service.save(children);
		service.saveImage(children, imageFile);
		ra.addFlashAttribute("message", "Trẻ em mới đã được lưu thành công");
		return "redirect:/admin/children/list";
	}
	
	@GetMapping("/admin/children/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
			Children children=service.get(id);
			model.addAttribute("children", children);
			model.addAttribute("pageTitle", "Chỉnh sửa thông tin trẻ em "+id);
			return "/children/admin/editChildren";
		
	} 
	
	@GetMapping("/admin/children/restore/{id}")
	public String restore(@PathVariable("id") Integer id, RedirectAttributes ra ) {
		service.restore(id);
		ra.addFlashAttribute("message", "Phục hồi trẻ em thành công");
		return "redirect:/admin/children/listDeleted";
		
	} 
	
	@PostMapping("/admin/children/save_edit")
	public String saveEdit(Children children, RedirectAttributes ra) throws IOException {
		service.saveEdit(children);
		ra.addFlashAttribute("message", "Chỉnh sửa thông tin trẻ em thành công!");
		return "redirect:/admin/children/list";
	}
	
	@GetMapping("/admin/children/detail/{id}")
	public String showDetail(@PathVariable("id") Integer id, Model model) {
			Children children=service.get(id);
			List<Study> studies = children.getStudies();
			model.addAttribute("children", children);
			model.addAttribute("studies",  studies);
			model.addAttribute("pageTitle", "Chi tiết thông tin trẻ em "+id);
			return "/children/admin/detailChildren";
		
	} 
	
	@GetMapping("/admin/children/changeImage/{id}")
	public String changeImage(@PathVariable("id") Integer id, Model model) {
			Children children=service.get(id);
			model.addAttribute("children", children);
			return "/children/admin/change_image_children";
		
	}
	
	@PostMapping("/admin/children/save_children_change_image")
	public String saveChildrenChangeImage(Children children, RedirectAttributes ra, @RequestParam("imageFile")MultipartFile imageFile) 
			throws IOException {
		service.saveChildrenEditImage(children, imageFile);	
		ra.addFlashAttribute("message", "Thay đổi hình ảnh thành công!");
		return "redirect:/admin/children/detail/" + children.getChildID();
	}
	
	@GetMapping("/admin/children/delete/{id}")
	public String deleteChidren(@PathVariable("id") Integer id, RedirectAttributes ra) {
			service.deleted(id);
			ra.addFlashAttribute("message", "Xóa Trẻ em có ID = "+id+" thành công");
		return "redirect:/admin/children/list";
	} 
	

}
