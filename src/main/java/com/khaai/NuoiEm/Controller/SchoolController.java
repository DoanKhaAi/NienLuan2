package com.khaai.NuoiEm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khaai.NuoiEm.Entities.School;
import com.khaai.NuoiEm.Service.DistrictService;
import com.khaai.NuoiEm.Service.SchoolService;



@Controller
public class SchoolController {
	@Autowired
	private SchoolService service;
	
	@Autowired
	private DistrictService districtService;
	
	@GetMapping("/admin/school/list")
	public String listSchool(Model model, @RequestParam(value = "districtID", defaultValue = "0") Integer districtID,
				@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int size) {
		Page<School> listSchools=service.listAll(page, size);
		
		if (districtID > 0) {
	        listSchools = service.findByDistrict(districtID, PageRequest.of(page, size));
	    } else {
	        listSchools = service.listAll(page, size);
	    }

	    model.addAttribute("districtID", districtID);
		model.addAttribute("listSchools",listSchools);
		
	    model.addAttribute("districts", districtService.listAll());
		return "/school/listSchool";
	}
	
	@GetMapping("/admin/school/listDeleted")
	public String listSchoolDeleted(Model model) {
		List<School> listSchools=service.listDeleted();
		model.addAttribute("listSchools",listSchools);
		return "/school/listSchoolDeleted";
	}
	
	@GetMapping("/admin/school/new")
	public String showNewForm(Model model) {
		model.addAttribute("school", new School());
		model.addAttribute("districts", districtService.listAll());
		model.addAttribute("pageTitle", "Thêm Trường học mới");
		return "/school/formSchool";
	}
	
	@PostMapping("/admin/school/save")
	public String saveSchool(School school, RedirectAttributes ra) {
		service.save(school);
		ra.addFlashAttribute("message", "Đã lưu thay đổi");
		return "redirect:/admin/school/list";
	}
	
	@GetMapping("/admin/school/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
			School school=service.get(id);
			model.addAttribute("school", school);
			model.addAttribute("districts", districtService.listAll());
			model.addAttribute("pageTitle", "Chỉnh sửa trường (ID: "+id+")");
			return "/school/formSchool";
		
	} 
	
	@GetMapping("/admin/school/delete/{id}")
	public String deleteSchool(@PathVariable("id") Integer id, RedirectAttributes ra) {
			service.deleted(id);
			ra.addFlashAttribute("message", "Xóa trường học có ID = "+id+" thành công");
		return "redirect:/admin/school/list";
	} 
	
	@GetMapping("/admin/school/restore/{id}")
	public String reStore(@PathVariable("id") Integer id, RedirectAttributes ra) {
		service.restore(id);
		ra.addFlashAttribute("message", "Phục hồi trường thành công");
		return "redirect:/admin/school/listDeleted";
	}
}
