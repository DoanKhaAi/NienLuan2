package com.khaai.NuoiEm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khaai.NuoiEm.Entities.Grade;
import com.khaai.NuoiEm.Service.GradeService;

@Controller
public class GradeController {
	@Autowired
	private GradeService service;
	
	@GetMapping("/admin/grade/list")
	public String listGrade(Model model) {
		List<Grade> listGrades=service.listAll();
		model.addAttribute("listGrades",listGrades);
		return "/grade/listGrade";
	}
	
	@GetMapping("/admin/grade/listDeleted")
	public String listGradeDeleted(Model model) {
		List<Grade> listGrades=service.listDeleted();
		model.addAttribute("listGrades",listGrades);
		return "/grade/listGradeDeleted";
	}
	
	@GetMapping("/admin/grade/new")
	public String showNewForm(Model model) {
		model.addAttribute("grade", new Grade());
		model.addAttribute("pageTitle", "Thêm khối lớp mới");
		return "/grade/formGrade";
	}
	
	@PostMapping("/admin/grade/save")
	public String saveGrade(Grade grade, RedirectAttributes ra) {
		String result=service.save(grade);	
		if ("Name already exists".equals(result)) {
			ra.addFlashAttribute("error", "Khối lớp đã tồn tại");
			return "redirect:/admin/grade/list";
		} else {
			ra.addFlashAttribute("message", "Khối lớp được lưu thành công");
			return "redirect:/admin/grade/list";
		}
		
	}
	
	@GetMapping("/admin/grade/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
			Grade grade=service.get(id);
			model.addAttribute("grade", grade);
			model.addAttribute("pageTitle", "Chỉnh sửa khối lớp (ID: "+id+")");
			return "/grade/formGrade";
		
	} 
	@GetMapping("/admin/grade/delete/{id}")
	public String deleteGrade(@PathVariable("id") Integer id, RedirectAttributes ra) {
			service.deleted(id);
			ra.addFlashAttribute("message", "Xóa Khối lớp có ID = "+id+" thành công");
		return "redirect:/admin/grade/list";
	} 
	
	@GetMapping("/admin/grade/restore/{id}")
	public String restoreGrade(@PathVariable("id") Integer id, RedirectAttributes ra) {
		service.restore(id);
		ra.addFlashAttribute("message", "Phục hồi khối lớp thành công");
		return "redirect:/admin/grade/listDeleted";
	} 
}
