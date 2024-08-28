package com.khaai.NuoiEm.Controller;

import java.util.List;
import java.util.Optional;

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

import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Service.ChildrenService;
import com.khaai.NuoiEm.Service.GradeService;
import com.khaai.NuoiEm.Service.SchoolService;
import com.khaai.NuoiEm.Service.SchoolYearService;
import com.khaai.NuoiEm.Service.StudyService;

@Controller
public class StudyController {
	@Autowired
	private StudyService service;
	
	@Autowired
	private GradeService serviceGrade;
	
	@Autowired
	private SchoolYearService serviceSchoolYear;
	
	@Autowired
	private SchoolService serviceSchool;
	
	@Autowired
	private ChildrenService serviceChildren;
	
	@GetMapping("/admin/study/list")
	public String listStudy(Model model,  @RequestParam(defaultValue = "0") int page,
	           @RequestParam(defaultValue = "1") int size,
	           @RequestParam(value = "childID", defaultValue = "0") Integer childID,
	           @RequestParam(value = "schoolID", defaultValue = "0") Integer schoolID,
	           @RequestParam(value = "schoolYearID", defaultValue = "0") Integer schoolYearID,
	           @RequestParam(value = "gradeID", defaultValue = "0")Integer gradeID) {
		//Page<Study> listStudies=service.listAll(page, size);
		Page<Study> listStudies = service.findByCriteria(childID, schoolID, schoolYearID, gradeID, PageRequest.of(page, size));
		
		model.addAttribute("listStudies", listStudies);
	    model.addAttribute("childID", childID);
	    model.addAttribute("schoolID", schoolID);
	    model.addAttribute("schoolYearID", schoolYearID);
	    model.addAttribute("gradeID", gradeID);
	    
	    
	    model.addAttribute("childrenList", serviceChildren.listAll());
	    model.addAttribute("schoolsList", serviceSchool.listAll());
	    model.addAttribute("schoolYearsList", serviceSchoolYear.listAll());
	    model.addAttribute("gradesList", serviceGrade.listAll());
		return "/study/listStudy";
	}
	
	@GetMapping("/admin/study/listDeleted")
	public String listStudyDeleted(Model model) {
		List<Study> listStudies=service.listDeleted();
		model.addAttribute("listStudies", listStudies);
		return "/study/listStudyDeleted";
	}
	
	@GetMapping("/admin/study/new")
	public String showNewForm(Model model) {
		model.addAttribute("study", new Study());
		model.addAttribute("grades", serviceGrade.listAll());
		model.addAttribute("schoolYears", serviceSchoolYear.listAll());
		model.addAttribute("schools", serviceSchool.listAll());
		model.addAttribute("childrens", serviceChildren.listAll());
		model.addAttribute("pageTitle", "Thêm mới việc học của trẻ em");
		return "/study/formStudy";
	}
	
	@PostMapping("/admin/study/save")
	public String saveStudy(Study study, RedirectAttributes ra) {
		Optional<Study> result = service.checkExists(study);
		if (result.isPresent()) {
			if (study.getStudyID() != result.get().getStudyID()) {
				ra.addFlashAttribute("error", "Việc học này đã được lưu trước đó");
				return "redirect:/admin/study/list";
			}
		}
		service.save(study);	
		ra.addFlashAttribute("message", "Việc học được lưu thành công");
		return "redirect:/admin/study/list";
	}
	
	@GetMapping("/admin/study/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
			Study study=service.get(id);
			model.addAttribute("study", study);
			model.addAttribute("grades", serviceGrade.listAll());
			model.addAttribute("schoolYears", serviceSchoolYear.listAll());
			model.addAttribute("schools", serviceSchool.listAll());
			model.addAttribute("childrens", serviceChildren.listAll());
			model.addAttribute("pageTitle", "Chỉnh sửa việc học");
			return "/study/formStudy";
		
	} 
	
	@GetMapping("/admin/study/delete/{id}")
	public String deleteStudy(@PathVariable("id") Integer id, RedirectAttributes ra) {
			service.deleted(id);
			ra.addFlashAttribute("message", "Xóa việc học thành công");
		return "redirect:/admin/study/list";
	} 
	
	@GetMapping("/admin/study/restore/{id}")
	public String restoreStudy(@PathVariable("id") Integer id, RedirectAttributes ra) {
			service.restore(id);
			ra.addFlashAttribute("message", "Phục hồi việc học thành công");
		return "redirect:/admin/study/listDeleted";
	} 
}
