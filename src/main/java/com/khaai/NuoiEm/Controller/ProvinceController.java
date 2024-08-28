package com.khaai.NuoiEm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khaai.NuoiEm.Entities.Province;
import com.khaai.NuoiEm.Service.ProvinceService;

@Controller
public class ProvinceController {
	@Autowired
	private ProvinceService service;
	@GetMapping("/admin/province/list")
	public String listProvince(Model model,  @RequestParam(defaultValue = "0") int page,
	           @RequestParam(defaultValue = "1") int size) {
		Page<Province> listProvinces=service.listAll(page, size);
		model.addAttribute("listProvinces",listProvinces);
		return "/province/listProvince";
	}
	
	@GetMapping("/admin/province/new")
	public String showNewForm(Model model) {
		model.addAttribute("province", new Province());
		model.addAttribute("pageTitle", "Thêm tỉnh mới");
		return "/province/formProvince";
	}
	
	@GetMapping("/admin/province/listDeleted")
	public String showListDelete(Model model) {
		List<Province> listProvinces=service.listDeleted();
		model.addAttribute("listProvinces",listProvinces);
		return "/province/listProvinceDeleted";
	}

	
	@PostMapping("/admin/province/save")
	public String saveProvince(Province province, RedirectAttributes ra) {		
		service.save(province);
		ra.addFlashAttribute("message", "Tỉnh mới đã được lưu thành công");
		return "redirect:/admin/province/list";
	}
	
	@GetMapping("/admin/province/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
			Province province=service.get(id);
			model.addAttribute("province", province);
			model.addAttribute("pageTitle", "Chỉnh sửa tỉnh (ID: "+id+")");
			return "/province/formProvince";
		
	} 
	
	@GetMapping("/admin/province/delete/{id}")
	public String deleteProvince(@PathVariable("id") Integer id, RedirectAttributes ra) {
			service.deleted(id);
			ra.addFlashAttribute("message", "Xóa Tỉnh có ID = "+id+" thành công");
		return "redirect:/admin/province/list";
	} 
	
	@GetMapping("/admin/province/restore/{id}")
	public String reStore(@PathVariable("id") Integer id, RedirectAttributes ra) {
		service.restore(id);
		ra.addFlashAttribute("message", "Phục hồi tỉnh thành công");
		return "redirect:/admin/province/listDeleted";
	}

	
}
