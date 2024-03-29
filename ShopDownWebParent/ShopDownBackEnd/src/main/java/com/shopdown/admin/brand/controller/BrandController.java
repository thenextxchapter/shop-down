package com.shopdown.admin.brand.controller;

import java.io.IOException;
import java.util.List;

import com.shopdown.admin.brand.exception.BrandNotFoundException;
import com.shopdown.admin.brand.service.BrandService;
import com.shopdown.admin.category.service.CategoryService;
import com.shopdown.admin.utils.FileUploadUtil;
import com.shopdown.common.entity.Brand;
import com.shopdown.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BrandController {

	@Autowired
	private BrandService service;

	@Autowired
	private CategoryService catService;

	@GetMapping("/brands")
	public String findAllBrands(Model model) {
		return listByPage(1, "name", "asc", null, model);
	}

	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model
	) {
		Page<Brand> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();

		long startCount = (pageNum - 1) * BrandService.ROOT_BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.ROOT_BRANDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listBrands", listBrands);

		return "brands/brand";
	}

	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = catService.listCategoriesUsedInForm();

		model.addAttribute("brand", new Brand());
		model.addAttribute("listCategories", listCategories);

		model.addAttribute("pageTitle", "Create New Brand");
		model.addAttribute("headerTitle", "New Brand");

		return "brands/brand_form";
	}

	@PostMapping("/brands/save")
	public String saveBrand(
			Brand brand,
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes
	) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand savedBrand = service.save(brand);

			String uploadDir = "../brand-logos/" + savedBrand.getId();

			FileUploadUtil.cleanDir(uploadDir);

			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			service.save(brand);
		}

		redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully");
		return "redirect:/brands";
	}

	@GetMapping("/brands/edit/{id}")
	public String editCategory(
			@PathVariable(name = "id") Integer id,
			RedirectAttributes redirectAttributes,
			Model model
	) {
		try {
			Brand brand = service.get(id);
			List<Category> listCategories = catService.listCategoriesUsedInForm();

			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
			model.addAttribute("headerTitle", "Edit Brand");

			return "brands/brand_form";
		} catch (BrandNotFoundException exception) {
			redirectAttributes.addFlashAttribute("message", exception.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(
			@PathVariable(name = "id") Integer id,
			RedirectAttributes redirectAttributes
	) {
		try {
			service.delete(id);
			String brandDir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(brandDir);

			redirectAttributes.addFlashAttribute("message",
					"The brand ID " + id + " has been deleted successfully");
		} catch (BrandNotFoundException exception) {
			redirectAttributes.addFlashAttribute("message", exception.getMessage());
		}

		return "redirect:/brands";
	}

}
