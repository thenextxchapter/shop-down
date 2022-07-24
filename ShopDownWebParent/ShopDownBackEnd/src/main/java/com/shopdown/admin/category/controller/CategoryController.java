package com.shopdown.admin.category.controller;

import java.util.List;

import com.shopdown.admin.category.service.CategoryService;
import com.shopdown.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService service;

	@GetMapping("/categories")
	public String listCategories(Model model) {
		List<Category> categories = service.listAll();
		model.addAttribute("categories", categories);

		return "categories/category";
	}

	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		model.addAttribute("category", new Category());
		return "categories/category_form";
	}
}
