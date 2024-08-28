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

import com.khaai.NuoiEm.Entities.District;
import com.khaai.NuoiEm.Service.DistrictService;
import com.khaai.NuoiEm.Service.ProvinceService;


@Controller
public class DistrictController {
	@Autowired
	private DistrictService service;
	
	@Autowired
	private ProvinceService provinceService;
	
	@GetMapping("/admin/district/list")
	public String listDistrict(Model model, @RequestParam(value = "provinceID", defaultValue = "0") Integer provinceID,
								@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int size) {
		Page<District> listDistricts=service.listAll(page, size);
		
		if (provinceID > 0) {
	        listDistricts = service.findByProvince(provinceID, PageRequest.of(page, size));
	    } else {
	        listDistricts = service.listAll(page, size);
	    }
		
		model.addAttribute("provinces", provinceService.listAll());
		model.addAttribute("provinceID", provinceID);
		model.addAttribute("listDistricts",listDistricts);
		return "/district/listDistrict";
	}
	
	@GetMapping("/admin/district/listDeleted")
	public String showListDelete(Model model) {
		List<District> listDistricts=service.listDeleted();
		model.addAttribute("listDistricts",listDistricts);
		return "/district/listDistrictDeleted";
	}
	
	@GetMapping("/admin/district/new")
	public String showNewForm(Model model) {
		model.addAttribute("district", new District());
		model.addAttribute("provinces", provinceService.listAll());
		model.addAttribute("pageTitle", "Thêm huyện mới");
		return "/district/formDistrict";
	}
	
	@PostMapping("/admin/district/save")
	public String saveDistrict(District district, RedirectAttributes ra) {
		service.save(district);
		ra.addFlashAttribute("message", "Đã lưu thay đổi");
		return "redirect:/admin/district/list";
	}
	
	@GetMapping("/admin/district/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
			District district=service.get(id);
			model.addAttribute("district", district);
			model.addAttribute("provinces", provinceService.listAll());
			model.addAttribute("pageTitle", "Chỉnh sửa huyện (ID: "+id+")");
			return "/district/formDistrict";
		
	} 
	
	@GetMapping("/admin/district/delete/{id}")
	public String deleteDistrict(@PathVariable("id") Integer id, RedirectAttributes ra) {
			service.deleted(id);
			ra.addFlashAttribute("message", "Xóa huyện có ID = "+id+" thành công");
		return "redirect:/admin/district/list";
	} 
	
	@GetMapping("/admin/district/restore/{id}")
	public String reStore(@PathVariable("id") Integer id, RedirectAttributes ra) {
		service.restore(id);
		ra.addFlashAttribute("message", "Phục hồi huyện thành công");
		return "redirect:/admin/district/listDeleted";
	}
}
