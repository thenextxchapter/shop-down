package com.shopdown.admin.product.controller;

import java.io.IOException;
import java.util.List;

import com.shopdown.admin.brand.service.BrandService;
import com.shopdown.admin.product.exception.ProductNotFoundException;
import com.shopdown.admin.product.service.ProductService;
import com.shopdown.admin.utils.FileUploadUtil;
import com.shopdown.common.entity.Brand;
import com.shopdown.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductController {

	@Autowired private ProductService productService;
	@Autowired private BrandService brandService;

	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = productService.listAll();

		model.addAttribute("listProducts", listProducts);

		return "products/product";
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("headerTitle", "New Product");

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(
			Product product,
			RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile mainImageMultipart,
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues
	) throws IOException {
		setMainImageName(mainImageMultipart, product);
		setExtraImageNames(extraImageMultiparts, product);
		setProductDetails(detailNames, detailValues, product);

		Product savedProduct = productService.save(product);

		saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

		ra.addFlashAttribute("message", "The product has been saved successfully.");

		return "redirect:/products";
	}

	private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length == 0) return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];

			if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
	}

	private void saveUploadedImages(
			MultipartFile mainImageMultipart,
			MultipartFile[] extraImageMultiparts,
			Product savedProduct
	) throws IOException {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();

			FileUploadUtil.cleanDir(uploadDir);

			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}

		if (extraImageMultiparts.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";

			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}
	}

	private void setExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					product.addExtraImage(fileName);
				}
			}
		}
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}


	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(
			@PathVariable("id") Integer id,
	@PathVariable("status") boolean enabled,
	RedirectAttributes redirectAttributes
	) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(
			@PathVariable(name = "id") Integer id,
			RedirectAttributes redirectAttributes
	) {
		try {
			productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;

			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);

			redirectAttributes.addFlashAttribute("message",
					"The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException exception) {
			redirectAttributes.addFlashAttribute("message", exception.getMessage());
		}

		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(
			@PathVariable("id") Integer id,
			Model model,
			RedirectAttributes  redirectAttributes
	) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("headerTitle", "Edit Product");

			return "products/product_form";
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());

			return "redirect:/products";
		}
	}

}
