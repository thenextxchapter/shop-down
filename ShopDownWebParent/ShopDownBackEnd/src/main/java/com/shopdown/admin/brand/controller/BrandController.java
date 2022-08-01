package com.shopdown.admin.brand.controller;

import java.util.List;

import com.shopdown.admin.brand.service.BrandService;
import com.shopdown.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrandController {

	@Autowired
	private BrandService service;

	@GetMapping("/brands")
	public String findAllBrands(Model model) {
		List<Brand> listBrands = service.listAll();
		model.addAttribute("listBrands", listBrands);

		return "brands/brand";
	}

}
